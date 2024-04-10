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
        // Accept employee information
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
        scanner.close();
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
            levelColors.put(1, new BaseColor(255, 0, 0));       // Red
            levelColors.put(2, new BaseColor(0, 255, 0));       // Green
            levelColors.put(3, new BaseColor(255, 255, 0));     // Yellow
            levelColors.put(4, new BaseColor(0, 0, 255));       // Blue
            levelColors.put(5, new BaseColor(128, 0, 128));     // Purple

            // Get the color for the specified level
            BaseColor backgroundColor = levelColors.get(level);

            // Generate badge PDF
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("badges/" + name + "_badge.pdf"));
            document.open();

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

            // Set background color
            Rectangle rect = new Rectangle(PageSize.A7);
            rect.setBackgroundColor(backgroundColor);
            document.add(rect);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
