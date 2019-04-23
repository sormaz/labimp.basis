package edu.ohiou.mfgresearch.labimp.table;

import javax.swing.table.*;
public class SquareSubModelCellGenerator implements TableCellGenerator {
  SquareTableModel parent;
  
  public SquareSubModelCellGenerator (SquareTableModel p) {
    parent = p;
  }

  public Object makeTableCell(Object o1, Object o2) {
    int row = parent.findRow(o1);
    int column = parent.findColumn(o2);
    return parent.getValueAt(row, column + 1);
  }

  public void updateRelation(Object o1, Object o2, Object dataValue) {
    parent.getGenerator().updateRelation(o1, o2, dataValue);  
  }

}


