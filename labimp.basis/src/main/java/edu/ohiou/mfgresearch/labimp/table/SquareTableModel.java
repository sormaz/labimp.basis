package edu.ohiou.mfgresearch.labimp.table;

/**
 * Title:     Generic classes for manufacturing planning
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

import java.util.*;
import javax.swing.table.AbstractTableModel;

/**
 * Class that represents a model of relations between elements from the same set
 */
public class SquareTableModel
    extends AbstractTableModel {
//	  final Object[] columnNames;
//	  final Object[][] data;
  private List columnNames;
  private List data;

  private TableCellGenerator generator;

  public SquareTableModel(Object[] columnNames) {
    this(columnNames, new DefaultCellGenerator());
  }

  public SquareTableModel(Object[] inColumns,
                          TableCellGenerator inCellGenerator) {
    this.columnNames = new ArrayList();
    this.data = new ArrayList();
    this.generator = inCellGenerator;
    this.columnNames.add(new String("Name")); // needed for JTable operations
    for (int i = 0; i < inColumns.length; i++) {
      this.columnNames.add(inColumns[i]);
    }

    for (int i = 0; i < inColumns.length; i++) {
      ArrayList list = new ArrayList();
      list.add(inColumns[i]);
      for (int j = 1; j < columnNames.size(); j++) {
        list.add(generator.makeTableCell(inColumns[i], columnNames.get(j)));
      }
      this.data.add(list);
    }
  }

  private void addRow(Object object) {
    ArrayList list = new ArrayList();
    list.add(object);
    for (int i = 1; i < columnNames.size(); i++) {
      /** @todo dsormaz flipped next two lines to test proc precedence graph */
      list.add(generator.makeTableCell(object, columnNames.get(i)));
//      list.add(generator.makeTableCell(object,object));
    }
    this.data.add(list);
    this.fireTableRowsInserted(this.data.size() - 1, this.data.size() - 1);
  }

  private void addColumn(Object object) {
    this.columnNames.add(object);
    for (int i = 0; i < this.data.size(); i++) {
      ( (ArrayList) data.get(i)).add(generator.makeTableCell( ( (ArrayList)
          data.get(i)).get(0), object));
    }
    this.fireTableStructureChanged();
  }

  public void addObject(Object object) {
    this.addColumn(object);
    this.addRow(object);
  }

  public void deleteObject(int rowNumber) {
    this.data.remove(rowNumber);
    this.columnNames.remove(rowNumber + 1);
    for (int i = 0; i < this.data.size(); i++) {
      ( (ArrayList)this.data.get(i)).remove(rowNumber + 1);
    }

    this.fireTableStructureChanged();
//				this.fireTableRowsDeleted(rowNumber,rowNumber);
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

  public TableCellGenerator getGenerator() {
    return this.generator;
  }

  public List getColumnObjects() {
    List list = new ArrayList (columnNames);
    list.remove("Name"); // removes the column for row headers
    return list;

  }

  public List getDataObjects() {
    return this.data;
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

  public int findColumn(Object o) {
    return columnNames.indexOf(o) - 1;
  }

  public int findRow(Object o) {
    return findColumn(o);
  }

  public int getRowCount() {
    return data.size();
  }

  public String getColumnName(int col) {
    return columnNames.get(col).toString();
  }

  public Object getValueAt(int row, int col) {
//    if (col == 0) {
//      return ( (ArrayList) data.get(row)).get(0);
//    }
//    else {
    try {
      return ( (ArrayList) data.get(row)).get(col);
//				}
    }
    catch (Exception ex) {
      return null;
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

  public void display() {
    ModelTable table = new ModelTable(this);
    table.display();
  }

  public static void main(String[] args) {
  }
}

class DefaultCellGenerator
    implements TableCellGenerator {
  public Object makeTableCell(Object o1, Object o2) {
    return new Boolean(o1.equals(o2));
  }

  public void updateRelation(Object o1, Object o2, Object value) {}
}
