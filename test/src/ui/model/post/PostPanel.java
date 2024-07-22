package ui.model.post;

import data.model.Post;
import ui.model.BasePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PostPanel extends BasePanel {
    private final static String TITLE = "Должности";
    private PostTableModel postTableModel;
    private JTable postTable;

    public PostPanel() throws HeadlessException {
        super();
        setSettings();
        addComponents();
    }


    public String getTitle() {
        return TITLE;
    }

    private void setSettings() {
        this.setVisible(true);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    private void addComponents() {
        addTable();
        this.add(Box.createRigidArea(new Dimension(0, 5)));
        addButtons();
        this.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void addTable() {
        postTableModel = new PostTableModel();
        postTable = new JTable(postTableModel);
        postTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        postTable.setComponentPopupMenu(createPopupMenu());
        JScrollPane jScrollPane = new JScrollPane(postTable);
        this.add(jScrollPane, BorderLayout.NORTH);
    }

    @Override
    protected void delete() {
        int selectedRow = postTable.getSelectedRow();
        if (selectedRow != -1) {
            int result = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить запись?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                postTableModel.delete(selectedRow);
            }
        }
    }

    @Override
    protected void update() {
        int selectedRow = postTable.getSelectedRow();
        if (selectedRow != -1) {
            Post post = postTableModel.getElementAt(selectedRow);
            PostDialog dialog = new PostDialog(getParentFrame());
            dialog.setPositionName(post.getPositionName());
            dialog.setSalary(post.getSalary());
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                post.setPositionName(dialog.getPositionName());
                post.setSalary(dialog.getSalary());
                postTableModel.update(selectedRow, post);
            }
        }
    }

    @Override
    protected void add() {
         PostDialog postDialog = new PostDialog(getParentFrame());
            postDialog.setVisible(true);
            if (postDialog.isSucceeded()) {
                Post post = new Post();
                post.setPositionName(postDialog.getPositionName());
                post.setSalary(postDialog.getSalary());
                postTableModel.insert(post);
            }
    }


    private void addButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton addButton = createAddButton();
        buttonPanel.add(addButton);
        this.add(buttonPanel);
    }

}

