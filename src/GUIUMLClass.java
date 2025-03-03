import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUIUMLClass {
    private final int PIXELS_PER_CHARACTER = 8;
    private final int DEFAULT_CLASS_PANEL_HEIGHT = 25;
    private final int DEFAULT_FIELD_PANEL_HEIGHT = 75;
    private final int DEFAULT_METHOD_PANEL_HEIGHT = 125;

    private JPanel classPanel;
    private JPanel fieldsPanel;
    private JPanel methodsPanel;
    private JLabel className;
    private JTextField classNameRenamer;
    private JLabel fields;
    private JLabel methods;
    private JLayeredPane background;
    private Color color;

    private UMLClass umlclass;
    private Controller controller;

    public GUIUMLClass(UMLClass umlclass, Controller controller)
        
    {
        this.umlclass = umlclass;
        this.controller = controller;

        // Creates a random color for the class
        color = new Color((int)(Math.random() * 225 + 15), (int)(Math.random() * 225 + 15), (int)(Math.random() * 225 + 15), 100);

        classPanel = new JPanel();
        // classPanel.setBackground(Color.RED);
        // classPanel.setBackground(new Color(255, 0, 0, 60));
        classPanel.setBackground(color);
        classPanel.setBounds(5, 5, 140, DEFAULT_CLASS_PANEL_HEIGHT);
        classPanel.setLayout(null);  // Set layout to null

        fieldsPanel = new JPanel();
        // fieldsPanel.setBackground(Color.GREEN);
        fieldsPanel.setBackground(color);
        fieldsPanel.setBounds(5, 35, 140, DEFAULT_FIELD_PANEL_HEIGHT);
        fieldsPanel.setLayout(null);  // Set layout to null

        methodsPanel = new JPanel();
        methodsPanel.setBackground(color);
        methodsPanel.setBounds(5, 115, 140, DEFAULT_METHOD_PANEL_HEIGHT);
        methodsPanel.setLayout(null);  // Set layout to null

        // Creates the labels for the different panels
        className = new JLabel();
        className.setText(umlclass.getName());
        className.setHorizontalAlignment(JLabel.CENTER); //LEFT, CENTER, RIGHT
        className.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        className.setForeground(Color.BLACK);
        className.setBounds(0, 2, 140, 25);  // Set bounds for the class name label
        
        classNameRenamer = new JTextField();
        classNameRenamer.setForeground(Color.BLACK);
        classNameRenamer.setBounds(0, 0, 147, 25);  // Set bounds for the class name label
        classNameRenamer.setBackground(color.brighter().brighter().brighter().brighter().brighter().brighter().brighter().brighter()); // we like out JTextFields bright
        classNameRenamer.setVisible(false);
        classNameRenamer.setHorizontalAlignment(JTextField.CENTER);
        
        // make className & classNameRenamer replace each other when being edited
        className.addMouseListener(new JLabelDoubleClickListener (classNameRenamer));
        classNameRenamer.addFocusListener(new JTextFieldFocusLossListener (className, Action.RENAME_CLASS));
        

        // Creates the labels for the different panels
        // fields = new JLabel();
        // fields.setHorizontalAlignment(JLabel.LEFT); //LEFT, CENTER, RIGHT
        // fields.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        // fields.setForeground(Color.BLACK);
        // // fields.setBounds(0, 0, 140, 25);  // Set bounds for the fields label

        // // Creates the labels for the different panels
        // methods = new JLabel();
        // methods.setHorizontalAlignment(JLabel.LEFT); //LEFT, CENTER, RIGHT
        // methods.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        // methods.setForeground(Color.BLACK);
        // methods.setBounds(0, 0, 140, 25);  // Set bounds for the methods label

        // Add labels
        classPanel.add(className);
        classPanel.add(classNameRenamer);
        // fieldsPanel.add(fields);
        // methodsPanel.add(methods);

        // JLabel f1 = new JLabel("");
        // f1.setText("11111");
        // f1.setHorizontalAlignment(JLabel.LEFT); //LEFT, CENTER, RIGHT
        // f1.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        // f1.setForeground(Color.BLACK);
        // f1.setBounds(0, 0, 140, 25);

        // JLabel f2 = new JLabel("");
        // f2.setText("22222");
        // f2.setHorizontalAlignment(JLabel.LEFT); //LEFT, CENTER, RIGHT
        // f2.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
        // f2.setForeground(Color.BLACK);
        // f2.setBounds(0, 0, 140, 25);

        // methodsPanel.add(f1);
        // fieldsPanel.add(f2);
        

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
        int newWidth = Math.max(classPanel.getWidth(), Math.max(fieldsPanel.getWidth(), methodsPanel.getWidth()));
        
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
        int offset = 0;
        JLabel txt;

        if (!umlclass.getFields().isEmpty())
        {
            // String text = "<html>";
            for (Field field : umlclass.getFields())
            {
                maxLength = Math.max(maxLength, field.getName().length());
                // text += field.getName() + "<br/>";

                txt = new JLabel();
                txt.setText(field.getName());
                txt.setHorizontalAlignment(JLabel.LEFT); //LEFT, CENTER, RIGHT
                txt.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
                txt.setForeground(Color.BLACK);
                txt.setBounds(10, 5 + offset * 20, field.getName().length() * PIXELS_PER_CHARACTER, 25);

                fieldsPanel.add(txt);
                offset++;
                
            }
            // text = text.substring(0, text.length() - 5); // trim off the extra \n
            // fields.setText(text + "</html>");
            newHeight = 25 * umlclass.getFields().size(); // Calculate height dynamically
        }
        // fields.setBounds(10, 5, maxLength * PIXELS_PER_CHARACTER, newHeight);
        fieldsPanel.setBounds(5, 35, maxLength * PIXELS_PER_CHARACTER  - 15, Math.max(DEFAULT_FIELD_PANEL_HEIGHT, newHeight - 20)); // Resize panel
    }

    public void updateMethods ()
    {
        int newHeight = methodsPanel.getHeight();
        int maxLength = 0;
        int offset = 0;
        JLabel txt;

        if (!umlclass.getMethods().isEmpty())
        {
            // String text = "<html>";
            for (Method method : umlclass.getMethods())
            {
                maxLength = Math.max(maxLength, method.toString().length());
                // maxLength = Math.max(maxLength, (method.getName().length() + method.toString().length()));
                // text += method + "<br/>";

                txt = new JLabel();
                txt.setText(method.toString());
                txt.setHorizontalAlignment(JLabel.LEFT); //LEFT, CENTER, RIGHT
                txt.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
                txt.setForeground(Color.BLACK);
                txt.setBounds(10, 5 + offset * 20, method.toString().length() * PIXELS_PER_CHARACTER, 25);

                methodsPanel.add(txt);
                offset++;
            }
            // text = text.substring(0, text.length() - 5); // trim off the extra \n
            // methods.setText(text + "</html>");
            newHeight = 25 * umlclass.getMethods().size(); // Calculate height dynamically
        }
        // methods.setBounds(10, 5, maxLength * PIXELS_PER_CHARACTER, newHeight);
        methodsPanel.setBounds(5, 40 + fieldsPanel.getHeight(), maxLength * PIXELS_PER_CHARACTER - 15, Math.max(DEFAULT_METHOD_PANEL_HEIGHT, newHeight - 20)); // Resize panel
    }
    
    
    /**
     * A listener that turns the JLabel invisible and turns a JTextField visible when the label is double clicked
     */
    class JLabelDoubleClickListener extends MouseAdapter 
    {
    	JTextField field;
    	
    	public JLabelDoubleClickListener (JTextField field)
    	{
    		this.field = field;
    	}
    	
    	public void mouseClicked(MouseEvent me)
    	{
    		if (me.getClickCount() == 2)
    		{
    			JLabel src = ((JLabel)me.getSource());
    			src.setVisible(false);
    			field.setVisible(true);
    			field.requestFocusInWindow();
    		}
    	}
    }
    
    /**
     * A listener that turns the JTextField invisible and turns a JLabel visible when the field loses focus
     * 
     * Whenever the JTextField loses focus, it also calls runHelper() with the given action & the text in the field as the arguments
     */
    class JTextFieldFocusLossListener implements FocusListener
    {
    	JLabel label;
    	Action action;
    	
    	public JTextFieldFocusLossListener (JLabel label, Action action)
    	{
    		this.label = label;
    		this.action = action;
    	}
    	
		@Override
		public void focusGained(FocusEvent e) {
			JTextField src = ((JTextField)e.getSource());
			classNameRenamer.setText(className.getText());
		}

		@Override
		public void focusLost(FocusEvent e) {
			JTextField src = ((JTextField)e.getSource());
			//controller.runHelper(action, new String[] {src.getText()});
			// TODO: decide whether to actually switch back to a JLabel or not based on whether runHelper() succeeds
			src.setVisible(false);
        	label.setVisible(true);
        	label.setText(classNameRenamer.getText());
		}
    }
}