package org.fingies;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.event.MouseEvent;
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
    private ArrayList<JComboBox> comboBoxes;

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
        comboBoxes = new ArrayList<JComboBox>();

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
    
    public void makeComboBoxes(Action a, String[] placeholders) {
        comboBoxes.clear();
        JComboBox<String> classComboBox = null;

        for (int i = 0; i < placeholders.length; i++) {
            JComboBox<String> box = new JComboBox<>();

            switch (placeholders[i]) {
                case "Class":
                    // Make a combo box with an item for every class in the diagram
                    String[] classes = GUIUMLClasses.keySet().toArray(String[]::new);
                    classComboBox = new JComboBox<>(classes);
                    box = classComboBox;
                    break;

                case "Field":
                    // Make a combo box with an item for every field the selected class has
                    updateComboBox(box, classComboBox, "getFields");
                    addComboBoxListener(classComboBox, box, "getFields");
                    break;

                case "Method":
                    // Make a combo box with an item for every method the selected class has
                    updateComboBox(box, classComboBox, "getMethods");
                    addComboBoxListener(classComboBox, box, "getMethods");
                    break;

                case "Parameter":
                    // Make a combo box with an item for every parameter (currently not implemented)
                    break;

                default:
                    throw new IllegalArgumentException("Unknown placeholder: " + placeholders[i]);
            }

            styleComboBox(box, i);
            comboBoxes.add(box);
            this.add(box);
        }

        reload();
    }

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

    private void addComboBoxListener(JComboBox<String> classComboBox, JComboBox<String> box, String methodType) {
        var listener = new ComboBoxListener(new JComboBox[]{box}) {
            @Override
            public void itemStateChanged(ItemEvent arg) {
                String selectedClass = (String) arg.getItem();
                if (selectedClass != null) {
                    box.removeAllItems();
                    String[] items = getClassItems(selectedClass, methodType);
                    for (String item : items) {
                        box.addItem(item);
                    }
                }
            }
        };
        classComboBox.addItemListener(listener);
    }

    private String[] getClassItems(String selectedClass, String methodType) {
        return switch (methodType) {
            case "getFields" -> GUIUMLClasses.get(selectedClass).getUMLClass().getFields().stream()
                    .map(x -> x.getName()).toArray(String[]::new);
            case "getMethods" -> GUIUMLClasses.get(selectedClass).getUMLClass().getMethods().stream()
                    .map(x -> x.getName()).toArray(String[]::new);
            default -> new String[0];
        };
    }

    private void styleComboBox(JComboBox<String> box, int index) {
        box.setBounds(index * 130 + 20, 80, 125, 30);
        box.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
    }


    public void makeTextBoxes(Action a, String [] placeholders)
    {
        textBoxes.clear();
        for (int i = 0; i < placeholders.length; i++)
        {
            JTextField text;
            text = new JTextField(20);
            text.setBounds(i * 130 + 20, 20, 125, 30); // Set position and size
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

            textBoxes.add(text);
            this.add(text);
            // if (i == 0)
            //     text.requestFocus(); // Automatically focus the text field
            addEnterKeyListenerToRemove(a, text);
            repaint(); // Refresh UI
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
    	Action a = ((GUIMenuItem) e.getSource()).action;

        if (e.getSource() == addClass && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name"});
            makeComboBoxes(a, new String [] {"Class"});
        }
        else if (e.getSource() == addField && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name", "Field Name"});
            makeComboBoxes(a, new String [] {"Class", "Field"});
        }
        else if (e.getSource() == addMethod && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name", "Method Name", "Parameters: a b c"});
            makeComboBoxes(a, new String [] {"Class", "Method"});
        }
        else if (e.getSource() == addParameter && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name", "Method Name", "Arity of Method", "Parameters: a b c"});
        }
        else if (e.getSource() == addRelationship && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Src Class", "Dest Class", "Relationship Type"});
        }
        else if (e.getSource() == removeClass && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name"});
        }
        else if (e.getSource() == removeField && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name", "Field Name"});
        }
        else if (e.getSource() == removeMethod && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name", "Method Name", "Arity of Method"});
        }
        else if (e.getSource() == removeParameter && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name", "Method Name", "Arity of Method", "Parameters: a b c"});
        }
        else if (e.getSource() == removeRelationship && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Src Class", "Dest Class"});
        }
        else if (e.getSource() == renameClass && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Old Class Name", "New Class Name"});
        }
        else if (e.getSource() == renameField && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name", "Old Field Name", "New Field Name"});
        }
        else if (e.getSource() == renameMethod && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name", "Old Method Name", "Arity of Method", "New Method Name"});
        }
        else if (e.getSource() == renameParameter && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Class Name", "Method Name", "Arity of Method", "Old Parameter", "New Parameter"});
        }
        else if (e.getSource() == renameRelationshipType && textBoxes.isEmpty())
        {
            makeTextBoxes(a, new String [] {"Src Class", "Dest Class", "Relationship Type"});
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

    	// String[] args = new String[0];
        // controller.runHelper(a, args);
    }

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

    private void addEnterKeyListenerToRemove(Action action, JTextField field) {
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	System.out.println("hi");
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String[] args = textBoxes.stream().map(JTextField::getText).toArray(String[]::new);
                    textBoxes.forEach(GUIView.this::remove); // Remove all text fields
                    textBoxes.clear();
                    repaint(); // Refresh UI
                    // System.out.println("arg0: " + args[0]);
                    // System.out.println("arg0: " + args[0] + "\n" + "arg1: " + args[1]);
                    //System.out.println("Original args: " + Arrays.toString(args));

                    if (action.equals(Action.ADD_METHOD) || action.equals(Action.ADD_PARAMETERS) || 
                    action.equals(Action.REMOVE_PARAMETERS) || action.equals(Action.RENAME_PARAMETER))
                    {
                        // Split last element in list of parameters
                        // Removes leading and trailing spaces from the string
                        // Splits the string by one or more whitespace characters (including spaces, tabs, etc.), ensuring no empty elements
                        String[] params = args[args.length - 1].trim().split("\\s+");
                        
                        // Remove parameters that are just empty or made of whitespace
                        params = Arrays.stream(params).filter(x -> !x.isBlank()).toArray(String[]::new);

                        // Remove the last element from args
                        args = Arrays.copyOf(args, args.length - 1);

                        // Combine both arrays
                        args = Stream.concat(Arrays.stream(args), Arrays.stream(params)).toArray(String[]::new);
                        //System.out.println("Updated args: " + Arrays.toString(args));
                    }

                    if (controller.runHelper(action, args))
                    {
                        actionHelper(action, args); 
                        repaint();
                    }
                    
                }
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    textBoxes.forEach(GUIView.this::remove); // Remove all text fields
                    textBoxes.clear();
                    repaint(); // Refresh UI
                }
            }
        }); 
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

    public void addUMLClass(String className)
    {
        GUIUMLClass newUMLClass = new GUIUMLClass(UMLClassHandler.getClass(className), controller, this);

        // Adds the JLayeredPane to the Frame (this) and to the HashMap of GUIUMLClasses
        this.add(newUMLClass.getJLayeredPane(), JLayeredPane.PALETTE_LAYER);
        GUIUMLClasses.put(className, newUMLClass);
        reload();
    }

    public void removeUMLClass(String className)
    {
        // this..getContentPane().remove(GUIUMLClasses.get(name).getJLayeredPane());
        this.remove(GUIUMLClasses.get(className).getJLayeredPane());
        GUIUMLClasses.remove(className);
        reload();
    }

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
	
	public void setController(Controller c)
	{
		controller = c;
	}
    
    
}

