package ui;

import ui.model.department.DepartmentPanel;
import ui.model.employee.EmployeePanel;
import ui.model.post.PostPanel;

import javax.swing.*;
import java.awt.*;

public class GeneralFrame extends JFrame {
    public GeneralFrame() throws HeadlessException {
        super();
        setSettings();
        addComponents();
    }

    private void setSettings() {
        this.setTitle("Учет сотрудников предприятия");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    private void addComponents() {
        JTabbedPane tabbedPane = new JTabbedPane();

        EmployeePanel employeePanel = new EmployeePanel();
        tabbedPane.addTab(employeePanel.getTitle(), employeePanel);

        PostPanel postPanel = new PostPanel();
        tabbedPane.addTab(postPanel.getTitle(), postPanel);

        DepartmentPanel departmentPanel = new DepartmentPanel();
        tabbedPane.addTab(departmentPanel.getTitle(), departmentPanel);

        this.add(tabbedPane);
    }
}
