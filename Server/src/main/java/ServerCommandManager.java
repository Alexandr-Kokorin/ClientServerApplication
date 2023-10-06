import java.io.*;

public class ServerCommandManager {

    private final Library library;

    public ServerCommandManager(Library library) {
        this.library = library;
    }

    public void sortCommand(String command) {
        String[] strings = command.split(" ");
        if (strings.length == 2) {
            switch (strings[0]) {
                case "load" -> runLoadCommand(strings[1]);
                case "save" -> runSaveCommand(strings[1]);
                default -> System.out.println("Incorrect command.");
            }
        } else System.out.println("Incorrect command.");
    }

    private void runLoadCommand(String filename) {
        File file = convertNameToFile(filename);
        if (!file.exists()) {
            System.out.println("There is no such file.");
            return;
        }
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (!line.contains("Library:")) {
                System.out.println("Incorrect file.");
                return;
            }
            library.load(reader, line);
            System.out.println("Data uploaded successfully.");
        } catch (IOException e) {
            System.out.println("Cloud not open the file");
        }
    }

    private void runSaveCommand(String filename) {
        File file = convertNameToFile(filename);
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.out.println("Failed to create file.");
                    return;
                }
            } catch (IOException e) {
                System.out.println("Failed to create file.");
                return;
            }
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            library.save(writer);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Cloud not open the file");
        }
    }

    private File convertNameToFile(String filename) {
        if (filename.endsWith(".txt")) return new File(filename);
        else return new File(filename + ".txt");
    }
}
