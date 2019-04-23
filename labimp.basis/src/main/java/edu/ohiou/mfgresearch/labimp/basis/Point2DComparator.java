package edu.ohiou.mfgresearch.labimp.basis;


import java.util.Comparator;
import java.awt.geom.Point2D;

/**
 *
 *  Title:        Generic classes for manufacturing planning
 *  Description:  Thsi project implements general classes for intelligent manufacturing planning. These are:
 *  ImpObject - umbrella class fro all objects
 *  MrgPartModel - general part object data
 *  Viewable - interface to display objects in applet
 *  GUIApplet - applet that utilizes viewable interface
 *  Copyright:    Copyright (c) 2001
 *  Company:      Ohio University
 *  @author Dusan Sormaz
 *  @version 1.0
 * 
 */

public class Point2DComparator implements Comparator {
	Point2D.Double comparingPoint;

	public Point2DComparator(Point2D.Double point) {
		comparingPoint = point;
	}

	public int compare(Object o1, Object o2) {
		Double d1 = new Double(comparingPoint.distance((Point2D.Double) o1));
		Double d2 = new Double(comparingPoint.distance((Point2D.Double) o2));
		return d1.compareTo(d2);
	}
}