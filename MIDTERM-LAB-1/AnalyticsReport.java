import java.io.*;
import java.util.*;

public class AnalyticsReport {

    // File Validation
    public static File promptAndValidateFile(Scanner scanner) {
        File file;
        while (true) {
            System.out.print("Enter dataset file path: ");
            String path = scanner.nextLine().trim();
            file = new File(path);

            if (!file.exists()) {
                System.out.println("  Error: File does not exist. Please try again.");
            } else if (!file.isFile()) {
                System.out.println("  Error: Path is not a file. Please try again.");
            } else if (!path.toLowerCase().endsWith(".csv")) {
                System.out.println("  Error: File must have a .csv extension. Please try again.");
            } else if (!file.canRead()) {
                System.out.println("  Error: File is not readable. Please try again.");
            } else {
                System.out.println("  File found and readable. Processing...");
                break;
            }
        }
        return file;
    }

    // CSV Parser
    public static List<DataRecord> parseCSV(File file, String[] headersOut) throws IOException {
        List<DataRecord> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String headerLine = br.readLine();
            if (headerLine == null || headerLine.trim().isEmpty()) {
                throw new IOException("CSV file is empty or missing headers.");
            }

            String[] headers = headerLine.split(",", -1);
            for (int i = 0; i < headers.length; i++) {
                headers[i] = headers[i].trim().replaceAll("^\"|\"$", "");
            }
            System.arraycopy(headers, 0, headersOut, 0,
                    Math.min(headers.length, headersOut.length));

            String line;
            int lineNum = 1;
            while ((line = br.readLine()) != null) {
                lineNum++;
                if (line.trim().isEmpty()) continue;
                String[] rawValues = line.split(",", -1);
                if (rawValues.length != headers.length) {
                    System.out.println("  Skipping malformed row at line " + lineNum);
                    continue;
                }
                String[] cleanValues = new String[rawValues.length];
                for (int i = 0; i < rawValues.length; i++) {
                    cleanValues[i] = rawValues[i].trim().replaceAll("^\"|\"$", "");
                }
                records.add(new DataRecord(headers, cleanValues));
            }
        }

        if (records.isEmpty()) {
            throw new IOException("No valid data records found in CSV file.");
        }

