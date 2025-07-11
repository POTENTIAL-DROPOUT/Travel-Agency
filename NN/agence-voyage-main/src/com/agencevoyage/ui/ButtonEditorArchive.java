package com.agencevoyage.ui;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.agencevoyage.service.DatabaseManager;

public class ButtonEditorArchive extends AbstractCellEditor implements TableCellEditor {
    private JPanel panel = new JPanel();
    private JButton restaurerButton = new JButton("Restaurer");
    private String currentId;
    private ArchivedClientList parent;

    public ButtonEditorArchive(ArchivedClientList parent) {
        this.parent = parent;
        panel.setLayout(new FlowLayout());
        panel.add(restaurerButton);

        restaurerButton.addActionListener((ActionEvent e) -> {
            try (Connection conn = DatabaseManager.getConnection()) {
                PreparedStatement stmt = conn.prepareStatement("UPDATE clients SET archive = false WHERE id = ?");
                stmt.setString(1, currentId);
                stmt.executeUpdate();
                parent.rafraichir(); // Reload the list
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        currentId = (String) table.getValueAt(row, 0); // assuming ID is in the first column
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
