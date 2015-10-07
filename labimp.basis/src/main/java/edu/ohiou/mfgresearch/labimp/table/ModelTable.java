package edu.ohiou.mfgresearch.labimp.table;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.table.*;

import edu.ohiou.mfgresearch.labimp.basis.GUIApplet;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;
import edu.ohiou.mfgresearch.labimp.basis.Viewable;
/**
 *
 * <p>Class ModelTable provides visual representation for any
* <code>AbstractTableModel</code>.
 * It is particularly useful for displaying <code>SquareTabelModel</code>
*  and <code>RectangularTableModel</code></p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Ohio University</p>
 * @author Dusan Sormaz
 * @version 1.0
 */
public class ModelTable
    extends JTable {
  boolean isEnabled;

  ViewObject.ViewPanel panel;
  JDialog tableDialog;
  JLabel statusLabel;

  /**
   * Constructor for a given <code>AbstractTabelModel</code>.
   * <code>ModelTable</code> is enabled by default
   * @param model <code>AbstractTableModel</code> that willbe displayed by this
   * table
   */
  public ModelTable(AbstractTableModel model) {
  this(model, true);
  }

  /**
   *
   * @param model <code>AbstractTableModel</code> that will be displayed by
   * this table
   * @param isEnabled <code>boolean</code> that denotes if table is enabled
   */
  public ModelTable(AbstractTableModel model, boolean isEnabled) {
  super(model);
    this.isEnabled = isEnabled;
    init();
  }

  /**
   * Returns an instance of <code>JPanel</code> for applications which desire
   * to embed this <code>ModelTable</code> into other visual framework
   * @return <code>JPanel</code> member field panel
   */
  public JPanel getPanel() {
  return panel;
  }

  /**
   * Displays this table with default title (string representation of the
   * table's <code>AbstractTableModel</code>. Calls other method for display
   */
  public void display() {
  display(this.getModel().toString());

  }

  /**
   * Displays the <code>ModelTable</code> with a given title.
   * Sets <code>GUIApplet</code> on field <code>panel</code>
   * and displays it
   * @param title <code>String</code> the title to be shown in the display
   */
  public void display(String title) {
  panel.settApplet(new GUIApplet(panel));
    panel.display(title);
    //    panel.getApplet().appletFrame.setTitle(title);
  }

  public JDialog createDialog (boolean isModal) {
    tableDialog = new JDialog ((Frame) null, "Table Model Editor", true);
    tableDialog.setContentPane(panel);
    return tableDialog;
  }

  /**
   * Initializes visual components for this Table. All visual components and
   * <code>ModelTable</code> itself are embedded into field <code>panel</code>
   */
  public void init() {
  panel = new ViewObject.ViewPanel();
    panel.setLayout(new BorderLayout());
    statusLabel = new JLabel("Mouse location will show here");
    setToolTipText(" table data");
    setEnabled(isEnabled);
    panel.add(new JScrollPane(this), BorderLayout.CENTER);
    panel.add(statusLabel, BorderLayout.SOUTH);
    if (isEnabled) {
      addMouseMotionListener(new TableMouseMotionAdapter(this));

      final JTableHeader tableHeader = getTableHeader();
      tableHeader.addMouseMotionListener(new MouseMotionAdapter() {
        public void mouseMoved(MouseEvent e) {
          Point p = e.getPoint();
          statusLabel.setText("");
          tableHeader.setToolTipText(getModel().getColumnName(
              tableHeader.columnAtPoint(p)));
        }
      });
    }
  }

  public static void main (String args []) {
  	String strings []= {"a", "b", "c", "d", "e"};
  	SquareTableModel m = new SquareTableModel (strings);
  	m.display();
  }

}

class  TableMouseMotionAdapter extends MouseMotionAdapter {
	ModelTable modelTable;

	public TableMouseMotionAdapter (ModelTable modelTable) {
		this.modelTable = modelTable;
	}
    public void mouseMoved(MouseEvent e) {
      Point p = e.getPoint();
      TableModel model = modelTable.getModel();
      Object o = model.getValueAt(modelTable.rowAtPoint(p),
    		  modelTable.columnAtPoint(p));
      modelTable.statusLabel.setText("Row: "
                          + model.getValueAt(modelTable.rowAtPoint(p), 0)
                          + "; Column: "
                          + model.getColumnName(modelTable.columnAtPoint(p)));
      if (o != null) {
    	  if (o instanceof ViewObject) {
    		  modelTable.setToolTipText(((Viewable)o).toToolTipString());
    	  }
    	  else{
    		  modelTable.setToolTipText(o.toString());
    	  }
      }
      else {
    	  modelTable.setToolTipText("null");
      }
    }
  }
