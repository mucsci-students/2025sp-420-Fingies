package org.fingies;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class GUIView extends JFrame implements ActionListener, View {

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu addMenu;
    private JMenu removeMenu;
    private JMenu renameMenu;

    private GUIMenuItem load;
    private GUIMenuItem save;
    private GUIMenuItem exit;
    
    private GUIMenuItem addClass;
    private GUIMenuItem addField;
    private GUIMenuItem addMethod;
    private GUIMenuItem addParameter;
    private GUIMenuItem addRelationship;

    private GUIMenuItem removeClass;
    private GUIMenuItem removeField;
    private GUIMenuItem removeMethod;
    private GUIMenuItem removeParameter;
    private GUIMenuItem removeRelationship;

    private GUIMenuItem renameClass;
    private GUIMenuItem renameField;
    private GUIMenuItem renameMethod;
    private GUIMenuItem renameParameter;
    private GUIMenuItem renameRelationshipType;

    private ArrayList<JTextField> textBoxes;
    private ArrayList<JComboBox<String>> comboBoxes;

    private JButton submitButton;
    private JButton cancelButton;

    private Controller controller;
    final JFileChooser fileChooser = new JFileChooser();

    // HashMap with key as name of class and value as GUIUMLClass associated with the name
    private HashMap<String, GUIUMLClass> GUIUMLClasses;

    // List of all current arrows representing relationships
    private List<ArrowComponent> arrows;

    public GUIView ()
    {
        GUIUMLClasses = new HashMap<String, GUIUMLClass>();
        arrows = new ArrayList<ArrowComponent>();
        textBoxes = new ArrayList<JTextField>();
        comboBoxes = new ArrayList<JComboBox<String>>();

        // Creates a JMenuBar and menus
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        addMenu = new JMenu("Add");
        removeMenu = new JMenu("Remove");
        renameMenu = new JMenu("Rename");

        // Adds menus to menubar
        menuBar.add(fileMenu);
        menuBar.add(addMenu);
        menuBar.add(removeMenu);
        menuBar.add(renameMenu);

        // Creates JMenu submenus
        load = new GUIMenuItem("Open", Action.LOAD);
        save = new GUIMenuItem("Save", Action.SAVE);
        exit = new GUIMenuItem("Exit", Action.EXIT);
        
        // ADD
        addClass = new GUIMenuItem("Class", Action.ADD_CLASS);
        addField = new GUIMenuItem("Field", Action.ADD_FIELD);
        addMethod = new GUIMenuItem("Method", Action.ADD_METHOD);
        addParameter = new GUIMenuItem("Parameter", Action.ADD_PARAMETERS);
        addRelationship = new GUIMenuItem("Relationship", Action.ADD_RELATIONSHIP);

        // REMOVE
        removeClass = new GUIMenuItem("Class", Action.REMOVE_CLASS);
        removeField = new GUIMenuItem("Field", Action.REMOVE_FIELD);
        removeMethod = new GUIMenuItem("Method", Action.REMOVE_METHOD);
        removeParameter = new GUIMenuItem("Parameter", Action.REMOVE_PARAMETERS);
        removeRelationship = new GUIMenuItem("Relationship", Action.REMOVE_RELATIONSHIP);

        // RENAME
        renameClass = new GUIMenuItem("Class", Action.RENAME_CLASS);
        renameField = new GUIMenuItem("Field", Action.RENAME_FIELD);
        renameMethod = new GUIMenuItem("Method", Action.RENAME_METHOD);
        renameParameter = new GUIMenuItem("Parameter", Action.RENAME_PARAMETER);
        renameRelationshipType = new GUIMenuItem("Relationship Type", Action.CHANGE_RELATIONSHIP_TYPE);

        // Creates action listeners for the different submenu actions
        load.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);

        addClass.addActionListener(this);
        addField.addActionListener(this);
        addMethod.addActionListener(this);
        addParameter.addActionListener(this);
        addRelationship.addActionListener(this);

        removeClass.addActionListener(this);
        removeField.addActionListener(this);
        removeMethod.addActionListener(this);
        removeParameter.addActionListener(this);
        removeRelationship.addActionListener(this);

        renameClass.addActionListener(this);
        renameField.addActionListener(this);
        renameMethod.addActionListener(this);
        renameParameter.addActionListener(this);
        renameRelationshipType.addActionListener(this);

        // Allows the press of a key to do the function of clicking the menu item WHILE in the menu
        save.setMnemonic(KeyEvent.VK_S); // S for save


        // Adds submenus to menu
        fileMenu.add(load);
        fileMenu.add(save);
        fileMenu.add(exit);

        addMenu.add(addClass);
        addMenu.add(addField);
        addMenu.add(addMethod);
        addMenu.add(addParameter);
        addMenu.add(addRelationship);

        removeMenu.add(removeClass);
        removeMenu.add(removeField);
        removeMenu.add(removeMethod);
        removeMenu.add(removeParameter);
        removeMenu.add(removeRelationship);

        renameMenu.add(renameClass);
        renameMenu.add(renameField);
        renameMenu.add(renameMethod);
        renameMenu.add(renameParameter);
        renameMenu.add(renameRelationshipType);

        // Sets main attributes of the "frame" (this)
        this.setTitle("UMLEditor");
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // Prevent default closing behavior
        this.setLayout(null);
        // this.setLayout(new FlowLayout());
        this.setSize(1000, 1000);
        this.setJMenuBar(menuBar);

        this.setVisible(true);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                boolean result = controller.runHelper(Action.EXIT, new String[] {});
                if (result)
                {
                	System.exit(0);
                }
            }
        });
    }

    private void createButtons(Action a, int offset) {
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");

        submitButton.setBounds(offset * 130 + 20, 20, 125, 30); // Position and size for submit button
        submitButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        cancelButton.setBounds((offset + 1) * 130 + 20, 20, 125, 30); // Position and size for cancel button
        cancelButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    
        // Add ActionListener for Submit button
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSubmitAction(a);
            }
        });
    
        // Add ActionListener for Cancel button
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleCancelAction();
            }
        });
    
        // Add the buttons to the GUI
        this.add(submitButton);
        this.add(cancelButton);
        repaint(); // Refresh UI
    }

    private void handleSubmitAction(Action action) {
        // Concatenate the text from text fields and combo boxes just like pressing Enter
        this.remove(submitButton);
        this.remove(cancelButton);

        String[] textInputs = textBoxes.stream()
                .map(JTextField::getText)
                .toArray(String[]::new);
    
        String[] comboBoxInputs = comboBoxes.stream()
                .map(comboBox -> (String) ((JComboBox<?>) comboBox).getSelectedItem())
                .toArray(String[]::new);
    
        // Concatenate both arrays
        String[] allInputs = Stream.concat(Arrays.stream(comboBoxInputs), Arrays.stream(textInputs))
                .filter(input -> input != null && !input.trim().isEmpty())  // Remove null or empty values
                .toArray(String[]::new);
    
        // Remove all text fields and combo boxes
        textBoxes.forEach(GUIView.this::remove);
        textBoxes.clear();
        comboBoxes.forEach(GUIView.this::remove);
        comboBoxes.clear();
        repaint(); // Refresh UI
        
        if (action.equals(Action.RENAME_FIELD))
        {
            // Initialize a list to store the final inputs
            List<String> finalInputsList = new ArrayList<>();
            finalInputsList.add(allInputs[0]);  // Class Name
            finalInputsList.add(allInputs[1]);  // Field Name
            finalInputsList.add(UMLClassHandler.getClass(allInputs[0]).getField(allInputs[1]).getType());
            finalInputsList.add(allInputs[2]);  // New Field Name
            allInputs = finalInputsList.toArray(new String[0]);
        }

        // Special handling for actions that require parameter formatting 
        // Turns method1 (String int String) into method1 without the type list
        if (action.equals(Action.ADD_METHOD) || action.equals(Action.ADD_PARAMETERS) 
        || action.equals(Action.REMOVE_METHOD) || action.equals(Action.REMOVE_PARAMETERS) 
        || action.equals(Action.RENAME_METHOD) || action.equals(Action.RENAME_PARAMETER))
        {
            String [] parameters = null;
            // separates the method name and its parameters
            if (allInputs.length > 1) { // Ensure there's a method name to process
                String fullMethodName = allInputs[1];
                // Extract the method name (before space or parenthesis)
                String methodName = fullMethodName.split("\\s|\\(")[0];
                // Extract the method parameters (if any exist)
                String methodParams = fullMethodName.contains("(") ? 
                    fullMethodName.substring(fullMethodName.indexOf("(") + 1, fullMethodName.indexOf(")")) : "";
                // Store them in a list
                parameters = methodParams.split(", ");
                // System.out.println("parameters are " + Arrays.toString(parameters));
                allInputs[1] = methodName;
            }

            if (allInputs.length >= 2) // Ensure we have enough arguments 
            {
                String [] types = null;
                String [] names = null; 
                
                // Initialize a list to store the final inputs
                List<String> finalInputsList = new ArrayList<>();
                finalInputsList.add(allInputs[0]);  // Class Name
                finalInputsList.add(allInputs[1]);  // Method name

                if (action.equals(Action.ADD_METHOD))
                {
                    types = allInputs[3].trim().split("\\s+");   // Extract types 
                    names = allInputs[4].trim().split("\\s+");   // Extract names 
                }
                else if (action.equals(Action.REMOVE_PARAMETERS))
                {
                    finalInputsList.addAll(Arrays.asList(parameters));
                    finalInputsList.add(";"); 
                    finalInputsList.add(allInputs[2]);  // Parameter Name
                }
                else if (action.equals(Action.RENAME_METHOD))
                {
                    finalInputsList.addAll(Arrays.asList(parameters));
                    finalInputsList.add(allInputs[2]);  // New Method Name
                }
                else if (action.equals(Action.REMOVE_METHOD))
                {
                    finalInputsList.addAll(Arrays.asList(parameters));
                }
                else
                {
                    types = allInputs[2].trim().split("\\s+");   // Extract types 
                    names = allInputs[3].trim().split("\\s+");   // Extract names 
                }
                

                // Ensure type-name pairing is valid 
                if (types != null && types.length == names.length) { 
                    List <String> altered = new ArrayList<>();
                    for (int i = 0; i < types.length; i++)
                    {
                        altered.add(types[i]);
                        altered.add(names[i]);
                    }

                    String [] alteredArr = altered.toArray(new String[0]);

                    if (action.equals(Action.ADD_METHOD)) {
                        finalInputsList.add(allInputs[2]); // return type
                        finalInputsList.addAll(Arrays.asList(alteredArr));
                    } 
                    else if (action.equals(Action.ADD_PARAMETERS))
                    {
                        finalInputsList.addAll(Arrays.asList(parameters));
                        finalInputsList.add(";"); 
                        finalInputsList.addAll(Arrays.asList(alteredArr));
                    } 
                    else 
                    {
                        finalInputsList.addAll(Arrays.asList(parameters));
                        finalInputsList.addAll(Arrays.asList(alteredArr));
                    }
                } 
                // Now finalInputsList will contain the elements in the desired format
                allInputs = finalInputsList.toArray(new String[0]);
            } 
        }
        System.out.println("Input: " + Arrays.toString(allInputs));
    
        // Call controller helper with the concatenated arguments
        if (controller.runHelper(action, allInputs)) {
            actionHelper(action, allInputs);
            repaint();
        }
    }

    // Removes Submit and Cancel buttons when the Cancel button is clicked
    private void handleCancelAction() {
        this.remove(submitButton);
        this.remove(cancelButton);
        textBoxes.forEach(GUIView.this::remove);
        textBoxes.clear();
        comboBoxes.forEach(GUIView.this::remove);
        comboBoxes.clear();
        repaint(); // Refresh UI
    }
    
    /**
     * Makes x number of comboboxes based on the number of placeholders
     * @param a action assigned to the combobox
     * @param placeholders array of what type of comboboxes should be made
     */
    public void makeComboBoxes(Action a, String[] placeholders) {
        comboBoxes.clear();
        JComboBox<String> classComboBox = null;
        JComboBox<String> methodComboBox = null;
        JComboBox<String> parameterComboBox = null;
    
        for (int i = 0; i < placeholders.length; i++) {
            String placeholder = placeholders[i];
            JComboBox<String> box = new JComboBox<>();
    
            switch (placeholder) {
                case "Class":
                    classComboBox = createClassComboBox();
                    box = classComboBox;
                    break;
    
                case "Field":
                    box = createComboBoxForClassItems(classComboBox, "getFields");
                    addComboBoxListener(classComboBox, box, "getFields");
                    break;
    
                case "Method":
                    methodComboBox = createComboBoxForClassItems(classComboBox, "getMethods");
                    addComboBoxListener(classComboBox, methodComboBox, "getMethods");
                    box = methodComboBox;
                    break;
    
                case "Parameter":
                    parameterComboBox = new JComboBox<>();
                    updateParameterComboBox(parameterComboBox, classComboBox, methodComboBox); // Initial population
                    addMethodComboBoxListener(classComboBox, methodComboBox, parameterComboBox);
                    box = parameterComboBox;
                    break;
                    
                case "Relationship":
                    JComboBox<String> relationships = new JComboBox<>();
                    relationships.addItem("Aggregation");
                    relationships.addItem("Composition");
                    relationships.addItem("Inheritance");
                    relationships.addItem("Realization");
                    box = relationships;
                    break;
    
                default:
                    throw new IllegalArgumentException("Unknown placeholder: " + placeholder);
            }
            
            // Allows the text to wrap and sets the max amount of rows to be shown per box at a time
            box.setRenderer(new WrappingComboBoxRenderer());
            box.setMaximumRowCount(8);
            styleComboBox(box, i);
            comboBoxes.add(box);
            this.add(box);
        }
        reload();
    }
    
    /**
     * Creates a combobox consisting of all current classes that exist within the model
     * @return a new combobox with all currect classes that exist within the model
     */
    private JComboBox<String> createClassComboBox() {
        String[] classes = GUIUMLClasses.keySet().toArray(String[]::new);
        JComboBox<String> classComboBox = new JComboBox<>(classes);
        return classComboBox;
    }
    
    /**
     * Creates comboboxes for either the fields or methods based on methodType
     * @param classComboBox combobox consisting of all currect classes that exist within the model
     * @param methodType either "getFields" for fields or "getMethods" for methods
     * @return a combobox containing either all of the fields or methods that exist within a class
     */
    private JComboBox<String> createComboBoxForClassItems(JComboBox<String> classComboBox, String methodType) {
        JComboBox<String> box = new JComboBox<>();
        updateComboBox(box, classComboBox, methodType);
        return box;
    }
    
    /**
     * Adds a listener for the combobox of parameters that will update based on what item is selected in the methodComboBox
     * @param classComboBox combobox consisting of all currect classes that exist within the model
     * @param methodComboBox combobox consisting of all currect methods that exist within a certain class
     * @param paramBox combobox consisting of all currect parameters that exist within a method
     */
    private void addMethodComboBoxListener(JComboBox<String> classComboBox, JComboBox<String> methodComboBox, JComboBox<String> paramBox) {
        methodComboBox.addItemListener(new ComboBoxListener(new JComboBox[]{paramBox}) {
            @Override
            protected void updateComboBox(JComboBox<String> box) {
                updateParameterComboBox((JComboBox<String>) box, classComboBox, methodComboBox);
            }
        });
    }
    
    /**
     * Updates a combobox to fill it with new updated data
     * @param box combobox to be updated
     * @param classComboBox combobox consisting of all currect classes that exist within the model
     * @param methodType either "getFields" for fields or "getMethods" for methods
     */
    private void updateComboBox(JComboBox<String> box, JComboBox<String> classComboBox, String methodType) {
        String selectedClass = (String) classComboBox.getSelectedItem();
        if (selectedClass != null) {
            box.removeAllItems();
            String[] items = getClassItems(selectedClass, methodType);
            for (String item : items) {
                box.addItem(item);
            }
        }
    }
    
    /**
     * Updates a combobox consisting of all currect parameters that exist within a method and fills it with new updated data
     * @param paramBox combobox consisting of all currect parameters that exist within a method
     * @param classComboBox combobox consisting of all currect classes that exist within the model
     * @param methodComboBox combobox consisting of all currect methods that exist within a certain class
     */
    private void updateParameterComboBox(JComboBox<String> paramBox, JComboBox<String> classComboBox, JComboBox<String> methodComboBox) {
        String selectedClass = (String) classComboBox.getSelectedItem();
        String selectedMethodSignature = (String) methodComboBox.getSelectedItem();
        
        if (selectedClass != null && selectedMethodSignature != null) {
            String methodName = selectedMethodSignature.split("\\(")[0];  // Get method name (split by '(')
            
            paramBox.removeAllItems();

            String[] parameters = getMethodParameters(selectedClass, methodName);
            for (String param : parameters) {
                paramBox.addItem(param);
            }
        }
    }
    
    /**
     * Retrieved the info needed to update the parameterComboBox
     * @param selectedClass combobox consisting of all currect classes that exist within the model
     * @param methodName combobox consisting of all currect methods that exist within a certain class
     * @return list containing all parameters that exist within a certain method
     */
    private String[] getMethodParameters(String selectedClass, String methodName) {
        if (!GUIUMLClasses.containsKey(selectedClass)) {
            throw new IllegalArgumentException("Class not found: " + selectedClass);
        }
        // Get the UML class and its methods
        UMLClass umlClass = GUIUMLClasses.get(selectedClass).getUMLClass();
        if (umlClass == null) {
            throw new IllegalArgumentException("UMLClass not found for: " + selectedClass);
        }
        List<Method> methods = umlClass.getMethods();
        if (methods == null) {
            throw new IllegalArgumentException("Methods not found for class: " + selectedClass);
        }

        // Filter methods by name and extract parameter names
        return methods.stream()
                .filter(method -> method.getName().trim().equalsIgnoreCase(methodName.trim()))
                .flatMap(method -> method.getParameters().stream())
                .map(param -> param.getName())
                .toArray(String[]::new);
    }
    
    /**
     * Adds a listener for a combobox that will update based on what methodType is provided
     * @param classComboBox combobox consisting of all currect classes that exist within the model
     * @param box box to be given a listener and filled with updated data from the model
     * @param methodType either "getFields" for fields or "getMethods" for methods
     */
    private void addComboBoxListener(JComboBox<String> classComboBox, JComboBox<String> box, String methodType) {
        classComboBox.addItemListener(new ComboBoxListener(new JComboBox[]{box}) {
            @Override
            protected void updateComboBox(JComboBox<String> box) {
                String selectedClass = (String) classComboBox.getSelectedItem();
                if (selectedClass != null) {
                    box.removeAllItems();
                    String[] items = getClassItems(selectedClass, methodType);
                    for (String item : items) {
                        box.addItem(item);
                    }
                }
            }
        });
    }
    
    /**
     * Returns list of either fields or methods based on methodType provided
     * @param selectedClass classname that was selected within the classComboBox
     * @param methodType either "getFields" for fields or "getMethods" for methods
     * @return list of either all fields or all methods that exist within a certain class
     */
    private String[] getClassItems(String selectedClass, String methodType) {
        return switch (methodType) {
            case "getFields" -> GUIUMLClasses.get(selectedClass).getUMLClass().getFields().stream()
                    .map(x -> x.getName()).toArray(String[]::new);
            case "getMethods" -> GUIUMLClasses.get(selectedClass).getUMLClass().getMethods().stream()
                    .map(x -> x.toTypes()).toArray(String[]::new);
            default -> new String[0];
        };
    }
    
    /**
     * Styles a combobox in order to make it look a little nicer for the user
     * @param box box to be styled
     * @param index determines the offset horizontally, so the boxes don't overlap
     */
    private void styleComboBox(JComboBox<String> box, int index) {
        box.setBounds(index * 130 + 20, 20, 125, 30);
        box.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }
    

    /**
     * Creates textboxes
     * @param a action of the textbox
     * @param placeholders values to be filled in for the textbox
     * @param offset how many parameters come before this, so these don't overlap with combo boxes
     */
    public void makeTextBoxes(Action a, String [] placeholders, int offset)
    {
        textBoxes.clear();
        for (int i = 0; i < placeholders.length; i++)
        {
            JTextField text;
            text = new JTextField(20);
            text.setBounds(offset * 130 + 20, 20, 125, 30); // Set position and size
            String placeholder = placeholders[i];
            text.setText(placeholder);
            text.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Red border with thickness of 5
            text.setHorizontalAlignment(SwingConstants.CENTER);

            text.addFocusListener(new java.awt.event.FocusListener() {
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (text.getText().equals(placeholder)) {
                        text.setText(""); // Clear placeholder when focused
                    }
                }
    
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (text.getText().isEmpty()) {
                        text.setText(placeholder); // Restore placeholder if the field is empty
                    }
                }
            });

            offset++;
            textBoxes.add(text);
            this.add(text);
            repaint(); // Refresh UI
        }
        reload();
    }

    // Based on the action sent in, make the correct number of textboxes and comboboxes along with the Submit and Cancel buttons
    @Override
    public void actionPerformed(ActionEvent e) {
    	Action a = ((GUIMenuItem) e.getSource()).action;

        if (e.getSource() == addClass && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name"}, 0);
            createButtons(a, 1);
        }
        else if (e.getSource() == addField && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class"});
            makeTextBoxes(a, new String [] {"Type: int String", "Field Name"}, 1);
            createButtons(a, 3);
        }
        else if (e.getSource() == addMethod && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class"});
            makeTextBoxes(a, new String [] {"Method Name", "Return Type", "Types: int String", "Parameters: a b c"}, 1);
            createButtons(a, 5);
        }
        else if (e.getSource() == addParameter && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class", "Method"});
            makeTextBoxes(a, new String [] {"Types: int String", "Parameters: a b c"}, 2);
            createButtons(a, 4);
        }
        else if (e.getSource() == addRelationship && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class", "Class", "Relationship"});
            createButtons(a, 3);
        }
        else if (e.getSource() == removeClass && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class"});
            createButtons(a, 1);
        }
        else if (e.getSource() == removeField && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class", "Field"});
            createButtons(a, 2);
        }
        else if (e.getSource() == removeMethod && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class", "Method"});
            createButtons(a, 2);
        }
        else if (e.getSource() == removeParameter && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class", "Method", "Parameter"});
            createButtons(a, 3);
        }
        else if (e.getSource() == removeRelationship && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class", "Class"});
            createButtons(a, 2);
        }
        else if (e.getSource() == renameClass && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class"});
            makeTextBoxes(a, new String [] {"New Class Name"}, 1);
            createButtons(a, 2);
        }
        else if (e.getSource() == renameField && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class", "Field"});
            makeTextBoxes(a, new String [] {"New Field Name"}, 2);
            createButtons(a, 3);
        }
        else if (e.getSource() == renameMethod && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class", "Method"});
            makeTextBoxes(a, new String [] {"New Method Name"}, 2);
            createButtons(a, 3);
        }
        else if (e.getSource() == renameParameter && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class", "Method", "Parameter"});
            makeTextBoxes(a, new String [] {"New Parameter"}, 3);
            createButtons(a, 4);
        }
        else if (e.getSource() == renameRelationshipType && textBoxes.isEmpty())
        {
            makeComboBoxes(a, new String [] {"Class", "Class", "Relationship"});
            createButtons(a, 3);
        }
        else if (e.getSource() == load)
        {
        	boolean result = controller.runHelper(a, new String[] {});
        	if (result)
        	{
        		loadGUIObjects();
        	}
        }
        else if (e.getSource() == save)
        {
        	controller.runHelper(a, new String[] {});
        }
        else if (e.getSource() == exit)
        {
        	boolean result = controller.runHelper(a, new String[] {});
        	if (result)
        	{
        		System.exit(0);
        	}
        }
    }

    /**
     * Based on the action, perform actions within the GUIView to update it wit new data from JModel
     * @param action action to be performed
     * @param args list of arguments 
     */
    private void actionHelper(Action action, String[] args)
    {
        switch(action) {
            case ADD_CLASS:
                addUMLClass(args[0]);
                break;
            case REMOVE_CLASS:
                removeUMLClass(args[0]);
                // updateArrows();
                break;
            case RENAME_CLASS:
                renameUMLClass(args[0], args[1]);
                break;
            case ADD_RELATIONSHIP:
                // updateArrows();
                break;
            case REMOVE_RELATIONSHIP:
                // updateArrows();
                break;
            case CHANGE_RELATIONSHIP_TYPE:
                // updateArrows();
                break;
            case ADD_METHOD:
                updateAttributes(args[0]);
                break;
            case REMOVE_METHOD:
                updateAttributes(args[0]);
                break;
            case RENAME_METHOD:
                updateAttributes(args[0]);
                break;
            case ADD_FIELD:
                updateAttributes(args[0]);
                break;
            case REMOVE_FIELD:
                updateAttributes(args[0]);
                break;
            case RENAME_FIELD:
                updateAttributes(args[0]);
                break;
            case ADD_PARAMETERS:
                updateAttributes(args[0]);
                break;
            case REMOVE_PARAMETERS:
                updateAttributes(args[0]);
                break;
            case RENAME_PARAMETER:
                updateAttributes(args[0]);
                break;
            default:
                break;
            }
            updateArrows();
    }
    
    /**
     * Creates a GUIUMLObject for every UMLClass in UMLClassHandler
     */
    public void loadGUIObjects()
    {
    	new HashSet<>(GUIUMLClasses.keySet()).forEach(x -> removeUMLClass(x));
    	UMLClassHandler.getAllClasses().stream().forEach(x -> addUMLClass(x.getName()));
    	updateArrows();
    }

    /**
     * Creates an arrow between two UMLClasses of a specific relationship
     * @param relationship relationship between two UMLClasses
     */
    public void addArrowForRelationship(Relationship relationship) {
        GUIUMLClass srcClass = GUIUMLClasses.get(relationship.getSrc().getName());
        GUIUMLClass destClass = GUIUMLClasses.get(relationship.getDest().getName());

        Point start = new Point(srcClass.getJLayeredPane().getX() + srcClass.getJLayeredPane().getWidth() / 2,
                            srcClass.getJLayeredPane().getY() + srcClass.getJLayeredPane().getHeight() / 2);
        Point end = new Point(destClass.getJLayeredPane().getX() + destClass.getJLayeredPane().getWidth() / 2,
                            destClass.getJLayeredPane().getY() + destClass.getJLayeredPane().getHeight() / 2);

        // Create a new ArrowComponent and add it to the arrows list
        ArrowComponent arrow = new ArrowComponent(start, end, relationship.getType(), 
        destClass.getJLayeredPane().getWidth(), destClass.getJLayeredPane().getHeight(), srcClass == destClass);
        arrows.add(arrow);

        // Add the arrow to the JLayeredPane (or other container)
        this.add(arrow, JLayeredPane.DEFAULT_LAYER);
        reload();  // Force a reload to revalidate and repaint
    }

    /**
     * Updates all of the current arrows by removing them all and redrawing them
     */
    public void updateArrows() {
        for (ArrowComponent arrow : arrows) 
        {
            this.remove(arrow); // Remove from JFrame
        }
        arrows.clear();  // Clear the list of arrows
        
        // Get the relationships and create arrows for each one
        for (Relationship relationship : RelationshipHandler.getRelationships()) 
        {
            addArrowForRelationship(relationship);
        }
        reload();
    }

    /**
     * Refreshes the GUI to update for movement of UMLClasses
     */
    public void reload()
    {
        // Force the GUI to refresh and update
        this.revalidate(); // Recalculate layout
        this.repaint();    // Repaint to reflect changes
    }

    /**
     * Adds a UMLClass to list of GUIUMLClasses, which contains all current classes that exist within the GUI
     * @param className name of class to be added
     */
    public void addUMLClass(String className)
    {
        GUIUMLClass newUMLClass = new GUIUMLClass(UMLClassHandler.getClass(className), controller, this);

        // Adds the JLayeredPane to the Frame (this) and to the HashMap of GUIUMLClasses
        this.add(newUMLClass.getJLayeredPane(), JLayeredPane.PALETTE_LAYER);
        GUIUMLClasses.put(className, newUMLClass);
        reload();
    }

    /**
     * Removes a UMLClass from the list of GUIUMLClasses
     * @param className names of class to be removed
     */
    public void removeUMLClass(String className)
    {
        // this..getContentPane().remove(GUIUMLClasses.get(name).getJLayeredPane());
        this.remove(GUIUMLClasses.get(className).getJLayeredPane());
        GUIUMLClasses.remove(className);
        reload();
    }

    /**
     * Renames a UMLClass in the list of GUIUMLClasses
     * @param className name of class to be renamed
     * @param newName new name of class to be assigned
     */
    public void renameUMLClass(String className, String newName)
    {
        GUIUMLClass temp = GUIUMLClasses.get(className);
        GUIUMLClasses.remove(className);
        temp.update();
        GUIUMLClasses.put(newName, temp);
        reload();
    }

    /**
     * Updates the fields in the GUI UMLclasses based on user input
     * @param className name of UMLClass to be updated
     */
    public void updateAttributes(String className)
    {
        GUIUMLClasses.get(className).update();
    }

	@Override
	public String promptForInput(String message) {
		return JOptionPane.showInputDialog(message);
	}
	@Override
	public List<String> promptForInput(List<String> messages) {
		List<String> result = new ArrayList<String>();
        for(String m : messages)
        {
            result.add(promptForInput(m));
        }
        return result;
	}
	@Override
	public List<String> promptForInput(List<String> messages, List<InputCheck> checks) {
		List<String> result = new ArrayList<String>();
        for(int i = 0; i < messages.size(); ++i)
        {
            String ans = promptForInput(messages.get(i));
            String checkMsg = checks.get(i).check(ans); // This will either be "" or "message"
            while(!checkMsg.equals("")) // This loop will keep prompting the user until they input something that satisfies the check
            {
            	notifyFail(checkMsg);
                ans = JOptionPane.showInputDialog(messages.get(i));
                checkMsg = checks.get(i).check(ans);
            }
            result.add(ans);
        }
        return result;
	}
	
	@Override
    public String promptForSaveInput(String message)
    {
    	fileChooser.setDialogTitle(message);
        int returnValue = fileChooser.showSaveDialog(this);
        if(returnValue != JFileChooser.APPROVE_OPTION)
        	return null;
        return fileChooser.getSelectedFile().getPath();
    }
	
	@Override
	public String promptForOpenInput(String message) {
    	fileChooser.setDialogTitle(message);
        int returnValue = fileChooser.showOpenDialog(this);
        if(returnValue != JFileChooser.APPROVE_OPTION)
        	return null;
        return fileChooser.getSelectedFile().getPath();
	}
	
	@Override
	public void notifySuccess() {
		// DON'T IMPLEMENT THIS
	}
	@Override
	public void notifySuccess(String message) {
		// DO NOT IMPLEMENT PLS!
	}
	@Override
	public void notifyFail(String message) {
		JOptionPane.showMessageDialog(this, message);
		
	}
	@Override
	public void display(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	@Override
	public void help() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void help(String command) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void run() 
	{
		// Nothing to be implemented here
	}
	
	@Override
	public void setController(Controller c)
	{
		controller = c;
	}
}

