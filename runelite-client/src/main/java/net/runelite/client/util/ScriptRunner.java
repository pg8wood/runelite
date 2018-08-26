package net.runelite.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ScriptRunner {

    /**
     * Runs a subprocess and returns its output
     * @param script The script to run and its arguments
     * @return The contents of the process' standard output
     */
    static String shell(String[] script) {
        try
        {
            Process recordControlStripPrefs = Runtime.getRuntime().exec(script);
            BufferedReader scriptInputReader = new BufferedReader(new InputStreamReader(recordControlStripPrefs.getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = scriptInputReader.readLine()) != null) {
                sb.append(line);
            }

            scriptInputReader.close();

            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
