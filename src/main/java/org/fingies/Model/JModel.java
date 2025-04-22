package org.fingies.Model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Main model class for UMLEditor. Contains JSON interaction & helpful
 * filesystem-related methods.
 * 
 * @author trush
 */
public class JModel {

    private final String LOG_PATH = "/umleditor-debug.log";
    private String filepath;
    private String latestError;
    // Get filepath to log file
    private String programPath = getProgramDirectory();
    private String fullLogPath = programPath + LOG_PATH;

    // GSON is built to ignore fields with "final" modifier.
    private Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.FINAL, Modifier.VOLATILE).setPrettyPrinting().create();

    /**
     * Model class for saving and loading to standardized JSON format
     * 
     * @author trush
     */
    public class UMLModel {
        HashSet<UMLClass> classes;
        ArrayList<RelationStringAdapter> relationships;

        /**
         * Constructor for model class.
         */
        public UMLModel() {
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
         */
        public void reloadClasses() {
            for (UMLClass classObject : classes) {
                UMLClassHandler.addClassObject(classObject);
            }
        }

        /**
         * Reloads the relations into the RelationshipHandler from the model class.
         */
        public void reloadRelations() {
            for (RelationStringAdapter relationString : relationships) {
                RelationshipHandler.addRelationship(relationString.getSource(), relationString.getDestination(),
                        relationString.getType());
            }
        }

        /**
         * Relationship String Adapter
         * 
         * @author trush
         */
        public class RelationStringAdapter {

            String source;
            String destination;
            RelationshipType type;

            /**
             * Takes a relation object and adapts it for saving
             * 
             * @param relation
             */
            public RelationStringAdapter(Relationship relation) {
                source = relation.getSrc().getName();
                destination = relation.getDest().getName();
                type = relation.getType();
            }

            /**
             * Gets the current source
             * 
             * @return the currents source
             */
            public String getSource() {
                return source;
            }

            /**
             * Gets the current destination
             * 
             * @return the currents destination
             */
            public String getDestination() {
                return destination;
            }

            /**
             * Gets the current type
             * 
             * @return the currents type
             */
            public RelationshipType getType() {
                return type;
            }
        }
    }

    /**
     * Default constructor, does not set a filepath! Please ensure to set a filepath
     * before attempting to save
     */
    public JModel() {
        filepath = null;
    }

    /**
     * Constructor to initialize model with a known filepath for the JSON... Does
     * not do any file validation!
     * 
     * @param filepath Path to valid json file
     */
    public JModel(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Mutator method for filepath field
     * 
     * @param filepath The filepath to save data to
     */
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    /**
     * Accessor method for filepath field
     * 
     * @return the current filepath for the model
     */
    public String getFilepath() {
        return filepath;
    }

    /**
     * Gets the latest error message from the model
     * 
     * @return the latest error message
     */
    public String getLatestError() {
        return latestError;
    }

    /**
     * Checks if the file at the specified filepath exists.
     * 
     * @param filepath the filepath to check for a file at.
     * @return true if file exists, false otherwise.
     */
    public boolean fileExist(String filepath) {
        if (filepath == null) {
            latestError = "Invalid Argument: null, for fileExist";
            return false;
        }
        File f = new File(filepath);
        return (f.exists() ? true : false);
    }

    /**
     * Gets the current directory where the program resides.
     * 
     * @return Current filepath to program directory, null if error.
     */
    public String getProgramDirectory() {
        Path currentRelativePath = Paths.get("");
        return (currentRelativePath.toAbsolutePath().toString());
    }

    /**
     * Writes a message to the error log.
     * 
     * @param message The desired message to be added to the error log.
     * @return true if message successfully written, false otherwise.
     */
    public boolean writeToLog(String message) {
        try {

            // Get current time in mm/dd/yy HH:mm:ss format (24hr time).
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy HH:mm:ss");
            String dt = LocalDateTime.now().format(formatter);

            // Writes message to debug log with a timestamp, appends message onto previous
            // messages.
            FileWriter writer = new FileWriter(fullLogPath, true);
            writer.write("[" + dt + "]: " + message);
            writer.write(System.lineSeparator());
            writer.close();
            return true;
        } catch (Exception e) {
            latestError = e.toString();
            return false;
        }
    }

    /**
     * Saves data from the UMLEditor class to the desired filepath. ** ALWAYS
     * OVERWRITES **
     * 
     * @return True if successfully saved, false otherwise.
     */
    public boolean saveData() {
        try {
            // Using UMLClassHandler object is crucial for loading data, don't change.
            UMLModel model = new UMLModel();
            String classJson = gson.toJson(model);

            // PrintWriter allows us to write to file. It will **always** overwrite.
            FileWriter writer = new FileWriter(filepath, false);
            writer.write(classJson);
            writer.close();
            return true;
        } catch (Exception e) {
            latestError = e.toString();
            return false;
        }
    }

    /**
     * Saves data from the UMLEditor class to the desired filepath. ** ALWAYS
     * OVERWRITES && SETS MODEL FILEPATH **
     * 
     * @param filepath The desired filepath to save to.
     * @return True if successfully saved, false otherwise.
     */
    public boolean saveData(String filepath) {
        setFilepath(filepath);
        return saveData();
    }

    /**
     * Loads data from the desired filepath, Checks for field constraints as well.
     * **Overwrites any existing data!**
     * 
     * @return UMLClassHandler class if successful, null otherwise.
     */
    public UMLModel loadData() {
        try {
            // Read data from json using scanner
            Scanner in = new Scanner(new File(filepath));
            String jsonData = "";
            while (in.hasNextLine()) {
                jsonData += in.nextLine();
            }
            in.close();
            // Try to parse data from string. Throws exception if incorrect format
            UMLClassHandler.reset();
            RelationshipHandler.reset();
            UMLModel data = gson.fromJson(jsonData, UMLModel.class);
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
     * 
     * @param filepath The desired filepath to load the data from.
     * @return UMLClassHandler class if successful, null otherwise.
     */
    public UMLModel loadData(String filepath) {
        setFilepath(filepath);
        return loadData();
    }

	/**
	 * Represents a JComponent as an image, and exports it to the filepath.
	 * @param filepath The filepath to export the image to.
	 * @param component The JComponent to convert into an image.
	 * @return True if the export was successful, false otherwise.
	 */
	public boolean exportImage(String filepath, JComponent component) {
		File file;
        try {
        	if (filepath == null)
                throw new IllegalArgumentException("Invalid Argument: null, in exportImage");
            file = new File(filepath);
        } catch (Exception e) {
            writeToLog(e.toString());
            latestError = e.toString();
            return false;
        }
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        
        // paint the image
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, component.getWidth(), component.getHeight());
        component.printAll(g);
        
        g.dispose();
        try { 
            ImageIO.write(image, "png", file); 
            return true;
        } catch (IOException e) {
        	writeToLog(e.toString());
            latestError = e.toString();
            return false;
        }
	}
}
