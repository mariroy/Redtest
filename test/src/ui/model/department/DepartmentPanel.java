package ui.model.department;

import data.model.Department;
import ui.model.BasePanel;

import javax.swing.*;
import java.awt.*;

public class DepartmentPanel extends BasePanel {
    private final static String TITLE = "Отделы";
    private DepartmentTableModel departmentTableModel;
    private JTable departmentTable;

    public DepartmentPanel() throws HeadlessException {
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
        departmentTableModel = new DepartmentTableModel();
        departmentTable = new JTable(departmentTableModel);
        departmentTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPopupMenu popupMenu = createPopupMenu();
        departmentTable.setComponentPopupMenu(popupMenu);
        JScrollPane jScrollPane = new JScrollPane(departmentTable);
        this.add(jScrollPane, BorderLayout.NORTH);
    }

    @Override
    protected void delete() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow != -1) {
            int result = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить запись?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                departmentTableModel.delete(selectedRow);
            }
        }
    }

    @Override
    protected void update() {
        int selectedRow = departmentTable.getSelectedRow();
        if (selectedRow != -1) {
            Department department = departmentTableModel.getElementAt(selectedRow);
            DepartmentDialog dialog = new DepartmentDialog(getParentFrame());
            dialog.setDepartmentID(department.getId());
            dialog.setHeadId(department.getHeadId());
            dialog.setEmail(department.getEmail());
            dialog.setPhone(department.getPhone());
            dialog.setDepartmentName(department.getDepartmentName());
            dialog.setNew(false);
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                department.setHeadId(dialog.getHeadId());
                department.setEmail(dialog.getEmail());
                department.setPhone(dialog.getPhone());
                department.setDepartmentName(dialog.getDepartmentName());
                departmentTableModel.update(selectedRow, department);
            }
        }
    }


    @Override
    protected void add() {
        DepartmentDialog departmentDialog = new DepartmentDialog(getParentFrame());
        departmentDialog.setNew(true);
        departmentDialog.setVisible(true);
        if (departmentDialog.isSucceeded()) {
            Department department = new Department();
            department.setHeadId(departmentDialog.getHeadId());
            department.setEmail(departmentDialog.getEmail());
            department.setPhone(departmentDialog.getPhone());
            department.setDepartmentName(departmentDialog.getDepartmentName());
            departmentTableModel.insert(department);
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
