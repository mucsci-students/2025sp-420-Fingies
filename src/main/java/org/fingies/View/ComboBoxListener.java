package org.fingies.View;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;

public abstract class ComboBoxListener implements ItemListener{
    public JComboBox<String>[] boxesToUpdate;

    public ComboBoxListener(JComboBox<String>[] box)
    {
        boxesToUpdate = box;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Update all the combo boxes when the state changes
        for (JComboBox<String> box : boxesToUpdate) {
            updateComboBox(box);
        }
    }

    protected abstract void updateComboBox(JComboBox<String> box);
}
