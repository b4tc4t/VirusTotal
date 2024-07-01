package hust.ite15.main;

import hust.ite15.scan.*;
//import org.knowm.xchart.BitmapEncoder;
//import org.knowm.xchart.PieChart;
//import org.knowm.xchart.SwingWrapper;

import java.io.IOException;
import java.util.Scanner;

public class Handler {
    static Scanner input = new Scanner(System.in);


    static FileScan fileS = new FileScan();
    static URLScan urlS = new URLScan();
    static DomainScan domainS = new DomainScan();
    static IPScan ipS = new IPScan();
    public static void fileScan(String apiKey) throws IOException, InterruptedException {

        System.out.println("Press Enter to browse a file or input MD5/SHA-1/SHA-256 hash to scan: ");
        System.out.println();
        System.out.print("Enter your choice: ");
        String filename = input.nextLine().strip();

        if (filename.isBlank()) {
            // Mode: Choose File to Upload
            UploadFile file = new UploadFile();
            fileS.setFilepath(file.getFile());
        }

        if (fileS.isValid()) {
            System.out.println("Uploading...");
            fileS.post(apiKey);
        } else if (filename.matches("[a-fA-F0-9]{64}") || filename.matches("[a-fA-F0-9]{40}") || filename.matches("[a-fA-F0-9]{32}")) {
            System.out.println("Scanning using hash");
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

        fileS.printSummary();
        Thread.sleep(1000);
    }

    public static void domainScan(String apikey) throws IOException, InterruptedException {
        System.out.print("\nDOMAIN ANALYSIS\n");
        System.out.print("\nEnter domain: ");

        String domain = input.nextLine().strip();

        domainS.setName(domain);

        if (domainS.isValid()) {
            domainS.getReport(apikey);
        } else{
            System.out.println("Invalid input");
            Thread.sleep(1000);
            return;
        }

        domainS.printSummary();
        Thread.sleep(1000);
    }

    public static void urlScan(String apikey) throws InterruptedException, IOException {
        System.out.print("\nURL ANALYSIS\n");
        System.out.print("\nEnter URL: ");

        String url = input.nextLine().strip();

        System.out.println();

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

        urlS.printSummary();
        Thread.sleep(1000);
    }

    public static void ipScan(String apikey) throws IOException, InterruptedException {
        System.out.print("\nIP ANALYSIS\n");
        System.out.print("\nEnter IP: ");

        String ip = input.nextLine().strip();

        ipS.setName(ip);

        if (ipS.isValid() ) {
            ipS.getReport(apikey);
        } else{
            System.out.println("ERROR: Invalid Input...\n");
            Thread.sleep(1000);
            return;
        }

        ipS.printSummary();
        Thread.sleep(1000);
    }

    // EXPORT HANDLER
    public static void exportJSON(String type){
        switch(type) {
            case "file" -> fileS.toJsonReport();
            case "domain" -> domainS.toJsonReport();
            case "url" -> urlS.toJsonReport();
            case "ip" -> ipS.toJsonReport();
        }
    }

    public static void exportXLSX(String type){
        switch(type) {
            case "file" -> fileS.toExcelReport("FILE");
            case "domain" -> domainS.toExcelReport("DOMAIN");
            case "url" -> urlS.toExcelReport("URL");
            case "ip" -> ipS.toExcelReport("IP");
        }
    }

    public static void exportChart(String type) throws IOException{
        switch(type){
            case "file" -> fileS.writeChart();
            case "domain" -> domainS.writeChart();
            case "url" -> urlS.writeChart();
            case "ip" -> ipS.writeChart();
        }
    }


}
