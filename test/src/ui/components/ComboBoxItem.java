package ui.components;

import java.util.Objects;

public class ComboBoxItem {
    private final long id;
    private final String caption;

    public ComboBoxItem(long id, String caption) {
        this.id = id;
        this.caption = caption;
    }

    public long getId() {
        return id;
    }

    public String getCaption() {
        return caption;
    }

    @Override
    public String toString() {
        return caption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComboBoxItem that = (ComboBoxItem) o;
        return id == that.id && Objects.equals(caption, that.caption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, caption);
    }
}
