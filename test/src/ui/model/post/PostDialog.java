package ui.model.post;

import ui.model.BaseDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.math.BigDecimal;

public class PostDialog extends BaseDialog {
    private JTextField positionNameField;
    private JTextField salaryField;

    public PostDialog(Frame parent) {
        super(parent, "Добавить/Изменить Должность");
    }


    @Override
    protected void addPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Должность:"));
        positionNameField = new JTextField();
        panel.add(positionNameField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        panel.add(new JLabel("Зарплата:"));
        salaryField = new JTextField();
        panel.add(salaryField);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        this.add(panel, BorderLayout.CENTER);
    }

    @Override
    protected void save() {
        String positionValue = positionNameField.getText().trim();
        String salaryValue = salaryField.getText().trim();

        if (positionValue.isEmpty() || salaryValue.isEmpty()) {
            JOptionPane.showMessageDialog(PostDialog.this, "Введите данные", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!positionValue.matches("[a-zA-Zа-яА-Я -]{0,60}")) {
            JOptionPane.showMessageDialog(PostDialog.this, "Должность должна содержать только буквы", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!salaryValue.matches("[0-9]{1,8}[.,][0-9]{2}")) {
            JOptionPane.showMessageDialog(PostDialog.this, "Зарплата должна содержать число с двумя знаками после запятой", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        succeeded = true;
        dispose();
    }


    public String getPositionName() {
        return positionNameField.getText().trim();
    }

    public BigDecimal getSalary() {
        String[] split = salaryField.getText().trim().split("[.,]");
        String unscaledVal = split[0].concat(split[1]);
        return BigDecimal.valueOf(Long.parseLong(unscaledVal), split[1].length());
    }

    public void setPositionName(String positionName) {
        this.positionNameField.setText(positionName);
    }

    public void setSalary(BigDecimal salary) {
        this.salaryField.setText(String.valueOf(salary));
    }
}
