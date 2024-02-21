package com.liferay.sales.selenium.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class ScriptManager {
    /**
     * Syntactic sugar for System.out.println
     *
     * @param message
     */
    protected static void log(String message) {
        System.out.println(message);
    }

    /**
     * Read a stupidly simple CSV format: No title, content is just
     * rows with "name,password" (comma-separated, no escaping, no quotes)
     * Luxury trimming done to individual entries without extra charge.
     *
     * @param filename
     * @return a two-dimensional array containing the users
     */
    public static String[][] readUserCSV(String filename) {
        ArrayList<String[]> content = new ArrayList<String[]>();
        try (Scanner scanner = new Scanner(new File(filename))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();

                try (Scanner rowScanner = new Scanner(line)) {
                    ArrayList<String> row = new ArrayList<String>(2);
                    rowScanner.useDelimiter(",");
                    while (rowScanner.hasNext()) {
                        row.add(rowScanner.next().trim());
                    }
                    content.add(row.toArray(new String[row.size()]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content.toArray(new String[content.size()][]);
    }
}
