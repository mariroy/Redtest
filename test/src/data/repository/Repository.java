package data.repository;

import data.model.Model;

import java.util.List;

public interface Repository<T extends Model> {

    List<T> getAll() throws RepositoryException;

    void update(T model) throws RepositoryException;

    T getById(Long id) throws RepositoryException;

    void delete(Long id) throws RepositoryException;

    void insert(T model) throws RepositoryException;
}
