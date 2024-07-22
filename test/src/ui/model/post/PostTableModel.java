package ui.model.post;

import data.database.RedDatabase;
import data.model.Post;
import data.repository.PostRepository;
import data.repository.Repository;
import ui.model.BaseTableModel;

import java.sql.SQLException;

public class PostTableModel extends BaseTableModel<Post> {
    private static final String[] COLUMN_NAMES = {"Должность", "Зарплата"};

    public PostTableModel() {
        super();
    }

    @Override
    protected Repository<Post> createRepository() {
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
        Post post = getElements().get(rowIndex);
        switch (columnIndex) {
            case 0:
                return post.getPositionName();
            case 1:
                return post.getSalary();
            default:
                throw new IllegalArgumentException("Invalid column index");
        }
    }
}
