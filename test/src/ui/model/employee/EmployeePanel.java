package ui.model.employee;

import data.model.Employee;
import ui.model.BasePanel;

import javax.swing.*;
import java.awt.*;

public class EmployeePanel extends BasePanel {

    private final static String TITLE = "Сотрудники";
    private EmployeeTableModel employeeTableModel;
    private JTable employeeTable;

    public EmployeePanel() {
        super();
        setSettings();
        addComponents();
    }

    @Override
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
        employeeTableModel = new EmployeeTableModel();
        employeeTable = new JTable(employeeTableModel);
        employeeTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPopupMenu popupMenu = createPopupMenu();
        employeeTable.setComponentPopupMenu(popupMenu);
        JScrollPane jScrollPane = new JScrollPane(employeeTable);
        this.add(jScrollPane, BorderLayout.NORTH);
    }

    @Override
    protected void delete() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            int result = JOptionPane.showConfirmDialog(this, "Вы уверены, что хотите удалить запись?", "Подтверждение", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                employeeTableModel.delete(selectedRow);
            }
        }
    }

    @Override
    protected void update() {
        int selectedRow = employeeTable.getSelectedRow();
        if (selectedRow != -1) {
            Employee employee = employeeTableModel.getElementAt(selectedRow);
            EmployeeDialog dialog = new EmployeeDialog(getParentFrame());
            dialog.setPostId(employee.getPostId());
            dialog.setDepartmentId(employee.getDepartmentId());
            dialog.setSurname(employee.getSurname());
            dialog.setFirstName(employee.getFirstName());
            dialog.setPatronymic(employee.getPatronymic());
            dialog.setBonus(employee.getBonus());
            dialog.setSalary(employee.getSalary());
            dialog.setVisible(true);
            if (dialog.isSucceeded()) {
                employee.setPostId(dialog.getPostId());
                employee.setDepartmentId(dialog.getDepartmentId());
                employee.setPatronymic(dialog.getPatronymic());
                employee.setSurname(dialog.getSurname());
                employee.setFirstName(dialog.getFirstName());
                employee.setSalary(dialog.getSalary());
                employee.setBonus(dialog.getBonus());
                employeeTableModel.update(selectedRow, employee);
            }
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

    @Override
    protected void add() {
        EmployeeDialog dialog = new EmployeeDialog(getParentFrame());
        dialog.setVisible(true);
        if (dialog.isSucceeded()) {
            Employee employee = new Employee();

            employeeTableModel.insert(employee);
        }
    }
}
