package ui.model;

import data.model.Model;
import data.repository.Repository;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTableModel<T extends Model> extends AbstractTableModel {
    private List<T> elements;
    private final Repository<T> repository;

    protected BaseTableModel() {
        this.repository = createRepository();
        loadElements();
    }

    protected abstract Repository<T> createRepository();

    protected List<T> getElements() {
        return elements;
    }

    private void loadElements() {
        if (repository != null) {
            this.elements = repository.getAll();
        } else {
            this.elements = new ArrayList<>();
            System.out.println("Список не был загружен по причине отсуствия Repository");
        }
        fireTableDataChanged();
    }

    protected abstract String[] getColumns();

    @Override
    public int getColumnCount() {
        return getColumns().length;
    }

    @Override
    public int getRowCount() {
        return elements.size();
    }

    @Override
    public String getColumnName(int column) {
        return getColumns()[column];
    }

    public void insert(T post) {
        if (repository != null) {
            repository.insert(post);
            elements.add(post);
            fireTableRowsInserted(elements.size() - 1, elements.size() - 1);
        }
    }

    public void update(int rowIndex, T post) {
        if (repository != null) {
            repository.update(post);
            elements.set(rowIndex, post);
            fireTableRowsUpdated(rowIndex, rowIndex);
        }
    }

    public void delete(int rowIndex) {
        if (repository != null) {
            repository.delete(elements.get(rowIndex).getId());
            elements.remove(rowIndex);
            fireTableRowsDeleted(rowIndex, rowIndex);
        }
    }

    public T getElementAt(int rowIndex) {
        return elements.get(rowIndex);
    }
}
