import java.io.FileWriter;
import java.lang.reflect.Modifier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Main model class for UMLEditor. Contains JSON interaction & helpful filesystem-related methods.
 * @author trush
 */
public class JModel {
    private final String LOG_PATH = "/umleditor-debug.log";
    private Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL).create();

    /**
     * Checks if the file at the specified filepath exists.
     * @param filepath the filepath to check for a file at.
     * @return true if file exists, false otherwise.
     */
    public boolean fileExist(String filepath) {
        if (filepath == null) return false;
        try {
            File f = new File(filepath);
            return (f.exists() ? true : false);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets the current directory where the program resides.
     * @return Current filepath to program directory, null if error.
     */
    public String getProgramDirectory() {
        try {
            Path currentRelativePath = Paths.get("");
            return(currentRelativePath.toAbsolutePath().toString());
        } catch(Exception e) {
            return null;
        }
    }

    /**
     * Writes a message to the error log.
     * @param message The desired message to be added to the error log.
     * @return true if message successfully written, false otherwise.
     */
    public boolean writeToLog(String message) {
        //Get filepath to log file
        String programPath = getProgramDirectory();
        if (programPath == null) return false;
        String fullLogPath = programPath + LOG_PATH;
        try {
            //Get current time in mm/dd/yy hh:mm:ss format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm:ss");
            String dt = LocalDateTime.now().format(formatter);

            FileWriter writer = new FileWriter(fullLogPath, true);
            writer.write("[" + dt + "]: " + message);
            writer.write(System.lineSeparator());
            writer.close();
            return true;
        } catch(Exception e) {
            return false;
        }
    }

    /**
     * Saves data from the UMLEditor class to the desired filepath. ** ALWAYS OVERWRITES **
     * @param filepath The desired filepath to save to.
     * @return True if successfully saved, false otherwise.
     */
    public boolean saveData(String filepath) {
        if (filepath == null) return false;
        try {
            //Using UMLClassHandler object is crucial for loading data, don't change.
            UMLClassHandler classHandler = new UMLClassHandler();
            String classJson = gson.toJson(classHandler);

            //PrintWriter allows us to write to file. It will **always** overwrite.
            FileWriter writer = new FileWriter(filepath, false);
            writer.write(classJson);
            writer.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Loads data from the desired filepath.
     * @param filepath The desired filepath to load the data from.
     * @return UMLClassHandler class if successful, null otherwise.
     */
    public UMLClassHandler loadData(String filepath) {
        // check if argument is null, or filepath is invalid
        if (filepath == null) return null;
        if (!fileExist(filepath)) return null;
        try {
            //Read data from json using scanner
            Scanner in = new Scanner(new File(filepath));
            String jsonData = in.nextLine();
            in.close();
            //Check if content of file is null/empty
            if (jsonData.isEmpty()) return null;
            //Try to parse data from string. Throws exception if incorrect format
            UMLClassHandler data = gson.fromJson(jsonData, UMLClassHandler.class);
            return data;
        } catch (Exception e) {
            return null;
        }
    }
}
