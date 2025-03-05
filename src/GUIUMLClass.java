import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GUIUMLClass {
    private final int PIXELSPERCHARACTER = 8;
    private JPanel classPanel;
    private JPanel fieldsPanel;
    private JPanel methodsPanel;
    private JLabel className;
    private JLabel fields;
    private JLabel methods;
    private JLayeredPane background;
    private Color color;

    private UMLClass umlclass;

    public GUIUMLClass(UMLClass umlclass)
        
    {
        this.umlclass = umlclass;

        // Creates a random color for the class
        color = new Color((int)(Math.random() * 225 + 15), (int)(Math.random() * 225 + 15), (int)(Math.random() * 225 + 15), 100);

        classPanel = new JPanel();
        // classPanel.setBackground(Color.RED);
        // classPanel.setBackground(new Color(255, 0, 0, 60));
        classPanel.setBackground(color);
        classPanel.setBounds(5, 5, 140, 25);
        classPanel.setLayout(null);  // Set layout to null

        fieldsPanel = new JPanel();
        // fieldsPanel.setBackground(Color.GREEN);
        fieldsPanel.setBackground(color);
        fieldsPanel.setBounds(5, 35, 140, 75);
        fieldsPanel.setLayout(null);  // Set layout to null

        methodsPanel = new JPanel();
        methodsPanel.setBackground(color);
        methodsPanel.setBounds(5, 115, 140, 125);
        methodsPanel.setLayout(null);  // Set layout to null

        // Creates the labels for the different panels
        className = new JLabel();
        className.setText(umlclass.getName());
        className.setHorizontalAlignment(JLabel.CENTER); //LEFT, CENTER, RIGHT
        className.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        className.setForeground(Color.BLACK);
        className.setBounds(0, 2, 140, 25);  // Set bounds for the class name label

        // Creates the labels for the different panels
        fields = new JLabel();
        fields.setHorizontalAlignment(JLabel.LEFT); //LEFT, CENTER, RIGHT
        fields.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        fields.setForeground(Color.BLACK);
        // fields.setBounds(0, 0, 140, 25);  // Set bounds for the fields label

        // Creates the labels for the different panels
        methods = new JLabel();
        methods.setHorizontalAlignment(JLabel.LEFT); //LEFT, CENTER, RIGHT
        methods.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        methods.setForeground(Color.BLACK);
        // methods.setBounds(0, 0, 140, 25);  // Set bounds for the methods label

        // Add labels
        classPanel.add(className);
        fieldsPanel.add(fields);
        methodsPanel.add(methods);

        /* Here are the different layers in order for a JLayeredPane:
                JLayeredPane.DEFAULT_LAYER
                JLayeredPane.PALETTE_LAYER
                JLayeredPane.MODAL_LAYER
                JLayeredPane.POPUP_LAYER
                JLayeredPane.DRAG_LAYER 
        */

        // Create a JLayeredPane
        background = new JLayeredPane();
        background.setBounds(0, 0, 150, 250);

        // This is here just to see temporary border of JLayeredPane
        // background.setBackground(Color.WHITE);
        background.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5)); // Red border with thickness of 5
        background.setOpaque(true); // Make it visible

        // Add all panels on top of it including bgPanel
        //background.add(bgPanel, JLayeredPane.DEFAULT_LAYER);
        background.add(classPanel, JLayeredPane.PALETTE_LAYER);
        background.add(fieldsPanel, JLayeredPane.PALETTE_LAYER);
        background.add(methodsPanel, JLayeredPane.PALETTE_LAYER);

        update();
    }

    public JLayeredPane getJLayeredPane()
    {
        return background;
    }

    public void update ()
    {
        updateClassName();
        updateFields();
        updateMethods();

        // Calculate new total height
        int newHeight = classPanel.getHeight() + fieldsPanel.getHeight() + methodsPanel.getHeight() + 20;
        int newWidth = Math.max(classPanel.getWidth(), Math.max(fieldsPanel.getWidth(), methodsPanel.getWidth())) + 10;
        
        background.setBounds(0, 0, newWidth, newHeight);
        className.setSize(newWidth - 10, classPanel.getHeight());

        classPanel.setSize(newWidth - 10, classPanel.getHeight());
        fieldsPanel.setSize(newWidth - 10, fieldsPanel.getHeight());
        methodsPanel.setSize(newWidth - 10, methodsPanel.getHeight());

        background.revalidate();
        background.repaint();
    }

    public void updateClassName()
    {
        String text = umlclass.getName();
        className.setText(text);
        className.setBounds(0, 2, 140, 25);
        classPanel.setBounds(5, 5, 140 + text.length(), 25); // Resize panel

    }

    public void updateFields ()
    {
        int newHeight = fieldsPanel.getHeight();
        int maxLength = 0;
        if (!umlclass.getFields().isEmpty())
        {
            String text = "<html>";
            for (Field field : umlclass.getFields())
            {
                maxLength = Math.max(maxLength, field.getName().length());
                text += field.getName() + "<br/>";
            }
            text = text.substring(0, text.length() - 5); // trim off the extra \n
            fields.setText(text + "</html>");
            newHeight = 20 * umlclass.getFields().size(); // Calculate height dynamically
        }
        fields.setBounds(10, 5, maxLength * PIXELSPERCHARACTER, newHeight);
        fieldsPanel.setBounds(5, 35, maxLength * PIXELSPERCHARACTER, newHeight); // Resize panel
    }

    public void updateMethods ()
    {
        int newHeight = methodsPanel.getHeight();
        int maxLength = 0;
        if (!umlclass.getMethods().isEmpty())
        {
            String text = "<html>";
            for (Method method : umlclass.getMethods())
            {
                maxLength = Math.max(maxLength, (method.getName() + method.toString()).length());
                text += method + "<br/>";
            }
            text = text.substring(0, text.length() - 5); // trim off the extra \n
            methods.setText(text + "</html>");
            newHeight = 20 * umlclass.getFields().size(); // Calculate height dynamically
        }
        methods.setBounds(10, 5, maxLength * PIXELSPERCHARACTER, newHeight);
        methodsPanel.setBounds(5, 40 + fieldsPanel.getHeight(), maxLength * PIXELSPERCHARACTER, newHeight); // Resize panel
    }
}
