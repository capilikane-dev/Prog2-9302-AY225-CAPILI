import java.io.*;
import java.util.*;

class DataRecord {
    private int quantity;
    private double price;

    public DataRecord(int quantity, double price) {
        this.quantity = quantity;
        this.price = price;
    }

    public double getTotal() {
        return quantity * price;
    }

    public int getQuantity() {
        return quantity;
    }
}

public class CSVProcessor {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            List<DataRecord> records = new ArrayList<>();
            String fileName = "vgchartz-2024.csv"; // the expected CSV file in the project folder

            System.out.print("Enter the CSV file name: ");
            String userInput = scanner.nextLine().trim();

            // Check if the user entered the correct file
            if (!userInput.equals(fileName)) {
                System.out.println("Error: File does not exist or wrong filename.");
                return;
            }

            File file = new File(fileName);

            if (!file.exists() || !file.isFile()) {
                System.out.println("Error: File does not exist in the project folder.");
                return;
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String header = br.readLine();
                if (header == null || !header.contains(",")) {
                    System.out.println("Error: Not a valid CSV file.");
                    return;
                }

                String line;
                int lineNumber = 2;

                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length < 3) {
                        System.out.println("Skipping invalid line " + lineNumber + ": " + line);
                        lineNumber++;
                        continue;
                    }

                    try {
                        int quantity = Integer.parseInt(parts[1].trim());
                        double price = Double.parseDouble(parts[2].trim());
                        records.add(new DataRecord(quantity, price));
                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid numbers on line " + lineNumber + ": " + line);
                    }
                    lineNumber++;
                }

            } catch (IOException e) {
                System.out.println("Error reading CSV: " + e.getMessage());
                return;
            }

            if (records.isEmpty()) {
                System.out.println("No valid data found in CSV.");
                return;
            }

            // Generate summary
            int totalItems = 0;
            double totalRevenue = 0;

            for (DataRecord r : records) {
                totalItems += r.getQuantity();
                totalRevenue += r.getTotal();
            }

            System.out.println("\nCSV Analytics Summary:");
            System.out.println("-------------------------");
            System.out.println("Total Items Sold: " + totalItems);
            System.out.printf("Total Revenue: $%.2f%n", totalRevenue);

            // Export summary
            try (FileWriter writer = new FileWriter("summary_report.csv")) {
                writer.append("Metric,Value\n");
                writer.append("Total Items Sold," + totalItems + "\n");
                writer.append(String.format("Total Revenue,%.2f\n", totalRevenue));
                System.out.println("\nSummary exported to summary_report.csv");
            } catch (IOException e) {
                System.out.println("Error writing summary CSV: " + e.getMessage());
            }
        }
    }
}