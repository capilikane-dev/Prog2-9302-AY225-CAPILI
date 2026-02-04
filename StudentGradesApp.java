import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;

public class StudentGradesApp extends JFrame {

    JTable table;
    DefaultTableModel model;

    JTextField txtID, txtFirst, txtLast;
    JTextField txtLab1, txtLab2, txtLab3, txtPrelim, txtAttendance;

    JButton btnAdd, btnDelete;

    public StudentGradesApp() {
        setTitle("Student Grade Records");
        setSize(1000, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== TABLE =====
        String[] columns = {
                "StudentID", "First Name", "Last Name",
                "LAB WORK 1", "LAB WORK 2", "LAB WORK 3",
                "PRELIM EXAM", "ATTENDANCE GRADE"
        };

        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Load CSV
        loadCSV();

        // ===== FORM PANEL =====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int y = 0;

        // Student ID
        addField(formPanel, gbc, y++, "Student ID:", txtID = new JTextField(12));

        // First Name
        addField(formPanel, gbc, y++, "First Name:", txtFirst = new JTextField(12));

        // Last Name
        addField(formPanel, gbc, y++, "Last Name:", txtLast = new JTextField(12));

        // Lab Works
        addField(formPanel, gbc, y++, "Lab Work 1:", txtLab1 = new JTextField(12));
        addField(formPanel, gbc, y++, "Lab Work 2:", txtLab2 = new JTextField(12));
        addField(formPanel, gbc, y++, "Lab Work 3:", txtLab3 = new JTextField(12));

        // Exams
        addField(formPanel, gbc, y++, "Prelim Exam:", txtPrelim = new JTextField(12));
        addField(formPanel, gbc, y++, "Attendance Grade:", txtAttendance = new JTextField(12));

        // ===== BUTTON PANEL =====
        JPanel buttonPanel = new JPanel();
        btnAdd = new JButton("Add");
        btnDelete = new JButton("Delete");
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDelete);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(formPanel, BorderLayout.CENTER);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // ===== ACTIONS =====
        btnAdd.addActionListener(e -> addRow());
        btnDelete.addActionListener(e -> deleteRow());
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int y,
                          String label, JTextField field) {

        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private void loadCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("MOCK_DATA.csv"))) {
            String line;
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                model.addRow(data);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Error reading CSV file.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addRow() {
        JTextField[] fields = {
                txtID, txtFirst, txtLast,
                txtLab1, txtLab2, txtLab3,
                txtPrelim, txtAttendance
        };

        for (JTextField f : fields) {
            if (f.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.");
                return;
            }
        }

        model.addRow(new String[]{
                txtID.getText(),
                txtFirst.getText(),
                txtLast.getText(),
                txtLab1.getText(),
                txtLab2.getText(),
                txtLab3.getText(),
                txtPrelim.getText(),
                txtAttendance.getText()
        });

        for (JTextField f : fields) {
            f.setText("");
        }
    }

    private void deleteRow() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
            return;
        }

        model.removeRow(row);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentGradesApp().setVisible(true));
    }
}

