import java.io.*;
import java.util.*;

public class CSVAnalyticsApp {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        File file;

        // REQUIRED LOOP STRUCTURE
        while (true) {
            System.out.print("Enter dataset file path: ");
            String path = input.nextLine();
            file = new File(path);

            // Required validation using exists() and isFile()
            if (file.exists() && file.isFile()) {

                // Additional validations
                if (!file.canRead()) {
                    System.out.println("Error: File cannot be read.\n");
                    continue;
                }

                if (!path.toLowerCase().endsWith(".csv")) {
                    System.out.println("Error: File is not in CSV format.\n");
                    continue;
                }

                break; // VALID FILE
            } else {
                System.out.println("Invalid file path. Please try again.\n");
            }
        }

        List<DataRecord> records = new ArrayList<>();

        // READ FILE USING BufferedReader
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {

                if (isHeader) {
                    isHeader = false;
                    continue; // skip header
                }

                String[] parts = line.split(",");

                if (parts.length < 2) {
                    System.out.println("Warning: Skipping invalid row.");
                    continue;
                }

                try {
                    String name = parts[0].trim();
                    double amount = Double.parseDouble(parts[1].trim());

                    records.add(new DataRecord(name, amount));

                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid number format. Row skipped.");
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            input.close();
            return;
        }

        // PROCESS DATA
        double total = 0;
        for (DataRecord record : records) {
            total += record.getAmount();
        }

        double average = records.size() > 0 ? total / records.size() : 0;

        // FORMATTED OUTPUT
        System.out.println("\n========== ANALYTICS SUMMARY ==========");
        System.out.printf("Total Records : %d%n", records.size());
        System.out.printf("Total Amount  : %.2f%n", total);
        System.out.printf("Average       : %.2f%n", average);
        System.out.println("=======================================\n");

        // EXPORT SUMMARY TO CSV
        try (FileWriter writer = new FileWriter("summary_report.csv")) {

            writer.write("Total Records,Total Amount,Average\n");
            writer.write(records.size() + "," + total + "," + average + "\n");

            System.out.println("Summary report exported successfully to summary_report.csv");

        } catch (IOException e) {
            System.out.println("Error writing summary report: " + e.getMessage());
        }

        input.close();
    }
}