package com.agencevoyage.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel = new JPanel();
    private JButton modifierButton = new JButton("Modifier");
    private JButton supprimerButton = new JButton("Supprimer");
    private String currentId;
    private ClientList parent;

    public ButtonEditor(JCheckBox checkBox, ClientList parent) {
        this.parent = parent;
        panel.setLayout(new FlowLayout());
        panel.add(modifierButton);
        panel.add(supprimerButton);

        modifierButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.modifierClient(currentId);
            }
        });

        supprimerButton.setText("Archiver");
        supprimerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.archiverClient(currentId);
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