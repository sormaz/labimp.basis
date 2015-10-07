package edu.ohiou.mfgresearch.labimp.basis;

/**
 * Title:        Generic classes for manufacturing planning
 * Description:  Thsi project implements general classes for imtelligent manufacturing planning. These are:
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
import java.awt.*;

public class Draw2DApplet extends GUIApplet {
  Draw2DPanel canvas;

  public Draw2DApplet(Drawable2D target) {
    super ( (Viewable) target);
    canvas = new Draw2DPanel(target, this);
  }

  public Draw2DApplet() {
    this (null);
  }

  //Initialize the applet
  public void init() {
    super.init();
//    canvas.init();
    try {
      target.settApplet(this);
      ((Drawable2D)target).settCanvas ( (Draw2DPanel) canvas);
      ((Draw2DPanel)canvas).setTarget( (Drawable2D) target);
    } catch (NullPointerException e) {e.printStackTrace();}
    catch (Exception ex) {ex.printStackTrace();}
//    canvas.init();
  }

     /**
    * Method that paints applet. It calls canvas's paintComponent
    */
    public void paintComponent (Graphics2D g) {
      canvas.paintComponent(g);
    }

    /**
    * Method that paints applet. It calls canvas's paintComponent
    */
    public void paintComponent (Graphics g) {
      canvas.paintComponent(g);
    }


  public void start() {
    try {
      JSplitPane contentPane= new JSplitPane();
      contentPane.setDividerLocation(500);
      contentPane.setDividerSize(5);
      setContentPane(contentPane );
      addPanel();
    }
    catch (Exception e) {}
  }


  /**
   * Returns the size of applet for display
   */
   public Dimension getAppletSize () {
    Dimension targetSize;
    try {
    targetSize = target.getAppletSize();
    }
    catch (NullPointerException e) {
      targetSize = new Dimension (0,0);
    }
    Dimension canvasSize = canvas.getAppletSize();//drawWFCanvas.getAppletSize();
 //   ImpObject.doNothing(this, targetSize.toString() + canvasSize.toString());
    return new Dimension (Math.max(targetSize.width, canvasSize.width) + 10,
                            targetSize.height + canvasSize.height + 10);
    }
/*
  public Drawable2D getTarget () {
    return (Drawable2D) target;
    }*/

      /** Method to return drawWFCanvas of applet.
   *
   */
  public JPanel getCanvas () {
    return this.canvas;
  }

  public void setCanvas (Draw2DPanel inCanvas) {
    canvas = inCanvas;
  }

  public static void main (String args []) {
    Draw2DApplet a = new Draw2DApplet ();
    a.display();
  }
}