        return records;
    }

    // Analytics Engine
    public static List<String> detectNumericColumns(String[] headers, List<DataRecord> records) {
        List<String> numericCols = new ArrayList<>();
        for (String col : headers) {
            final String currentCol = col;
            boolean allNumeric = true;
            for (DataRecord r : records) {
                if (!r.isNumeric(currentCol)) {
                    allNumeric = false;
                    break;
                }
            }
            if (allNumeric) numericCols.add(col);
        }
        return numericCols;
    }

    public static Map<String, double[]> computeNumericStats(List<String> numericCols,
            List<DataRecord> records) {
        Map<String, double[]> stats = new LinkedHashMap<>();
        for (String col : numericCols) {
            double[] values = new double[records.size()];
            for (int i = 0; i < records.size(); i++) {
                values[i] = records.get(i).getDouble(col);
            }
            Arrays.sort(values);
            double sum = 0;
            for (double v : values) sum += v;
            double mean = sum / values.length;
            double median;
            if (values.length % 2 == 0) {
                median = (values[values.length / 2 - 1] + values[values.length / 2]) / 2.0;
            } else {
                median = values[values.length / 2];
            }
            double variance = 0;
            for (double v : values) {
                variance += Math.pow(v - mean, 2);
            }
            variance /= values.length;
            double stdDev = Math.sqrt(variance);
            stats.put(col, new double[]{sum, mean, median, values[0],
                    values[values.length - 1], stdDev, values.length});
        }
        return stats;
    }

    public static Map<String, Map<String, Integer>> computeCategoricalStats(
            String[] headers, List<String> numericCols, List<DataRecord> records) {
        Map<String, Map<String, Integer>> catStats = new LinkedHashMap<>();
        for (String col : headers) {
            if (numericCols.contains(col)) continue;
            Map<String, Integer> counts = new LinkedHashMap<>();
            for (DataRecord r : records) {
                String val = r.get(col);
                if (val.isEmpty()) val = "(empty)";
                counts.merge(val, 1, Integer::sum);
            }
            catStats.put(col, counts);
        }
        return catStats;
    }

    // Display Formatter
    public static void displayResults(String filePath, String[] headers,
            List<DataRecord> records, Map<String, double[]> numericStats,
            Map<String, Map<String, Integer>> catStats) {

        String div = "============================================================";
        String sub = "------------------------------------------------------------";

        System.out.println("\n" + div);
        System.out.println("         CSV ANALYTICS REPORT");
        System.out.println(div);
        System.out.println("  Source File : " + filePath);
        System.out.println("  Total Rows  : " + records.size());
        System.out.println("  Columns     : " + String.join(", ", headers));
        System.out.println(div);

        if (!numericStats.isEmpty()) {
            System.out.println("\n  NUMERIC COLUMN SUMMARIES");
            System.out.println(sub);
            for (Map.Entry<String, double[]> e : numericStats.entrySet()) {
                double[] s = e.getValue();
                System.out.println("\n  Column: " + e.getKey());
                System.out.printf("    Count   : %.0f%n", s[6]);
                System.out.printf("    Sum     : %.2f%n", s[0]);
                System.out.printf("    Mean    : %.2f%n", s[1]);
                System.out.printf("    Median  : %.2f%n", s[2]);
                System.out.printf("    Min     : %.2f%n", s[3]);
                System.out.printf("    Max     : %.2f%n", s[4]);
                System.out.printf("    Std Dev : %.2f%n", s[5]);
            }
        }

        if (!catStats.isEmpty()) {
            System.out.println("\n\n  CATEGORICAL COLUMN SUMMARIES");
            System.out.println(sub);
            for (Map.Entry<String, Map<String, Integer>> e : catStats.entrySet()) {
                System.out.println("\n  Column: " + e.getKey());
                List<Map.Entry<String, Integer>> entries = new ArrayList<>(e.getValue().entrySet());
                entries.sort((a, b) -> b.getValue() - a.getValue());
                for (Map.Entry<String, Integer> entry : entries) {
                    double pct = (entry.getValue() * 100.0) / records.size();
                    System.out.printf("    %-25s : %d (%.1f%%)%n",
                            entry.getKey(), entry.getValue(), pct);
                }
            }
        }

        System.out.println("\n" + div + "\n");
    }

    // CSV Report Exporter
    public static void exportSummaryReport(String inputPath, String[] headers,
            List<DataRecord> records, Map<String, double[]> numericStats,
            Map<String, Map<String, Integer>> catStats) throws IOException {

        File inputFile = new File(inputPath);
        String outputPath = inputFile.getParent() + File.separator + "summary_report.csv";

        try (FileWriter fw = new FileWriter(outputPath);
             BufferedWriter bw = new BufferedWriter(fw)) {

            bw.write("\"Section\",\"Column\",\"Metric\",\"Value\"");
            bw.newLine();

            bw.write(csvRow("General", "All", "Total Rows", String.valueOf(records.size())));
            bw.newLine();
            bw.write(csvRow("General", "All", "Total Columns", String.valueOf(headers.length)));
            bw.newLine();
            bw.write(csvRow("General", "All", "Columns", String.join(" | ", headers)));
            bw.newLine();

            String[] metrics = {"Count", "Sum", "Mean", "Median", "Min", "Max", "Std Dev"};
            int[] indices = {6, 0, 1, 2, 3, 4, 5};
            for (Map.Entry<String, double[]> e : numericStats.entrySet()) {
                double[] s = e.getValue();
                for (int i = 0; i < metrics.length; i++) {
                    String val;
                    if (i == 0) {
                        val = String.format("%.0f", s[indices[i]]);
                    } else {
                        val = String.format("%.2f", s[indices[i]]);
                    }
                    bw.write(csvRow("Numeric", e.getKey(), metrics[i], val));
                    bw.newLine();
                }
            }

            for (Map.Entry<String, Map<String, Integer>> e : catStats.entrySet()) {
                for (Map.Entry<String, Integer> ce : e.getValue().entrySet()) {
                    double pct = (ce.getValue() * 100.0) / records.size();
                    String val = ce.getValue() + " (" + String.format("%.1f", pct) + "%)";
                    bw.write(csvRow("Categorical", e.getKey(), ce.getKey(), val));
                    bw.newLine();
                }
            }
        }

        System.out.println("  Summary report exported to: " + outputPath + "\n");
    }

    private static String csvRow(String... cells) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cells.length; i++) {
            if (i > 0) sb.append(',');
            sb.append('"').append(cells[i].replace("\"", "\"\"")).append('"');
        }
        return sb.toString();
    }

    // Main
    public static void main(String[] args) {
        System.out.println("\n==========================================");
        System.out.println("    CSV ANALYTICS REPORT GENERATOR");
        System.out.println("==========================================\n");

        Scanner scanner = new Scanner(System.in);
        File file = promptAndValidateFile(scanner);
        String[] headers = new String[100];

        try {
            List<DataRecord> records = parseCSV(file, headers);

            int colCount = 0;
            while (colCount < headers.length && headers[colCount] != null) colCount++;
            headers = Arrays.copyOf(headers, colCount);

            System.out.println("  Loaded " + records.size() + " records with " + colCount + " columns.");

            List<String> numericCols = detectNumericColumns(headers, records);
            Map<String, double[]> numericStats = computeNumericStats(numericCols, records);
            Map<String, Map<String, Integer>> catStats =
                    computeCategoricalStats(headers, numericCols, records);

            displayResults(file.getAbsolutePath(), headers, records, numericStats, catStats);
            exportSummaryReport(file.getAbsolutePath(), headers, records, numericStats, catStats);

        } catch (IOException e) {
            System.out.println("  Error processing file: " + e.getMessage());
        }

        scanner.close();
    }
}