import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrelimCalculator extends JFrame {

    // Input fields
    private JTextField txtAttendance, txtLab1, txtLab2, txtLab3;

    // Output fields
    private JTextField txtLabAvg, txtClassStanding, txtPass, txtExcellent;

    private JTextArea txtRemark;

    public PrelimCalculator() {
        setTitle("Prelim Exam Calculator");
        setSize(520, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // ===== TITLE =====
        JLabel lblTitle = new JLabel("PRELIM EXAM CALCULATOR", JLabel.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(lblTitle, BorderLayout.NORTH);

        // ===== MAIN PANEL =====
        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // ===== INPUT PANEL =====
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Student Inputs"));

        txtAttendance = new JTextField();
        txtLab1 = new JTextField();
        txtLab2 = new JTextField();
        txtLab3 = new JTextField();

        inputPanel.add(new JLabel("Attendance (0–100):"));
        inputPanel.add(txtAttendance);

        inputPanel.add(new JLabel("Lab Work 1:"));
        inputPanel.add(txtLab1);

        inputPanel.add(new JLabel("Lab Work 2:"));
        inputPanel.add(txtLab2);

        inputPanel.add(new JLabel("Lab Work 3:"));
        inputPanel.add(txtLab3);

        // Spacer
        inputPanel.add(new JLabel(""));
        inputPanel.add(new JLabel(""));

        // ===== RESULT PANEL =====
        JPanel resultPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        resultPanel.setBorder(BorderFactory.createTitledBorder("Computed Results"));

        txtLabAvg = new JTextField();
        txtClassStanding = new JTextField();
        txtPass = new JTextField();
        txtExcellent = new JTextField();

        txtLabAvg.setEditable(false);
        txtClassStanding.setEditable(false);
        txtPass.setEditable(false);
        txtExcellent.setEditable(false);

        resultPanel.add(new JLabel("Lab Work Average:"));
        resultPanel.add(txtLabAvg);

        resultPanel.add(new JLabel("Class Standing:"));
        resultPanel.add(txtClassStanding);

        resultPanel.add(new JLabel("Exam Needed (Pass – 75):"));
        resultPanel.add(txtPass);

        resultPanel.add(new JLabel("Exam Needed (Excellent – 100):"));
        resultPanel.add(txtExcellent);

        // Spacer
        resultPanel.add(new JLabel(""));
        resultPanel.add(new JLabel(""));

        mainPanel.add(inputPanel);
        mainPanel.add(resultPanel);

        add(mainPanel, BorderLayout.CENTER);

        // ===== BOTTOM PANEL =====
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));

        JButton btnCompute = new JButton("COMPUTE RESULTS");
        btnCompute.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bottomPanel.add(btnCompute, BorderLayout.NORTH);

        txtRemark = new JTextArea(3, 30);
        txtRemark.setEditable(false);
        txtRemark.setLineWrap(true);
        txtRemark.setWrapStyleWord(true);
        txtRemark.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRemark.setBorder(BorderFactory.createTitledBorder("Remark"));
        bottomPanel.add(txtRemark, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        // ===== BUTTON ACTION =====
        btnCompute.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                computeResults();
            }
        });
    }

    private void computeResults() {
        try {
            double attendance = Double.parseDouble(txtAttendance.getText());
            double lab1 = Double.parseDouble(txtLab1.getText());
            double lab2 = Double.parseDouble(txtLab2.getText());
            double lab3 = Double.parseDouble(txtLab3.getText());

            double labAverage = (lab1 + lab2 + lab3) / 3;
            double classStanding = (attendance * 0.40) + (labAverage * 0.60);

            double requiredPass = (75 - (classStanding * 0.70)) / 0.30;
            double requiredExcellent = (100 - (classStanding * 0.70)) / 0.30;

            txtLabAvg.setText(String.format("%.2f", labAverage));
            txtClassStanding.setText(String.format("%.2f", classStanding));
            txtPass.setText(String.format("%.2f", requiredPass));
            txtExcellent.setText(String.format("%.2f", requiredExcellent));

            if (requiredPass > 100) {
                txtRemark.setText(
                        "Sorry. Based on your current class standing, passing the Prelim period is not possible even with a perfect exam."
                );
            } else if (requiredPass <= 0) {
                txtRemark.setText(
                        "Excellent work! You have already passed the Prelim period based on your class standing."
                );
            } else {
                txtRemark.setText(
                        "You are still on track. Aim for the required Prelim Exam score to pass the period."
                );
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values only.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {
        new PrelimCalculator().setVisible(true);
    }
}
