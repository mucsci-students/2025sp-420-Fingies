package org.fingies;

import javax.swing.*;
import java.awt.*;

public class WrappingComboBoxRenderer extends JPanel implements ListCellRenderer<String> {
    private final JTextArea textArea;

    public WrappingComboBoxRenderer() {
        setLayout(new BorderLayout());  
        textArea = new JTextArea();
        textArea.setWrapStyleWord(true);  
        textArea.setLineWrap(true);       
        textArea.setOpaque(true);         
        textArea.setEditable(false);      
        textArea.setBorder(null);         

        add(textArea, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(
        JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {

        textArea.setText(value);  

        // Apply colors when selected
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            textArea.setBackground(list.getSelectionBackground());
            textArea.setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            textArea.setBackground(list.getBackground());
            textArea.setForeground(list.getForeground());
        }

        return this;
    }
}
