package ui.model.employee;

import data.database.RedDatabase;
import data.model.Department;
import data.model.Post;
import data.repository.DepartmentRepository;
import data.repository.PostRepository;
import ui.components.ComboBoxItem;
import ui.components.CustomComboBox;
import ui.model.BaseDialog;
import ui.model.post.PostDialog;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class EmployeeDialog extends BaseDialog {

    private Long postId;
    private CustomComboBox positionNameComboBox;
    private Long departmentId;
    private CustomComboBox departmentComboBox;

    private JTextField firstnameField;
    private JTextField surnameField;
    private JTextField patronymicField;
    private JTextField salaryField;
    private JComboBox<Double> bonusComboBox;

    private PostRepository postRepository;
    private DepartmentRepository departmentRepository;

    public EmployeeDialog(Frame parent) {
        super(parent, "Добавить/Изменить Должность");
    }

    @Override
    protected void beforeSettings() {
        try {
            Connection connection = RedDatabase.createConnection();
            postRepository = new PostRepository(connection);
            departmentRepository = new DepartmentRepository(connection);
        } catch (SQLException e) {
            System.out.println("Ошибка создания репозиториев POST и DEPARTMENT");
        }
    }

    @Override
    protected void addPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel positionLabel = new JLabel("Должность:");
        positionLabel.setForeground(Color.RED);
        panel.add(positionLabel);
        positionNameComboBox = new CustomComboBox(getPositionNameItems());
        positionNameComboBox.addActionListener(e -> calculateSalary());
        panel.add(positionNameComboBox);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel departmentLabel = new JLabel("Отдел:");
        departmentLabel.setForeground(Color.RED);
        panel.add(departmentLabel);
        departmentComboBox = new CustomComboBox(getDepartmentItems());
        panel.add(departmentComboBox);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel surnameLabel = new JLabel("Фамилия:");
        surnameLabel.setForeground(Color.RED);
        panel.add(surnameLabel);
        surnameField = new JTextField();

        panel.add(surnameField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel firstnameLabel = new JLabel("Имя:");
        firstnameLabel.setForeground(Color.RED);
        panel.add(firstnameLabel);
        firstnameField = new JTextField();
        panel.add(firstnameField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel patronymicLabel = new JLabel("Отчество:");
        patronymicLabel.setForeground(Color.RED);
        panel.add(patronymicLabel);
        patronymicField = new JTextField();
        panel.add(patronymicField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        panel.add(new JLabel("Множитель доплаты:"));
        Double[] bonuses = new Double[]{1.0, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.7, 2.0, 2.5, 3.0};
        bonusComboBox = new JComboBox<>(bonuses);
        bonusComboBox.setSelectedItem(1.0);
        bonusComboBox.addActionListener(e -> calculateSalary());
        panel.add(bonusComboBox);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        panel.add(new JLabel("Зарплата:"));
        salaryField = new JTextField();
        salaryField.setEditable(false);
        panel.add(salaryField);

        this.add(panel, BorderLayout.CENTER);
    }

    private void calculateSalary() {
        Double selectedBonus = (Double) bonusComboBox.getSelectedItem();
        ComboBoxItem selectedPosition = positionNameComboBox.getSelectedItem();
        if (selectedBonus != null && selectedPosition != null) {
            Post post = postRepository.getById(selectedPosition.getId());
            BigDecimal salary = post.getSalary().multiply(new BigDecimal(selectedBonus)).setScale(2, RoundingMode.HALF_UP);
            salaryField.setText(salary.toString());
        }
    }


    private ComboBoxItem[] getPositionNameItems() {
        ComboBoxItem[] comboBoxItems = new ComboBoxItem[0];
        if (postRepository != null) {
            List<Post> posts = postRepository.getAll();
            comboBoxItems = new ComboBoxItem[posts.size()];
            for (int i = 0; i < posts.size(); i++) {
                Post post = posts.get(i);
                comboBoxItems[i] = createPositionItem(post);
            }
        }
        return comboBoxItems;
    }

    private ComboBoxItem createPositionItem(Post post) {
        String caption = String.format("%s - %s", post.getPositionName(), post.getSalary().toString());
        return new ComboBoxItem(post.getId(), caption);
    }

    private ComboBoxItem[] getDepartmentItems() {
        ComboBoxItem[] comboBoxItems = new ComboBoxItem[0];
        if (departmentRepository != null) {
            List<Department> departments = departmentRepository.getAll();
            comboBoxItems = new ComboBoxItem[departments.size()];
            for (int i = 0; i < departments.size(); i++) {
                Department department = departments.get(i);
                comboBoxItems[i] = createDepartmentItem(department);
            }
        }
        return comboBoxItems;
    }

    private ComboBoxItem createDepartmentItem(Department department) {
        String caption = String.format("%s (%s, %s)", department.getDepartmentName(), department.getEmail(), department.getPhone());
        return new ComboBoxItem(department.getId(), caption);
    }

    @Override
    protected void save() {
        String firstname = firstnameField.getText().trim();
        String surname = surnameField.getText().trim();
        String patronymic = patronymicField.getText().trim();

        if (firstname.isEmpty() || surname.isEmpty() || patronymic.isEmpty()) {
            JOptionPane.showMessageDialog(EmployeeDialog.this, "Введите данные", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!firstname.matches("[a-zA-Zа-яА-Я -]{0,60}")) {
            JOptionPane.showMessageDialog(EmployeeDialog.this, "Имя должно содержать только буквы", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!surname.matches("[a-zA-Zа-яА-Я -]{0,60}")) {
            JOptionPane.showMessageDialog(EmployeeDialog.this, "Фамилия должна содержать только буквы", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!patronymic.matches("[a-zA-Zа-яА-Я -]{0,60}")) {
            JOptionPane.showMessageDialog(EmployeeDialog.this, "Отчкство должно содержать только буквы", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        succeeded = true;
        dispose();
    }

    public Long getDepartmentId() {
        ComboBoxItem selectedItem = departmentComboBox.getSelectedItem();
        if (selectedItem != null) {
            return selectedItem.getId();
        }
        return departmentId;
    }

    public String getFirstName() {
        return firstnameField.getText().trim();
    }

    public String getSurname() {
        return surnameField.getText().trim();
    }

    public String getPatronymic() {
        return patronymicField.getText().trim();
    }

    public Double getBonus() {
        return (Double) bonusComboBox.getSelectedItem();
    }

    public BigDecimal getSalary() {
        String[] split = salaryField.getText().trim().split("[.,]");
        String unscaledVal = split[0].concat(split[1]);
        return BigDecimal.valueOf(Long.parseLong(unscaledVal), split[1].length());
    }

    public Long getPostId() {
        ComboBoxItem selectedItem = positionNameComboBox.getSelectedItem();
        if (selectedItem != null) {
            return selectedItem.getId();
        }
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
        if (postId != null) {
            Post post = postRepository.getById(postId);
            ComboBoxItem positionItem = post == null ? null : createPositionItem(post);
            positionNameComboBox.setSelectedItem(positionItem);
        }
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
        if (departmentId != null) {
            Department department = departmentRepository.getById(departmentId);
            ComboBoxItem departmentItem = department == null ? null : createDepartmentItem(department);
            departmentComboBox.setSelectedItem(departmentItem);
        }
    }


    public void setFirstName(String firstName) {
        this.firstnameField.setText(firstName);
    }

    public void setSurname(String surname) {
        this.surnameField.setText(surname);
    }

    public void setPatronymic(String patronymic) {
        this.patronymicField.setText(patronymic);
    }

    public void setBonus(Double bonus) {
        this.bonusComboBox.setSelectedItem(bonus);
    }

    public void setSalary(BigDecimal salary) {
        this.salaryField.setText(String.valueOf(salary));
    }
}
