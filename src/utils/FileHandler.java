package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {

    public static void ensureBaseDirectories() {
        File res = new File("resources");
        if (!res.exists()) res.mkdirs();
        File users = new File("resources/users");
        if (!users.exists()) users.mkdirs();
    }

    public static List<String> readAllLines(File file) {
        List<String> lines = new ArrayList<>();
        if (file == null || !file.exists()) return lines;
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static void writeLines(File file, List<String> lines) {
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


