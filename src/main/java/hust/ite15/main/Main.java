package hust.ite15.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Main {
    public static String apiKey = "";
    private static boolean running = true;
    public static Scanner input = new Scanner(System.in);
    public static String exportScanType = "";

    private static boolean loadConfig() {
        FileInputStream configLoader = null;
        try {
            Properties prop = new Properties();
            String configPath = System.getProperty("user.dir") + "/src/main/resources/config.properties";
            configLoader = new FileInputStream(configPath);
            prop.load(configLoader);
            apiKey = prop.getProperty("apikey");
            configLoader.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void mainLoop() throws IOException, InterruptedException {
        int choice = 0;

        System.out.println("  ______      __        ______             __       __   __ ");
        System.out.println(" /_  __/___  / /_____ _/ / __ \\_________  / /____  / /__/ /_");
        System.out.println("  / / / __ \\/ __/ __ `/ / /_/ / ___/ __ \\/ __/ _ \\/ //_/ __/");
        System.out.println(" / / / /_/ / /_/ /_/ / / ____/ /  / /_/ / /_/  __/ ,< / /_  ");
        System.out.println("/_/  \\____/\\__/\\__,_/_/_/   /_/   \\____/\\__/\\___/_/|_|\\__/  ");
        System.out.println();
        System.out.println("1. Scan a file");
        System.out.println("2. Scan a domain");
        System.out.println("3. Scan an URL");
        System.out.println("4. Scan an IP address");
        System.out.println("5. Exit");

        System.out.print("\nEnter a choice: ");

        if (input.hasNextInt()) {
            choice = input.nextInt();
            input.nextLine();
        } else {
            input.nextLine();
            System.out.println("Invalid input!");
        }

        switch (choice) {
            case 1 -> {
                Handler.fileScan(apiKey);
                exportScanType="file";
            }
            case 2 -> {
                Handler.domainScan(apiKey);
                exportScanType="domain";
            }
            case 3 -> {
                Handler.urlScan(apiKey);
                exportScanType="url";
            }
            case 4 -> {
                Handler.ipScan(apiKey);
                exportScanType="ip";
            }
            case 5 -> System.exit(0);
            default -> System.out.println("Invalid input!");
        }

        // Exporting Options:
        System.out.println("-------------------------------------------------------------");
        System.out.println("\n\n");
        System.out.println(">>> ANALYSIS COMPLETED, DO YOU WANT TO EXPORT THE RESULT? <<<");
        System.out.println("""
               1. Export to JSON
               2. Export to XLSX
               3. Export Pie chart only (PNG)
               4. Return to main menu
        """);
        System.out.print("\nEnter a choice: ");

        if (input.hasNextInt()) {
            choice = input.nextInt();
            input.nextLine();
        } else {
            input.nextLine();
            System.out.println("Invalid input!");
        }

        switch(choice) {
            case 1 -> Handler.exportJSON(exportScanType);
            case 2 -> Handler.exportXLSX(exportScanType);
            case 3 -> Handler.exportChart(exportScanType);
            case 4 -> System.out.println("Returning to main menu");
            default -> System.out.println("Invalid input!");
        }

        exportScanType = "";
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (!loadConfig()) {
            System.out.println("Failed to load config file");
            return;
        }

        while (running) {
            mainLoop();
        }
    }
}