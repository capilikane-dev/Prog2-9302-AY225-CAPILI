package Works;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class AttendanceTracker {

    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Attendance Tracker");
        frame.setSize(450, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use GridBagLayout for proper alignment
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Labels
        JLabel nameLabel = new JLabel("Attendance Name:");
        JLabel courseLabel = new JLabel("Course / Year:");
        JLabel timeInLabel = new JLabel("Time In:");
        JLabel timeOutLabel = new JLabel("Time Out:");
        JLabel signatureLabel = new JLabel("E-Signature:");

        // Text fields
        JTextField nameField = new JTextField(20);
        JTextField courseField = new JTextField(20);
        JTextField timeInField = new JTextField(20);
        JTextField timeOutField = new JTextField(20);
        JTextField signatureField = new JTextField(20);

        // Non-editable fields
        timeInField.setEditable(false);
        timeOutField.setEditable(false);
        signatureField.setEditable(false);

        // Format date/time nicely
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Set Time In automatically
        String timeIn = LocalDateTime.now().format(dtf);
        timeInField.setText(timeIn);

        // Generate E-Signature
        String eSignature = UUID.randomUUID().toString();
        signatureField.setText(eSignature);

        // Row 0 - Name
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(nameLabel, gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        // Row 1 - Course/Year
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(courseLabel, gbc);
        gbc.gridx = 1;
        panel.add(courseField, gbc);

        // Row 2 - Time In
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(timeInLabel, gbc);
        gbc.gridx = 1;
        panel.add(timeInField, gbc);

        // Row 3 - Time Out
        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(timeOutLabel, gbc);
        gbc.gridx = 1;
        panel.add(timeOutField, gbc);

        // Row 4 - E-Signature
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(signatureLabel, gbc);
        gbc.gridx = 1;
        panel.add(signatureField, gbc);

        // Row 5 - Buttons
        JPanel buttonPanel = new JPanel();
        JButton timeOutButton = new JButton("Record Time Out");
        JButton submitButton = new JButton("Submit Attendance");

        buttonPanel.add(timeOutButton);
        buttonPanel.add(submitButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        panel.add(buttonPanel, gbc);

        // Action for Time Out button
        timeOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String timeOut = LocalDateTime.now().format(dtf);
                timeOutField.setText(timeOut);
            }
        });

        // Action for Submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Simple message box to simulate submission
                String message = "Attendance Submitted!\n"
                        + "Name: " + nameField.getText() + "\n"
                        + "Course/Year: " + courseField.getText() + "\n"
                        + "Time In: " + timeInField.getText() + "\n"
                        + "Time Out: " + timeOutField.getText() + "\n"
                        + "E-Signature: " + signatureField.getText();
                JOptionPane.showMessageDialog(frame, message, "Confirmation", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add panel to frame
        frame.add(panel);

        // Center window
        frame.setLocationRelativeTo(null);

        // Show frame
        frame.setVisible(true);
    }
}
