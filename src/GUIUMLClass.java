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
import javax.swing.SwingConstants;

public class GUIUMLClass {
    private final int PIXELS_PER_CHARACTER = 8;
    private final int DEFAULT_CLASS_PANEL_HEIGHT = 25;
    private final int DEFAULT_FIELD_PANEL_HEIGHT = 75;
    private final int DEFAULT_METHOD_PANEL_HEIGHT = 125;

    private JPanel classPanel;
    private JPanel fieldsPanel;
    private JPanel methodsPanel;
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
        updateFields();
        updateMethods();

        // Calculate new total height
        int newHeight = classPanel.getHeight() + fieldsPanel.getHeight() + methodsPanel.getHeight() + 20;
        int newWidth = Math.max(classPanel.getWidth(), Math.max(fieldsPanel.getWidth(), methodsPanel.getWidth()));
        
        background.setBounds(0, 0, newWidth, newHeight);
        
        // Must be called down here because it relies on new size of background
        updateClassName();

        classPanel.setSize(newWidth - 10, classPanel.getHeight());
        fieldsPanel.setSize(newWidth - 10, fieldsPanel.getHeight());
        methodsPanel.setSize(newWidth - 10, methodsPanel.getHeight());

        background.revalidate();
        background.repaint();
    }

    public void updateClassName()
    {
        classPanel.removeAll(); // Clear panel before updating

        JLabel classLabel = new JLabel(umlclass.getName());
        int labelWidth = umlclass.getName().length() * PIXELS_PER_CHARACTER;
        classLabel.setForeground(Color.BLACK);
        classLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Red border with thickness of 5
        classLabel.setHorizontalAlignment(SwingConstants.CENTER);

        classLabel.setBounds((background.getWidth() - labelWidth) / 2 - 4, 2, labelWidth, DEFAULT_CLASS_PANEL_HEIGHT);  // Set bounds for the class name label
        

        JTextField classEditor = new JTextField(umlclass.getName());
        classEditor.setBounds(classLabel.getBounds());
        classEditor.setBackground(Color.WHITE);
        classEditor.setVisible(false);

        classLabel.addMouseListener(new JLabelDoubleClickListener (classEditor));
        classEditor.addFocusListener(new JTextFieldFocusLossListener (classLabel, Action.RENAME_CLASS));

        classPanel.add(classLabel);
        classPanel.add(classEditor);

        int panelWidth = Math.max(140, labelWidth + 20); // Ensure minimum width
        classPanel.setBounds(5, 5, panelWidth, DEFAULT_CLASS_PANEL_HEIGHT); // Resize panel
        // classPanel.setBounds(5, 5, 140 + classLabel.getText().length() * PIXELS_PER_CHARACTER - 15, DEFAULT_CLASS_PANEL_HEIGHT); // Resize panel
        classPanel.revalidate();
        classPanel.repaint();
    }

    public void updateFields ()
    {
        fieldsPanel.removeAll(); // Clear panel before updating
        int newHeight = fieldsPanel.getHeight();
        int maxLength = 0;
        int offset = 0;
        // JLabel txt;

        if (!umlclass.getFields().isEmpty())
        {
            // String text = "<html>";
            for (Field field : umlclass.getFields())
            {
                maxLength = Math.max(maxLength, field.getName().length());
                // text += field.getName() + "<br/>";

                JLabel fieldLabel = new JLabel(field.getName());
                fieldLabel.setHorizontalAlignment(JLabel.LEFT);
                fieldLabel.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
                fieldLabel.setForeground(Color.BLACK);
                fieldLabel.setBounds(10, 5 + offset * 20, field.getName().length() * PIXELS_PER_CHARACTER, 25);

                JTextField fieldEditor = new JTextField(field.getName());
                fieldEditor.setBounds(fieldLabel.getBounds());
                fieldEditor.setBackground(Color.WHITE);
                fieldEditor.setVisible(false);

                fieldLabel.addMouseListener(new JLabelDoubleClickListener(fieldEditor));
                fieldEditor.addFocusListener(new JTextFieldFocusLossListener(fieldLabel, Action.RENAME_FIELD));

                fieldsPanel.add(fieldLabel);
                fieldsPanel.add(fieldEditor);

                offset++;
                
            }
            // text = text.substring(0, text.length() - 5); // trim off the extra \n
            // fields.setText(text + "</html>");
            newHeight = 25 * umlclass.getFields().size(); // Calculate height dynamically
        }
        // fields.setBounds(10, 5, maxLength * PIXELS_PER_CHARACTER, newHeight);
        fieldsPanel.setBounds(5, 35, maxLength * PIXELS_PER_CHARACTER  - 15, Math.max(DEFAULT_FIELD_PANEL_HEIGHT, newHeight - 20)); // Resize panel
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }

    public void updateMethods ()
    {
        methodsPanel.removeAll(); // Clear panel before updating
        int newHeight = methodsPanel.getHeight();
        int maxLength = 0;
        int offset = 0;
        // JLabel txt;

        if (!umlclass.getMethods().isEmpty())
        {
            // String text = "<html>";
            for (Method method : umlclass.getMethods())
            {
                maxLength = Math.max(maxLength, method.toString().length());
                // maxLength = Math.max(maxLength, (method.getName().length() + method.toString().length()));
                // text += method + "<br/>";

                // methodsPanel.add(txt);
                JLabel methodLabel = new JLabel(method.toString());
                methodLabel.setHorizontalAlignment(JLabel.LEFT);
                methodLabel.setForeground(Color.BLACK);
                methodLabel.setBounds(10, 5 + offset * 20, method.toString().length() * PIXELS_PER_CHARACTER, 25);

                JTextField methodEditor = new JTextField(method.toString());
                methodEditor.setBounds(methodLabel.getBounds());
                methodEditor.setBackground(Color.WHITE);
                methodEditor.setVisible(false);

                methodLabel.addMouseListener(new JLabelDoubleClickListener(methodEditor));
                methodEditor.addFocusListener(new JTextFieldFocusLossListener(methodLabel, Action.RENAME_METHOD));

                methodsPanel.add(methodLabel);
                methodsPanel.add(methodEditor);

                offset++;
            }
            // text = text.substring(0, text.length() - 5); // trim off the extra \n
            // methods.setText(text + "</html>");
            newHeight = 25 * umlclass.getMethods().size(); // Calculate height dynamically
        }
        // methods.setBounds(10, 5, maxLength * PIXELS_PER_CHARACTER, newHeight);
        methodsPanel.setBounds(5, 40 + fieldsPanel.getHeight(), maxLength * PIXELS_PER_CHARACTER - 15, Math.max(DEFAULT_METHOD_PANEL_HEIGHT, newHeight - 20)); // Resize panel
        methodsPanel.revalidate();
        methodsPanel.repaint();
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
			JTextField src = (JTextField) e.getSource();
            src.setText(label.getText()); // Ensure the text field starts with the label’s text
		}

		@Override
		public void focusLost(FocusEvent e) {
            JTextField src = (JTextField) e.getSource();
            //controller.runHelper(action, new String[] {src.getText()});
			// TODO: decide whether to actually switch back to a JLabel or not based on whether runHelper() succeeds
            label.setText(src.getText()); // Update the label with the new text
            src.setVisible(false);
            label.setVisible(true);
			
		}

		// @Override
		// public void focusGained(FocusEvent e) {
		// 	JTextField src = ((JTextField)e.getSource());
		// 	classNameRenamer.setText(className.getText());
		// }

		// @Override
		// public void focusLost(FocusEvent e) {
		// 	JTextField src = ((JTextField)e.getSource());
		// 	//controller.runHelper(action, new String[] {src.getText()});
		// 	// TODO: decide whether to actually switch back to a JLabel or not based on whether runHelper() succeeds
		// 	src.setVisible(false);
        // 	label.setVisible(true);
        // 	label.setText(classNameRenamer.getText());
		// }
    }
}