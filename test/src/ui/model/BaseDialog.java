package ui.model;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDialog extends JDialog {
    protected final Frame parent;
    protected boolean succeeded;

    public BaseDialog(Frame parent, String title) {
        super(parent, title, true);
        this.parent = parent;
        beforeSettings();
        addComponents();
        setSettings();
    }

    protected void beforeSettings() {

    }

    protected void setSettings() {
        pack();
        this.setResizable(false);
        this.setLocationRelativeTo(parent);
    }

    protected void addComponents() {
        addPanel();
        addButtons();
    }

    protected abstract void addPanel();

    protected void addButtons() {
        JButton saveButton = new JButton("Сохранить");
        saveButton.addActionListener(e -> save());
        JButton cancelButton = new JButton("Отменить");
        cancelButton.addActionListener(e -> cancel());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        buttonPanel.add(cancelButton);

        this.add(buttonPanel, BorderLayout.PAGE_END);
    }

    protected abstract void save();

    protected void cancel() {
        succeeded = false;
        dispose();
    }

    public boolean isSucceeded() {
        return succeeded;
    }
}
