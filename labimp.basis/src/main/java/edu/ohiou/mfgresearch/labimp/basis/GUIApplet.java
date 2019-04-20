//Title:
//Version:     1.0
//Copyright:   Copyright (c)
//Author:
//Company:
//Description:
package edu.ohiou.mfgresearch.labimp.basis;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import edu.ohiou.mfgresearch.labimp.table.ClassNameRenderer;

import java.lang.reflect.*;

/**
 * This class is able to show any Viewable object as standalone or within browser
 * It can be invkoed as stand alone, without target, or with target.
 * It is also invoked when any viewabble object need to disply itself.
 */
public class GUIApplet
    extends JApplet
    implements Viewable {

  // STATIC ATRIBUTES
  /**
   * Location of the last created attribute on the screen
   */
  static Dimension appletLocation = new Dimension();

  /**
   * Increment for applet location
       * This is added to <code>appletLocation</code> whenever new applet is displayed
   */
  static Dimension appletLocIncrement = new Dimension(100, 100);

  /**
   * this stores the current screen size from the system (or Toolkit)
   */
  static private final Dimension screenSize =
      Toolkit.getDefaultToolkit().getScreenSize();

  /**
   * defulat size of the aplet
   * Used when target does not specify applet's size
   */
  static public final Dimension defaultGUIAppletSize =
      new Dimension(screenSize.width / 2, screenSize.height / 3);

  // ATTRIBUTES
  /**
   * defines if GUIApplet is running in stand alone (without browser) mode
   */
  protected boolean isStandAlone = false;
  protected BorderLayout appletLayout = new BorderLayout();
  public JFrame appletFrame;
  protected Viewable target;
  //CONSTRUCTORS

  /**
   * this is default constructor
   * It is used when called from browser
   */
  public GUIApplet() {

  }

  /**
   * Constructor with target parameter
   * This constructor should be called when target needs its display
   *
   * @param target an object of Viewable interface that will be seen in the applet
   */
  public GUIApplet(Viewable target) {
    this.target = target;
    try {
      target.settApplet(this);
    }
    catch (NullPointerException e) {}

  }

  //ACCESSORS AND MODIFIERS
  //Get a parameter value
  /**
   * Method returns th evalue for applet parameter,
   * implemented after JBuilder code for JApplet extension that returns
   * paramter in both browser and stand-alone modes
   * @param name name of parameter returned
   * @param defaultValue default value, if parameter can not be found
   * @return the value of requested parameter, or defautl value if not found
   */
  public String getParameter(String name, String defaultValue) {
     return isStandAlone ? System.getProperty(name, defaultValue) :
	(getParameter(name) != null ? getParameter(name) : defaultValue);
  }

  //
  /**
   * Returns Applet information which includes target information
   * @return Strign with information about applet and target
   */
  public String getAppletInfo() {
    return "GUI Applet showing "  + target;
  }

  //Get parameter info
  public String[][] getParameterInfo() {
    return null;
  }
  /**
   *  Returns the target of thsi applet
   * @return target field of the applet
   */
  public Viewable getTarget() {
    return target;
  }

  /**
   * Sets target for this applet to supplied Viewable object
   * @param inTarget new target for the applet
   */
  public void setTarget(Viewable inTarget) {
    target = inTarget;
  }

  /**
   * Sets Viewable object that has a focus of operating system to supplied
   * Viewable object
   * @param guiObject new Viewable objet that has OS focus
   */
  public void settGuiObject(Viewable guiObject) {
    if (target != null) {
      target.settGuiObject(guiObject);
    }
  }

  /**
   *  Returns the Viewable object that currently has operating system focus.
   * Returns target's guiObject
   * @return viewbale object that has focus
   */

  public Viewable geettGuiObject() {
    return target.geettGuiObject();
  }

  /**
   * Returns the color of this applet target
   * @return the color of the target
   */
  public Color geettColor() {
    return target.geettColor();
  }

  /**
   * Sets the targets color to tehsupplied color
   * @param color new target's color
   */
  public void settColor(Color color) {
    target.settColor(color);
  }

  /**
   * Return tis
   * Implemented to provide Viewable interface requirement
   * @return this
   */
  public GUIApplet geettApplet() {
    return this;
  }

  /**
   * Sets applet of this applet target to supplied applet
   * @param applet new applet for target
   */
  public void settApplet(GUIApplet applet) {
    if (target != null) {
      target.settApplet(applet);
    }
  }
  
  public void setStandAlone (boolean isAlone) {
    this.isStandAlone = isAlone;
  }

  /**
   * Sets this applet location on the screen using static varaibles for applet
   * location and applet location increment
   */
  protected void setAppletLocation() {
    appletLocation.width = (appletLocation.width + appletLocIncrement.width) %
	screenSize.width;
    appletLocation.height = (appletLocation.height + appletLocIncrement.height) %
	screenSize.height;
  }


  /**
   * Initialize all visual components of tihs applet and its target.
   * This method calls target's init in order to initialize its visual
   * components
   */
  public void iinit() {

    this.getContentPane().setLayout(appletLayout);
    if (target == null) {
      Class c = null;
      String targetName = this.getParameter("TARGET", "");
      try {
        c = Class.forName(targetName);
        target = (Viewable) c.newInstance();
        target.settApplet(this);
        target.init();
      }
      catch (Exception e) {
        try {
          Method typeMethod = c.getMethod("getConstructorParamTypes", (Class[])null);
          Method objectMethod = c.getMethod("getConstructorObjects", (Class [])null);
          Class[] types = (Class[]) typeMethod.invoke(null, (Object [])null);
          Object[] objects = (Object[]) objectMethod.invoke(null, (Object [])null);
          Constructor constructor = c.getConstructor(types);
          target = (Viewable) constructor.newInstance(objects);

        }
        catch (Exception ex) {
          System.err.print("Class " + targetName + " can not be loaded");
          e.printStackTrace();
        }
      }
    }
    else {
      target.init();
    }
  }

  public void init() {

  this.getContentPane().setLayout(appletLayout);
  if (target == null) {
    Class c;
    String targetName = this.getParameter("TARGET", "");
//			System.out.println("target name" + targetName);
    try {
      c = Class.forName(targetName);
      target = (Viewable) c.newInstance();
      target.settApplet(this);
      target.init();
    }
    catch (Exception e) {
      System.err.print("Class " + targetName + " can not be loaded");
      e.printStackTrace();
    }
  }
  else {
    target.init();
  }
}


  //Start the applet
  public void start() {
    try {
      addPanel();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Stop the applet
  public void stop() {
  }

  //Destroy the applet
  public void destroy() {
    super.destroy();
  }

  //implement the display methods in Viewable interface
  /**
   * Displays this applet and its target in stand-alone frame with
   * supplied title, supplied size and default window closing
   * operation (JFrame.DISPOSE_ON_CLOSE)
   * @param inTitle String for the frame's title
   * @param inSize desired size of the applet
   * @param appletClosing appletClosing operation code
   */
  public void display(String inTitle, Dimension inSize, int appletClosing) {
    try {
      this.isStandAlone = true;
      JFrame frame = new JFrame();
      appletFrame = frame;
      frame.setDefaultCloseOperation(appletClosing);
      AppletWindowListener awl = new AppletWindowListener();
      frame.addWindowListener(awl);
      frame.setTitle(inTitle);
      frame.getContentPane().add(this, BorderLayout.CENTER);
      this.init();
      this.start();
      frame.setSize(inSize);
      frame.setLocation(appletLocation.width, appletLocation.height);
      this.setAppletLocation();
      if (target != null) {
        String resourceName = ClassNameRenderer.getShortClassName (target) + ".gif";
        try {
          ImageIcon image =
            new ImageIcon(target.getClass().getResource(resourceName));
          frame.setIconImage(image.getImage());
        } catch (RuntimeException e) {
//        If image does not exist it is not critical
//        e.printStackTrace();
        }
      }
      frame.setVisible(true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Displays this applet and its target in stand-alone frame with
   * supplied title, default size and supplied window closing
   * operation (from JFrame constants)
   * @param inTitle String for the frame's title
   * @param appletClosing appletClosing operation code
   */
  public void display(String inTitle, int appletClosing) {

    display(inTitle, geetAppletSize(), appletClosing);
  }

  /**
   * Displays this applet and its target in stand-alone frame with
   * supplied title, supplied size and default window closing
   * operation (JFrame.DISPOSE_ON_CLOSE)
   * @param inTitle String for the frame's title
   * @param inSize desired size of the applet
   */
  public void display(String inTitle, Dimension inSize) {
    this.display(inTitle, inSize, JFrame.DISPOSE_ON_CLOSE);
  }

  /**
   * Displays this applet and its target in stand-alone frame with
   * supplied title, supplied window width nad height and default window closing operation
   * @param inTitle String for the frame's title
   * @param inWidth desired width of applet
   * @param inHeight desired height of applet
   */
  public void display(String inTitle, int inWidth, int inHeight) {
    this.display(inTitle, new Dimension(inWidth, inHeight),
		 JFrame.DISPOSE_ON_CLOSE);
  }
  /**
   * Displays this applet and its target in stand-alone frame with
   * supplied title, defualt size and default window closing operation
   * @param inTitle String for the frame's title
   */
  public void display(String inTitle) {
    this.display(inTitle, JFrame.DISPOSE_ON_CLOSE);
  }
  /**
   * Displays this applet and its target in stand-alone frame with
   * default title, defualt size and default window closing operation
   */
  public void display() {
    this.display(toString(), geetAppletSize(), JFrame.DISPOSE_ON_CLOSE);
  }

  /**
   * Displays this applet and its target in stand-alone frame with
   * default title, defualt size and supplied window closing operation
   * @param appletClosing appletClosing operation code
   */
  public void display(int appletClosing) {
    display(toString(), geetAppletSize(), appletClosing);
  }

  /**
   * Returns the panel of theapplet's target.
   * Implemented to satisfy requirement from Viewable interface.
   * @return panel of the applet's target
   */
  public JPanel geettPanel() {
    return target.geettPanel();
  }

  public JPanel makePanel() {
    if (target != null) {
      return target.makePanel();
    }
    else {
      return new JPanel();
    }
  }

  public void addPanel() {
    if (target != null) {
      target.addPanel();
    }
    else {
      this.getContentPane().add(new JLabel("this applet does not have target"),
                            BorderLayout.CENTER);
    }
  }

  public int getPanelLocation() {
    return target.getPanelLocation();
  }

  public int getPanelOrientation() {
    return target.getPanelOrientation();
  }

  public void settPanel(JPanel inPanel) {
    target.settPanel(inPanel);
  }

  public Dimension geetAppletSize() {
    //  return this.defaultGUIAppletSize;
    if (target != null) {
      return target.geetAppletSize();
    }
    else {
      return GUIApplet.defaultGUIAppletSize;
    }
  }
  public String toToolTipString() {
	  return "Class " + target.getClass().toString() +
      " needs to implement toToolTipString() method";

  }

  public void toggleVisible() {
    target.toggleVisible();
  }

  static public void main(String[] arg) {

//    GUIApplet guia = new GUIApplet(new ViewObject());
    GUIApplet guia2 = new GUIApplet();
    guia2.display();
  }

  //static initializer for setting look & feel
  static {
    try {
      // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    }
    catch (Exception e) {
    }
  }
  /**
   *
   * <p>Title: </p>
   * <p>Description: </p>
   * <p>Copyright: Copyright (c) 2003</p>
   * <p>Company: </p>
   * @author not attributable
   * @version 1.0
   */
  class AppletWindowListener
      extends WindowAdapter {

    public AppletWindowListener() {
    }

    public void windowClosing(WindowEvent e) {
      if (target != null) {
	settPanel(null);
	toggleVisible();
      }
    }

    public void windowActivated(WindowEvent e) {
      settGuiObject(target);
    }

    public void windowDeactivated(WindowEvent e) {
//      setGuiObject(null);
    }
  } // EofClass AppletWindowListener

} // EofClass GUIApplet
