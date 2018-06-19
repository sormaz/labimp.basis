package edu.ohiou.mfgresearch.labimp.basis;



import java.awt.*;
import java.awt.geom.*;
import java.util.LinkedList;
import javax.swing.*;


/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class DrawString extends ViewObject {
  String contentString;
  Point2D.Float position;
  double size;

  public DrawString(String content, float xPos, float yPos) {
    this (content, new Point2D.Float (xPos, yPos), Double.NaN, Color.blue);
  }

  public DrawString(String content, float xPos, float yPos, double size) {
    this (content, new Point2D.Float (xPos, yPos), size, Color.blue);
  }


  public DrawString (String content, Point2D.Float point, Color color) {
    this (content, point, Double.NaN, color);
  }
  
  public DrawString (String content, Point2D.Float point) {
    this (content, point, Double.NaN, Color.blue);
  }


  public DrawString (String content, Point2D.Float point, double size) {
     this (content, new Point2D.Float (point.x, point.y), size, Color.blue);
   }

  public DrawString (String content, Point2D.Float point, double size, Color color) {
    contentString = new String (content);
    position = new Point2D.Float (point.x, point.y);
    this.size = size;
    this.color = color;
  }

  public Point2D.Float geettPosition () {
    return position;
  }

  public String getContent () {
    return contentString;
  }
  
  public double getSize() {
	  return size;
  }

  public void init () {
    panel = new JPanel();
    panel.add (new JLabel (this.contentString) ) ;
  }

  public LinkedList<DrawString> geetStringList () {
    LinkedList<DrawString> ll = new LinkedList<DrawString>();
    ll.add (this);
    return ll ;
  }

  public LinkedList<Shape> geetDrawList () {
    return new LinkedList<Shape> ();
  }

  /**
   * Method for drawing the string content on given graphics using given
   * transformation. Only position is ruled by transformation.
   * Font is based on the current font in Graphics object
   * 
   * @param g - Graphics2D on which the draw string should be drawn
   * @param transform
   */
  public void drawContent (Graphics2D g, AffineTransform transform) {
    Point2D.Float point = new Point2D.Float();
    transform.transform( position, point);
    Font f = g.getFont();
    if (!Double.isNaN(size)) {
      double newsize = -transform.getScaleY() * size;
      Font newf = new Font(f.getName(), f.getStyle(), (int) newsize);
      g.setFont(newf);
    }
    Color oldColor = g.getColor();
    g.setColor(color);
    g.drawString(contentString, point.x, point.y);
    g.setFont(f);
    g.setColor(oldColor);
  }
  
  public void transform (AffineTransform xForm) {
    xForm.transform(position, position);
  }

  public void paintComponent (Graphics2D g) {
    g.drawString (contentString, position.x, position.y);
  }

}
