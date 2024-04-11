package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfWriter;

public class Main extends JFrame {

    private JLabel numBadgesLabel;
    private JTextField numBadgesField;
    private JButton generateButton;
    private JTabbedPane tabbedPane;

    public Main() {
        super("Employee Badge Generator");

        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        numBadgesLabel = new JLabel("How many badges? Up to five:");
        topPanel.add(numBadgesLabel);
        numBadgesField = new JTextField(5);
        topPanel.add(numBadgesField);
        generateButton = new JButton("Generate Badges");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int numBadges = Integer.parseInt(numBadgesField.getText());
                if (numBadges > 0 && numBadges <= 5) {
                    createBadgeTabs(numBadges);
                    topPanel.setVisible(false); // Hide badge number selection fields
                } else {
                    JOptionPane.showMessageDialog(Main.this, "Invalid number of badges. Please enter a number between 1 and 5.");
                }
            }
        });
        topPanel.add(generateButton);
        add(topPanel, BorderLayout.NORTH);

        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createBadgeTabs(int numBadges) {
        for (int i = 0; i < numBadges; i++) {
            JPanel panel = new JPanel(new GridLayout(6, 2));

            JLabel nameLabel = new JLabel("Name:");
            JTextField nameField = new JTextField();
            JLabel idLabel = new JLabel("ID:");
            JTextField idField = new JTextField();
            JLabel departmentLabel = new JLabel("Department:");
            JTextField departmentField = new JTextField();
            JLabel levelLabel = new JLabel("Level:");
            JTextField levelField = new JTextField();

            panel.add(nameLabel);
            panel.add(nameField);
            panel.add(idLabel);
            panel.add(idField);
            panel.add(departmentLabel);
            panel.add(departmentField);
            panel.add(levelLabel);
            panel.add(levelField);

            JButton generateBadgeButton = new JButton("Generate Badge");
            generateBadgeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String name = nameField.getText();
                    String id = idField.getText();
                    String department = departmentField.getText();
                    int level;
                    try {
                        level = Integer.parseInt(levelField.getText());
                        if (level < 1 || level > 5) {
                            JOptionPane.showMessageDialog(Main.this, "Invalid level. Please enter a number between 1 and 5.");
                            return;
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(Main.this, "Invalid level. Please enter a number between 1 and 5.");
                        return;
                    }

                    generateBadgePDF(name, id, department, level);
                    JOptionPane.showMessageDialog(Main.this, "Badge generated successfully!");

                    // Close the tab after generating badge
                    int index = tabbedPane.indexOfComponent(panel);
                    if (index != -1) {
                        tabbedPane.removeTabAt(index);
                        if (tabbedPane.getTabCount() == 0) {
                            dispose(); // Close the main window if all badges are generated
                        }
                    }
                }
            });

            panel.add(generateBadgeButton);

            tabbedPane.addTab("Badge #" + (i + 1), panel);
        }
    }

    private void generateBadgePDF(String name, String id, String department, int level) {
        Document document = new Document();

        try {
            // Create directory if it doesn't exist
            File directory = new File("badges");
            if (!directory.exists()) {
                directory.mkdir();
            }

            // Define badge colors for each level
            Map<Integer, BaseColor> levelColors = new HashMap<>();
            levelColors.put(1, new BaseColor(255, 204, 204)); // Light Red
            levelColors.put(2, new BaseColor(204, 255, 204)); // Light Green
            levelColors.put(3, new BaseColor(255, 255, 204)); // Light Yellow
            levelColors.put(4, new BaseColor(204, 204, 255)); // Light Blue
            levelColors.put(5, new BaseColor(229, 204, 255)); // Light Purple

            // Get the color for the specified level
            BaseColor backgroundColor = levelColors.get(level);

            // Generate badge PDF
            String fileName = name.replaceAll("\\s+", "_") + "_badge.pdf"; // Replace spaces in name with underscores
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("badges/" + fileName));
            document.open();

            // Set background color of the document
            Rectangle rect = new Rectangle(document.getPageSize());
            rect.setBackgroundColor(backgroundColor);
            document.add(rect);

// Custom font
            com.itextpdf.text.Font font = com.itextpdf.text.FontFactory.getFont(com.itextpdf.text.FontFactory.HELVETICA_BOLD, 16, com.itextpdf.text.BaseColor.BLACK);

// Add employee information
            document.add(new com.itextpdf.text.Paragraph("Employee Badge", font));
            document.add(new com.itextpdf.text.Paragraph("Name: " + name, font));
            document.add(new com.itextpdf.text.Paragraph("ID: " + id, font));
            document.add(new com.itextpdf.text.Paragraph("Department: " + department, font));
            document.add(new com.itextpdf.text.Paragraph("Level: " + level, font));

            // Add QR code
            BarcodeQRCode qrCode = new BarcodeQRCode(id, 1, 1, null);
            Image qrImage = qrCode.getImage();
            qrImage.scaleAbsolute(100, 100);
            document.add(qrImage);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Main();
            }
        });
    }
}
