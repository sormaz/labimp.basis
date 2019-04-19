package edu.ohiou.mfgresearch.labimp.table;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import edu.ohiou.mfgresearch.labimp.basis.ViewObject;

import java.awt.*;

import javax.swing.border.*;

import java.awt.event.*;

public class PropertyTable
    extends ViewObject {

  String name;

  Hashtable propertyHashTable = new Hashtable();
  String[] columnNames = {
      "Value"};
  public String[] propertyNames;
//  = {
//      "negativeTolerance", "positiveTolerance", "roundness",
//      "largestToolDiameter",
//      "straightness", "parallelism", "truePosition", "surfaceFinish",
//      "perpendicularity", "primaryDatum", "secondaryDatum", "tertiaryDatum"};  //mayur
  String clickMessage = "Select item from the list";
  JComboBox nameColumnComboBox;// = new JComboBox(propertyNames); ;
  ToleranceTableModel toleranceTableModel;
  Border panelLineBorder = BorderFactory.createLineBorder(Color.blue);
  Border panelBorder = BorderFactory.createTitledBorder(panelLineBorder,
      " Tolerance Table ");

  /**
   *
   */
  public PropertyTable() {
    this("", (String []) null);

  }

  public PropertyTable(String name, String[] properties) {
    this.name = name;
    if (properties != null) {
      propertyNames = properties;
    }
  }

  public PropertyTable (String name, Properties properties) {
       this (name,  (String []) null);
       propertyNames = new String [properties.size()];
//       Set nameSet = new TreeSet();
       Enumeration names = properties.propertyNames();


       for (int i = 0; i < propertyNames.length; i++) {
	 propertyNames[i] = (String) names.nextElement();
	 put(propertyNames[i], properties.getProperty(propertyNames[i]));
       }
  }

  public Hashtable getToleranceHashTable() {
    return propertyHashTable;
  }

  protected JPanel createPanel() {
    if (panel == null) {
      init();
    }
    return panel;
  }

  public void init() {

    TableColumnModel tableColumnModel;
    TableColumn nameColumn;
    Set usedNames = propertyHashTable.keySet();
    ArrayList unusedNames = new ArrayList();
    for (int i =0; i < propertyNames.length;i++) {
      if (!usedNames.contains(propertyNames[i])) {
	unusedNames.add(propertyNames[i]);
      }
    }
    TreeSet s = new TreeSet (unusedNames);
	nameColumnComboBox = new JComboBox(s.toArray());

    nameColumnComboBox.insertItemAt(clickMessage, 0);
    nameColumnComboBox.setSelectedItem(clickMessage);
    TreeSet names = new TreeSet(propertyHashTable.keySet());
    Object[] allNames = new Object[names.size() + 1];
    int i = 0;
    for (Iterator itr = names.iterator(); i < allNames.length - 1; i++) {
      allNames[i] = itr.next();
    }
    allNames[i] = clickMessage;
    toleranceTableModel =
	new ToleranceTableModel(allNames,
				columnNames, new ToleranceTableCellGenerator());

    final JTable toleranceTable = new JTable(toleranceTableModel);
    tableColumnModel = toleranceTable.getColumnModel();
    nameColumn = tableColumnModel.getColumn(0);
   
    nameColumn.setCellEditor(new NameColumnEditor());
    toleranceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    JScrollPane tableScrollPane = new JScrollPane(toleranceTable);
    JPanel toleranceButtonPanel = new JPanel();
    JButton toleranceDeleteButton = new JButton("Delete");
    Border scrollBorder = BorderFactory.createLineBorder(Color.blue, 2);
    Border tolerancePanelBorder = BorderFactory.createTitledBorder(scrollBorder,
	"Tolerance:");

    panel = new JPanel();
//        panel.setBorder(panelBorder);
    toleranceButtonPanel.add(toleranceDeleteButton);
    panel.setLayout(new BorderLayout());
    panel.add(tableScrollPane, BorderLayout.CENTER);
    panel.add(toleranceButtonPanel, BorderLayout.SOUTH);

    toleranceDeleteButton.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e) {
	int currentIndex = toleranceTable.getSelectedRow();
	if (currentIndex >= 0) {
	  Object currentName = toleranceTableModel.getValueAt(currentIndex, 0);
	  System.out.println("curretn name" + currentName);
	  if (! ( (String) currentName).equalsIgnoreCase(clickMessage)) {
	    propertyHashTable.remove(currentName);
	    toleranceTableModel.deleteRow(toleranceTable.getSelectedRow());
	    nameColumnComboBox.addItem(currentName);
	    System.out.println("ht: " + propertyHashTable.size());
	  }
	}
      }
    }
    );
  }

  /**
   *
   * <p>Title: NameColumnEditor </p>
   * <p>Description: </p>
   * <p>Copyright: Copyright (c) 2002</p>
   * <p>Company:Ohio University </p>
   * @author not attributable
   * @version 1.0
   */
  class NameColumnEditor
      extends DefaultCellEditor {

    /**
     *
     * @param toleranceComboBox
     */
    public NameColumnEditor() {
      super(nameColumnComboBox);
    }

    /**
     *
     * @return
     */
    public Object getCellEditorValue() {

      String name = " ";
      System.out.println("in line 83");
      nameColumnComboBox = (JComboBox) editorComponent;
      name = (String) nameColumnComboBox.getSelectedItem();
      if (!name.equalsIgnoreCase(clickMessage)) {
	nameColumnComboBox.removeItem(name);
	toleranceTableModel.addRow(name, toleranceTableModel.getRowCount() - 1);
	System.out.println("in getCellEditor ");
      }
      return name;

    }
  }

  /**
   *
   * <p>Title: ToelranceTableModel </p>
   * <p>Description: </p>
   * <p>Copyright: Copyright (c) 2002</p>
   * <p>Company: Ohio University </p>
   * @author not attributable
   * @version 1.0
   */
  class ToleranceTableModel
      extends RectangularTableModel {

    /**
     *
     * @param rows
     * @param columns
     * @param tcg
     */
    public ToleranceTableModel(Object[] rows, Object[] columns,
			       TableCellGenerator tcg) {
      super(rows, columns, tcg);

    }

    /**
     *
     * @param i
     * @param j
     * @return
     */
    public boolean isCellEditable(int i, int j) {
      if ( (i == toleranceTableModel.getRowCount() - 1) && (j == 0) || (j > 0)) {
	return true;
      }
      return false;
    }
  }

  /**
   *
   * <p>Title: ToleranceTableCellGenerator </p>
   * <p>Description: </p>
   * <p>Copyright: Copyright (c) 2002</p>
   * <p>Company: Ohio University </p>
   * @author not attributable
   * @version 1.0
   */

  class ToleranceTableCellGenerator
      implements TableCellGenerator {
    String name = "";

    /**
     *
     * @param o1
     * @param o2
     * @return
     */
    public Object makeTableCell(Object o1, Object o2) {
//      System.out.println("o1 = " + o1);
//
//      System.out.println("o2 " + o2);
      Object o;
      if ( ( (String) o2).equalsIgnoreCase("value")) {
	if ( (o = propertyHashTable.get( (String) o1)) != null)
	  return o;
      }

      return new Double(Double.NaN);
    }

    /**
     *
     * @param o1
     * @param o2
     * @param dataValue
     */

    public void updateRelation(Object o1, Object o2, Object dataValue) {

      if ( ( (String) o2).equalsIgnoreCase("value")) {
	propertyHashTable.put( (String) o1, new Double(dataValue.toString()));
      }
      System.out.println("ht update: " + propertyHashTable.size());

    }
  }

  public boolean put(String name, String value) {

    if (isProperty(name)) {
      propertyHashTable.put(name, value);
      if (nameColumnComboBox != null) {
	nameColumnComboBox.removeItem(name);
      }
      return true;
    }
    return false;
  }

  private boolean isProperty(String name) {
    for (int i = 0; i < propertyNames.length; i++) {
      if (name.equalsIgnoreCase(propertyNames[i])) {
	return true;
      }
    }
    return false;
  }

  public Object get(String name) {
    return propertyHashTable.get(name);
//		System.out.println("the hastable is "+propertyHashTable.toString());
//		System.out.println("after obj got "+temp);
//		if (temp == null || temp == "" || temp == " ") {
//			return null;
//		}
//		System.out.println("the cha value "+temp.toString().charAt(0));
//		if (temp.toString().charAt(0) == '(')
//		{
//			return temp.toString();
//		}
//		System.out.println("the object temp is "+temp);
//		System.out.println("inside if for "+name+" and "+temp);
//		return  new Double(temp.toString());
  }

  public String toString() {
    return name + ", number of options: " + propertyNames.length;
  }

  public static void main(String[] args) {
    PropertyTable tolerance1 = new PropertyTable("Tolerances", (String []) null);
    tolerance1.display();

    PropertyTable sysProp = new PropertyTable ("System properties", System.getProperties());
    sysProp.display();
    PropertyTable viewProp = new PropertyTable ("ViewObject properties", properties);
    viewProp.display();
  }
}