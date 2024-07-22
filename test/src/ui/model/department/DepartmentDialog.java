package ui.model.department;

import data.database.RedDatabase;
import data.model.Employee;
import data.repository.EmployeeRepository;
import ui.components.ComboBoxItem;
import ui.components.CustomComboBox;
import ui.model.BaseDialog;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDialog extends BaseDialog {
    private Long departmentId;
    private Long headId;
    private boolean isNew;
    private CustomComboBox headComboBox;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField departmentNameField;

    private EmployeeRepository employeeRepository;

    public DepartmentDialog(Frame parent) {
        super(parent, "Добавить/Изменить Отдел");
    }

    @Override
    protected void beforeSettings() {
        try {
            Connection connection = RedDatabase.createConnection();
            employeeRepository = new EmployeeRepository(connection);
        } catch (SQLException e) {
            System.out.println("Ошибка создания репозитория EMPLOYEE");
        }
    }

    @Override
    protected void addPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        if (!isNew) {
            addHeadComboBox(panel);
        }

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel emailLabel = new JLabel("Почта:");
        emailLabel.setForeground(Color.RED);
        panel.add(emailLabel);
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel phoneLabel = new JLabel("Телефон:");
        phoneLabel.setForeground(Color.RED);
        panel.add(phoneLabel);
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel departmentNameLabel = new JLabel("Название отдела:");
        departmentNameLabel.setForeground(Color.RED);
        panel.add(departmentNameLabel);
        departmentNameField = new JTextField();
        panel.add(departmentNameField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        this.add(panel, BorderLayout.CENTER);
    }

    private void addHeadComboBox(JPanel container) {
        container.add(new JLabel("Руководитель отдела:"));
        headComboBox = new CustomComboBox(new ComboBoxItem[0]);
        container.add(headComboBox);
    }

    public void fillHeadComboBox(Long departmentId) {
        if (employeeRepository != null) {
            List<Employee> employees = departmentId == null ? new ArrayList<>() : employeeRepository.getByDepartmentId(departmentId);
            for (Employee employee : employees) {
                headComboBox.addItem(createHeadItem(employee));
            }
        }
    }

    private ComboBoxItem createHeadItem(Employee employee) {
        String caption = String.format("%s %s %s", employee.getSurname(), employee.getFirstName(), employee.getPatronymic());
        return new ComboBoxItem(employee.getId(), caption);
    }

    @Override
    protected void save() {
        String emailValue = emailField.getText().trim();
        String phoneValue = phoneField.getText().trim();
        String departmentNameValue = departmentNameField.getText().trim();

        if (emailValue.isEmpty() || phoneValue.isEmpty() || departmentNameValue.isEmpty()) {
            JOptionPane.showMessageDialog(DepartmentDialog.this, "Введите данные", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!emailValue.matches("[a-zA-Z0-9()@.\\\\-]{0,100}")) {
            JOptionPane.showMessageDialog(DepartmentDialog.this, "Почта может содержать только буквы, цифры и специальные символы @ и .", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!phoneValue.matches("[0-9()\\\\-]{0,20}")) {
            JOptionPane.showMessageDialog(DepartmentDialog.this, "Номер телефона должен содержать только цифры и специальные символы (, ) и -", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!departmentNameValue.matches("[a-zA-Zа-яА-Я -]{0,100}")) {
            JOptionPane.showMessageDialog(DepartmentDialog.this, "Название отдела должно содержать только буквы", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        succeeded = true;
        dispose();
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public String getEmail() {
        return emailField.getText().trim();
    }

    public String getPhone() {
        return phoneField.getText().trim();
    }

    public String getDepartmentName() {
        return departmentNameField.getText().trim();
    }

    public void setDepartmentID(Long departmentId) {
        this.departmentId = departmentId;
        fillHeadComboBox(departmentId);
    }

    public void setEmail(String email) {
        this.emailField.setText(email);
    }

    public void setPhone(String phone) {
        this.phoneField.setText(phone);
    }

    public void setDepartmentName(String departmentName) {
        this.departmentNameField.setText(departmentName);
    }

    public boolean isNew() {
        return isNew;
    }

    public Long getHeadId() {
        ComboBoxItem selectedItem = headComboBox.getSelectedItem();
        if(selectedItem != null) {
            return selectedItem.getId();
        }
        return headId;
    }

    public void setHeadId(Long headId) {
        this.headId = headId;
        if (employeeRepository != null) {
            Employee head = employeeRepository.getById(headId);
            ComboBoxItem headItem = head == null ? null : createHeadItem(head);
            headComboBox.setSelectedItem(headItem);
        }
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}
