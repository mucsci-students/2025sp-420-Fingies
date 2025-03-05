import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Main model class for UMLEditor. Contains JSON interaction & helpful filesystem-related methods.
 * @author trush
 */
public class JModel {

    private final String LOG_PATH = "/umleditor-debug.log";
    private String filepath;
    private String latestError;

    //GSON is built to ignore fields with "final" modifier.
    private Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL).create();

    /**
     * Model class for saving and loading to standardized JSON format
     * @author trush
     */
    class Model {
        HashSet<UMLClass> classes;
        ArrayList<RelationStringAdapter> relationships;

        /**
         * Constructor for model class.
         */
        public Model() {
            classes = UMLClassHandler.getAllClasses();
            relationships = new ArrayList<>();
            List<Relationship> relations = RelationshipHandler.getRelationObjects();
            for (Relationship relation : relations) {
                RelationStringAdapter relationString = new RelationStringAdapter(relation);
                relationships.add(relationString);
            }
        }

        /**
         * Reloads classes into UMLClassHandler from the model class.
         * @return true if reload succeeded, false otherwise
         */
        public boolean reloadClasses() {
            try {
                for (UMLClass classObject : classes) {
                    boolean worked = UMLClassHandler.addClassObject(classObject);
                    if (!worked) return false;
                }
                return true;
            } catch(Exception e) {
                writeToLog(e.toString());
                latestError = e.toString();
                return false;
            }
        }

        /**
         * Reloads the relations into the RelationshipHandler from the model class.
         * @return true if reload succeeded, false otherwise
         */
        public boolean reloadRelations() {
            try {
                for (RelationStringAdapter relationString : relationships) {
                    RelationshipHandler.addRelationship(relationString.getSource(), relationString.getDestination(), relationString.getType());
                }
                return true;
            } catch(Exception e) {
                writeToLog(e.toString());
                latestError = e.toString();
                return false;
            }
        }

        /**
         * Relationship String Adapter
         * @author trush
         */
        public class RelationStringAdapter {

            String source;
            String destination;
            RelationshipType type;

            /**
             * Takes a relation object and adapts it for saving
             * @param relation
             */
            public RelationStringAdapter(Relationship relation) {
                source = relation.getSrc().getName();
                destination = relation.getDest().getName();
                type = relation.getType();
            }

            /**
             * Gets the current source
             * @return the currents source
             */
            public String getSource() {
                return source;
            }

            /**
             * Gets the current destination
             * @return the currents destination
             */
            public String getDestination() {
                return destination;
            }

            /**
             * Gets the current type
             * @return the currents type
             */
            public RelationshipType getType() {
                return type;
            }
        }
    }

    /**
     * Default constructor, does not set a filepath! Please ensure to set a filepath before attempting to save
     */
    public JModel() {
        filepath = null;
    }

    /**
     * Constructor to initialize model with a known filepath for the JSON... Does not do any file validation!
     * @param filepath Path to valid json file
     */
    public JModel(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Mutator method for filepath field
     * @param filepath The filepath to save data to
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Accessor method for filepath field
     * @return the current filepath for the model
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Gets the latest error message from the model
     * @return the latest error message
     */
    public String getLatestError() {
        return latestError;
    }

    /**
     * Checks if the file at the specified filepath exists.
     * @param filepath the filepath to check for a file at.
     * @return true if file exists, false otherwise.
     */
    public boolean fileExist(String filepath) {
        if (filepath == null) {
            latestError = "Invalid Argument: null, for fileExist";
            return false;
        };
        try {
            File f = new File(filepath);
            return (f.exists() ? true : false);
        } catch (Exception e) {
            writeToLog(e.toString());
            latestError = e.toString();
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
            writeToLog(e.toString());
            latestError = e.toString();
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
        if (programPath == null) {
            latestError = "Error retrieving program path";
            return false;
        };
        String fullLogPath = programPath + LOG_PATH;
        try {
            //Get current time in mm/dd/yy HH:mm:ss format (24hr time).
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
            String dt = LocalDateTime.now().format(formatter);

            //Writes message to debug log with a timestamp, appends message onto previous messages.
            FileWriter writer = new FileWriter(fullLogPath, true);
            writer.write("[" + dt + "]: " + message);
            writer.write(System.lineSeparator());
            writer.close();
            return true;
        } catch(Exception e) {
            latestError = e.toString();
            return false;
        }
    }

    /**
     * Saves data from the UMLEditor class to the desired filepath. ** ALWAYS OVERWRITES **
     * @return True if successfully saved, false otherwise.
     */
    public boolean saveData() {
        if (filepath == null) {
            latestError = "Invalid Argument: null, in saveData"; 
            return false;
        };
        try {
            //Using UMLClassHandler object is crucial for loading data, don't change.
            Model model = new Model();
            String classJson = gson.toJson(model);

            //PrintWriter allows us to write to file. It will **always** overwrite.
            FileWriter writer = new FileWriter(filepath, false);
            writer.write(classJson);
            writer.close();
            return true;
        } catch (Exception e) {
            writeToLog(e.toString());
            latestError = e.toString();
            return false;
        }
    }

    /**
     * Saves data from the UMLEditor class to the desired filepath. ** ALWAYS OVERWRITES && SETS MODEL FILEPATH **
     * @param filepath The desired filepath to save to.
     * @return True if successfully saved, false otherwise.
     */
    public boolean saveData(String filepath) {
        setFilepath(filepath);
        return saveData();
    }

    /**
     * Loads data from the desired filepath, Checks for field constraints as well.
     * @return UMLClassHandler class if successful, null otherwise.
     */
    public Model loadData() {
        // check if argument is null, or filepath is invalid
        if (filepath == null) {
            latestError = "Invalid Argument: null, in loadData";
            return null;
        };
        if (!fileExist(filepath)) {
            latestError = "Tried to load data from non-existant file!";
            return null;
        };
        try {
            //Read data from json using scanner
            Scanner in = new Scanner(new File(filepath));
            String jsonData = in.nextLine();
            in.close();
            //Check if content of file is null/empty
            if (jsonData.isEmpty()) return null;
            //Try to parse data from string. Throws exception if incorrect format
            Model data = gson.fromJson(jsonData, Model.class);
            data.reloadClasses();
            HashSet<UMLClass> classes = UMLClassHandler.getAllClasses();
            for (UMLClass umlClass : classes) {
                umlClass.validateCharacters(umlClass.getName());
            }
            data.reloadRelations();
            return data;
        } catch (Exception e) {
            writeToLog(e.toString());
            latestError = e.toString();
            return null;
        }
    }

    /**
     * Loads data from the desired filepath, Checks for field constraints as well.
     * @param filepath The desired filepath to load the data from.
     * @return UMLClassHandler class if successful, null otherwise.
     */
    public Model loadData(String filepath) {
        setFilepath(filepath);
        return loadData();
    }
}
