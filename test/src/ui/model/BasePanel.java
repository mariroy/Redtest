package ui.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class BasePanel extends JPanel {

    public BasePanel() {
        super();
    }

    protected Frame getParentFrame() {
        return (Frame) SwingUtilities.getWindowAncestor(this);
    }

    public abstract String getTitle();

    protected JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Удалить");

        deleteItem.addActionListener(e -> delete());
        popupMenu.add(deleteItem);

        JMenuItem updateItem = new JMenuItem("Изменить");
        updateItem.addActionListener(e -> update());
        popupMenu.add(updateItem);

        return popupMenu;
    }

    protected abstract void delete();

    protected abstract void update();

    protected JButton createAddButton() {
        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(e -> add());
        return addButton;
    }



    protected abstract void add();
}
