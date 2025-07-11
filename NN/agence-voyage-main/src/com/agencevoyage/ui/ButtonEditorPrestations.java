package com.agencevoyage.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditorPrestations extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel = new JPanel();
    private JButton modifierButton = new JButton("Modifier");
    private JButton supprimerButton = new JButton("Supprimer");
    private String currentId;
    private PrestationList parent;

    public ButtonEditorPrestations(JCheckBox checkBox, PrestationList parent) {
        this.parent = parent;
        panel.setLayout(new FlowLayout());
        panel.add(modifierButton);
        panel.add(supprimerButton);

        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.modifierPrestation(currentId);
            }
        });

        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.supprimerPrestation(currentId);
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentId = (String) table.getValueAt(row, 0);
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}