package edu.ohiou.mfgresearch.labimp.basis;

import javax.swing.*;
import javax.swing.tree.*;

import java.awt.*;
import java.awt.image.*;

import javax.swing.table.TableCellRenderer;
import javax.swing.ListCellRenderer;

import java.awt.geom.*;
import java.util.*;
import java.io.*;
import java.net.URL;




import edu.ohiou.mfgresearch.labimp.table.ClassNameRenderer;
import edu.ohiou.mfgresearch.labimp.table.PropertyTable;

import java.io.Serializable;

/**
 * ViewObject class is a root class for all process planning related objects in
 * labimp and IMPlanner development.
 * It provides for easy implementation of visual displays on any object in 
 * IMPlanner development
 *
 * @author: Dusan N. Sormaz
 * @copyright, D. N. Sormaz, Ohio University
 */
public class ViewObject
    implements Viewable, Drawable2D, Serializable {

  static public final int NO_DISPLAY = 0;
  static public final int PANEL = 1;
  static public final int CANVAS_2D = 2;
  static public final int CANVAS_WF = 3;
  static public final int CANVAS_3D = 4;
  static public final int PANEL_CANVAS_2D = 5;
  static public final int PANEL_CANVAS_WF = 6;
  static public final int PANEL_CANVAS_3D = 7;

//  static protected DefaultMutableTreeNode classNode =
//      new DefaultMutableTreeNode(ViewObject.class);
//  static protected JTree classTree = new JTree(classNode);
  static {
//    classTree.addPropertyChangeListener(new PropertyChangeListener() );
  }
/**
 * field for reading various properties from several properties file
 */
  static protected Properties properties = new Properties();
  
  static protected GraphicsConfiguration graphics = new GraphicsConfiguration();
  
  static public Properties getProperties () {
	  return properties;
  }
  
  static public GraphicsConfiguration geetGraphics () {
	  return graphics;
  }
  
  static public File chooseFile(String fileType, String title) {
	  String folder = ViewObject.getProperties().getProperty(fileType + "_FOLDER");
	  JFileChooser chooser = new JFileChooser(folder);    
	  File f = null;
	  try {
		  chooser.setDialogTitle(title);
		  int returnVal = chooser.showOpenDialog(null);
		  if(returnVal == JFileChooser.APPROVE_OPTION) 
		  {
			  f = chooser.getSelectedFile();
		  }
	  } catch (HeadlessException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
	  }
	  return f;
  }
  
  public static void loadProperties (Class c, String name) {
	   try {
		      URL resourcePropertyURL = c.getResource(
		      "/META-INF/properties/" + name + ".properties");
		      properties.load(resourcePropertyURL.openStream());
		      System.out.println(
		      "Properties loaded from " + c.getSimpleName() + " resource, " + resourcePropertyURL);
		    }
		    catch (Exception ex) {
		      System.err.println(
		      "\nProperties "  + name + " not loaded from ViewObject resource file.");
		    }
		    try {
		      File propertyFile = new File(System.getProperty("user.home"),
		      name + ".properties");
		      properties.load(new FileInputStream(propertyFile));
		      System.out.println(
		      "Properties loaded from user home, file " + propertyFile);
		    }
		    catch (Exception ex) {
		      System.err.println(
		      "Properties " + name + " not loaded from user home.");
		    }
		    try {
		      File propertyFile = new File(System.getProperty("user.dir"),
		    		 name +  ".properties");
		      properties.load(new FileInputStream(propertyFile));
		      System.out.println(
		      "Properties loaded from current folder, file " + propertyFile);
		    }
		    catch (Exception ex) {
		      System.err.println(
		      "\nProperties " + name + " not loaded from current folder.");
		    }
  }
  
  public static void displayProperties () {
	    if (properties.getProperty("edu.ohiou.mfgresearch.labimp.basis.ViewObject.addSystem","YES").
	            equalsIgnoreCase("YES")) {
	          try {
	            properties.putAll(System.getProperties());
	            System.out.println("Properties added from system");
	          } catch (java.security.AccessControlException ace) {
	            System.out.println("Properties not added from system, access not granted");		
	          }
	        }
	        if (properties.getProperty(
	        "edu.ohiou.mfgresearch.labimp.basis.ViewObject.displayProperties", "NO").
	        equalsIgnoreCase("YES")) {
	          PropertyTable viewProp = new PropertyTable("ViewObject properties",
	              properties);
	          viewProp.display();
	          System.out.println("Properties shown in table");
	        }
  }

  static {

	  
	  loadProperties (ViewObject.class, "labimp.basis");
	  displayProperties();
    graphics.configure ();    	
  }



  /**
   * This variable represents an instance of this class that has current focus
   * of OS gui. Since only one component may be in this state, attribute is static
   */
  static protected Viewable guiObject;

  /**
   * Panel to hold all visual components of the class
   * Subclasses are responsible for constructing their own panel variable
   */
  transient protected JPanel panel;
  protected Color color;
  transient protected DefaultMutableTreeNode node;

//  {
//    if (panel == null) System.out.println("in top level block");
//  }

  /**
   * variable to set panel location. should take on of values from BorderLayout
   */
  transient protected int panelLocation;

  /**
   * variable to set panel orientation in split pane. use SwingConstants only
   */
  transient protected int panelOrientation;

  /**
   * Applet in which the object will be shown
   */
  transient public GUIApplet applet;

  /**
   * panel that provides particular view of this object.
   * For example if this is shown as Drawable2D this would be Draw2DPanel
   */
  transient public JPanel canvas;

  /**
   * The current status of the object visibilty (being displayed in applet)
   */
  transient private boolean isVisible = false;

  /**
   * Default constructor.
   */
  public ViewObject() {
  }

//  private boolean isValid() {
//    return false;
//  }

// methods for Viewable interface

  /**
   * Toggles visibility status of the object.
   * This method changes the visibility status of object (this denotes
   * if the object is currently shown in any applet on the screen) from
   * visible to invisible and vice versa
   */
  public void toggleVisible() {
    isVisible = !isVisible;
  }
  /**
   * returns the current value of visible attribute.
   *
   * @return
   */
  public boolean isVisible() {
	  return isVisible;
  }

  public String toToolTipString() {
	  return "<html><body>Note: " + this.getClass().toString() +
      " <br>needs to implement <i>toToolTipString()</i> method</body></html>";

  }

  public String toString() {
	  return "Note: " + this.getClass().toString() +
      " needs to implement toString() method";

  }


  public int getDisplayMode() {
    if (applet == null)
      return NO_DISPLAY;
//      if (applet instanceof DrawWFApplet)
//        return PANEL_CANVAS_WF;
    if (applet instanceof Draw2DApplet)
      return PANEL_CANVAS_2D;
    if (applet instanceof GUIApplet)
      return PANEL;
//      if (canvas instanceof DrawWFPanel)
//        return CANVAS_WF;
    if (canvas == null)
      return PANEL;

    return NO_DISPLAY;
  }

  public boolean needCanvas() {
    switch (getDisplayMode()) {
      case PANEL_CANVAS_WF:
      case PANEL_CANVAS_2D:
      case CANVAS_WF:
      case CANVAS_2D:
        return true;
      case CANVAS_3D:
      case PANEL_CANVAS_3D:
      default:
        return false;
    }
  }

  public boolean needPanel() {
    switch (getDisplayMode()) {
      case PANEL_CANVAS_WF:
      case PANEL_CANVAS_2D:
      case PANEL_CANVAS_3D:
      case PANEL:
        return true;
      default:
        return false;
    }
  }

  /**
   * Initializes visual components for GUIApplet
   *
   */
  public void init() {

    if (needPanel()) {
      panel = new ViewPanel();
      panel.setLayout(new BorderLayout());
      panelLocation = SwingConstants.LEFT;
      try {
        if (this.getClass() ==
            Class.forName("edu.ohiou.mfgresearch.labimp.basis.ViewObject")) {
          panel.add(new JLabel("This is instance of " +
                               this.getClass().toString()), BorderLayout.NORTH);
          JTree tree = new JTree(new DefaultMutableTreeNode (this) );
          tree.getSelectionModel().setSelectionMode

         (TreeSelectionModel.SINGLE_TREE_SELECTION);
//tree.setEnabled(true);
     //Enable tool tips.
ToolTipManager.sharedInstance().registerComponent(tree);


    tree.setCellRenderer(new ToolTipTreeRenderer());


     panel.add(new JScrollPane(tree), BorderLayout.CENTER);
        }
        else
          panel.add(new JLabel("Class " + this.getClass().toString() +
                               " needs to implement init()"));
        panel.setSize(new Dimension(640, 100));
      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Returns object panel for display of components
   *
   * @return panel attribute of given object
   */
  public JPanel geettPanel() {
    return panel;
  }

  final public JPanel makePanel() {
    if (panel != null)
      return panel;
    else
      return createPanel();
  }

  protected JPanel createPanel() {
    return new ViewPanel();
  }

  public int getPanelLocation() {
    return panelLocation;
  }

  public int getPanelOrientation() {
    return panelOrientation;
  }

  public void addPanel() {
    if (applet instanceof Draw2DApplet) {
      Container appletPanel = applet.getContentPane();
      if (appletPanel instanceof JSplitPane) {
        JSplitPane splitPanel = (JSplitPane) appletPanel;
        splitPanel.setOrientation(panelOrientation);
        splitPanel.setLeftComponent( ( (Draw2DApplet) applet).getCanvas());
        splitPanel.setRightComponent(panel);
      }
      else {
        appletPanel.setLayout(new BorderLayout());
        appletPanel.add(panel, panelLocation);
        appletPanel.add( ( (Draw2DApplet) applet).getCanvas(),
                        BorderLayout.CENTER);
      }
    }
    else {
      applet.getContentPane().add(panel, BorderLayout.CENTER);
    }

  }

  public Viewable geettTarget() {
    return this;
  }

  public void settTarget(Viewable inTarget) {}
  
  public String getProperty (String propertyName) {
	  return properties.getProperty(this.getClass().getName() 
			  								+ "." + propertyName, 
			  						"Property " + propertyName + " is not defined");
  }
  
  public static String getProperty (Object o, String propertyName) {
	  return properties.getProperty(o.getClass().getName() 
			  								+ "." + propertyName, 
			  						"Property " + propertyName + " is not defined");
  }
  
  public static String getProperty (Class c, String propertyName) {
	  return properties.getProperty(c.getName() 
			  								+ "." + propertyName, 
			  						"Property " + propertyName + " is not defined");
  }
  

  public void settGuiObject(Viewable inGuiObject) {
    guiObject = inGuiObject;
  }

  public Viewable geettGuiObject() {
    return guiObject;
  }

  /**
   * sets boolean for updating
   *
   *  */
  public void setNeedUpdate(boolean needUpdate) {
    ( (Drawable2D) canvas).setNeedUpdate(needUpdate);
  }

  public void settColor(Color color) {
    this.color = color;
  }

  public Color geettColor() {
    return color;
  }
  
  public GraphicsConfiguration geetGraphicsConfig () {
	  return graphics;
	  
  }

  /**
   * Sets applet for given object to new GUI applet
   */
  public void setApplet() {
    settApplet(new GUIApplet(this));
  }

  /**
   * Sets panel for given object to supplied panel
   *
   * @param inPanel panel to be assigned to object
   */
  public void settPanel(JPanel inPanel) {
    panel = inPanel;
  }

  public void settCanvas(Draw2DPanel inCanvas) {
    canvas = inCanvas;
  }

  public JPanel geettCanvas() {
//          ViewObject.doNothing(this, "in getcanvas DrawWFPanel value" + canvas);

    return canvas;
  }

// implementaiton of the next method is not final !!!!!
  /**
   * Sets object's applet to supplied applet
   *
   * @param inApplet applet to be assigned
   */
  public void settApplet(GUIApplet inApplet) {
    applet = inApplet;
    // inApplet.target = this;
  }

  /**
   * Returns applet in which obejct is being displayed
   *
   * @return GUIApplet
   */
  public GUIApplet geettApplet() {
    return applet;
  }

  /**
   * Returns the size of applet for display
   */
  public Dimension geetAppletSize() {
    try {
      if (canvas == null)
        return panel.getSize();
      else
        return new Dimension(500, 600);
//        Gtk.add (panel.getSize(), canvas.getSize(),
//                            GeometryConstants.HEIGHT);
    }
    catch (NullPointerException npe) {
      return GUIApplet.defaultGUIAppletSize;
    }
  }

  /**
   * Displays the object in its applet.
   *
   * This method is implementation method for verifying object visibility,
   * applet existnace and sending the request to applet for display
   *
   * @param inTitle String to be display in the title bar of the applet's frame
   * @param inSize Dimansion of the applet's frame on screen
   * @param appletClosing the desired behavior when applat frame window is closed
   */
  public void display(String inTitle, int appletClosing) {
    display(inTitle, this.geetAppletSize(), appletClosing);
  }

  /**
   * Displays the object in its applet.
   * This method is called when desired behavior of window closing is dispose.
   * Necessary parameter is defaulted and other display called
   *
   * @param inTitle String to be display in the title bar of the applet's frame
   * @param inSize Dimansion of the applet's frame on screen in pixels
   */
  public void display(String inTitle, Dimension inSize) {
    this.display(inTitle, inSize, JFrame.DISPOSE_ON_CLOSE);
  }

  /**
   * Displays the object in its applet.
   * This method is called when size is specified by two integers and
   * desired behavior of window closing is dispose.
   * Necessary parameter is defaulted and other display called
   *
   * @param inTitle <code>String</code> to be displayed in the title bar of the applet's frame
   * @param inWidth <code>int</code> width of the applet's frame on screen in pixels
   * @param inHeight <code>int</code> height of the applet's frame on screen in pixels
   */
  public void display(String inTitle, int inWidth, int inHeight) {
    display(inTitle, new Dimension(inWidth, inHeight), JFrame.DISPOSE_ON_CLOSE);
  }

  /**
   * Displays the object in its applet.
   * This method is called when applet frame default parameters are acceptable.
   * Necessary parameters are defaulted and other display called
   *
   * @param inTitle String to be display in the title bar of the applet's frame
   */
  public void display(String inTitle) {
    this.display(inTitle, JFrame.DISPOSE_ON_CLOSE);
  }

  /**
   * Displays the object in its applet.
   * This method is called when applet frame default parameters are acceptable.
   * All parameters are defaulted
   */
  public void display() {
    this.display(toString(), JFrame.DISPOSE_ON_CLOSE);
  }

  public void display(int appletClosing) {
    display(toString(), appletClosing);
  }

  public void display(String inTitle, Dimension inSize, int appletClosing) {
    if (!isVisible) {
      if (applet != null) {
//        panel = new JPanel();
        applet.display(inTitle, inSize, appletClosing);
      }
      else {
        setApplet();
//        panel =  new JPanel();
        applet.display(inTitle, inSize, appletClosing);
        //System.out.println ("new applet has been initialized");
      }
      isVisible = true;
    }
    else {
      // attempt to move focus to this object, buut it does not work
      applet.requestFocus();
    }
  }

  // methods for Drawble2D interface

  /**
   * Returns list of draw elements to be drawn in Canvas2D
   * This method creates two diagonals in Canvas2D if called from <code>impObject</code>
   * or adds warning label into object's panel if called from any other object
   */
  public LinkedList<Shape> geetDrawList() {
    LinkedList<Shape> drawList = new LinkedList<Shape>();
    try {
      if (this.getClass() == Class.forName("edu.ohiou.mfgresearch.labimp.basis.ViewObject")) {

        Line2D.Double line =
            new Line2D.Double( -3, -3, 2, 2);
        drawList.add(line);
        line = new Line2D.Double( -2, 2, 2, -2);
        drawList.add(line);
        line = new Line2D.Double( -2, 2, 2, 2);
        drawList.add(line);
        line = new Line2D.Double(2, 2, 2, -2);
        drawList.add(line);
      }
      else {
        System.out.println("Method getDrawList() should be implemented in " +
                           this.getClass().toString());
        if ((panel != null) && (panel.getComponentCount() < 2))
          panel.add(new JLabel(this.getClass().toString() +
                               " needs to implement method getDrawList()"));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return drawList;
  }

  public void makeDrawSets() {
	Color color = this.color;
    if (color == null) {
      String propColor = properties.getProperty(this.getClass().getName() +
                                                ".color", "000000");
      color = new Color(Integer.parseInt(propColor, 16));
    }
 
    if (canvas instanceof Draw2DPanel) {
      Draw2DPanel drawPanel = (Draw2DPanel) canvas;
      drawPanel.addDrawShapes(color, geetDrawList());
      String propColor = properties.getProperty(this.getClass().getName() +
                                                ".fillColor", "FFFFFF");
      color = new Color(Integer.parseInt(propColor, 16));
      drawPanel.addFillShapes(color, geetFillList());
    }

  }
  
  
  
  public Collection giveSelectables () {
    LinkedList list= new LinkedList();
    list.add(this);
    list.add(new Point2D.Double(2,2));
    return list;
    
  }
  
  public Point2D geettPosition () {
    return new Point2D.Double (0.0, 0.0);
  }
  
  public void settPosition (Point2D point) {
    System.out.println("Setting position to: " + point.toString());
  }

  public LinkedList<Shape> geetFillList () {
    LinkedList<Shape> fillShapes = new LinkedList<Shape>();
//    fillShapes.add (new Rectangle (0,0,10,10));
    return fillShapes;
  }

  public void generateImageList () {
    Image  icon = Toolkit.getDefaultToolkit().
        createImage(ViewObject.class.getResource("ViewObject.gif"));
//    BufferedImage  imIcon = (BufferedImage) new ImageIcon (
//        ViewObject.class.getResource("ViewObject.gif")).getImage().getScaledInstance(-1,20,Image.SCALE_SMOOTH);
     ImageIcon icon2 = new ImageIcon (ViewObject.class.getResource("ViewObject.gif"));
//     Image  viewim = icon.getImage().getScaledInstance(-1,20,0);
    if (canvas instanceof Draw2DPanel) {
      Draw2DPanel panel2D = (Draw2DPanel) canvas;
      AffineTransform transform =AffineTransform.getScaleInstance(1, -1);
      transform.translate (-4,-4);
      if (Math.random()> 1.) {
//        panel2D.addImage(imIcon, transform);
      }
      else
        panel2D.addImage(icon, transform);
        
      panel2D.addImage(new ImageIcon (
          ViewObject.class.getResource("branka-AHS-junior.jpg")).getImage().
          getScaledInstance(-1,100,0), transform);
    
    
    }
  }

  public LinkedList<DrawString> geetStringList() {
    LinkedList<DrawString> stringList = new LinkedList<DrawString>();
    try {
      if (this.getClass() == Class.forName("edu.ohiou.mfgresearch.labimp.basis.ViewObject")) {
        stringList.add(new DrawString("Welcome to ViewObject 2D canvas", -1, -1));
        stringList.add (new DrawString ("This string is scalable", -1,1, 1) );
      }
      else {
        System.out.println("Method getStringList() should be implemented in " +
                           this.getClass().toString());
        stringList.add(new DrawString(toString(), 0, 0));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return stringList;
  }

  /** @todo dsormaz move all wf methods to impobject */
  // methods for DrawableWF interface

  public void repaint() {
    try {
      ( (JPanel)this.geettCanvas()).repaint();
    }
    catch (NullPointerException e) {}
    // change
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2d = (Graphics2D) g;
    paintComponent(g2d);
  }

  /**
   *  Paints this object into graphics context
   *
   * @param g <code>Graphics2D</code> context for holding painted objects
   *
   */
  public void paintComponent(Graphics2D g) {
    //g.draw(this.getDrawLine());
    // call canvas paintcomponent.
//    ((DrawWFPanel) this.getCanvas()).paintComponent(g);
  }

  public LinkedList getPointSet() {
    return new LinkedList();
  }

  /**
   * Prints on System out stream message useful for debuging purposes.
   * The method prints obj to Sting method and user specified message
   *
   * @param obj object reference when using this metod
   * @param s String with the content of debugging message
   */
  public static void doNothing(Object obj, String s) {
    if (obj != null)
      System.out.println("In " + obj.toString() + "> Message: " + s);
    else
      System.out.println("In static context> Message: " + s);
  }

  /**
   * Prints on System out stream message useful for debuging purposes
   * The method prints the class name of the curretn object and
   * user supplied message
   *
   * @param s String with the content of debugging message
   */
  public void doNothing(String s) {
    System.out.println("In " + this.getClass().getName() + "> Message: " + s);
  }

  public String getClassName() {

    return ClassNameRenderer.getShortClassName (this);
  }

  /**
   * Writes the content of this object to string buffer in xml format
   *
   * @param buffer string buffer, destination of XMl string
   * @indent string that is used to provide good xml formatting
   */
  public void writeXML(StringBuffer buffer, String indent) {
    String className = getClassName();
    buffer.append(indent).append("<" + className + ">").
        append(this.getClass().getName() + " needs to implement writeXML() method").
        append("</" + className + ">\n");
  }

  public void writeXMLHeader(StringBuffer buffer) {
    buffer.append(
        "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>");
    buffer.append("\n");
  }

  /**
   *
   * @param file
   * @throws IOException
   */
  public void saveXML(File file) throws IOException {

    saveXML(new BufferedWriter(new FileWriter(file)));
  }

  /**
   *
   * @param writer
   * @throws IOException
   */
  public void saveXML(BufferedWriter writer) throws IOException {
    StringBuffer buffer = new StringBuffer();
    this.writeXMLHeader(buffer);
    writeXML(buffer, new String(""));
    writer.write(buffer.toString());
    writer.flush();
    writer.close();
  }

  public static class ViewPanel
      extends JPanel
      implements Viewable {
    protected GUIApplet applet;
    public ViewObject object;

    /**
     * variable to set panel location. should take on of values from BorderLayout
     */
    transient protected int panelLocation;

    /**
     * variable to set panel orientation in split pane. use SwingConstants only
     */
    transient protected int panelOrientation;

    public ViewPanel() {
    }

    public ViewPanel(ViewObject inObject) {
      object = inObject;
    }

    public void init() {
    }

    /**
     * @todo implement all methods for Viewable interface
     */
    public JPanel geettPanel() {
      return this;
    }

    public int getPanelLocation() {
      return object.getPanelLocation();
    }

    public JPanel makePanel() {
      return this;
    }

    public int getPanelOrientation() {
      return object.panelOrientation;
    }

    public void addPanel() {
//    if (applet instanceof DrawWFApplet || applet instanceof Draw2DApplet) {
//      Container appletPanel = applet.getContentPane();
//      if (appletPanel instanceof JSplitPane) {
//        JSplitPane splitPanel = (JSplitPane) appletPanel;
//        splitPanel.setOrientation(panelOrientation);
//        try {
//          splitPanel.setLeftComponent(((Draw2DApplet)applet).getCanvas());}
//        catch (Exception e) {
//          try {
//            splitPanel.setLeftComponent(((DrawWFApplet)applet).getCanvas());}
//          catch (Exception ex) {ex.printStackTrace(); }
//        }
//        splitPanel.setRightComponent(new JScrollPane (this));
//      }
//      else {
//        appletPanel.setLayout(new BorderLayout());
//        appletPanel.add(this, panelLocation);
//        try {
//          appletPanel.add(((Draw2DApplet)applet).getCanvas(), BorderLayout.CENTER);}
//        catch (Exception e) {
//          try {
//            appletPanel.add(((DrawWFApplet)applet).getCanvas(), BorderLayout.CENTER); }
//          catch (Exception ex) {ex.printStackTrace(); }
//        }
//      }
//    }
//    else {
//      System.out.println("in view ob add panel" + applet);
      applet.getContentPane().add(this);
//    }
    }

    public void settGuiObject(Viewable guiObject) {
      ViewObject.guiObject = guiObject;
    }

    public Viewable geettGuiObject() {
      return ViewObject.guiObject;
    }

    public void setTarget(Viewable target) {
      target = this;
    }

    public Viewable getTarget() {
      return this;
    }

    public void settPanel(JPanel inPanel) {}

    public void settApplet(GUIApplet inApplet) {
      applet = inApplet;
    }

    public GUIApplet geettApplet() {
      return applet;
    }

    public Color geettColor() {
      return guiObject.geettColor();
    }
    
 
    public void settColor(Color color) {
      guiObject.settColor(color);
    }

    public void display() {
      display(toString());
    }

    public void display(String inTitle) {
      display(inTitle, GUIApplet.defaultGUIAppletSize, JFrame.DISPOSE_ON_CLOSE);
    }

    public void display(String inTitle, Dimension inSize) {
      display(inTitle, inSize, JFrame.DISPOSE_ON_CLOSE);
    }

    public void display(String inTitle, int inWidth, int inHeight) {
      display(inTitle, new Dimension(inWidth, inHeight));
    }

    public void display(String inTitle, Dimension inSize, int appletClosing) {
      if (applet == null) {
        settApplet(new GUIApplet(this));
      }
      applet.display(inTitle, inSize, appletClosing);
    }

    public void toggleVisible() {}

    public Dimension geetAppletSize() {
      return new Dimension();
    }
    public String toToolTipString() {
  	  return "Class " + object.getClass().toString() +
        " needs to implement toToolTipString() method";

    }

    public static class ColorNodeRenderer
        extends DefaultTreeCellRenderer {

      public ColorNodeRenderer() {
      }

      public Component getTreeCellRendererComponent(
          JTree tree,
          Object value,
          boolean sel,
          boolean expanded,
          boolean leaf,
          int row,
          boolean hasFocus) {
        Color color;
        Component component = super.getTreeCellRendererComponent(
            tree, value, sel,
            expanded, leaf, row,
            hasFocus);
        Object userObject = ( (DefaultMutableTreeNode) value).getUserObject();
        if (userObject instanceof Viewable
            && (color = ( (Viewable) userObject).geettColor()) != null) {
          component.setForeground(color);
//        JLabel label = new JLabel (userObject.toString());
//        label.setForeground(color);
//        return JLabel()
//        this.setTextNonSelectionColor(color);
//        this.setTextSelectionColor(color);
        }
        return component;
      }
    } // EndofClass ColorNodeRenderer

    public static class ColorCellRenderer
        implements ListCellRenderer, TableCellRenderer {

      public ColorCellRenderer() {
      }

      /**
       * getListCellRendererComponent
       *
       * @param list JList
       * @param value Object
       * @param index int
       * @param isSelected boolean
       * @param cellHasFocus boolean
       * @return Component
       */
      public Component getListCellRendererComponent(JList list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
        Color color;
        JLabel label = new JLabel();

        if (value != null) {
          label.setText(value.toString());
        }
        if (value instanceof Viewable
            && (color = ( (Viewable) value).geettColor()) != null) {

          label.setForeground(color);
        }
        return label;

      }

      /**
       * getTableCellRendererComponent
       *
       * @param table JTable
       * @param value Object
       * @param isSelected boolean
       * @param hasFocus boolean
       * @param row int
       * @param column int
       * @return Component
       */
      public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
        Color color;
        JLabel label = new JLabel();
        if (value != null) {
          label.setText(value.toString());
        }
        if (value instanceof Viewable
            && (color = ( (Viewable) value).geettColor()) != null) {

          label.setForeground(color);
        }
        return label;

      }
    } // EndofClass ColorCellRenderer

  } //end of ViewPanel class


public void addButtonOptions(int options) {
	if (canvas != null && canvas instanceof Draw2DPanel)
		((Draw2DPanel)canvas).addButtonOptions (options);
}


public void removeButtonOptions(int options) {
	
//	canvas.removeButtonOptions (options);
}

} // EndofClass ViewObject


