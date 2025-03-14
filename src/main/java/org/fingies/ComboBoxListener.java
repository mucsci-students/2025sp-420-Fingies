package org.fingies;

import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

public abstract class ComboBoxListener implements ItemListener{
    public JComboBox[] boxesToUpdate;

    public ComboBoxListener(JComboBox[] box)
    {
        boxesToUpdate = box;
    }
}
