package ui.components;

import javax.swing.*;
import java.awt.*;

public class CustomComboBox extends JComboBox<ComboBoxItem> {
    public CustomComboBox(ComboBoxItem[] items) {
        super(items);
        this.setRenderer(createRenderer());
    }

    public ComboBoxItem getSelectedItem() {
        return (ComboBoxItem) super.getSelectedItem();
    }

    private DefaultListCellRenderer createRenderer() {
        return new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component listCellRendererComponent = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof ComboBoxItem) {
                    setText(((ComboBoxItem) value).getCaption());
                }
                return listCellRendererComponent;
            }
        };
    }

}
