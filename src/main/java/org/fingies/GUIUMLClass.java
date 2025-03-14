package org.fingies;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GUIUMLClass {
    private final int PIXELS_PER_CHARACTER = 9;
    private final int DEFAULT_CLASS_PANEL_HEIGHT = 25;
    private final int DEFAULT_FIELD_PANEL_HEIGHT = 75;
    private final int DEFAULT_METHOD_PANEL_HEIGHT = 125;
    private final int DEFAULT_PANEL_WIDTH = 140;

    private JPanel classPanel;
    private JPanel fieldsPanel;
    private JPanel methodsPanel;
    private JLayeredPane background;
    private Color color;

    private UMLClass umlclass; // TODO: consider having GUIUMLClass interact with the controller, rather than a UMLClass, for better MVC

    public GUIUMLClass(UMLClass umlclass, Controller controller, GUIView guiView)
        
    {
        this.umlclass = umlclass;

        // Creates a random color for the class
        color = new Color((int)(Math.random() * 225 + 15), (int)(Math.random() * 225 + 15), (int)(Math.random() * 225 + 15), 100);

        classPanel = new JPanel();
        // classPanel.setBackground(Color.RED);
        // classPanel.setBackground(new Color(255, 0, 0, 60));
        classPanel.setBackground(color);
        classPanel.setBounds(5, 5, DEFAULT_PANEL_WIDTH, DEFAULT_CLASS_PANEL_HEIGHT);
        classPanel.setLayout(null);  // Set layout to null

        fieldsPanel = new JPanel();
        // fieldsPanel.setBackground(Color.GREEN);
        fieldsPanel.setBackground(color);
        fieldsPanel.setBounds(5, 35, DEFAULT_PANEL_WIDTH, DEFAULT_FIELD_PANEL_HEIGHT);
        fieldsPanel.setLayout(null);  // Set layout to null

        methodsPanel = new JPanel();
        methodsPanel.setBackground(color);
        methodsPanel.setBounds(5, 115, DEFAULT_PANEL_WIDTH, DEFAULT_METHOD_PANEL_HEIGHT);
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
        background.setBorder(BorderFactory.createLineBorder(Color.BLACK, 5)); // Black border with thickness of 5
        background.setOpaque(true); // Make it visible

        // Add all panels on top of it including bgPanel
        //background.add(bgPanel, JLayeredPane.DEFAULT_LAYER);
        background.add(classPanel, JLayeredPane.PALETTE_LAYER);
        background.add(fieldsPanel, JLayeredPane.PALETTE_LAYER);
        background.add(methodsPanel, JLayeredPane.PALETTE_LAYER);
        
     // Creates new listener for the newly added JLayeredPane
        DragListener dragListener = new DragListener(background, guiView);
        background.addMouseListener(dragListener);
        background.addMouseMotionListener(dragListener);

        initializePosition(background, guiView.getWidth(), guiView.getHeight());

        update();
    }

    public UMLClass getUMLClass()
    {
        return umlclass;
    }

    public JLayeredPane getJLayeredPane()
    {
        return background;
    }

    public void initializePosition (JLayeredPane pane, int maxWidth, int maxHeight)
    {
        Position pos = umlclass.getPosition();
        if (pos.getX() < 0 || pos.getY() < 75)
        {
        	// the position is invalid, so randomize it
        	int randX = (int)(Math.random() * (maxWidth - pane.getWidth()));
            int randY = (int)(Math.random() * (maxHeight - pane.getHeight() - 75)) + 75;
            pos = new Position(randX, randY);
            umlclass.setPosition(randX, randY);
        }
        pane.setBounds(pos.getX(), pos.getY(), pane.getWidth(), pane.getHeight());
    }

    public void update ()
    {
        updateFields();
        updateMethods();

        // Calculate new total height
        int newHeight = classPanel.getHeight() + fieldsPanel.getHeight() + methodsPanel.getHeight() + 20;
        int newWidth = Math.max(DEFAULT_PANEL_WIDTH, Math.max(fieldsPanel.getWidth(), methodsPanel.getWidth()));

        //System.out.println("Update classPanel width is " + classPanel.getWidth());
        //System.out.println("Update fieldsPanel width is " + fieldsPanel.getWidth());
        //System.out.println("Update methodsPanel width is " + methodsPanel.getWidth());
        //System.out.println("Update new width is " + newWidth + "\n");
        
        background.setBounds(background.getX(), background.getY(), newWidth + 10, newHeight);
        
        // Must be called down here because it relies on new size of background
        updateClassName();

        classPanel.setSize(newWidth, classPanel.getHeight());
        fieldsPanel.setSize(newWidth, fieldsPanel.getHeight());
        methodsPanel.setSize(newWidth, methodsPanel.getHeight());

        // background.revalidate();
        background.repaint();
    }

    public void updateClassName()
    {
        classPanel.removeAll(); // Clear panel before updating

        JLabel classLabel = new JLabel(umlclass.getName());
        int labelWidth = umlclass.getName().length() * PIXELS_PER_CHARACTER;
        classLabel.setForeground(Color.BLACK);
        classLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // classLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // Red border with thickness of 2

        classLabel.setBounds((background.getWidth() - labelWidth) / 2 - 5, 2, labelWidth, DEFAULT_CLASS_PANEL_HEIGHT);  // Set bounds for the class name label

        // JTextField classEditor = new JTextField(umlclass.getName());
        // classEditor.setBounds(classLabel.getBounds());
        // classEditor.setBackground(Color.WHITE);
        // classEditor.setVisible(false);
        
        // var labelListener = new JLabelDoubleClickListener (classEditor, background);
        // classLabel.addMouseListener(labelListener);
        // classLabel.addMouseMotionListener(labelListener);
        // classEditor.addFocusListener(new JTextFieldFocusLossListener (classLabel, Action.RENAME_CLASS));

        classPanel.add(classLabel);
        // classPanel.add(classEditor);

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

        if (!umlclass.getFields().isEmpty())
        {
            // String text = "<html>";
            for (Field field : umlclass.getFields())
            {
                maxLength = Math.max(maxLength, field.getName().length());

                JLabel fieldLabel = new JLabel(field.toString());
                fieldLabel.setHorizontalAlignment(JLabel.LEFT);
                fieldLabel.setVerticalAlignment(JLabel.TOP); // TOP, CENTER, BOTTOM
                fieldLabel.setForeground(Color.BLACK);

                int labelWidth = field.toString().length() * PIXELS_PER_CHARACTER; // Approximate width based on max line length
                int labelHeight = 25; // Adjust height based on number of lines

                fieldLabel.setBounds(PIXELS_PER_CHARACTER, 5 + offset * 20, labelWidth, labelHeight);

                fieldsPanel.add(fieldLabel);

                offset++;

                // JTextField fieldEditor = new JTextField(field.getName());
                // fieldEditor.setBounds(fieldLabel.getBounds());
                // fieldEditor.setBackground(Color.WHITE);
                // fieldEditor.setVisible(false);

                // var labelListener = new JLabelDoubleClickListener (fieldEditor, background);
                // fieldLabel.addMouseListener(labelListener);
                // fieldLabel.addMouseMotionListener(labelListener);
                // fieldEditor.addFocusListener(new JTextFieldFocusLossListener(fieldLabel, Action.RENAME_FIELD));
            }
            newHeight = 25 * umlclass.getFields().size(); // Calculate height dynamically
        }
        // Resize the methodsPanel dynamically
        int panelWidth = (maxLength - 2) * PIXELS_PER_CHARACTER;
        int panelHeight = Math.max(DEFAULT_FIELD_PANEL_HEIGHT, newHeight - 20);

        fieldsPanel.setBounds(5, 35, panelWidth, panelHeight); // Resize panel
        fieldsPanel.revalidate();
        fieldsPanel.repaint();
    }

    public void updateMethods ()
    {
        methodsPanel.removeAll(); // Clear panel before updating
        int newHeight = methodsPanel.getHeight();
        int maxLength = 0;
        int maxLineLength = 40;
        int offset = 0;
        int lineHeight = 20; // Approximate line height for each wrapped line

        if (!umlclass.getMethods().isEmpty())
        {
            for (Method method : umlclass.getMethods())
            {  
                String methodString = method.toString();
                StringBuilder formattedText = new StringBuilder("<html>");

                maxLength = Math.max(maxLength, methodString.length());

                // Insert line breaks every maxLineLength characters
                int lineCount = 0;
                for (int i = 0; i < methodString.length(); i += maxLineLength) {
                    int endIndex = Math.min(i + maxLineLength, methodString.length());
                    formattedText.append(methodString, i, endIndex).append("<br>");
                    lineCount++;
                }
                
                formattedText.append("</html>");

                JLabel methodLabel = new JLabel(formattedText.toString());
                methodLabel.setHorizontalAlignment(JLabel.LEFT);
                methodLabel.setForeground(Color.BLACK);

                int labelWidth = Math.min (methodString.length(), maxLineLength) * PIXELS_PER_CHARACTER; // Approximate width based on max line length
                int labelHeight = lineCount * lineHeight; // Adjust height based on number of lines

                methodLabel.setBounds(PIXELS_PER_CHARACTER, 5 + offset * 20, labelWidth, labelHeight);

                methodsPanel.add(methodLabel);

                offset += lineCount; // Increase offset by number of lines to avoid overlap
                
                // methodLabel.setBounds(PIXELS_PER_CHARACTER, 5 + offset * 20, formattedText.toString().length() * PIXELS_PER_CHARACTER, 25);
                // methodLabel.setBounds(PIXELS_PER_CHARACTER, 5 + offset * 20, method.toString().length() * PIXELS_PER_CHARACTER, 25);

                // JTextField methodEditor = new JTextField(method.toString());
                // methodEditor.setBounds(methodLabel.getBounds());
                // methodEditor.setBackground(Color.WHITE);
                // methodEditor.setVisible(false);

                // var labelListener = new JLabelDoubleClickListener (methodEditor, background);
                // methodLabel.addMouseListener(labelListener);
                // methodLabel.addMouseMotionListener(labelListener);
                // methodEditor.addFocusListener(new JTextFieldFocusLossListener(methodLabel, Action.RENAME_METHOD));

                // methodsPanel.add(methodEditor);
            }
            newHeight = offset * lineHeight; // Calculate dynamic height based on total lines
        }
        // Resize the methodsPanel dynamically
        int panelWidth = Math.min(maxLineLength, maxLength) * PIXELS_PER_CHARACTER - 30;
        int panelHeight = Math.max(DEFAULT_METHOD_PANEL_HEIGHT, newHeight - 20);
        
        methodsPanel.setBounds(5, 40 + fieldsPanel.getHeight(), panelWidth, panelHeight);
        methodsPanel.revalidate();
        methodsPanel.repaint();
    }
    
    
    /**
     * A listener that turns the JLabel invisible and turns a JTextField visible when the label is double clicked
     */
    class JLabelDoubleClickListener extends MouseAdapter 
    {
    	JTextField field;
    	JLayeredPane parentPane;
    	
    	public JLabelDoubleClickListener (JTextField field, JLayeredPane parentPane)
    	{
    		this.field = field;
    		this.parentPane = parentPane;
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
    	
    	@Override
        public void mousePressed(MouseEvent e) {
    		e.setSource(parentPane);
    		parentPane.dispatchEvent(e);
        }

        @Override
        public void mouseDragged(MouseEvent e) {
        	e.setSource(parentPane);
    		parentPane.dispatchEvent(e);
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
            //controller.runHelper(action, new String[] {label.getText(), src.getText()});
            label.setText(src.getText()); // Update the label with the new text
            src.setVisible(false);
            label.setVisible(true);
            update();
			// background.revalidate();
            // background.repaint();
		}
    }
    
 // Drag listener for JLayeredPane
    class DragListener extends MouseAdapter {
        private final JComponent component;
        private final GUIView parentView;
        private Point initialClick;

        public DragListener(JComponent component, GUIView parentView) {
            this.component = component;
            this.parentView = parentView;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            initialClick = e.getPoint(); // Store initial click position
            ((JComponent)e.getSource()).requestFocusInWindow();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (initialClick == null) return;

            // Brings current frame being dragged to the front
            parentView.getContentPane().setComponentZOrder(component, JLayeredPane.DEFAULT_LAYER); // Bring to front
            parentView.getContentPane().revalidate();
            parentView.getContentPane().repaint();

            // Get current location of the JLayeredPane
            int x = component.getX() + e.getX() - initialClick.x;
            int y = component.getY() + e.getY() - initialClick.y;

            // Get parent frame size
            int frameWidth = parentView.getWidth();
            int frameHeight = parentView.getHeight();

            // Prevent dragging off the screen (Constrain within parent frame)
            int maxX = frameWidth - component.getWidth();
            int maxY = frameHeight - component.getHeight();

            // Constrain X and Y to stay within the frame bounds
            if (x < 0) x = 0;
            if (x > maxX) x = maxX;
            if (y < 75) y = 75;
            if (y > maxY) y = maxY;

            // Move JLayeredPane to new position within bounds
            component.setLocation(x, y);

            // Update the arrows after moving the class
            parentView.updateArrows();  // This will update the arrows to reflect new positions
        }
        
        @Override
        public void mouseReleased(MouseEvent e)
        {
        	Rectangle r = background.getBounds();
            umlclass.setPosition(r.x, r.y);
        }
    }
    
}