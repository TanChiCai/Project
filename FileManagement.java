import java.io.*;

public abstract class FileManagement {
    protected static final String SEPARATOR = "\t";

    public FileManagement() {}

    protected static void createFileIfNotExists(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println("Error creating file: " + e.getMessage());
            }
        }
    }

    public static BufferedReader getFileReader(String fileName) throws IOException {
        createFileIfNotExists(fileName);
        return new BufferedReader(new FileReader(fileName));
    }

    public static BufferedWriter getFileWriter(String fileName, boolean append) throws IOException {
        createFileIfNotExists(fileName);
        return new BufferedWriter(new FileWriter(fileName, append));
    }
}
