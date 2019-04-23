/**
 *
 */
package edu.ohiou.mfgresearch.labimp.basis;

import java.awt.event.MouseEvent;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.tree.*;

/**
 * @author Dusan Sormaz
 *
 */
public class TreeMouseMotionAdapter
    extends MouseMotionAdapter {

  JTree tree;

  public TreeMouseMotionAdapter(JTree jTree) {
    this.tree = jTree;
  }

  public void mouseMoved(MouseEvent e) {

    int selRow = tree.getRowForLocation(e.getX(), e.getY());
    TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
    JComponent c = (JComponent) tree.getComponentAt(e.getX(), e.getY());

    if (selRow != -1) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.
          getLastPathComponent();

//	      DefaultMutableTreeNode node = (DefaultMutableTreeNode) jTree.getPathForLocation(p.x, p.y).getLastPathComponent();

      Object o = node.getUserObject();
      System.out.println (o.toString() + c.toString());
      if (o != null) {
        if (o instanceof ViewObject) {
          c.setToolTipText( ( (Viewable) o).toToolTipString());
        }
        else {
          c.setToolTipText(o.toString());
        }
      }
      else {
        c.setToolTipText("null");
      }
    }
    else {
      tree.setToolTipText("no node selected");
    }
  }
}
