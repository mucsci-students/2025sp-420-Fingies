import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.util.HashMap;

public class GUIView extends JFrame implements ActionListener{

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu addMenu;
    private JMenu removeMenu;
    private JMenu renameMenu;

    private JMenuItem load;
    private JMenuItem save;
    private JMenuItem exit;

    // Hashmap for creating UMLClasses represented as JLayeredPanes with String for classname and easy identificaton
    private HashMap<String, JLayeredPane> UMLClasses;

    public GUIView ()
    {
        // Creates the list for UMLClasses to be added
        UMLClasses = new HashMap<String, JLayeredPane>();

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
        load = new JMenuItem("Open");
        save = new JMenuItem("Save");
        exit = new JMenuItem("Exit");

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

        

        // Sets main attributes of the "frame" itself and adds the JLayeredPane
        this.setTitle("UMLEditor");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(1000, 1000);
        this.setJMenuBar(menuBar);

        this.setVisible(true);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == load)
        {
            System.out.println("loaded file");
        }
        else if (e.getSource() == save)
        {
            System.out.println("saved file");
        }
        else if (e.getSource() == exit)
        {
            System.exit(0);
        }
    }

    public void addUMLClass(String name)
    {
        // Creates a bunch of different panels
        JPanel bgPanel = new JPanel();
        bgPanel.setBackground(Color.BLACK);
        bgPanel.setBounds(0, 0, 150, 250);

        JPanel classPanel = new JPanel();
        classPanel.setBackground(Color.RED);
        classPanel.setBounds(5, 5, 140, 25);

        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setBackground(Color.GREEN);
        fieldsPanel.setBounds(5, 35, 140, 75);

        JPanel methodsPanel = new JPanel();
        methodsPanel.setBackground(Color.BLUE);
        methodsPanel.setBounds(5, 115, 140, 125);

        /* Here are the different layers in order for a JLayeredPane:
                JLayeredPane.DEFAULT_LAYER
                JLayeredPane.PALETTE_LAYER
                JLayeredPane.MODAL_LAYER
                JLayeredPane.POPUP_LAYER
                JLayeredPane.DRAG_LAYER 
        */

        // Create a JLayeredPane
        JLayeredPane background = new JLayeredPane();
        background.setBounds(0, 0, 150, 250);

        // This is here just to see temporary border of JLayeredPane
        background.setBackground(Color.YELLOW);
        background.setOpaque(true); // Make it visible

        // Add all panels on top of it including bgPanel
        background.add(bgPanel, JLayeredPane.DEFAULT_LAYER);
        background.add(classPanel, JLayeredPane.PALETTE_LAYER);
        background.add(fieldsPanel, JLayeredPane.PALETTE_LAYER);
        background.add(methodsPanel, JLayeredPane.PALETTE_LAYER);

        // Creates new listener for the newly added JLayeredPane
        DragListener dragListener = new DragListener(background, this);
        background.addMouseListener(dragListener);
        background.addMouseMotionListener(dragListener);

        // Adds the JLayeredPane to the Frame (this) and to the HashMap of UMLClasses
        this.add(background);
        UMLClasses.put(name, background);
    }

    public static void main (String[] args)
    {
        // Creates the frame
        GUIView frame = new GUIView();
        frame.addUMLClass("Class1");
        frame.addUMLClass("Class2");
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

