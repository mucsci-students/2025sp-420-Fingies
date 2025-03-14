package org.fingies;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

public abstract class ComboBoxListener implements ItemListener{
    public JComboBox[] boxesToUpdate;

    public ComboBoxListener(JComboBox[] box)
    {
        boxesToUpdate = box;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Update all the combo boxes when the state changes
        for (JComboBox box : boxesToUpdate) {
            updateComboBox(box);
        }
    }

    protected abstract void updateComboBox(JComboBox box);
}
