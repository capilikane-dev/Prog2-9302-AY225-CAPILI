import java.util.*;

/**
 * Represents a single row/record from the CSV dataset.
 */
public class DataRecord {
    private final Map<String, String> fields;

    public DataRecord(String[] headers, String[] values) {
        fields = new LinkedHashMap<>();
        for (int i = 0; i < headers.length && i < values.length; i++) {
            fields.put(headers[i].trim(), values[i].trim().replaceAll("^\"|\"$", ""));
        }
    }

    public String get(String column) {
        return fields.getOrDefault(column, "");
    }

    public Set<String> getColumns() {
        return fields.keySet();
    }

    public boolean isNumeric(String column) {
        String val = get(column);
        if (val.isEmpty()) return false;
        try {
            Double.parseDouble(val);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public double getDouble(String column) {
        return Double.parseDouble(get(column));
    }

    @Override
    public String toString() {
        return fields.toString();
    }
}
