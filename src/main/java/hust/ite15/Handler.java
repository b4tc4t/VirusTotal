package hust.ite15;

import hust.ite15.scan.*;
//import org.knowm.xchart.BitmapEncoder;
//import org.knowm.xchart.PieChart;
//import org.knowm.xchart.SwingWrapper;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Handler {
    static Scanner input = new Scanner(System.in);

    public static void fileScan(String apiKey) throws IOException, InterruptedException {

        System.out.println("a) Press ENTER to Browse Files...");
        System.out.println("b) OR INPUT Filename (same directory) to upload...");
        System.out.println("c) OR INPUT File's MD5/SHA1/SHA256 to search.");
        System.out.println("*******************");
        System.out.print("> Input: ");
        String filename = input.nextLine().strip();
        FileScan fileS = new FileScan();
        if (filename.isBlank()) {
            // Mode: Choose File to Upload
            UploadFile file = new UploadFile();
            fileS.setFilepath(file.getFile());
        } else {
            // Mode: Find File in directory to Upload
            File file = new File(filename);
            fileS.setFilepath(file);
        }

        if (fileS.isValid()) {
            System.out.println("...Uploading & Scanning...");
            fileS.post(apiKey);
        } else if (filename.matches("[a-fA-F0-9]{64}") || filename.matches("[a-fA-F0-9]{40}") || filename.matches("[a-fA-F0-9]{32}")) {
            // Mode: Try MD5/SHA1/SHA256 File Lookup
            System.out.println("...Assuming File MD5/SHA1/SHA256 Lookup...");
            fileS.setObjectId(filename);
            fileS.setFilepath(null);
            Thread.sleep(250);
        }

        fileS.getReport(apiKey);
        if (fileS.getJson() == null) {
            System.out.println("No file to analyze\n");
            Thread.sleep(1000);

            return;
        }
        Thread.sleep(1000);
    }

    public static void domainScan(String apikey) throws IOException, InterruptedException {
        System.out.printf("\nDOMAIN ANALYSIS\n");
        System.out.printf("\nEnter domain: ");

        String domain = input.nextLine().strip();
        DomainScan domainS = new DomainScan();
        domainS.setName(domain);

        if (domainS.isValid()) {
            domainS.getReport(apikey);
        } else{
            System.out.println("Invalid input");
            Thread.sleep(1000);
            return;
        }
        Thread.sleep(1000);
    }

    public static void urlScan(String apikey) throws InterruptedException, IOException {
        System.out.printf("\nURL ANALYSIS\n");
        System.out.printf("\nEnter URL: ");

        String url = input.nextLine().strip();

        System.out.println();
        URLScan urlS = new URLScan();
        if (!url.isBlank()) {
            urlS.setName(url);
            System.out.println("...Scanning...");
            urlS.post(apikey);
        } else {
            System.out.println("Invalid input");
            Thread.sleep(1000);
            return;
        }

        if (urlS.getObjectId() != null) {
            urlS.getReport(apikey);
        }
        Thread.sleep(1000);
    }

    public static void ipScan(String apikey) throws IOException, InterruptedException {
        System.out.printf("\nIP ANALYSIS\n");
        System.out.printf("\nEnter IP: ");

        String ip = input.nextLine().strip();
        IPScan ipS = new IPScan();
        ipS.setName(ip);

        if (ipS.isValid() ) {
            ipS.getReport(apikey);
        } else{
            System.out.println("ERROR: Invalid Input...\n");
            Thread.sleep(1000);
            return;
        }

        Thread.sleep(1000);
    }
}
