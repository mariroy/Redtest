package ui.model.department;

import data.database.RedDatabase;
import data.model.Department;
import data.model.Post;
import data.repository.DepartmentRepository;
import data.repository.PostRepository;
import data.repository.Repository;
import ui.model.BaseTableModel;

import java.sql.SQLException;

public class DepartmentTableModel extends BaseTableModel<Department> {
    private static final String[] COLUMN_NAMES = {"Начальник", "Почта", "Телефон", "Название отдела"};

    public DepartmentTableModel() {
        super();
    }

    @Override
    protected Repository<Department> createRepository() {
        try {
            return new DepartmentRepository(RedDatabase.createConnection());
        } catch (SQLException e) {
            System.out.println("Ошибка создания DepartmentRepository");
            System.out.printf("Причина: \"%s\"%n", e.getMessage());
            return null;
        }
    }

    @Override
    protected String[] getColumns() {
        return COLUMN_NAMES;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Department department = getElements().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return getNullForLong(department.getHeadId());
            case 1:
                return department.getEmail();
            case 2:
                return department.getPhone();
            case 3:
                return department.getDepartmentName();
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }
    private Object getNullForLong(Long id) {
        return id != null && id == 0L ? "" : id;
    }
}
