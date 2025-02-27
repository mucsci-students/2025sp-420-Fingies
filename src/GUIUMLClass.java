import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;

public class GUIUMLClass {
    String name;
    private JPanel bgPanel;
    private JPanel classPanel;
    private JPanel fieldsPanel;
    private JPanel methodsPanel;
    private JLabel className;
    private JLabel fields;
    private JLabel methods;
    private JLayeredPane background;
    private Color color;

    public GUIUMLClass(String name)
    {
        // Sets the name of the class
        this.name = name;

        // Creates a random color for the class
        color = new Color((int)(Math.random() * 240 + 15), (int)(Math.random() * 240 + 15), (int)(Math.random() * 240 + 15), 100);

        // Creates a bunch of different panels
        // bgPanel = new JPanel();
        // bgPanel.setBackground(Color.BLACK);
        // bgPanel.setBounds(0, 0, 150, 250);

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
        className.setText(name);
        className.setHorizontalAlignment(JLabel.CENTER); //LEFT, CENTER, RIGHT
        className.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        className.setForeground(Color.BLACK);
        className.setBounds(0, 0, 140, 25);  // Set bounds for the class name label

        // Creates the labels for the different panels
        fields = new JLabel();
        fields.setText("Fields:");
        fields.setHorizontalAlignment(JLabel.CENTER); //LEFT, CENTER, RIGHT
        fields.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        fields.setForeground(Color.BLACK);
        fields.setBounds(0, 0, 140, 25);  // Set bounds for the fields label

        // Creates the labels for the different panels
        methods = new JLabel();
        methods.setText("Methods:");
        methods.setHorizontalAlignment(JLabel.CENTER); //LEFT, CENTER, RIGHT
        methods.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        methods.setForeground(Color.BLACK);
        methods.setBounds(0, 0, 140, 25);  // Set bounds for the methods label

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
    }

    public String getName()
    {
        return name;
    }

    public JLayeredPane getJLayeredPane()
    {
        return background;
    }

    public void renameClass (String name)
    {
        this.name = name;
        className.setText(name);
    }

}
