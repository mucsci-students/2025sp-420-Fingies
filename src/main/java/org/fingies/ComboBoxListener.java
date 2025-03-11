package org.fingies;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

public abstract class ComboBoxListener implements ItemListener{
    public JComboBox boxToUpdate;

    public ComboBoxListener(JComboBox box)
    {
        boxToUpdate = box;
    }
}
