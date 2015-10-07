package edu.ohiou.mfgresearch.labimp.basis;

import javax.swing.*;
import java.awt.*;
import javax.swing.tree.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ToolTipTreeRenderer extends DefaultTreeCellRenderer {
  public Component getTreeCellRendererComponent(
      JTree tree, Object value, boolean sel, boolean expanded,
      boolean leaf, int row, boolean hasFocus) {

    super.getTreeCellRendererComponent(
        tree, value, sel,
        expanded, leaf, row,
        hasFocus);
    DefaultMutableTreeNode node =
        (DefaultMutableTreeNode) value;
    Object o = node.getUserObject();

    if (o != null) {
      if (o instanceof Viewable) {
    	  Color color = null;
          if  ((color = ( (Viewable) o).gettColor()) != null) {
              setForeground(color);
          }
        setToolTipText( ( (Viewable) o).toToolTipString());
      }
      else {
        setToolTipText(o.toString());
      }
    }
    else {
      setToolTipText(null);
    }
    return this;
  }
}
