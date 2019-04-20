package edu.ohiou.mfgresearch.labimp.basis;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;


import java.awt.print.*;

import java.util.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

/**
 * Title:        Generic classes for manufacturing planning
 * Description:  This project implements general classes for imtelligent manufacturing planning. These are:
 * ImpObject - umbrella class fro all objects
 * MrgPartModel - general part object data
 * Viewable - interface to display objects in applet
 * GUIApplet - applet that utilizes viewable interface
 * Copyright:    Copyright (c) 2001
 * Company:      Ohio University
 * @author Dusan Sormaz
 * @version 1.0
 */

public class Draw2DPanel
extends JPanel
implements Drawable2D, Scalable, Printable {

	public static final AffineTransform DEFAULT_TRANSFORM = new AffineTransform(
			1.0, 0, 0, 1.0, 0, 0);
	public static final double DEFAULT_SCALE = 100; // default scale value.
	public static final double ZOOM_SCALE = 1.25;
	public static final double SHIFT_FACTOR = 0.25;

	public static final double RADIUS = 10; // pixel based 

	public static final String TOOL_TIP_TEXT =
		"Press and drag left button to pan, Press and drag right button to zoom";

	protected Draw2DApplet applet;
	protected Drawable2D target;
	protected Draw2DCanvas drawPanel;
	protected JPanel viewPanel = new JPanel();
	GraphicsToolBar myToolbar = new GraphicsToolBar(this);
	private AffineTransform viewTransform;
	//  private AffineTransform initTransform;
	private double scale;
	public final static Dimension canvasSize = new Dimension(600, 400);
	public final static Dimension scrollPaneSize = new Dimension(600, 400);
	private Rectangle2D boundingBox = null;
	private HashMap<Object, HashSet<Shape>> drawMap = new HashMap<Object, HashSet<Shape>>();
	private HashMap<Color, HashSet<Shape>> fillMap = new HashMap<Color, HashSet<Shape>>();
	private HashMap<Image, AffineTransform> imageMap = new HashMap<Image, AffineTransform>();
	private java.util.List<Shape> drawSet = new LinkedList<Shape>();
	boolean needsUpdate = true;
	private boolean mouseMode;
	private boolean printText = false;
	private boolean paintImages = false;
	private Hashtable<Point2D,Object> targetTable = new Hashtable<Point2D,Object>();

	//  private boolean flip = false;
	private int rotationCounter = 0, signX = 1, signY = -1;

	JLabel scaleLabel = new JLabel();
	JTextField scaleField = new JTextField();
	JLabel viewXformLabel;
	public Draw2DPanel() {
		this(null, null, DEFAULT_TRANSFORM, DEFAULT_SCALE);

	}

	/**
	 * Constructor taking DrawWFApplet.
	 */
	public Draw2DPanel(Drawable2D inTarget) throws IllegalArgumentException {
		this(inTarget, (Draw2DApplet) ((Viewable)inTarget).geettApplet(), DEFAULT_TRANSFORM,
				DEFAULT_SCALE);
	}

	public Draw2DPanel(Drawable2D inTarget, Draw2DApplet inApplet) {
		this(inTarget, inApplet, DEFAULT_TRANSFORM, DEFAULT_SCALE);
	}

	public Draw2DPanel(Drawable2D inTarget, Draw2DApplet inApplet,
			AffineTransform transform, double inScale) {
		target = inTarget;
		applet = inApplet;
		viewTransform = new AffineTransform(transform);
		viewTransform.scale(inScale, -inScale);
		//    viewTransform.rotate(Math.PI);

		//    initTransform = new AffineTransform(viewTransform);
		if (applet != null) {
			applet.setCanvas(this);
		}
		if (target != null) {
			target.settCanvas(this);
			computeBoundingBox();
		}
		scale = inScale;
		init();
		//		scaleFitTarget();
		//		panAllButton_actionPerformed(new ActionEvent(this, 1, "none") );
	}

	public void init() {
		try {
			jbInit();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	//public JPanel getPanel() {
	//return target.getPanel();
	//}

	/** Method to get view transformation matrix.
	 *
	 */
	public AffineTransform getViewTransform() {
		return this.viewTransform;
	}

	//public JPanel makePanel() {
	//if (target != null) {
	//return target.makePanel();
	//}
	//else {
	//return new JPanel();
	//}
	//}

	//public int getPanelLocation() {
	//return target.getPanelLocation();
	//}

	//public int getPanelOrientation() {
	//return target.getPanelOrientation();
	//}
	//dns change viewable
	//public void addPanel() {
	//target.addPanel();
	//}

	/**
	 * @todo change code for return vlaue of this metohd
	 * returns target of given canvas
	 */
	public Drawable2D getTarget() {
		return target;
	}

	//public void setGuiObject(Viewable guiObject) {
	//target.setGuiObject(guiObject);
	//}

	//public Viewable getGuiObject() {
	//return target.getGuiObject();
	//}

	//public Color getColor() {
	//return target.getColor();
	//}

	//public void setColor(Color color) {
	//target.setColor(color);
	//}

	public void setMouseMode(boolean mode) {
		mouseMode = mode;
	}

	public boolean getMouseMode() {
		return mouseMode;
	}

	public void setNeedUpdate(boolean needUpdate) {
		this.needsUpdate = needUpdate;
	}

	public JPanel geettCanvas() {
		return this;
	}

	public Draw2DCanvas getDrawCanvas() {
		return drawPanel;

	}

	public void settCanvas(Draw2DPanel inCanvas) {}

	public void setTarget(Drawable2D inTarget) {
		target = (Drawable2D) inTarget;
		target.settCanvas(this);
		if (inTarget instanceof Viewable)
			applet = (Draw2DApplet) ((Viewable)inTarget).geettApplet();
		computeBoundingBox();

	}

	public void setToolTipText (String text) {
		drawPanel.setToolTipText(text);
	}

	public void resetToolTipText () {
		drawPanel.setToolTipText(TOOL_TIP_TEXT);
	}

	public GUIApplet getApplet() {
		return applet;
	}

	public void setApplet(Draw2DApplet inApplet) {
		applet = inApplet;
	}

	public Dimension getAppletSize() {
		return GUIApplet.defaultGUIAppletSize;
	}

	//public void toggleVisible() {
		//target.toggleVisible();
	//}
	public String toToolTipString() {
		return "Class " + target.getClass().toString() +
		" needs to implement toToolTipString() method";

	}

	public void setPanel(JPanel inPanel) {}

	//  public void paint (Graphics g) {
	//    Graphics2D gd = (Graphics2D) g;
	//    this.paintComponent (gd);
	//    }

	public void repaint() {
		super.repaint();
	}

	private void setDrawList() {

		drawSet = new LinkedList<Shape>();
		if (target != null) {
			makeDrawSets();
			for (Iterator<HashSet<Shape>> itr = drawMap.values().iterator(); itr.hasNext();) {
				Collection<Shape> collection =  itr.next();
				drawSet.addAll(collection);
			}
			for (Iterator<HashSet<Shape>> itr = fillMap.values().iterator(); itr.hasNext();) {
				Collection<Shape> collection = itr.next();
				drawSet.addAll(collection);
			}
		}
		else {
		}
	}

	public LinkedList<Shape> geetDrawList() {
		if (target != null) {
			return target.geetDrawList();
		}
		else {
			return new LinkedList<Shape>();
		}
	}

	public LinkedList<Shape> geetFillList() {
		if (target != null) {
			return target.geetFillList();
		}
		else {
			return new LinkedList<Shape>();
		}
	}

	/** Methods that returns 
	 * 
	 */
	 public void generateImageList() {
		 imageMap.clear();
		 if (target != null && paintImages) {
			 target.generateImageList();
		 }
	 }

	 public void computeBoundingBox() {
		 setDrawList();
		 //  System.out.println("begin boundingbox" + boundingBox);
		 if (!drawSet.isEmpty()) {
			 Iterator itr = drawSet.iterator();
			 Shape firstShape = (Shape) itr.next();
			 //    if (firstShape != null) {
			 boundingBox = firstShape.getBounds2D();
			 while (itr.hasNext()) {
				 Shape shape = (Shape) itr.next();
				 //      System.out.println("boundingbox" + boundingBox);
				 //      System.out.println("shape" + shape);
				 if (shape != null) {
					 Rectangle2D r = shape.getBounds2D();
					 Rectangle2D.union(r, boundingBox, boundingBox);
				 }
			 }
		 }
		 else {
			 boundingBox = new Rectangle2D.Double(0,0,1,1);
		 }
	 }

	 public void createTargetTable() {
		 if (target != null) {
			 targetTable = new Hashtable<Point2D, Object>();
			 Collection targets = target.giveSelectables();
			 for (Iterator itr = targets.iterator(); itr.hasNext();) {
				 Object o = itr.next();
				 if (o instanceof Drawable2D) {
					 Drawable2D selectable = (Drawable2D) o;
					 this.targetTable.put(selectable.geettPosition(), selectable);
				 }
				 else if (o instanceof Point2D) {
					 Point2D point = (Point2D) o;
					 this.targetTable.put(point, point);
				 }
			 }
		 }
	 }

	 public Collection giveSelectables () {
		 return target.giveSelectables();

	 }
	 public GraphicsConfiguration geetGraphicsConfig () {
		 if (target != null)
			 return target.geetGraphicsConfig();
		 else {
			 GraphicsConfiguration gc = new GraphicsConfiguration();
			 gc.configure();
			 return gc;

		 }

	 }


	 public Point2D geettPosition () {
		 return target.geettPosition();
	 }

	 public void settPosition (Point2D point) {

	 }

	 public void makeDrawSets() {
		 drawMap = new HashMap<Object, HashSet<Shape>>();
		 fillMap = new HashMap<Color, HashSet<Shape>>();
		 if (target != null) {
			 target.makeDrawSets();
		 }
	 }


	 public void addDrawShape (Color color, Shape shape) {

		 // if key in map retrive set
		 // add new shape to set
		 if (drawMap.containsKey(color) ) {
			 HashSet<Shape> shapes = (HashSet<Shape>) drawMap.get(color);
			 shapes.add(shape);
		 }
		 else {
			 HashSet<Shape> shapes = new HashSet<Shape> ();
			 shapes.add(shape);
			 drawMap.put(color, shapes);
		 }
	 }

	 public void addDrawShapes (Color color, Collection<Shape> newShapes) {

		 // if key in map retrive set
		 // add new shape to set
		 if (drawMap.containsKey(color) ) {
			 HashSet<Shape> shapes =  drawMap.get(color);
			 shapes.addAll(newShapes);
		 }
		 else {
			 HashSet<Shape> shapes = new HashSet<Shape> (newShapes);
			 drawMap.put(color, shapes);
		 }
	 }

	 public void addDrawShapes (GraphicsConfiguration config, Collection<Shape> newShapes) {

		 // if key in map retrive set
		 // add new shape to set
		 if (drawMap.containsKey(config) ) {
			 HashSet<Shape> shapes =  drawMap.get(config);
			 shapes.addAll(newShapes);
		 }
		 else {
			 HashSet<Shape> shapes = new HashSet<Shape> (newShapes);
			 drawMap.put(config, shapes);
		 }
	 }



	 public void addFillShape (Color color, Shape shape) {

		 // if key in map retrive set
		 // add new shape to set
		 if (fillMap.containsKey(color) ) {
			 HashSet<Shape> shapes = (HashSet<Shape>) fillMap.get(color);
			 shapes.add(shape);
		 }
		 else {
			 HashSet<Shape> shapes = new HashSet<Shape> ();
			 shapes.add(shape);
			 fillMap.put(color, shapes);
		 }
	 }
	 /**
	  * This method adds image to the image map for painting. Image should be
	  *  scaled for the target's space and supplied transform should refer to 
	  *  transfom in the target's coordinates.
	  *  
	  * @param image - the image to be painted
	  * @param transform -  transform of the image in target's world space
	  */ 
	 public void addImage (Image image, AffineTransform transform) {
		 imageMap.put(image, transform); 
	 }

	 public void addFillShapes (Color color, Collection<Shape> newShapes) {

		 // if key in map retrive set
		 // add new shape to set
		 if (fillMap.containsKey(color) ) {
			 HashSet<Shape> shapes =  fillMap.get(color);
			 shapes.addAll(newShapes);
		 }
		 else {
			 HashSet<Shape> shapes = new HashSet<Shape> (newShapes);
			 fillMap.put(color, shapes);
		 }
	 }


	 public void setDisplayStrings (boolean printText) {
		 if (printText != stringBox.isSelected()) {
			 stringBox.setSelected(printText);
		 }

	 }

	 public LinkedList<DrawString> geetStringList() {
		 if (target != null && printText) {
			 return target.geetStringList();
		 }
		 else {
			 return new LinkedList<DrawString>();
		 }
	 }

	 public void addMouseMotionListener(MouseMotionListener listener) {
		 drawPanel.addMouseMotionListener(listener);
	 }

	 public void paintComponent(Graphics2D g) {
		 paintComponent( (Graphics) g);
	 }

	 public void paintComponent(Graphics g) {
		 super.paintComponent(g);
	 }


	 /** display method taking title, width and height details of applet.
	  *
	  */
	 public void display(String inTitle, int inWidth, int inHeight) {
		 if (applet != null) applet.display(inTitle, inWidth, inHeight);
	 }

	 /** display method taking title and dimension(w,h) details of applet.
	  *
	  */
	 public void display(String inTitle, Dimension inSize) {
		 if (applet != null)  applet.display(inTitle, inSize);
	 }
	 //dns change viewable
	 public void display(String inTitle, Dimension inSize, int appletClosing) {
		 if (applet != null)    applet.display(inTitle, inSize, appletClosing);
	 }

	 /** display method taking title of applet.
	  *
	  */
	 public void display(String inTitle) {
		 if (applet != null)    applet.display(inTitle);
	 }

	 /** display method taking no arguments.
	  *
	  */
	 public void display() {
		 if (applet != null) {
			 applet.display();
		 }
	 }

	 JCheckBox xMirrorBox;
	 JCheckBox yMirrorBox;
	 JCheckBox stringBox;
	 JCheckBox imageBox;
	 private void jbInit() throws Exception {
		 drawPanel = new Draw2DCanvas();
		 drawPanel.setBackground(Color.white);
		 //    drawPanel.setPreferredSize(new Dimension(600, 400));
		 drawPanel.setPreferredSize(this.getSize());
		 JScrollPane scrollPanel = new JScrollPane(drawPanel);

		 scrollPanel.setPreferredSize(scrollPaneSize);
		 this.setLayout(new BorderLayout());
		 this.setBorder(BorderFactory.createLineBorder(Color.black));
		 //    this.add(scrollPanel, BorderLayout.CENTER);
		 this.add(drawPanel, BorderLayout.CENTER);
//		 add(viewPanel, BorderLayout.SOUTH);
		 myToolbar.addButtonOptions(GraphicsToolBar.ALL);
		 myToolbar.setFloatable(true);
		 add(myToolbar, BorderLayout.SOUTH);
		 // viewPanel size
		 viewPanel.setBackground(Color.pink);
		 viewPanel.setLayout(new BorderLayout());
		 viewPanel.setToolTipText("Enter view point and scale here.");
		 scaleLabel.setText("Scale:");
		 scaleField.setHorizontalAlignment(SwingConstants.RIGHT);
		 scaleField.setText("" + scale);
		 scaleField.setPreferredSize(new Dimension(70, 21));
		 scaleField.setMinimumSize(new Dimension(70, 21));
		 scaleField.setMaximumSize(new Dimension(70, 21));

		 JPanel scalePanel = new JPanel();
		 scalePanel.setLayout(new BorderLayout());
		 scalePanel.add(scaleLabel, BorderLayout.WEST);
		 scalePanel.add(scaleField, BorderLayout.CENTER);
		 //    viewPanel.add(scalePanel, BorderLayout.SOUTH);

		 stringBox = new JCheckBox("Paint strings", printText);
		 stringBox.setBackground(Color.pink);
		 stringBox.addItemListener(new StringBoxAdapter());

		 imageBox = new JCheckBox("Paint images", paintImages);
		 imageBox.setBackground(Color.pink);
		 imageBox.addItemListener(new ImageBoxAdapter());

		 xMirrorBox = new JCheckBox();
		 xMirrorBox.setText("Mirror around X");
		 xMirrorBox.setBackground(Color.pink);
		 xMirrorBox.addItemListener(new MirrorXBoxAdapter());
		 yMirrorBox = new JCheckBox();
		 viewXformLabel = new JLabel(viewTransform.toString());
		 viewXformLabel = new JLabel("" + printText);
		 yMirrorBox.setText("Mirror around Y");
		 yMirrorBox.setBackground(Color.pink);
		 yMirrorBox.addItemListener(new MirrorYBoxAdapter());
		 JPanel checkPanel = new JPanel();
		 checkPanel.setLayout(new GridLayout(1, 4));
		 checkPanel.add(xMirrorBox);
		 checkPanel.add(yMirrorBox);
		 checkPanel.add(viewXformLabel);
		 checkPanel.add(stringBox);
		 checkPanel.add(imageBox);
		 viewPanel.add(checkPanel, BorderLayout.NORTH);
		 viewPanel.add(viewXformLabel, BorderLayout.CENTER);
/*		 JButton zoomInButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/iconZoomIn.gif")));																																			
		 JButton zoomOutButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/iconZoomOut.gif")));
		 JButton panLeftButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/iconArrowLeft.gif")));
		 JButton panRightButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/iconArrowRight.gif")));
		 JButton panUpButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/iconArrowUp.gif")));
		 JButton panDownButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/iconArrowDown.gif")));
		 JButton panAllButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/iconViewAll.gif")));
		 JButton rotateCWButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/iconRotateCW.gif")));
		 JButton rotateCCWButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/iconRotateCCW.gif")));
		 JButton saveButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/Save24.gif")));
		 JButton printButton = new JButton(new ImageIcon
				 (this.getClass().getResource(
						 "/META-INF/Print24.gif")));
		 //    new ImageIcon
		 //                            (this.getClass().getResource("/META-INF/iconArrowDown.gif")));

		 zoomInButton.setToolTipText("Zoom In");
		 zoomOutButton.setToolTipText("Zoom Out");
		 panLeftButton.setToolTipText("Move to Left");
		 panRightButton.setToolTipText("Move to Right");
		 panUpButton.setToolTipText("Move Up");
		 panDownButton.setToolTipText("Move Down");
		 panAllButton.setToolTipText("Zoom All");
		 rotateCWButton.setToolTipText("Rotate clockwise");
		 rotateCCWButton.setToolTipText("Rotate counterclockwise");
		 saveButton.setToolTipText("Save 2D canvas as JPEG file");
		 printButton.setToolTipText("Print 2D canvas content");

		 zoomInButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 zoomInButton_actionPerformed(e);
			 }
		 });
		 zoomOutButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 zoomOutButton_actionPerformed(e);
			 }
		 });
		 panLeftButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 panLeftButton_actionPerformed(e);
			 }
		 });
		 panRightButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 panRightButton_actionPerformed(e);
			 }
		 });
		 panUpButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 panUpButton_actionPerformed(e);
			 }
		 });
		 panDownButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 panDownButton_actionPerformed(e);
			 }
		 });
		 panAllButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 panAllButton_actionPerformed(e);
			 }
		 });
		 rotateCWButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 rotateCWButton_actionPerformed(e);
			 }
		 });
		 rotateCCWButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 rotateCCWButton_actionPerformed(e);
			 }
		 });
		 saveButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 saveButton_actionPerformed(e);
			 }
		 });
		 printButton.addActionListener(new java.awt.event.ActionListener() {
			 public void actionPerformed(ActionEvent e) {
				 printButton_actionPerformed(e);
			 }
		 });
*/

		 revalidate();
		 repaint();
	 }

	 public class Draw2DCanvas
	 extends JPanel implements Printable {

		 protected Draw2DCanvas() {
			 setToolTipText(TOOL_TIP_TEXT);
			 Draw2DCanvasMouseAdapter mouseAdapter = new Draw2DCanvasMouseAdapter(this);
			 this.addMouseMotionListener(mouseAdapter);
			 this.addMouseListener(mouseAdapter);
		 }

		 protected void paintComponent(Graphics g) {
			 g.clearRect(0, 0, this.getSize().width, this.getSize().height);
			 Graphics2D gd = (Graphics2D) g;
			 paintComponent(gd);
		 }

		 /** paintComponent method for drawing wireframes.
		  *  (taking graphics2D context)
		  */
		 protected void paintComponent(Graphics2D g) {
			 //    System.out.println("target" + target);
			 makeDrawSets();
			 createTargetTable();
			 AffineTransform finalTransform = new AffineTransform(viewTransform);
			 finalTransform.preConcatenate(AffineTransform.getTranslateInstance(this.
					 getWidth() / 2, this.getHeight() / 2));
			 if (paintImages) {
				 generateImageList();
				 Collection images = imageMap.keySet();
				 System.out.println("image list size: " + images.size());
				 for (Iterator itr = images.iterator(); itr.hasNext();) {
					 Image image = (Image)itr.next(); //
					 AffineTransform xform = 
						 new AffineTransform ((AffineTransform)imageMap.get(image));
					 xform.preConcatenate(finalTransform);
					 //          boolean done = g.drawImage(image, new AffineTransform(), null);
					 //          System.out.println("image done is before icon: " + done);
					 ImageIcon icon =new ImageIcon(image);
					 //          icon.paintIcon(this, g, 0, 0);
					 boolean done = g.drawImage(icon.getImage(), xform, null);
					 System.out.println("image done is: " + done);
				 }
				 //        makeIcon(g);

			 }
			 Collection colors = fillMap.keySet();
			 for (Iterator colorItr = colors.iterator(); colorItr.hasNext(); ) {
				 Color currentColor = (Color) colorItr.next();
				 HashSet shapeSet = (HashSet) fillMap.get(currentColor);
				 if (shapeSet != null) {
					 for (Iterator shapeItr = shapeSet.iterator(); shapeItr.hasNext(); ) {
						 Shape currentShape = (Shape) shapeItr.next();
						 Shape fillShape = finalTransform.createTransformedShape(
								 currentShape);
						 //          Rectangle rect = shape.getBounds();
						 if (fillShape.intersects(0, 0, getSize().width, getSize().height)) {
							 g.setColor(currentColor );
							 g.fill(fillShape);
						 }
					 }
				 }

			 }
			 g.setStroke(geetGraphicsConfig().makeStroke());
			 colors = drawMap.keySet();
			 for (Iterator colorItr = colors.iterator(); colorItr.hasNext();) {
				 Object o = colorItr.next();
				 if (o instanceof Color) {
					 g.setColor((Color) o);
					 g.setStroke(geetGraphicsConfig().makeStroke());
				 }
				 if (o instanceof GraphicsConfiguration) {
					 GraphicsConfiguration gc = (GraphicsConfiguration) o;
					 g.setColor (gc.makeColor());
					 g.setStroke(gc.makeStroke());
				 }
				 HashSet shapeSet = (HashSet) drawMap.get(o);
				 for (Iterator shapeItr = shapeSet.iterator(); shapeItr.hasNext();) {
					 Shape currentShape = (Shape) shapeItr.next();
					 Shape drawShape = finalTransform.createTransformedShape(currentShape);
					 if (drawShape.intersects(0, 0, getSize().width, getSize().height)) {
						 g.draw(drawShape);
					 }
				 }
			 }
			 LinkedList stringList = geetStringList();
			 Font f = g.getFont();
			 int size = f.getSize();
			 //    if (applet != null) {
				 //    try {
					 //    size = Integer.parseInt(applet.
			 //    getParameter("FONT_SIZE", Integer.toString(f.getSize())));
			 //    } catch (NumberFormatException e) {
			 //    size = f.getSize();

			 //    }

			 //    }

			 Font newf = new Font (f.getName(), f.getStyle(), size);
			 g.setFont(newf);
			 for (Iterator itr = stringList.iterator(); itr.hasNext(); ) {
				 ( (DrawString) itr.next()).drawContent(g, finalTransform);
			 }
			 //    paintScale(g, 1.0);
		 }
		 public void makeIcon (Graphics2D g) {
			 AffineTransform transform =AffineTransform.getScaleInstance(1, -1);
			 transform.translate (-4,-4);
			 Image image =  Toolkit.getDefaultToolkit().
			 createImage(ViewObject.class.getResource("ViewObject.gif")).getScaledInstance(-1, 100, 0);
			 //  Image image = new Image (
			 //      ViewObject.class.getResource("branka-AHS-junior.jpg")).getScaledInstance(-1, 20, 0);
			 ImageIcon icon = new ImageIcon(image);
			 boolean done = g.drawImage(image,new AffineTransform(), null);
			 System.out.println("in ameke image " + done);
			 //  icon.paintIcon(this, g, 5, 5);


		 }
		 //  int in, count = 0;

		 private void paintScale(Graphics2D g, double size) {
			 //      System.out.println("in piant scale");
			 g.setColor(Color.blue);
			 int pos = this.getSize().height - 10;
			 //      System.out.println("before canvas scale");
			 double canvasScale = Math.sqrt(Math.abs(viewTransform.getDeterminant()));
			 //      System.out.println("canvas scale" + canvasScale);
			 int paintSize = (int) Math.floor(size * canvasScale);
			 if (paintSize >= this.getSize().width) {
				 paintScale(g, size / 10);
			 }
			 else if (paintSize <= 20) {
				 paintScale(g, size * 10.);
			 }
			 else {
				 g.draw(new Line2D.Double(10, pos, paintSize + 10, pos));
				 g.drawString("" + size + " in", 10, pos - 2);
			 }
		 }

		 // Printable interface

		 public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {

			 //    There is only one page so this check is ok
			 if (pi >= 1) {
				 return Printable.NO_SUCH_PAGE;
			 }

			 Graphics2D g2 = (Graphics2D) g;
			 g2.translate(pf.getImageableX(), pf.getImageableY());
			 Font f = new Font("Courier", Font.PLAIN, 12);
			 g2.setFont (f);
			 paint(g2);
			 return Printable.PAGE_EXISTS;
		 }

	 } // EndOFClass Draw2DCanvas

	 /**
	  * Mouse ADapter class for mouse events on 2D Canvas
	  */
	 class Draw2DCanvasMouseAdapter
	 extends MouseInputAdapter {
		 Draw2DCanvas adaptee;
		 Rectangle box;
		 Object selectedObject;
		 AffineTransform xForm; 
		 protected int oldX, oldY;

		 Draw2DCanvasMouseAdapter(Draw2DCanvas adaptee) {
			 this.adaptee = adaptee;
		 }

		 public void mouseMoved(MouseEvent e) {
			 Point2D.Double mousePoint = new Point2D.Double(e.getX(), e.getY());
			 Point2D.Double newPoint = new Point2D.Double(Double.MAX_VALUE, Double.MAX_VALUE);
			 Point2D temp = null;
			 try {
				 xForm = new AffineTransform(getViewTransform());

				 xForm.preConcatenate(AffineTransform.getTranslateInstance(
						 drawPanel.getWidth() / 2,
						 drawPanel.getHeight() / 2));

				 xForm.inverseTransform(mousePoint, newPoint);
				 TreeSet<Point2D> ts = new TreeSet<Point2D> (new Point2DComparator(newPoint));
				 ts.addAll(targetTable.keySet());
				 temp =(Point2D) ts.first();
			 } 
			 catch (Exception ex) {
				 //      exception may happen if target table is null, or treeset is empty or 
				 //      there is other problem, in all cases keep silent
				 //      ex.printStackTrace();
			 }    
			 double targetRadius = RADIUS / viewTransform.getScaleX();

			 if (temp != null && newPoint.distance(temp) < targetRadius) {
				 drawPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
				 setMouseMode(Drawable2D.MODIFY_TARGET);
				 selectedObject =  targetTable.get(temp);
				 if (selectedObject instanceof Viewable) {
					 setToolTipText(((Viewable) selectedObject).toToolTipString());
				 } else if (selectedObject instanceof Drawable2D) {
					 setToolTipText(((Drawable2D) selectedObject).toToolTipString());
				 }
				 else {
					 setToolTipText(selectedObject.toString());
				 }
			 } else {
				 drawPanel.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				 setMouseMode(Drawable2D.MODIFY_VIEW);
				 resetToolTipText();
				 temp = null;
			 }
		 }


		 public void mouseReleased(MouseEvent e) {
			 if (SwingUtilities.isRightMouseButton(e)) {
				 AffineTransform canvasTransform =
					 AffineTransform.getTranslateInstance( -adaptee.getWidth() / 2,
							 -adaptee.getHeight() / 2);
				 Shape newBox = canvasTransform.createTransformedShape(box);
				 try {
					 AffineTransform inverseTransform = viewTransform.createInverse();
					 viewTransform.translate(boundingBox.getX() +
							 boundingBox.getWidth() / 2,
							 boundingBox.getY() +
							 boundingBox.getHeight() / 2);
					 boundingBox = inverseTransform.
					 createTransformedShape(newBox).getBounds2D();
					 double xScale = drawPanel.getSize().width / boundingBox.getWidth();
					 double yScale = drawPanel.getSize().height / boundingBox.getHeight();
					 double newScale = Math.min(xScale, yScale);
					 double oldScale = Math.sqrt(Math.abs(viewTransform.getDeterminant()));
					 //	  viewTransform = new AffineTransform();
					 viewTransform.scale(signX / oldScale, signY / oldScale);
					 viewTransform.translate( -boundingBox.getX() -
							 boundingBox.getWidth() / 2,
							 -boundingBox.getY() -
							 boundingBox.getHeight() / 2);
					 viewTransform.scale(signX * newScale, signY * newScale);
					 viewTransform.translate( -boundingBox.getX() -
							 boundingBox.getWidth() / 2,
							 -boundingBox.getY() -
							 boundingBox.getHeight() / 2);
					 viewXformLabel.setText(viewTransform.toString());
					 repaint();
				 }
				 catch (Exception ex) {
				 }
			 }
		 }

		 public void mouseDragged(MouseEvent e) {
			 if (mouseMode == Drawable2D.MODIFY_VIEW) {
				 if (SwingUtilities.isLeftMouseButton(e) && boundingBox != null) {
					 viewTransform.translate(boundingBox.getX() +
							 boundingBox.getWidth() / 2,
							 boundingBox.getY() +
							 boundingBox.getHeight() / 2);
					 double scale = Math.sqrt(Math.abs(viewTransform.getDeterminant()));
					 viewTransform.rotate( -rotationCounter * Math.PI / 2.);
					 viewTransform.translate( (e.getX() - oldX) / (scale * signX),
							 (e.getY() - oldY) / (scale * signY));
					 viewTransform.rotate(rotationCounter * Math.PI / 2.);
					 viewTransform.translate( -boundingBox.getX() -
							 boundingBox.getWidth() / 2,
							 -boundingBox.getY() -
							 boundingBox.getHeight() / 2);
					 viewXformLabel.setText(viewTransform.toString());
					 repaint();
					 oldX = e.getX();
					 oldY = e.getY();
				 }
				 else {
					 if (oldY==e.getY()) {
						 drawPanel.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
					 } else if (oldX==e.getX()) {
						 drawPanel.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
					 } else if ((oldX>e.getX() && oldY>e.getY()) ||
							 (oldX<e.getX() && oldY<e.getY())) {
						 drawPanel.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
					 } else if ((oldX>e.getX() && oldY<e.getY()) ||
							 (oldX<e.getX() && oldY>e.getY())) {
						 drawPanel.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
					 }
					 box = new Rectangle(Math.min(oldX, e.getX()),
							 Math.min(oldY, e.getY()),
							 Math.abs(oldX - e.getX()),
							 Math.abs(oldY - e.getY()));
					 ( (Graphics2D) adaptee.getGraphics()).draw(box);
					 repaint();
				 }
			 }
			 else {
				 //modify selected target object
				 Point2D.Double mousePoint = new Point2D.Double(
						 e.getX(),
						 e.getY());
				 try {
					 xForm.inverseTransform(mousePoint, mousePoint);
					 if (selectedObject instanceof Drawable2D) {
						 ((Drawable2D) selectedObject).settPosition(mousePoint);
					 }
					 else if (selectedObject instanceof Point2D) {
						 Point2D point = (Point2D) selectedObject;
						 point.setLocation(mousePoint.x, mousePoint.y);
					 }
				 } catch (NoninvertibleTransformException e1) {
					 System.out.println ("non-invertable xform should never happen");
				 }
			 }
			 repaint();
		 }

		 public void mouseClicked(MouseEvent e) {
			 //viewPoint = new Point3d ( (double) (e.getX() - oldX), (double) (e. getY() - oldY), 0.0);
			 repaint();
		 }

		 /** Mouse pressed event.
		  *
		  */
		 public void mousePressed(MouseEvent e) {
			 oldX = e.getX();
			 oldY = e.getY();
			 if (mouseMode == Drawable2D.MODIFY_VIEW)  {
				 if (SwingUtilities.isLeftMouseButton(e)) {
					 drawPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
				 }
				 else {
					 drawPanel.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
				 }
			 }
		 }

	 }

	 void zoomInButton_actionPerformed(ActionEvent e) {
		 if (boundingBox != null) {
			 viewTransform.translate(boundingBox.getX() + boundingBox.getWidth() / 2,
					 boundingBox.getY() + boundingBox.getHeight() / 2);
			 viewTransform.scale(ZOOM_SCALE, ZOOM_SCALE);
			 viewTransform.translate( -boundingBox.getX() - boundingBox.getWidth() / 2,
					 -boundingBox.getY() - boundingBox.getHeight() / 2);

			 scaleField.setText("" + viewTransform.getScaleX() + "," +
					 viewTransform.getScaleY());
			 viewXformLabel.setText(viewTransform.toString());

		 }
		 repaint();
	 }

	 void zoomOutButton_actionPerformed(ActionEvent e) {
		 if (boundingBox != null) {
			 viewTransform.translate(boundingBox.getX() + boundingBox.getWidth() / 2,
					 boundingBox.getY() + boundingBox.getHeight() / 2);
			 viewTransform.scale(1 / ZOOM_SCALE, 1 / ZOOM_SCALE);
			 viewTransform.translate( -boundingBox.getX() - boundingBox.getWidth() / 2,
					 -boundingBox.getY() - boundingBox.getHeight() / 2);
			 scaleField.setText("" + viewTransform.getScaleX() + "," +
					 viewTransform.getScaleY());
			 viewXformLabel.setText(viewTransform.toString());
		 }
		 repaint();
	 }

	 void panLeftButton_actionPerformed(ActionEvent e) {
		 if (boundingBox != null) {
			 viewTransform.translate( -boundingBox.getWidth() * SHIFT_FACTOR, 0);
			 viewXformLabel.setText(viewTransform.toString());
		 }
		 repaint();
	 }

	 void panRightButton_actionPerformed(ActionEvent e) {
		 if (boundingBox != null) {
			 viewTransform.translate(boundingBox.getWidth() * SHIFT_FACTOR, 0);
			 viewXformLabel.setText(viewTransform.toString());
		 }
		 repaint();
	 }

	 void panUpButton_actionPerformed(ActionEvent e) {
		 if (boundingBox != null) {
			 viewTransform.translate(0, -boundingBox.getHeight() * SHIFT_FACTOR);
			 viewXformLabel.setText(viewTransform.toString());
		 }
		 repaint();
	 }

	 void panDownButton_actionPerformed(ActionEvent e) {
		 if (boundingBox != null) {
			 viewTransform.translate(0, boundingBox.getHeight() * SHIFT_FACTOR);
			 viewXformLabel.setText(viewTransform.toString());
		 }
		 repaint();
	 }

	 void rotateCWButton_actionPerformed(ActionEvent e) {
		 if (boundingBox != null) {
			 viewTransform.translate(boundingBox.getX() + boundingBox.getWidth() / 2,
					 boundingBox.getY() + boundingBox.getHeight() / 2);
			 viewTransform.rotate( -Math.PI / 2.);
			 viewTransform.translate( -boundingBox.getX() - boundingBox.getWidth() / 2,
					 -boundingBox.getY() - boundingBox.getHeight() / 2);
			 viewXformLabel.setText(viewTransform.toString());
		 }
		 //    flip = !flip;
		 rotationCounter--;
		 rotationCounter = rotationCounter % 4;
		 repaint();
	 }

	 void rotateCCWButton_actionPerformed(ActionEvent e) {
		 if (boundingBox != null) {
			 System.out.println("before" + viewTransform);
			 viewTransform.translate(boundingBox.getX() + boundingBox.getWidth() / 2,
					 boundingBox.getY() + boundingBox.getHeight() / 2);
			 System.out.println("trans" + viewTransform);
			 viewTransform.rotate(Math.PI / 2.);
			 System.out.println("rot" + viewTransform);
			 viewTransform.translate( -boundingBox.getX() - boundingBox.getWidth() / 2,
					 -boundingBox.getY() - boundingBox.getHeight() / 2);
			 System.out.println("trans back" + viewTransform);
			 viewXformLabel.setText(viewTransform.toString());
		 }
		 //    flip = !flip;
		 rotationCounter++;
		 rotationCounter = rotationCounter % 4;
		 repaint();
	 }

	 void saveButton_actionPerformed(ActionEvent e) {
		 try {
			 int w = drawPanel.getWidth(), h = drawPanel.getHeight();
			 BufferedImage image = new BufferedImage(w, h,
					 BufferedImage.TYPE_3BYTE_BGR);
			 Graphics2D g2 = image.createGraphics();
			 g2.setBackground(Color.white);
			 drawPanel.paint(g2);
			 g2.dispose();
			 JFileChooser fileChooser = new JFileChooser(".");
			 int response = fileChooser.showOpenDialog(this);
			 if (response == JFileChooser.APPROVE_OPTION) {
				 File file = fileChooser.getSelectedFile();
				 ImageIO.write(image, "jpeg", file);
			 }
		 } catch (IOException ex) {
			 System.err.println(ex);
		 }
	 }

	 void printButton_actionPerformed(ActionEvent e) {
		 PrinterJob printJob = PrinterJob.getPrinterJob();
		 printJob.setPrintable(drawPanel);

		 //  Page dialog
		 printJob.pageDialog(printJob.defaultPage());
		 //  Print dialog
		 if(printJob.printDialog()){
			 try { printJob.print(); } catch (Exception PrintException) { }
		 }
		 //  No dialogs
		 //  try { printJob.print(); } catch (Exception PrintException) { }
	 }


	 public void scaleFitTarget() {
		 if (boundingBox != null) {
			 double oldXScale = viewTransform.getScaleX();
			 double oldYScale = viewTransform.getScaleY();


			 double xScale = drawPanel.getSize().width / boundingBox.getWidth();
			 double yScale = drawPanel.getSize().height / boundingBox.getHeight();
			 double scale = Math.min(xScale, yScale);
			 viewTransform = new AffineTransform();
			 viewTransform.scale(scale, -scale);
			 if (oldXScale < 0) {
				 viewTransform.scale( -1, 1);
			 }
			 if (oldYScale > 0) {
				 viewTransform.scale(1, -1);
			 }
			 viewTransform.translate( -boundingBox.getX() - boundingBox.getWidth() / 2,
					 -boundingBox.getY() - boundingBox.getHeight() / 2);
			 viewXformLabel.setText(viewTransform.toString());
		 }

	 }

	 void panAllButton_actionPerformed(ActionEvent e) {
		 computeBoundingBox();
		 scaleFitTarget();
		 xMirrorBox.setSelected(false);
		 yMirrorBox.setSelected(false);
		 //    System.out.println("view xform" + viewTransform);
		 //    System.out.println("boundingbox" + boundingBox);
		 repaint();
		 rotationCounter = 0;
		 signX = 1;
		 signY = -1;
		 //    flip = false;

	 }

	 class StringBoxAdapter
	 implements ItemListener {
		 public void itemStateChanged(ItemEvent e) {
			 printText = !printText;
			 viewXformLabel.setText("" + printText);
			 repaint();
		 }
	 }

	 class ImageBoxAdapter
	 implements ItemListener {
		 public void itemStateChanged(ItemEvent e) {
			 paintImages = !paintImages;
			 repaint();
		 }
	 }

	 class MirrorXBoxAdapter
	 implements ItemListener {
		 public void itemStateChanged(ItemEvent e) {
			 if (boundingBox != null) {
				 viewTransform.translate(boundingBox.getX() + boundingBox.getWidth() / 2,
						 boundingBox.getY() +
						 boundingBox.getHeight() / 2);
				 signY = -1 * signY;
				 viewTransform.scale(1, -1);
				 viewTransform.translate( -boundingBox.getX() -
						 boundingBox.getWidth() / 2,
						 -boundingBox.getY() -
						 boundingBox.getHeight() / 2);
				 scaleField.setText("" + viewTransform.getScaleX() + "," +
						 viewTransform.getScaleY());
				 viewXformLabel.setText(viewTransform.toString());

			 }
			 repaint();
		 }
	 }

	 class MirrorYBoxAdapter
	 implements ItemListener {
		 public void itemStateChanged(ItemEvent e) {
			 if (boundingBox != null) {
				 viewTransform.translate(boundingBox.getX() + boundingBox.getWidth() / 2,
						 boundingBox.getY() +
						 boundingBox.getHeight() / 2);
				 signX = -1 * signX;
				 viewTransform.scale( -1, 1);
				 viewTransform.translate( -boundingBox.getX() -
						 boundingBox.getWidth() / 2,
						 -boundingBox.getY() -
						 boundingBox.getHeight() / 2);
				 System.out.println("scale x : " + viewTransform.getScaleX());
				 System.out.println("scale y : " + viewTransform.getScaleY());
				 scaleField.setText("" + viewTransform.getScaleX() + "," +
						 viewTransform.getScaleY());
				 viewXformLabel.setText(viewTransform.toString());

			 }
			 repaint();
		 }
	 }


	 public AffineTransform getTransform() {
		 return viewTransform;
	 }


	 public Rectangle2D geetBoundigBox() {

		 return boundingBox;
	 }

	 public void addButtonOptions(int options) {

		 myToolbar.addButtonOptions (options);
	 }


	 public void removeButtonOptions(int options) {

		 myToolbar.removeButtonOptions (options);
	 }


	 public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
	 throws PrinterException {
		 drawPanel.print(graphics, pageFormat, pageIndex);
		 return 0;
	 }

	 public void scale(double sx, double sy) {
		 if (boundingBox != null) {
			 viewTransform.translate(boundingBox.getX() + boundingBox.getWidth() / 2,
					 boundingBox.getY() + boundingBox.getHeight() / 2);
			 viewTransform.scale(sx, sy);
			 viewTransform.translate( -boundingBox.getX() - boundingBox.getWidth() / 2,
					 -boundingBox.getY() - boundingBox.getHeight() / 2);

			 scaleField.setText("" + viewTransform.getScaleX() + "," +
					 viewTransform.getScaleY());
			 viewXformLabel.setText(viewTransform.toString());

		 }
		 repaint();

	 }

	 public void translate(double tx, double ty) {
		 if (boundingBox != null) {
			 viewTransform.translate(boundingBox.getWidth() * tx, boundingBox.getWidth() *ty);
			 viewXformLabel.setText(viewTransform.toString());
		 }
		 repaint();	
	 }

	 public void rotate(double angle) {
		 if (boundingBox != null) {
			 viewTransform.translate(boundingBox.getX() + boundingBox.getWidth() / 2,
					 boundingBox.getY() + boundingBox.getHeight() / 2);
			 viewTransform.rotate(angle);
			 viewTransform.translate( -boundingBox.getX() - boundingBox.getWidth() / 2,
					 -boundingBox.getY() - boundingBox.getHeight() / 2);
			 viewXformLabel.setText(viewTransform.toString());
		 }
		 //    flip = !flip;
		 rotationCounter--;
		 rotationCounter = rotationCounter % 4;
		 repaint();
	 }

	 public void togglePaintStrings() {
		 printText = !printText;
		 repaint();
	 }

	 public void togglePaintImages() {
		 paintImages = !paintImages;
		 repaint();

	 }

}