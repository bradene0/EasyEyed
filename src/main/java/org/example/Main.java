package org.example;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;

import java.io.File;
import java.io.FileOutputStream;
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
        System.out.println("Enter badge number:");
        String badgeNumber = scanner.nextLine();

        // Generate PDF
        generateBadgePDF(name, id, department, badgeNumber);
        System.out.println("Badge generated successfully!");
        scanner.close();
    }

    public static void generateBadgePDF(String name, String id, String department, String badgeNumber) {
        Document document = new Document();
        try {
            // Create directory if it doesn't exist
            File directory = new File("badges");
            if (!directory.exists()) {
                directory.mkdir();
            }

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("badges/" + badgeNumber + ".pdf"));
            document.open();

            // Custom font
            Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK);

            // Add employee information
            document.add(new Paragraph("Employee Badge", font));
            document.add(new Paragraph("Name: " + name, font));
            document.add(new Paragraph("ID: " + id, font));
            document.add(new Paragraph("Department: " + department, font));

            // Add QR code
            BarcodeQRCode qrCode = new BarcodeQRCode(id, 1, 1, null);
            Image qrImage = qrCode.getImage();
            qrImage.scaleAbsolute(100, 100);
            document.add(qrImage);

            // Set background color
            Rectangle rect = new Rectangle(PageSize.A7);
            rect.setBackgroundColor(new BaseColor(255, 255, 204));
            document.add(rect);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
