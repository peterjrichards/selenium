package com.liferay.sales.selenium.insurance;

import com.liferay.sales.selenium.api.ClickpathBase;
import com.liferay.sales.selenium.api.ScriptManager;
import com.liferay.sales.selenium.chrome.ChromeDriverInitializer;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.Locale;

public class LiferayInsuranceScriptManager extends ScriptManager {

    public static void main(String[] args) {
        try {
            doIt(args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void doIt(String[] args) {
// Before starting the script, make adjustments in this top block
// to reflect the behavior that you need.
// Inspect the clickpaths.
// If you run the A/B-Test, note that you'll have to prepare the 
// content according to the documentation
// https://docs.google.com/document/d/1h2E7UUt_i3yqwge25Pd8YXOLHt3Jujz4VBAdgHSeKQw/edit#heading=h.w4lf8kpcller

//			System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");

        String[][] insuranceUsers = readUserCSV("/home/olaf/insuranceUsers.csv" );
        String[][] insuranceAutoUsers = readUserCSV("/home/olaf/insuranceAutoUsers.csv" );
        String baseUrl = "https://webserver-lctidcanalyst2023v2-prd.lfr.cloud";
        int repeats = 1000;
        boolean headless = true;
        /**
         * Set up these referrers in your /etc/hosts and make sure they have a link
         * containing the text "IDC Demo" that can be clicked. Or don't set them to
         * click links without referrers. (default, if not set: empty array)
         */
        String[] referrers = new String[]{
                "http://twitter.example.com/links.html",
                "http://facebook.example.com/links.html"
        };
        LiferayInsuranceBaseClickpath.REFERRERS = referrers;

        String[] arguments;
        if (headless) {
            arguments = new String[]{"--headless", "--remote-allow-origins=*"};
        } else {
            arguments = new String[]{"--remote-allow-origins=*"};
        }

        ClickpathBase[] paths = new ClickpathBase[]{
                new LiferayInsuranceClickpathLifeLandingABTest(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpathAboutABTest(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpathAboutABTest(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpathAboutABTest(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpathAboutABTest(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpathAboutABTest(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpathAboutABTest(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpathAboutABTest(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpathAboutABTest(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpath1(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpath1(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpath1(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpath1(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpath2(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpath2(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpath3(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpath4(new ChromeDriverInitializer(arguments), baseUrl, insuranceAutoUsers[0][0], insuranceAutoUsers[0][1])
//				,new LiferayInsuranceClickpathContactABTest(new ChromeDriverInitializer(arguments), baseUrl)
//				,new LiferayInsuranceClickpathContactABTest(new ChromeDriverInitializer(arguments), baseUrl)
        };


// Typically, nothing more to "configure" below this line. 
// Anything that you need to customize your scripts is above.

        System.out.println("Running " + paths.length + " clickpaths for " + repeats + " times" );

        long start = System.currentTimeMillis();
        LinkedList<String> log = new LinkedList<String>();

        for (int i = 0; i < repeats; i++) {
            long thisStart = System.currentTimeMillis();
            ClickpathBase path = null;
            int pos = (int) (Math.random() * insuranceUsers.length);
            String[] user = insuranceUsers[pos];
            pos = (int) (Math.random() * paths.length);
            path = paths[pos];
            System.out.println("Number of failures so far:" + log.size());
            System.out.println("#" + i + "/" + repeats + ": Running user " + user[0] + " with path "
                    + pos + " (" + path.getClass().getSimpleName() + ", using "
                    + path.getDriver().getClass().getSimpleName()
                    + ")" );
            path.setDefaultSleep(4000);
            try {
                path.run(user[0], user[1]);
            } catch (Exception e) {
                System.out.println("handling " + e.getClass().getSimpleName() + ": " + e.getMessage());
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String tstamp = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss", Locale.ENGLISH).format(LocalDateTime.now());
                log.add(
                        tstamp + "\n" +
                                "" + i + ", [" + user[0] + ", path" + pos + "]\n" +
                                e.getClass().getName() + " " +
                                e.getMessage() + "\n" +
                                sw
                );
                String hint = "" + i + "-" + e.getClass().getSimpleName() + "-" + e.getMessage().replace(' ', '-');
                try {
                    path.writePageToDisk("ERROR", hint);
                } catch (IOException e1) {
                    System.out.println("ERROR (during error handling): " + hint);
                    e1.printStackTrace();
                }

            }
            if (path != null) {
                path.quit();
                path = null;
            }
            long now = System.currentTimeMillis();
            long runtime = now - start;
            long expectedTimeSpan = (runtime / (i + 1)) * repeats;
            long thisTimeSpan = now - thisStart;
            Date expectedEnd = new Date(start + expectedTimeSpan);
            System.out.println("Runtime (current/average) in sec: " + (thisTimeSpan / 1000) + "/" + (runtime / ((i + 1) * 1000)));
            System.out.println("Expected remaining run time:      " + expectedEnd);
            System.out.println("Current time:                     " + new Date());

            System.out.println();
        }

        System.out.println("==================================================" );
        System.out.println("End of run" );
        System.out.println("Failed attempts: " + log.size());
        for (String string : log) {
            System.out.println(string);
            System.out.println("---------------------------------------------" );
        }
    }
}
