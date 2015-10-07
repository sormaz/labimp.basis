package edu.ohiou.mfgresearch.labimp.table;

/**
 * Title:        Generic classes for manufacturing planning
 * Description:  Thsi project implements general classes for intelligent manufacturing planning. These are:
 * ImpObject - umbrella class fro all objects
 * MrgPartModel - general part object data
 * Viewable - interface to display objects in applet
 * GUIApplet - applet that utilizes viewable interface
 * Copyright:    Copyright (c) 2001
 * Company:      Ohio University
 * @author Dusan Sormaz
 * @version 1.0
 */
import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.event.TableModelEvent;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;


/**
 * The table model to represetn relationships between different sets of objects
 */
public class RectangularTableModel
    extends AbstractTableModel {
  private List columnNames;
  private List data;
  private TableCellGenerator generator;

  public RectangularTableModel() {
    columnNames = new ArrayList();
    data = new ArrayList();
  }

  public RectangularTableModel(Object[] inRows, Object[] inColumns) {
    this(inRows, inColumns, new DefaultCellGenerator());
  }

  public RectangularTableModel(Object[] inRows, Object[] inColumns,
			       TableCellGenerator generator) {
    TableListener listener = new TableListener();
    this.addTableModelListener(listener);
    this.columnNames = new ArrayList();
    this.data = new ArrayList();
    this.generator = generator;
    this.columnNames.add(new String("Name"));
    for (int i = 0; i < inColumns.length; i++) {
      this.columnNames.add(inColumns[i]);
    }
    for (int i = 0; i < inRows.length; i++) {
      ArrayList list = new ArrayList();
      list.add(inRows[i]);
      for (int j = 1; j < columnNames.size(); j++) {
	list.add(generator.makeTableCell(inRows[i], columnNames.get(j)));
      }
      this.data.add(list);
    }
  }

  public void addRow(Object rowObject) {
    addRow(rowObject, data.size());
  }

  /**
   *  Adds row to this table at the specified position.
   */
  public void addRow(Object rowObject, int rowIndex) {
    ArrayList list = new ArrayList();
    list.add(rowObject);
    for (int i = 1; i < columnNames.size(); i++) {
      list.add(generator.makeTableCell(rowObject, columnNames.get(i)));
    }
    this.data.add(rowIndex, list);
    this.fireTableRowsInserted(rowIndex, rowIndex);
  }

  public void deleteRow(int rowNumber) {
    this.data.remove(rowNumber);
    this.fireTableRowsDeleted(rowNumber, rowNumber);
  }

  public void deleteAll() {
    this.data.clear();
    this.columnNames.clear();
    this.fireTableStructureChanged();
  }

  public void deleteColumn(int colNumber) {
    this.columnNames.remove(colNumber);
    for (int i = 0; i < this.data.size(); i++) {
      ( (ArrayList)this.data.get(i)).remove(colNumber);
    }
    this.fireTableStructureChanged();
  }

  public boolean deleteColumn(Object o) {
    int columnNumber = 0;
    for (int i = 1; i < this.columnNames.size(); i++) {
      if (o.equals(this.columnNames.get(i))) {
	this.columnNames.remove(i);
	columnNumber = i;
      }
    }
    if (columnNumber == 0)
      return false;
    for (int i = 0; i < this.data.size(); i++) {
      ( (ArrayList)this.data.get(i)).remove(columnNumber);
    }
    this.fireTableStructureChanged();
    return true;
  }

  public void addColumnObject(Object colObject) {
    this.columnNames.add(colObject);
    for (int i = 0; i < this.data.size(); i++) {
      ( (ArrayList) data.get(i)).add(generator.makeTableCell( ( (ArrayList)
	  data.get(i)).get(0), colObject));
    }
    this.fireTableStructureChanged();
  }

  public void addAllColumns(Collection colObjects) {
    this.columnNames.clear();
    this.columnNames.add(new String("Name"));
    for (Iterator itr = colObjects.iterator(); itr.hasNext(); ) {
      columnNames.add(itr.next());
    }
    for (int i = 0; i < this.data.size(); i++) {
      Object firstObject = ( (ArrayList) data.get(i)).get(0);
      for (int j = 1; j < this.columnNames.size(); j++) {
	Object secondObject = this.columnNames.get(j);
	( (ArrayList) data.get(i)).set(j,
				       generator.makeTableCell(firstObject, secondObject));
      }
    }
    this.fireTableStructureChanged();
  }

  public void addAllRows(Collection rowObjects) {
    for (Iterator itr = rowObjects.iterator(); itr.hasNext(); ) {
      this.addRow(itr.next());
    }
    this.fireTableStructureChanged();
  }

  public void substituteColumnObjects(int columnIndex, Object[] colObjects) {
    for (int i = 0; i < colObjects.length; i++) {
      this.columnNames.set(columnIndex + 1, colObjects[i]);
      columnIndex++;
    }
    this.fireTableStructureChanged();
  }

  public void substituteRowObjects(int rowIndex, Object[] rowObjects) {
  }

  public List getColumnObjects() {
    return this.columnNames;
  }

  public List getDataObjects() {
    return this.data;
  }

  public void setGenerator(TableCellGenerator generator) {
    this.generator = generator;
  }

  public Class getColumnClass(int c) {
    if (getValueAt(0, c) != null)
      return getValueAt(0, c).getClass();
    else
      return Object.class;
  }

  public int getColumnCount() {
    return columnNames.size();
  }

  public int getRowCount() {
    return data.size();
  }

  public String getColumnName(int col) {
    return columnNames.get(col).toString();
  }

  public int findColumn (Object o) {
    return columnNames.indexOf(o)-1;
}

  public int findRow (Object o) {
    for (int i = 0; i < data.size(); i++) {
         ArrayList list = (ArrayList)data.get(i);
         if (o.equals(list.get(0))) {
           return i;
         }
    }
  return -1;
}



  public Object getValueAt(int row, int col) {
    if (col == 0) {
      return ( (ArrayList) data.get(row)).get(0);
    }
    else {
      return ( (ArrayList) data.get(row)).get(col);
    }
  }

  public boolean isCellEditable(int row, int col) {
    return true;
  }

  public void setValueAt(Object value, int row, int col) {
    if (value != null) {
      ( (ArrayList)this.data.get(row)).set(col, value);
      fireTableCellUpdated(row, col);
      generator.updateRelation( ( (ArrayList)this.data.get(row)).get(0),
			       this.columnNames.get(col), value);
    }
  }

  public TableCellGenerator getGenerator() {
    return this.generator;
  }

  public void display () {
    ModelTable  table = new ModelTable(this);

    table.display();
  }

  public static void main(String[] args) {
    Object o1 = new Object();
    Object o2 = new Object();
    Object o3 = new Object();
    Object o4 = new Object();
    Object o5 = new Object();
    Object[] objects = {
	o1, o2, o3};
    Object[] obj = {
	o1, o2, o4, o5};
    RectangularTableModel rectangularTableModel1 = new RectangularTableModel(
	objects, obj);
    ModelTable table = new ModelTable(rectangularTableModel1);
//    table.display();
    JDialog d = table.createDialog(true);
    d.setVisible(true);
  }
}

class TableListener
    implements javax.swing.event.TableModelListener {
  public void tableChanged(TableModelEvent e) {
  }
}
