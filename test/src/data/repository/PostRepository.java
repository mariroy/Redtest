package data.repository;

import data.model.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostRepository extends BaseRepository<Post> {
    public PostRepository(Connection connection) {
        super(connection);
    }

    @Override
    public List<Post> getAll() throws RepositoryException {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT ID, POSITION_NAME, SALARY FROM POST";
        try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            System.out.printf("Выполнен запрос \"%s\"%n", sql);
            while (rs.next()) {
                Post post = new Post(rs.getLong("ID"));
                post.setPositionName(rs.getString("POSITION_NAME"));
                post.setSalary(rs.getBigDecimal("SALARY"));
                posts.add(post);
            }
            System.out.printf("Получено %d POST%n", posts.size());
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
        return posts;
    }

    @Override
    public void update(Post post) throws RepositoryException {
        String sql = "UPDATE POST SET POSITION_NAME = ?, SALARY = ? WHERE ID = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            fill(post, ps);
            ps.setLong(3, post.getId());
            int result = ps.executeUpdate();
            System.out.printf("Выполнен запрос \"%s\"%n", sql);
            if (result == 0) {
                System.out.printf("Не удалось обновить объект POST %s%n", post);
            } else {
                System.out.printf("Обновлен объект POST %s%n", post);
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
    }

    private void fill(Post post, PreparedStatement ps) throws SQLException {
        ps.setString(1, post.getPositionName());
        ps.setBigDecimal(2, post.getSalary());
    }

    @Override
    public Post getById(Long id) throws RepositoryException {
        Post post = null;
        String sql = "SELECT POSITION_NAME, SALARY FROM POST WHERE ID = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            System.out.printf("Выполнен запрос \"%s\"%n", sql);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    post = new Post(id);
                    post.setPositionName(rs.getString("POSITION_NAME"));
                    post.setSalary(rs.getBigDecimal("SALARY"));
                    System.out.printf("Получен объект POST %s%n", post);
                } else {
                    System.out.printf("Не найден объект POST с id = %d%n", id);
                }
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
        return post;
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        String sql = "DELETE FROM POST WHERE ID = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setLong(1, id);
            int result = ps.executeUpdate();
            System.out.printf("Выполнен запрос \"%s\"%n", sql);

            if (result == 0) {
                System.out.printf("Не удалось удалить объект POST c id = %d%n", id);
            } else {
                System.out.printf("Удален объект POST c id = %d%n", id);
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public void insert(Post post) throws RepositoryException {
        String sql = "INSERT INTO POST (POSITION_NAME, SALARY) VALUES (?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            fill(post, ps);
            int result = ps.executeUpdate();
            System.out.printf("Выполнен запрос \"%s\"%n", sql);

            if (result == 0) {
                System.out.printf("Не удалось добавить объект POST %s%n", post);
            } else {
                System.out.printf("Добавлен объект POST %s%n", post);
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
    }
}
