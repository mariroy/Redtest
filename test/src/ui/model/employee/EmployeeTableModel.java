package ui.model.employee;

import data.database.RedDatabase;
import data.model.Employee;
import data.model.Post;
import data.repository.EmployeeRepository;
import data.repository.PostRepository;
import data.repository.Repository;
import ui.model.BaseTableModel;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeTableModel extends BaseTableModel<Employee> {
    private static final String[] COLUMN_NAMES = {"Фамилия", "Имя", "Отчество", "Должность", "Зарплата должности", "Множитель доплаты", "Зарплата"};
    private final PostRepository postRepository;
    private final Map<Long, Post> postMap = new HashMap<>();

    public EmployeeTableModel() {
        super();
        this.postRepository = createPostRepository();
        loadPost();
    }

    private void loadPost() {
        if (postRepository != null) {
            List<Post> all = postRepository.getAll();
            for (Post post : all) {
                postMap.put(post.getId(), post);
            }
        } else {
            System.out.println("Должности не были загружены по причине отсуствия Repository");
        }
    }

    @Override
    protected Repository<Employee> createRepository() {
        try {
            return new EmployeeRepository(RedDatabase.createConnection());
        } catch (SQLException e) {
            System.out.println("Ошибка создания EmployeeRepository");
            System.out.printf("Причина: \"%s\"%n", e.getMessage());
            return null;
        }
    }

    private PostRepository createPostRepository() {
        try {
            return new PostRepository(RedDatabase.createConnection());
        } catch (SQLException e) {
            System.out.println("Ошибка создания PostRepository");
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
        Employee employee = getElements().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return employee.getSurname();
            case 1:
                return employee.getFirstName();
            case 2:
                return employee.getPatronymic();
            case 3:
                return postMap.get(employee.getPostId()).getPositionName();
            case 4:
                return postMap.get(employee.getPostId()).getSalary();
            case 5:
                return employee.getBonus();
            case 6:
                return employee.getSalary();

            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }
}
