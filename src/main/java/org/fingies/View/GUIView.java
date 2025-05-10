package org.fingies.View;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.LayoutManager;
import java.awt.FlowLayout;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JMenu;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.OverlayLayout;
import javax.swing.SwingConstants;

import org.fingies.Controller.*;
import org.fingies.Model.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.GlyphVector;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

public class GUIView extends JFrame implements ActionListener, UMLView {

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu addMenu;
    private JMenu removeMenu;
    private JMenu renameMenu;
    private JMenu typeMenu;

    private GUIMenuItem load;
    private GUIMenuItem save;
    private GUIMenuItem export;
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
    
    private GUIMenuItem changeFieldType;
    private GUIMenuItem changeMethodType;
    private GUIMenuItem changeParameterType;
    private GUIMenuItem changeRelatoinshipType;
    
    private JButton undo;
    private JButton redo;

    private ArrayList<JTextField> textBoxes;
    private ArrayList<JComboBox<String>> comboBoxes;

    private JButton submitButton;
    private JButton cancelButton;
    
    private Action currentAction;
    private JLabel currentActionLabel;

    private UMLController controller;
    private JLayeredPane canvas;
    private JScrollPane scrollPane;
    private JPanel topPanel;
    private JPanel theAllPanel;
    final JFileChooser fileChooser = new JFileChooser();

    // HashMap with key as name of class and value as GUIUMLClass associated with the name
    private HashMap<String, GUIUMLClass> GUIUMLClasses;

    // List of all current arrows representing relationships
    private List<ArrowComponent> arrows;
    
    // The name of the last class to be dragged by the user
    private String lastClassTouched = null;
    private String secondToLastClassTouched = null;

