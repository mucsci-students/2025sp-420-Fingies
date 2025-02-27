import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

public class GUIView extends JFrame implements ActionListener, View {

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu addMenu;
    private JMenu removeMenu;
    private JMenu renameMenu;

    private GUIMenuItem load;
    private GUIMenuItem save;
    private GUIMenuItem exit;
    
    private Controller controller;
    final JFileChooser fileChooser = new JFileChooser();

    // HashMap with key as name of class and value as GUIUMLClass associated with the name
    private HashMap<String, GUIUMLClass> UMLClasses;

    public GUIView ()
    {
        // Creates the list for UMLClasses to be added
        UMLClasses = new HashMap<String, GUIUMLClass>();

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

        // Creates action listeners for the different submenu actions
        load.addActionListener(this);
        save.addActionListener(this);
        exit.addActionListener(this);

        // Allows the press of a key to do the function of clicking the menu item WHILE in the menu
        save.setMnemonic(KeyEvent.VK_S); // S for save


        // Adds submenus to menu
        fileMenu.add(load);
        fileMenu.add(save);
        fileMenu.add(exit);

        

        // Sets main attributes of the "frame" (this)
        this.setTitle("UMLEditor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        // this.setLayout(new FlowLayout());
        this.setSize(1000, 1000);
        this.setJMenuBar(menuBar);

        this.setVisible(true);

    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	Action a = ((GUIMenuItem) e.getSource()).action;
    	String[] args = new String[0];
        controller.runHelper(a, args);
    }

    public void addUMLClass(String name)
    {
        GUIUMLClass newUMLClass = new GUIUMLClass(name);
        // Creates new listener for the newly added JLayeredPane
        DragListener dragListener = new DragListener(newUMLClass.getJLayeredPane(), this);
        newUMLClass.getJLayeredPane().addMouseListener(dragListener);
        newUMLClass.getJLayeredPane().addMouseMotionListener(dragListener);

        // Adds the JLayeredPane to the Frame (this) and to the HashMap of UMLClasses
        this.add(newUMLClass.getJLayeredPane());
        UMLClasses.put(name, newUMLClass);

        // Force the GUI to refresh and update
        this.revalidate(); // Recalculate layout
        this.repaint();    // Repaint to reflect changes
    }

    public void removeUMLClass(String name)
    {
        // this..getContentPane().remove(UMLClasses.get(name).getJLayeredPane());
        this.remove(UMLClasses.get(name).getJLayeredPane());
        UMLClasses.remove(name);

        // Force the GUI to refresh and update
        this.revalidate(); // Recalculate layout
        this.repaint();    // Repaint to reflect changes
    }

    public void renameUMLClass(String className, String newName)
    {
        GUIUMLClass temp = UMLClasses.get(className);
        UMLClasses.remove(className);
        temp.renameClass(newName);
        UMLClasses.put(newName, temp);

        // Force the GUI to refresh and update
        this.revalidate(); // Recalculate layout
        this.repaint();    // Repaint to reflect changes
    }

    
    @Override
    public String promptForSaveInput(String message)
    {
    	fileChooser.setDialogTitle(message);
        int returnValue = fileChooser.showSaveDialog(this);
        if(returnValue != JFileChooser.APPROVE_OPTION)
        	return null;
        return fileChooser.getSelectedFile().getName();
    }
    
	@Override
	public String promptForInput(String message) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<String> promptForInput(List<String> messages) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<String> promptForInput(List<String> messages, List<InputCheck> checks) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void notifySuccess() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifySuccess(String message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyFail(String message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void display(String message) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void help() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void help(String command) {
		// TODO Auto-generated method stub
		
	}
	
	public void setController(Controller c)
	{
		controller = c;
	}
	
	@Override
	public String promptForOpenInput(String message) {
    	fileChooser.setDialogTitle(message);
        int returnValue = fileChooser.showOpenDialog(this);
        if(returnValue != JFileChooser.APPROVE_OPTION)
        	return null;
        return fileChooser.getSelectedFile().getName();
	}
	@Override
	public void run() 
	{
		// Nothing to be implemented here
	}
  
}

// Drag listener for JLayeredPane
class DragListener extends MouseAdapter {
    private final JComponent component;
    private final JFrame parentFrame;
    private Point initialClick;

    public DragListener(JComponent component, JFrame parentFrame) {
        this.component = component;
        this.parentFrame = parentFrame;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        initialClick = e.getPoint(); // Store initial click position
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (initialClick == null) return;

        // Get current location of the JLayeredPane
        int x = component.getX() + e.getX() - initialClick.x;
        int y = component.getY() + e.getY() - initialClick.y;

        // Get parent frame size
        int frameWidth = parentFrame.getWidth();
        int frameHeight = parentFrame.getHeight();

        // Prevent dragging off the screen (Constrain within parent frame)
        int maxX = frameWidth - component.getWidth();
        int maxY = frameHeight - component.getHeight();

        // Constrain X and Y to stay within the frame bounds
        if (x < 0) x = 0;
        if (x > maxX) x = maxX;
        if (y < 0) y = 0;
        if (y > maxY) y = maxY;

        // Move JLayeredPane to new position within bounds
        component.setLocation(x, y);
    }
}

