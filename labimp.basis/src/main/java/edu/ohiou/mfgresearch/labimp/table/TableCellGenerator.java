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

/**
 * This interface specifies the methods required for generation of TableModel data
 */
public interface TableCellGenerator {

/**
 * The method for generation of the object which represents the relationship
 * between objects o1 and o2 that participate in TableModel
 *
 * @param o1 - The first object in the relation
 * @param o2 - The second object in relation
 * @return - Object that represents the relation
 */
  public Object makeTableCell (Object o1, Object o2);

  /**
   * Method for updating object relation based on value entered into the
   * cell via gui
   */
  public void updateRelation (Object o1, Object o2 , Object dataValue);

}