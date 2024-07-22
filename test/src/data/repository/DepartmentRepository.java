package data.repository;

import data.model.Department;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentRepository extends BaseRepository<Department> {
    public DepartmentRepository(Connection connection) {
        super(connection);
    }

    @Override
    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT ID, HEAD_ID, EMAIL, PHONE, DEPARTMENT_NAME FROM DEPARTMENT";
        try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            System.out.printf("Выполнен запрос \"%s\"%n", sql);
            while (rs.next()) {
                Department department = new Department(rs.getLong("ID"));
                department.setHeadId(rs.getLong("HEAD_ID"));
                department.setEmail(rs.getString("EMAIL"));
                department.setPhone(rs.getString("PHONE"));
                department.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
                departments.add(department);
            }
            System.out.printf("Получено %d DEPARTMENT%n", departments.size());
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
        return departments;
    }


    @Override
    public void update(Department department) throws RepositoryException {
        String sql = "UPDATE DEPARTMENT SET HEAD_ID = ?, EMAIL = ?, PHONE = ?, DEPARTMENT_NAME = ? WHERE ID = ?";
        System.out.printf("Выполнен запрос \"%s\"%n", sql);
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            fill(department, ps);
            ps.setLong(5, department.getId());
            int result = ps.executeUpdate();

            if (result == 0) {
                System.out.printf("Не удалось обновить объект DEPARTMENT %s%n", department);
            } else {
                System.out.printf("Обновлен объект DEPARTMENT %s%n", department);
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
    }

    private void fill(Department department, PreparedStatement ps) throws SQLException {
        ps.setObject(1, department.getHeadId());
        ps.setString(2, department.getEmail());
        ps.setString(3, department.getPhone());
        ps.setString(4, department.getDepartmentName());
    }

    @Override
    public Department getById(Long id) throws RepositoryException {
        String sql = "SELECT HEAD_ID, EMAIL, PHONE, DEPARTMENT_NAME FROM DEPARTMENT WHERE ID = ?";
        Department department = null;
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            System.out.printf("Выполнен запрос \"%s\"%n", sql);
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    department = new Department(id);
                    department.setHeadId(rs.getLong("HEAD_ID"));
                    department.setEmail(rs.getString("EMAIL"));
                    department.setPhone(rs.getString("PHONE"));
                    department.setDepartmentName(rs.getString("DEPARTMENT_NAME"));
                    System.out.printf("Получен объект DEPARTMENT %s%n", department);
                } else {
                    System.out.printf("Не найден объект DEPARTMENT с id = %d%n", id);
                }
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
        return department;
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        String sql = "DELETE FROM DEPARTMENT WHERE ID = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setObject(1, id);
            int result = ps.executeUpdate();
            System.out.printf("Выполнен запрос \"%s\"%n", sql);

            if (result == 0) {
                System.out.printf("Не удалось удалить объект DEPARTMENT c id = %d%n", id);
            } else {
                System.out.printf("Удален объект DEPARTMENT c id = %d%n", id);
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public void insert(Department department) throws RepositoryException {
        String sql = "INSERT INTO DEPARTMENT (HEAD_ID, EMAIL, PHONE, DEPARTMENT_NAME) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            fill(department, ps);
            int result = ps.executeUpdate();
            System.out.printf("Выполнен запрос \"%s\"%n", sql);

            if (result == 0) {
                System.out.printf("Не удалось добавить объект DEPARTMENT %s%n", department);
            } else {
                System.out.printf("Добавлен объект DEPARTMENT %s%n", department);
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
    }
}
