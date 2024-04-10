package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BarcodeQRCode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of badges to generate (up to 5):");
        int numBadges = scanner.nextInt();

        if (numBadges > 0 && numBadges <= 5) {
            for (int i = 0; i < numBadges; i++) {
                System.out.println("\nBadge #" + (i + 1) + ":");
                generateBadge();
            }
            System.out.println("\nAll badges generated successfully!");
        } else {
            System.out.println("Invalid number of badges. Please enter a number between 1 and 5.");
        }

        scanner.close();
    }

    public static void generateBadge() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter employee name:");
        String name = scanner.nextLine();
        System.out.println("Enter employee ID:");
        String id = scanner.nextLine();
        System.out.println("Enter employee department:");
        String department = scanner.nextLine();
        System.out.println("Enter employee level (1-5):");
        int level = scanner.nextInt();

        // Generate PDF
        generateBadgePDF(name, id, department, level);

        System.out.println("Badge generated successfully!");
    }

    public static void generateBadgePDF(String name, String id, String department, int level) {
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
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);

            // Add employee information
            document.add(new Paragraph("Employee Badge", font));
            document.add(new Paragraph("Name: " + name, font));
            document.add(new Paragraph("ID: " + id, font));
            document.add(new Paragraph("Department: " + department, font));
            document.add(new Paragraph("Level: " + level, font));

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
}
