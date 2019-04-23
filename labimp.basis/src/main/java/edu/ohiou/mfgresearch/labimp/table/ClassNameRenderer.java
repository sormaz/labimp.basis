package edu.ohiou.mfgresearch.labimp.table;

/*
 * Created on Jan 9, 2005
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * @author Dusan Sormaz
 *
 */
public class ClassNameRenderer extends JLabel
implements ListCellRenderer, TableCellRenderer {

  public Component getListCellRendererComponent(JList list, Object value,
      int index, boolean isSelected,
      boolean cellHasFocus) {
    setAppearance();
    setText(getShortClassName(value));
    setBackground(isSelected ?
        list.getSelectionBackground() : list.getBackground());
    setForeground(isSelected ?
        list.getSelectionForeground() : list.getForeground());
    return this;
  }

  public Component getTableCellRendererComponent(JTable table, Object value,
      boolean isSelected,
      boolean hasFocus, int row,
      int column) {
    setAppearance();
    setText(getShortClassName(value));
    setBackground(isSelected ?
        table.getSelectionBackground() : table.getBackground());
    setForeground(isSelected ?
        table.getSelectionForeground() : table.getForeground());
    return this;
  }

  public void setAppearance() {
    setHorizontalAlignment(JLabel.CENTER);
    setOpaque(true);
  }

  public static String getShortClassName(Object o) {
    if (o instanceof String) {
      return (String) o;
    }
    else {
      String fullClassName = o.getClass().getName();
      String shortClassName = fullClassName.substring(fullClassName.lastIndexOf(
      '.') + 1);
      return shortClassName;
    }
  }

}
