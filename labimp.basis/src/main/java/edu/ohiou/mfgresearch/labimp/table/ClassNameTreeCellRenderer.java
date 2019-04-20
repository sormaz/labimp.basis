package edu.ohiou.mfgresearch.labimp.table;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.awt.Component;
import javax.swing.*;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ClassNameTreeCellRenderer extends DefaultTreeCellRenderer {
  public ClassNameTreeCellRenderer() {
  }
  /* (non-Javadoc)
   * @see javax.swing.tree.TreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
   */
  public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                boolean selected,
                                                boolean expanded, boolean leaf,
                                                int row, boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf,
                                       row, hasFocus);

    setLabelText(value);
    return this;
  }
  private void setLabelText(Object o) {
    setHorizontalAlignment(JLabel.CENTER);
    setOpaque(true);
    if (o instanceof String) {
      setText( (String) o);
    }
    else {
      String fullClassName = o.getClass().getName();
      String shortClassName = fullClassName.substring(fullClassName.lastIndexOf(
          '.') + 1);
      setText(shortClassName);
    }
  }

}
