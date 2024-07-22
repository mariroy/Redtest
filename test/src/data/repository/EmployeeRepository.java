package data.repository;

import data.model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository extends BaseRepository<Employee> {
    public EmployeeRepository(Connection connection) {
        super(connection);
    }

    @Override
    public List<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT ID, POST_ID, DEPARTMENT_ID, FIRST_NAME, SURNAME, PATRONYMIC, BONUS, SALARY FROM EMPLOYEE";
        try (PreparedStatement ps = getConnection().prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            System.out.printf("Выполнен запрос \"%s\"%n", sql);
            while (rs.next()) {
                employees.add(getEmployeeFromResultSet(rs));
            }
            System.out.printf("Получено %d EMPLOYEE%n", employees.size());
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
        return employees;
    }

    public List<Employee> getByDepartmentId(Long departmentId) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM EMPLOYEE WHERE DEPARTMENT_ID = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql);) {
            ps.setObject(1, departmentId);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("Выполнен запрос \"%s\"%n", sql);
                while (rs.next()) {
                    employees.add(getEmployeeFromResultSet(rs));
                }
            }

            System.out.printf("Получено %d EMPLOYEE%n", employees.size());
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
        return employees;
    }

    @Override
    public void update(Employee employee) throws RepositoryException {
        String sql = "UPDATE EMPLOYEE SET POST_ID = ?, DEPARTMENT_ID = ?, FIRST_NAME = ?, SURNAME = ?, PATRONYMIC = ?, BONUS = ? WHERE ID = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            System.out.printf("Выполнен запрос \"%s\"%n", sql);
            this.fillPreparedStatement(employee, ps);
            ps.setObject(7, employee.getId());
            int result = ps.executeUpdate();

            if (result == 0) {
                System.out.printf("Не удалось обновить объект EMPLOYEE %s%n", employee);
            } else {
                System.out.printf("Обновлен объект EMPLOYEE %s%n", employee);
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
    }

    private Employee getEmployeeFromResultSet(ResultSet rs) throws SQLException {
        Employee employee = new Employee(rs.getLong("ID"));
        employee.setPostId(rs.getLong("POST_ID"));
        employee.setDepartmentId(rs.getLong("DEPARTMENT_ID"));
        employee.setFirstName(rs.getString("FIRST_NAME"));
        employee.setSurname(rs.getString("SURNAME"));
        employee.setPatronymic(rs.getString("PATRONYMIC"));
        employee.setBonus(rs.getDouble("BONUS"));
        employee.setSalary(rs.getBigDecimal("SALARY"));
        return employee;
    }

    private void fillPreparedStatement(Employee employee, PreparedStatement ps) throws SQLException {
        ps.setLong(1, employee.getPostId());
        ps.setLong(2, employee.getDepartmentId());
        ps.setString(3, employee.getFirstName());
        ps.setString(4, employee.getSurname());
        ps.setString(5, employee.getPatronymic());
        ps.setDouble(6, employee.getBonus());
    }

    @Override
    public Employee getById(Long id) throws RepositoryException {
        String sql = "SELECT POST_ID, DEPARTMENT_ID, FIRST_NAME, SURNAME, PATRONYMIC, BONUS, SALARY  FROM EMPLOYEE WHERE ID = ?";
        Employee employee = null;
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            System.out.printf("Выполнен запрос \"%s\"%n", sql);
            ps.setObject(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    employee = new Employee(id);
                    employee.setPostId(rs.getLong("POST_ID"));
                    employee.setDepartmentId(rs.getLong("DEPARTMENT_ID"));
                    employee.setFirstName(rs.getString("FIRST_NAME"));
                    employee.setSurname(rs.getString("SURNAME"));
                    employee.setPatronymic(rs.getString("PATRONYMIC"));
                    employee.setBonus(rs.getDouble("BONUS"));
                    employee.setSalary(rs.getBigDecimal("SALARY"));
                    System.out.printf("Получен объект EMPLOYEE %s%n", employee);
                } else {
                    System.out.printf("не найден объект EMPLOYEE с id = %d%n", id);
                }

            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
        return employee;
    }

    @Override
    public void delete(Long id) throws RepositoryException {
        String sql = "DELETE FROM EMPLOYEE WHERE ID = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            System.out.printf("Выполнен запрос \"%s\"%n", sql);
            ps.setObject(1, id);
            int result = ps.executeUpdate();

            if (result == 0) {
                System.out.printf("Не удалось удалить объект EMPLOYEE c id = %d%n", id);
            } else {
                System.out.printf("Удален объект EMPLOYEE c id = %d%n", id);
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
    }

    @Override
    public void insert(Employee employee) throws RepositoryException {
        String sql = "INSERT INTO EMPLOYEE (POST_ID, DEPARTMENT_ID, FIRST_NAME, SURNAME, PATRONYMIC, BONUS) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            this.fillPreparedStatement(employee, ps);
            int result = ps.executeUpdate();
            System.out.printf("Выполнен запрос \"%s\"%n", sql);

            if (result == 0) {
                System.out.printf("Не удалось добавить объект EMPLOYEE %s%n", employee);
            } else {
                System.out.printf("Добавлен объект EMPLOYEE %s%n", employee);
            }
        } catch (SQLException e) {
            logException(sql, e);
            throw new RepositoryException(e.getMessage());
        }
    }
}
