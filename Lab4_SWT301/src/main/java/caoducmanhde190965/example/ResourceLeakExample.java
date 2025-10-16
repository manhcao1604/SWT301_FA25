package caoducmanhde190965.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ResourceLeakExample {
    private static final Logger LOGGER = Logger.getLogger(ResourceLeakExample.class.getName());

    public static void main(String[] args) {

        try (BufferedReader reader = new BufferedReader(new FileReader("data.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {

            LOGGER.log(Level.SEVERE, "An error occurred while reading the file.", e);
        }
    }
}