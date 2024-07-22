package data.repository;

import data.model.Model;

import java.sql.Connection;

public abstract class BaseRepository<T extends Model> implements Repository<T> {
    private final Connection CONNECTION;

    public BaseRepository(Connection connection) {
        CONNECTION = connection;
    }

    public Connection getConnection() {
        return CONNECTION;
    }

    protected void logException(String sql, Exception e) {
        System.out.printf("Ошибка выполнения запроса \"%s\"%n", sql);
        System.out.printf("Причина: \"%s\"%n", e.getMessage());
    }
}