    public GUIView ()
    {
        GUIUMLClasses = new HashMap<String, GUIUMLClass>();
        arrows = new ArrayList<ArrowComponent>();
        textBoxes = new ArrayList<JTextField>();
        comboBoxes = new ArrayList<JComboBox<String>>();

        JMenuBar menuBar = new JMenuBar();
        menuBar.setLayout(new BorderLayout()); // Needed for proper alignment

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        addMenu = new JMenu("Add");
        removeMenu = new JMenu("Remove");
        renameMenu = new JMenu("Rename");
        typeMenu = new JMenu("ChangeType");
        undo = new JButton("Undo");
        redo = new JButton("Redo");

        // Right-side undo/redo buttons
        JPanel rightMenuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        rightMenuPanel.setOpaque(false);
        rightMenuPanel.add(undo);
        rightMenuPanel.add(redo);

        // Add both panels to the menu bar
        menuBar.add(fileMenu, BorderLayout.WEST);
        menuBar.add(addMenu, BorderLayout.WEST);
        menuBar.add(removeMenu, BorderLayout.WEST);
        menuBar.add(renameMenu, BorderLayout.WEST);
        menuBar.add(typeMenu, BorderLayout.WEST);
        menuBar.add(rightMenuPanel, BorderLayout.EAST);


        // Creates JMenu submenus
        load = new GUIMenuItem("Open", Action.LOAD);
        save = new GUIMenuItem("Save", Action.SAVE);
        export = new GUIMenuItem("Export", Action.EXPORT);
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

        // TYPE
        changeFieldType= new GUIMenuItem("Field", Action.CHANGE_FIELD_TYPE);
        changeMethodType= new GUIMenuItem("Method", Action.CHANGE_METHOD_RETURN_TYPE);
        changeParameterType= new GUIMenuItem("Parameter", Action.CHANGE_PARAMETER_TYPE);
        changeRelatoinshipType = new GUIMenuItem("Relationship", Action.CHANGE_RELATIONSHIP_TYPE);

        // Creates action listeners for the different submenu actions
        load.addActionListener(this);
        save.addActionListener(this);
        export.addActionListener(this);
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

        changeFieldType.addActionListener(this);
        changeMethodType.addActionListener(this);
        changeParameterType.addActionListener(this);
        changeRelatoinshipType.addActionListener(this);
        
        undo.addActionListener(this);
        redo.addActionListener(this);
        
        fileMenu.setMnemonic(KeyEvent.VK_F);
        save.setMnemonic(KeyEvent.VK_S);

        // Adds submenus to menu
        fileMenu.add(load);
        fileMenu.add(save);
        fileMenu.add(export);
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

        typeMenu.add(changeFieldType);
        typeMenu.add(changeMethodType);
        typeMenu.add(changeParameterType);
        typeMenu.add(changeRelatoinshipType);
        
        // all panel
        theAllPanel = new JPanel();
        theAllPanel.setLayout(new OverlayLayout(theAllPanel));
        this.setContentPane(theAllPanel);

        // Create the canvas (JLayeredPane)
        canvas = new JLayeredPane();
        canvas.setLayout(null);  // Absolute positioning
        canvas.setPreferredSize(new Dimension(1500, 1500));  // Bigger than the frame

        // Scroll pane to hold the canvas
        scrollPane = new JScrollPane(canvas);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Make scrollPane fill the container
        scrollPane.setAlignmentX(0f);
        scrollPane.setAlignmentY(0f);
        
        // ensure sure the topPanel is painted on top of the scrollPane whenever the scrollPane gets painted
        scrollPane.getVerticalScrollBar().addAdjustmentListener(e -> {
            theAllPanel.repaint();
        });

        scrollPane.getHorizontalScrollBar().addAdjustmentListener(e -> {
            theAllPanel.repaint();
        });

        // Top panel to hold the command text boxes, combo boxes, submit & cancel buttons
        topPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();

                // === STEP 1: Draw white outline underneath everything ===
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setFont(currentActionLabel.getFont());

                String text = currentActionLabel.getText();
                FontMetrics fm = g2d.getFontMetrics();
                int x = 20;
                int y = 27 + fm.getAscent();

                GlyphVector gv = currentActionLabel.getFont().createGlyphVector(g2d.getFontRenderContext(), text);
                Shape outline = gv.getOutline(x, y);

                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2f));
                g2d.draw(outline);

                // === STEP 2: Draw semi-transparent gradient ===
                int width = getWidth();
                int height = getHeight();

                for (int row = 0; row < height; row++) {
                    double t = (double) row / height;
                    double angle = Math.PI * t;
                    float alpha = (float) ((Math.cos(angle) + 1.0) / 2.0);
                    int a = Math.min(255, Math.max(0, (int)(alpha * 55)));

                    g2d.setColor(new Color(0, 0, 25, a));
                    g2d.drawLine(0, row, width, row);
                }

                g2d.dispose();
                super.paintComponent(g); // Let Swing paint children (label fill)
            }
        };

        topPanel.setOpaque(false);
        topPanel.setPreferredSize(new Dimension(1000, 90)); // Width is ignored by layout
        topPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        topPanel.setLayout(null);
        topPanel.setAlignmentX(0f);
        topPanel.setAlignmentY(0f);
        topPanel.setVisible(false); // topPanel will be invisible until textboxes & comboboxes are added to it
        
        currentActionLabel = new JLabel("Outlined Text");
        currentActionLabel.setForeground(Color.BLACK);
        currentActionLabel.setFont(currentActionLabel.getFont().deriveFont(Font.BOLD, 14f));
        currentActionLabel.setBounds(20, 15, 200, 40);
        topPanel.add(currentActionLabel);


        // Add topPanel and scrollPane in order so topPanel is on top
        theAllPanel.add(topPanel);
        theAllPanel.add(scrollPane);
        
        this.setSize(1000, 1000);

        // Set JFrame attributes
        this.setTitle("UMLEditor");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setJMenuBar(menuBar);

        // Show the frame
        this.setVisible(true);

        // Force layout refresh
        Dimension size = this.getSize();
        this.setSize(size.width + 1, size.height + 1); // Tiny resize to force re-layout
        this.setSize(size); // Set back to original size
                
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
        
        
        // Add shortcuts for undo, redo, save, load, and exit
        int menuShortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        theAllPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, menuShortcutKeyMask), "undo");
        theAllPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, menuShortcutKeyMask | KeyEvent.SHIFT_DOWN_MASK), "redo");
        theAllPanel.getActionMap().put("undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (controller.runHelper(Action.UNDO, new String[] {}))
                {
                    actionHelper(Action.UNDO, new String[] {});
                }
            }
        });
        theAllPanel.getActionMap().put("redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (controller.runHelper(Action.REDO, new String[] {}))
                {
                    actionHelper(Action.REDO, new String[] {});
                }
            }
        });
        save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, menuShortcutKeyMask));
        load.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, menuShortcutKeyMask));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, menuShortcutKeyMask));
    }

    public JLayeredPane getCanvas()
    {
        return canvas;
    }

    public JScrollPane getScrollPane()
    {
        return scrollPane;
    }

    /**
     * Creates the submit and cancel buttons for each command
     * @param a the action the submit button will send to the handleSubmitAction method
     * @param offset offset of how many different boxes come before it for positioning in GUI
     */
    private void createButtons(Action a, int offset) {
        submitButton = new JButton("Submit");
        cancelButton = new JButton("Cancel");
        
        int currentActionLabelLen = currentActionLabel.getFontMetrics(currentActionLabel.getFont()).stringWidth(currentActionLabel.getText()) + 15;

        submitButton.setBounds(currentActionLabelLen + offset * 130 + 20, 20, 125, 30); // Position and size for submit button

        cancelButton.setBounds(currentActionLabelLen + (offset + 1) * 130 + 20, 20, 125, 30); // Position and size for cancel button
    
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
            	currentAction = null;
                handleCancelAction();
            }
        });
    
        // Add the buttons to the GUI
        topPanel.add(submitButton);
        topPanel.add(cancelButton);

        // Make Enter key trigger Submit
        getRootPane().setDefaultButton(submitButton);

        // Make Escape key trigger Cancel
        topPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "cancel");
        topPanel.getActionMap().put("cancel", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	currentAction = null;
                handleCancelAction();
            }
        });

        repaint(); // Refresh UI
    }

    /**
     * Concatenates text inputed by user + text from select boxes and parses them into commands to be sent to the Controller
     * @param action action to be performed that is sent to the Controller
     */
    private void handleSubmitAction(Action action) {
    	
        // Concatenate the text from text fields and combo boxes just like pressing Enter
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

        // Special handling for actions that require parameter formatting 
        // Turns method1 (String int String) into method1 without the type list
        if (action.equals(Action.ADD_METHOD) || action.equals(Action.ADD_PARAMETERS) 
        || action.equals(Action.REMOVE_METHOD) || action.equals(Action.REMOVE_PARAMETERS) 
        || action.equals(Action.RENAME_METHOD) || action.equals(Action.RENAME_PARAMETER)
        || action.equals(Action.CHANGE_METHOD_RETURN_TYPE) || action.equals(Action.CHANGE_PARAMETER_TYPE))
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
                    finalInputsList.add(allInputs[2]); // return type
                    if (allInputs.length == 5)
                    {
                        types = allInputs[3].trim().split("\\s+");   // Extract types 
                        names = allInputs[4].trim().split("\\s+");   // Extract names 
                    }
                }
                else if (action.equals(Action.REMOVE_PARAMETERS))
                {
                    finalInputsList.addAll(Arrays.asList(parameters));
                    if (!allInputs[2].equals("All Parameters"))
                    {
                    	finalInputsList.add(";"); 
                    	finalInputsList.add(allInputs[2]);  // Parameter Name
                    }
                }
                else if (action.equals(Action.RENAME_METHOD) || action.equals(Action.CHANGE_METHOD_RETURN_TYPE))
                {
                    finalInputsList.addAll(Arrays.asList(parameters));
                    finalInputsList.add(allInputs[2]);  // New Method Name or New Method Type
                }
                else if (action.equals(Action.CHANGE_PARAMETER_TYPE))
                {
                    finalInputsList.addAll(Arrays.asList(parameters));
                    finalInputsList.add(allInputs[2]); // Parameter Name
                    finalInputsList.add(allInputs[3]); // New Parameter Type
                }
                else if (action.equals(Action.REMOVE_METHOD))
                {
                    finalInputsList.addAll(Arrays.asList(parameters));
                }
                else
                {
                    if (allInputs.length >= 4)
                    {
                        types = allInputs[2].trim().split("\\s+");   // Extract types 
                        names = allInputs[3].trim().split("\\s+");   // Extract names 
                    }
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
        
        // Call controller helper with the concatenated arguments
        if (controller.runHelper(action, allInputs)) {
            actionHelper(action, allInputs);
            repaint();
        }
        
        showCommandBar(action);
    }

    // Removes Submit and Cancel buttons when the Cancel button is clicked
    private void handleCancelAction() {
    	
        topPanel.remove(submitButton);
        topPanel.remove(cancelButton);
        textBoxes.forEach(topPanel::remove);
        textBoxes.clear();
        comboBoxes.forEach(topPanel::remove);
        comboBoxes.clear();
        topPanel.setVisible(false);
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
        JComboBox<String> destinationComboBox = null;
    
        for (int i = 0; i < placeholders.length; i++) {
            String placeholder = placeholders[i];
            JComboBox<String> box = new JComboBox<>();
    
            switch (placeholder) {
                case "Class":
                    classComboBox = createClassComboBox(i);
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
                    boolean includeAllParametersOption = a == Action.REMOVE_PARAMETERS; // an option for 'All Parameters' should be available when removing parameters
                    updateParameterComboBox(parameterComboBox, classComboBox, methodComboBox, includeAllParametersOption); // Initial population
                    addMethodComboBoxListener(classComboBox, methodComboBox, parameterComboBox, includeAllParametersOption);
                    box = parameterComboBox;
                    break;

                case "Desination":
                    destinationComboBox = createComboBoxForClassItems(classComboBox, "getDesinations");
                    addComboBoxListener(classComboBox, destinationComboBox, "getDesinations");
                    box = destinationComboBox;
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
            topPanel.add(box);
        }
        reload();
    }
    
    /**
     * Creates a combobox consisting of all current classes that exist within the model
     * @return a new combobox with all currect classes that exist within the model
     */
    private JComboBox<String> createClassComboBox(int idx) {
        String[] classes = GUIUMLClasses.keySet().toArray(String[]::new);
        JComboBox<String> classComboBox = new JComboBox<>(classes);
        if (idx == 0 && lastClassTouched != null)
        	classComboBox.setSelectedItem(lastClassTouched);
        else if (idx == 1 && secondToLastClassTouched != null)
        	classComboBox.setSelectedItem(secondToLastClassTouched);
        classComboBox.setName("Class");
        classComboBox.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				secondToLastClassTouched = lastClassTouched;
                lastClassTouched = (String) classComboBox.getSelectedItem();
			}});
        return classComboBox;
    }
    
    /**
     * Creates comboboxes for either the fields or methods based on methodType
     * @param classComboBox combobox consisting of all currect classes that exist within the model
     * @param methodType either "getFields" for fields, "getMethods" for methods, or "getDestinations" for destinations
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
    private void addMethodComboBoxListener(JComboBox<String> classComboBox, JComboBox<String> methodComboBox, JComboBox<String> paramBox, boolean includeAllParametersOption) {
        methodComboBox.addItemListener(new ComboBoxListener(new JComboBox[]{ paramBox }) {
            @Override
            protected void updateComboBox(JComboBox<String> box) {
                updateParameterComboBox((JComboBox<String>) box, classComboBox, methodComboBox, includeAllParametersOption);
            }
        });
    }
    
    /**
     * Updates a combobox to fill it with new updated data
     * @param box combobox to be updated
     * @param classComboBox combobox consisting of all currect classes that exist within the model
     * @param methodType either "getFields" for fields, "getMethods" for methods, or "getDestinations" for destinations
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
    private void updateParameterComboBox(JComboBox<String> paramBox, JComboBox<String> classComboBox, JComboBox<String> methodComboBox, boolean includeAllParametersOption) {
        String selectedClass = (String) classComboBox.getSelectedItem();
        String selectedMethodSignature = (String) methodComboBox.getSelectedItem();
        
        if (selectedClass != null && selectedMethodSignature != null) {
            String methodName = selectedMethodSignature.split("\\(")[0];  // Get method name (split by '(')
            
            paramBox.removeAllItems();

            String[] parameters = getMethodParameters(selectedClass, methodName);
            for (String param : parameters) {
                paramBox.addItem(param);
            }
            if (includeAllParametersOption)
            	paramBox.addItem("All Parameters");
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
     * @param methodType either "getFields" for fields, "getMethods" for methods, or "getDestinations" for destinations
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
     * Returns list of either fields, methods, or destinations based on methodType provided
     * @param selectedClass classname that was selected within the classComboBox
     * @param methodType either "getFields" for fields, "getMethods" for methods, or "getDestinations" for destinations
     * @return list of either all fields, all methods, or all destinations that exist within a certain class
     */
    private String[] getClassItems(String selectedClass, String methodType) {
        return switch (methodType) {
            case "getFields" -> GUIUMLClasses.get(selectedClass).getUMLClass().getFields().stream()
                    .map(x -> x.getName()).toArray(String[]::new);
            case "getMethods" -> GUIUMLClasses.get(selectedClass).getUMLClass().getMethods().stream()
                    .map(x -> x.toTypes()).toArray(String[]::new);
            case "getDesinations" -> RelationshipHandler.getAllRelationshipsForClassname(selectedClass).stream()
                    .filter(x -> x.getSrc().getName().equals(selectedClass))
                    .map (x -> x.getDest().getName())
                    .toArray(String[]::new);
            default -> new String[0];
        };
    }
    
    /**
     * Styles a combobox in order to make it look a little nicer for the user
     * @param box box to be styled
     * @param index determines the offset horizontally, so the boxes don't overlap
     */
    private void styleComboBox(JComboBox<String> box, int index) {
    	int currentActionLabelLen = currentActionLabel.getFontMetrics(currentActionLabel.getFont()).stringWidth(currentActionLabel.getText()) + 15;
        box.setBounds(currentActionLabelLen + index * 130 + 20, 20, 125, 30);
    } 

    /**
     * Creates textboxes
     * @param a action of the textbox
     * @param placeholders values to be filled in for the textbox
     * @param offset how many parameters come before this, so these don't overlap with combo boxes
     */
    public void makeTextBoxes(Action a, String [] placeholders, int offset)
    {
        // Rectangle view = scrollPane.getViewport().getViewRect();
        textBoxes.clear();
        for (int i = 0; i < placeholders.length; i++)
        {
            JTextField text = new JTextField(20);

            int currentActionLabelLen = currentActionLabel.getFontMetrics(currentActionLabel.getFont()).stringWidth(currentActionLabel.getText()) + 15;
            
            int xPosition = currentActionLabelLen + (offset + i) * 130 + 20; // x relative to the visible area
            int yPosition = 20; // y relative to the visible area

            text.setBounds(xPosition, yPosition, 125, 30); // Set position and size

            String placeholder = placeholders[i];
            text.setText(placeholder);
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

            textBoxes.add(text);
            topPanel.add(text);
        }
        reload();
    }

    // Based on the action sent in, make the correct number of textboxes and comboboxes along with the Submit and Cancel buttons
    @Override
    public void actionPerformed(ActionEvent e) {
    	Action a;
    	if (e.getSource() instanceof GUIMenuItem)
    	{
    		a = ((GUIMenuItem) e.getSource()).action;
    	}
    	else
    	{
    		if (e.getSource() == undo)
    			a = Action.UNDO;
    		else
    			a = Action.REDO;
    	}    	
    	showCommandBar(a);
    }
    
    /**
     * Show the command bar with inputs for the specified action.
     * 
     * @param a The action to allow the user to perform in the command bar.
     */
    private void showCommandBar(Action a)
    {
    	currentAction = a;
    	currentActionLabel.setText(Command.COMMANDS[a.ordinal()]);
    	switch (a)
    	{
			case ADD_CLASS:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeTextBoxes(a, new String [] {"Class Name"}, 0);
		        createButtons(a, 1);
		        topPanel.setVisible(true);
		        break;
			case ADD_FIELD:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class"});
	            makeTextBoxes(a, new String [] {"Field Name", "Type: int String"}, 1);
	            createButtons(a, 3);
	            topPanel.setVisible(true);
	            break;
			case ADD_METHOD:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class"});
	            makeTextBoxes(a, new String [] {"Method Name", "Return Type", "Types: int String", "Parameters: a b c"}, 1);
	            createButtons(a, 5);
	            topPanel.setVisible(true);
	            break;
			case ADD_PARAMETERS:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Method"});
	            makeTextBoxes(a, new String [] {"Types: int String", "Parameters: a b c"}, 2);
	            createButtons(a, 4);
	            topPanel.setVisible(true);
	            break;
			case ADD_RELATIONSHIP:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Class", "Relationship"});
	            createButtons(a, 3);
	            topPanel.setVisible(true);
	            break;
			case REMOVE_CLASS:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class"});
	            createButtons(a, 1);
	            topPanel.setVisible(true);
	            break;
			case REMOVE_FIELD:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Field"});
	            createButtons(a, 2);
	            topPanel.setVisible(true);
	            break;
			case REMOVE_METHOD:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Method"});
	            createButtons(a, 2);
	            topPanel.setVisible(true);
	            break;
			case REMOVE_PARAMETERS:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Method", "Parameter"});
	            createButtons(a, 3);
	            topPanel.setVisible(true);
	            break;
			case REMOVE_RELATIONSHIP:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Desination"});
	            createButtons(a, 2);
	            topPanel.setVisible(true);
	            break;
			case RENAME_CLASS:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class"});
	            makeTextBoxes(a, new String [] {"New Class Name"}, 1);
	            createButtons(a, 2);
	            topPanel.setVisible(true);
	            break;
			case RENAME_FIELD:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Field"});
	            makeTextBoxes(a, new String [] {"New Field Name"}, 2);
	            createButtons(a, 3);
	            topPanel.setVisible(true);
	            break;
			case RENAME_METHOD:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Method"});
	            makeTextBoxes(a, new String [] {"New Method Name"}, 2);
	            createButtons(a, 3);
	            topPanel.setVisible(true);
	            break;
			case RENAME_PARAMETER:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Method", "Parameter"});
	            makeTextBoxes(a, new String [] {"New Parameter Name"}, 3);
	            createButtons(a, 4);
	            topPanel.setVisible(true);
	            break;
			case CHANGE_FIELD_TYPE:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Field"});
	            makeTextBoxes(a, new String [] {"New Type"}, 2);
	            createButtons(a, 3);
	            topPanel.setVisible(true);
	            break;
			case CHANGE_METHOD_RETURN_TYPE:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Method"});
	            makeTextBoxes(a, new String [] {"New Type"}, 2);
	            createButtons(a, 3);
	            topPanel.setVisible(true);
	            break;
			case CHANGE_PARAMETER_TYPE:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Method", "Parameter"});
	            makeTextBoxes(a, new String [] {"New Type"}, 3);
	            createButtons(a, 4);
	            topPanel.setVisible(true);
	            break;
			case CHANGE_RELATIONSHIP_TYPE:
				if (!textBoxes.isEmpty() || !comboBoxes.isEmpty())
		    		handleCancelAction();
				makeComboBoxes(a, new String [] {"Class", "Desination", "Relationship"});
	            createButtons(a, 3);
	            topPanel.setVisible(true);
	            break;
			case LOAD:
                if (controller.runHelper(a, new String[] {}))
                {
                    loadGUIObjects();
                }
	            break;
			case SAVE:
				controller.runHelper(a, new String[] {});
	            break;
			case EXPORT:
				controller.runHelper(a, new String[] {});
	            break;
			case UNDO:
				if (controller.runHelper(a, new String[] {}))
	            {
	                actionHelper(a, new String[] {});
	            }
	            break;
			case REDO:
				if (controller.runHelper(a, new String[] {}))
	            {
	                actionHelper(a, new String[] {});
	            }
	            break;
			case EXIT:
	        	if (controller.runHelper(a, new String[] {}))
	        	{
	        		System.exit(0);
	        	}
	            break;
		default:
			break;
    	}
    	requestFocusForCommandBar();
    }
    
    public boolean requestFocusForCommandBar()
    {
    	Action a = currentAction;
    	switch (a)
    	{
    		case ADD_CLASS:
    			return textBoxes.get(0).requestFocusInWindow(); // class name
    		case REMOVE_CLASS:
    			return comboBoxes.get(0).requestFocusInWindow(); // class name
    		case RENAME_CLASS:
    			return textBoxes.get(0).requestFocusInWindow(); // new class name // should be diff
    		case ADD_RELATIONSHIP:
    			return comboBoxes.get(2).requestFocusInWindow(); // relationship type // should be different second time
    		case REMOVE_RELATIONSHIP:
    			return comboBoxes.get(0).requestFocusInWindow(); // first class name
    		case ADD_METHOD:
    			return textBoxes.get(0).requestFocusInWindow(); // method name
    		case REMOVE_METHOD:
    			return comboBoxes.get(1).requestFocusInWindow(); // method
    		case RENAME_METHOD:
    			return comboBoxes.get(1).requestFocusInWindow(); // method
    		case ADD_FIELD:
    			return textBoxes.get(0).requestFocusInWindow(); // field name
    		case REMOVE_FIELD:
    			return comboBoxes.get(1).requestFocusInWindow(); // field name
    		case RENAME_FIELD:
    			return comboBoxes.get(1).requestFocusInWindow(); // field name
    		case ADD_PARAMETERS:
    			return comboBoxes.get(1).requestFocusInWindow(); // method
    		case REMOVE_PARAMETERS:
    			return comboBoxes.get(1).requestFocusInWindow(); // method
    		case RENAME_PARAMETER:
    			return comboBoxes.get(2).requestFocusInWindow(); // parameter name
    		case CHANGE_RELATIONSHIP_TYPE:
    			return comboBoxes.get(2).requestFocusInWindow(); // relationship type
    		case CHANGE_PARAMETER_TYPE:
    			return comboBoxes.get(1).requestFocusInWindow(); // method
    		case CHANGE_FIELD_TYPE:
    			return comboBoxes.get(1).requestFocusInWindow(); // field name
    		case CHANGE_METHOD_RETURN_TYPE:
    			return comboBoxes.get(1).requestFocusInWindow(); // method
    		default:
    			return false;
    	}
    }


    /**
     * Based on the action, perform actions within the GUIView to update it with new data from JModel
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
                break;
            case RENAME_CLASS:
                renameUMLClass(args[0], args[1]);
                break;
            case ADD_RELATIONSHIP:
            case REMOVE_RELATIONSHIP:
            case CHANGE_RELATIONSHIP_TYPE:
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
            case CHANGE_METHOD_RETURN_TYPE:
                updateAttributes(args[0]);
            case ADD_FIELD:
                updateAttributes(args[0]);
                break;
            case REMOVE_FIELD:
                updateAttributes(args[0]);
                break;
            case RENAME_FIELD:
                updateAttributes(args[0]);
                break;
            case CHANGE_FIELD_TYPE:
                updateAttributes(args[0]);
            case ADD_PARAMETERS:
                updateAttributes(args[0]);
                break;
            case REMOVE_PARAMETERS:
                updateAttributes(args[0]);
                break;
            case RENAME_PARAMETER:
                updateAttributes(args[0]);
                break;
            case CHANGE_PARAMETER_TYPE:
                updateAttributes(args[0]);
                break;
            case UNDO:
                updateClasses();
                break;
            case REDO:
                updateClasses();
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
    	
    	int maxX = 0, maxY = 0;
    	for (GUIUMLClass g : GUIUMLClasses.values()) {
    	    Position p = g.getUMLClass().getPosition();
    	    int w = g.getWidth();
    	    int h = g.getHeight();
    	    maxX = Math.max(maxX, p.getX() + w * 3);
    	    maxY = Math.max(maxY, p.getY() + h * 2);
    	}
    	canvas.setPreferredSize(new Dimension(maxX, maxY));
    	revalidate();
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
        canvas.add(arrow, JLayeredPane.DEFAULT_LAYER);
        reload();  // Force a reload to revalidate and repaint
    }

    /**
     * Updates all of the current arrows by removing them all and redrawing them
     */
    public void updateArrows() {
        for (ArrowComponent arrow : arrows) 
        {
            canvas.remove(arrow); // Remove from JFrame
        }
        arrows.clear();  // Clear the list of arrows
        
        // Get the relationships and create arrows for each one
        for (Relationship relationship : RelationshipHandler.getRelationObjects()) 
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
        theAllPanel.revalidate(); // Recalculate layout
        theAllPanel.repaint();    // Repaint to reflect changes
    }

    /**
     * Adds a UMLClass to list of GUIUMLClasses, which contains all current classes that exist within the GUI
     * @param className name of class to be added
     */
    public void addUMLClass(String className)
    {
        GUIUMLClass newUMLClass = new GUIUMLClass(UMLClassHandler.getClass(className), controller, this);

        // Adds the JLayeredPane to the Frame (this) and to the HashMap of GUIUMLClasses
        canvas.add(newUMLClass.getJLayeredPane(), JLayeredPane.PALETTE_LAYER);
        canvas.setComponentZOrder(newUMLClass.getJLayeredPane(), JLayeredPane.DEFAULT_LAYER); // Bring to front
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
        canvas.remove(GUIUMLClasses.get(className).getJLayeredPane());
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
        updateArrows();
        reload();
    }

    /**
     * Updates all of the classes after Undo is clicked and retains color except when name is changed
     */
    public void updateClasses()
    {
        for (GUIUMLClass g : GUIUMLClasses.values())
        {
            canvas.remove(g.getJLayeredPane());
        }
        GUIUMLClasses.clear();
        for (UMLClass c : UMLClassHandler.getAllClasses())
        {
            addUMLClass(c.getName());
        }
        updateArrows();
    }

    /**
     * Updates the fields in the GUI UMLclasses based on user input
     * @param className name of UMLClass to be updated
     */
    public void updateAttributes(String className)
    {
        GUIUMLClasses.get(className).update();
    }
    
    public void setLastClassTouched(String className)
    {
    	secondToLastClassTouched = lastClassTouched;
    	lastClassTouched = className;
    	if (!comboBoxes.isEmpty())
    	{
    		if ("Class".equals(comboBoxes.get(0).getName()))
    		{
    			if (comboBoxes.size() > 1 && "Class".equals(comboBoxes.get(1).getName()))
    			{
    				comboBoxes.get(0).setSelectedItem(secondToLastClassTouched);
    				comboBoxes.get(1).setSelectedItem(lastClassTouched);
    			}
    			else
    			{
    				comboBoxes.get(0).setSelectedItem(lastClassTouched);
    			}
    		}
    	}
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
            String checkMsg = checks.get(i).check(ans); // This will either be "" or an error message
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
	public void notifyFail(String message) {
		JOptionPane.showMessageDialog(this, message);	
	}

	@Override
	public void display(String message) {
		JOptionPane.showMessageDialog(this, message);
	}
	
	@Override
	public void setController(UMLController c)
	{
		controller = c;
	}

    @Override
	public int promptForYesNoInput(String message, String title) {
		int res = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
		if(res == 0 || res == 1 )
		{
			return res;
		}
		else 
		{
			return 2;
		}
	}
    
    @Override
	public JComponent getJComponentRepresentation() {
		return canvas;
	}
}

