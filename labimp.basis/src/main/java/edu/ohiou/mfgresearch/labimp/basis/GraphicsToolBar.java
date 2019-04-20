package edu.ohiou.mfgresearch.labimp.basis;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

public class GraphicsToolBar extends JToolBar {
	public static int NONE = 0;
	
	public static int ZOOM_IN = 1;
	public static int ZOOM_OUT = 2;
	public static int ZOOM_ALL = 4;
	public static int PAN_LEFT = 8;
	public static int PAN_RIGHT = 16;
	public static int PAN_UP = 32;
	public static int PAN_DOWN = 64;
	public static int ROTATE_CW = 128;
	public static int ROTATE_CCW = 256;	
	public static int MIRROR_VERTICAL = 512;
	public static int MIRROR_HORIZONTAL = 1024;
	public static int PRINT = 2048;
	public static int SAVE = 4096;
	public static int PAINT_STRINGS = 8192;
	public static int PAINT_IMAGES = 16384;
	public static int ALL = PAINT_IMAGES + PAINT_STRINGS +SAVE + PRINT + 
							MIRROR_HORIZONTAL + MIRROR_VERTICAL + ROTATE_CCW + 
							ROTATE_CW + PAN_DOWN + PAN_UP + PAN_RIGHT + 
							PAN_LEFT + ZOOM_ALL + ZOOM_OUT + ZOOM_IN;
	int buttonOptions = ALL;

    JButton zoomInButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/iconZoomIn.gif")));
        JButton zoomOutButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/iconZoomOut.gif")));
        JButton panLeftButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/iconArrowLeft.gif")));
        JButton panRightButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/iconArrowRight.gif")));
        JButton panUpButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/iconArrowUp.gif")));
        JButton panDownButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/iconArrowDown.gif")));
        JButton panAllButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/iconViewAll.gif")));
        JButton rotateCWButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/iconRotateCW.gif")));
        JButton rotateCCWButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/iconRotateCCW.gif")));
        JButton mirrorVerticalButton = new JButton(new ImageIcon
                (this.getClass().getResource(
                "/META-INF/icons/shape_flip_vertical.png")));
        JButton mirrorHorizontalButton = new JButton(new ImageIcon
                (this.getClass().getResource(
                "/META-INF/icons/shape_flip_horizontal.png")));     
        JButton saveButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/Save24.gif")));
        JButton printButton = new JButton(new ImageIcon
            (this.getClass().getResource(
            "/META-INF/icons/Print24.gif")));
        JToggleButton paintStringsButton = new JToggleButton(new ImageIcon
                (this.getClass().getResource(
                "/META-INF/icons/text.png")));
        JToggleButton paintImagesButton = new JToggleButton(new ImageIcon
                (this.getClass().getResource(
                "/META-INF/icons/picture_add.png")));



  public static final double DEFAULT_SCALE = 100; // default scale value.
  public static final double ZOOM_SCALE = 1.25;
  public static final double SHIFT_FACTOR = 0.25;

  private int rotationCounter = 0, signX = 1, signY = -1;
//  private AffineTransform viewTransform;
//private AffineTransform initTransform;
  private double scale;
//public final static Dimension canvasSize = new Dimension(600, 400);
//public final static Dimension scrollPaneSize = new Dimension(600, 400);
//  private Rectangle2D boundingBox = new Rectangle2D.Double(0,0,300,300);

  Scalable user;
  JLabel viewXformLabel = new JLabel("label");
  JTextField scaleField = new JTextField("text");

  public GraphicsToolBar(Scalable user) {
    // TODO Auto-generated constructor stub
    this(user, "toolbar", HORIZONTAL);
  }

  public GraphicsToolBar(Scalable user, int arg0) {
    this(user, "toolbar", arg0);
    // TODO Auto-generated constructor stub
  }

  public GraphicsToolBar(Scalable user, String name) {
    this(user, name, HORIZONTAL);
    // TODO Auto-generated constructor stub
  }

  public GraphicsToolBar(Scalable user, String name, int orientation) {
    super(name, orientation);
    this.user = user;
//    viewTransform = user.getTransform();
//    boundingBox = user.getBoundigBox();
    init();
//    boundingBox = new Rectangle2D.Double(0,0,400,300);

  }
  
  public void addButtonOptions (int options) {
	  this.buttonOptions = this.buttonOptions | options;
	  configureButtons();
  }
  public void removeButtonOptions (int options) {
	  this.buttonOptions = this.buttonOptions & ~options;
	  configureButtons();
	  
  }
  
  public void test () {
	  addButtonOptions (ZOOM_IN | ZOOM_ALL | PAN_LEFT | ROTATE_CW);
  }
  
  public boolean isOptionActive(int option) {
	  return (this.buttonOptions & option) == option;
  }

  public void init () {
    //    new ImageIcon
    //                            (this.getClass().getResource("/META-INF/icons/iconArrowDown.gif")));

    zoomInButton.setToolTipText("Zoom In");
    zoomOutButton.setToolTipText("Zoom Out");
    panAllButton.setToolTipText("Zoom All");
    panLeftButton.setToolTipText("Move to Left");
    panRightButton.setToolTipText("Move to Right");
    panUpButton.setToolTipText("Move Up");
    panDownButton.setToolTipText("Move Down");
    rotateCWButton.setToolTipText("Rotate clockwise");
    rotateCCWButton.setToolTipText("Rotate counterclockwise");
    mirrorVerticalButton.setToolTipText("Mirror vertically (around X axis)");
    mirrorHorizontalButton.setToolTipText("Mirror horizontally (around Y axis)");
    saveButton.setToolTipText("Save 2D canvas as JPEG file");
    printButton.setToolTipText("Print 2D canvas content");
    paintStringsButton.setToolTipText("Show strings in the canvas");
    paintImagesButton.setToolTipText("Show images in the canvas");
   
    setFloatable(false);
//    add(viewXformLabel);
//    add(scaleField);

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
    mirrorHorizontalButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
          mirrorHorizontal_actionPerformed(e);
        }
      });
    mirrorVerticalButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	mirrorVertical_actionPerformed(e);
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
    paintStringsButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	paintStringsButton_actionPerformed(e);
        }
      });
    paintImagesButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(ActionEvent e) {
        	paintImagesButton_actionPerformed(e);
        }
      });

  }
  
  protected void paintImagesButton_actionPerformed(ActionEvent e) {

	if (paintImagesButton.isSelected()) {
		paintImagesButton.setToolTipText("Hide images in the canvas");
	}
	else {
	paintImagesButton.setToolTipText("Show images in the canvas");
	}
	user.togglePaintImages();
  }

  protected void paintStringsButton_actionPerformed(ActionEvent e) {

	  if (paintStringsButton.isSelected()) {
		  paintStringsButton.setToolTipText("Hide strings in the canvas");
	  }
	  else {
		  paintStringsButton.setToolTipText("Show strings in the canvas");
	  }
	  user.togglePaintStrings();
  }

private void configureButtons () {
	  this.removeAll();

		if (isOptionActive(ZOOM_ALL)) add(panAllButton);
		if (isOptionActive(ZOOM_IN)) add(zoomInButton);
	    if (isOptionActive(ZOOM_OUT)) add(zoomOutButton);
	    addSeparator();
	    
	    if (isOptionActive(PAN_LEFT)) add(panLeftButton);
	    if (isOptionActive(PAN_RIGHT)) add(panRightButton);
	    if (isOptionActive(PAN_UP)) add(panUpButton);
	    if (isOptionActive(PAN_DOWN)) add(panDownButton);
	    addSeparator();

	   	if (isOptionActive(ROTATE_CW)) add(rotateCWButton);
	   	if (isOptionActive(ROTATE_CCW)) add(rotateCCWButton);
	   	if (isOptionActive(MIRROR_HORIZONTAL)) add(mirrorHorizontalButton);
	   	if (isOptionActive(MIRROR_VERTICAL)) add(mirrorVerticalButton);
	    addSeparator();
	   
	   	if (isOptionActive(PAINT_STRINGS)) add(paintStringsButton);
	   	if (isOptionActive(PAINT_IMAGES)) add(paintImagesButton);
	    addSeparator();
	   	
	    if (user instanceof Printable) {
	    	if (isOptionActive(SAVE)) add(saveButton);
	    	if (isOptionActive(PRINT)) add(printButton);
	    }
  }
  
  void zoomInButton_actionPerformed(ActionEvent e) {

      user.scale(ZOOM_SCALE, ZOOM_SCALE);
    
  }

  void zoomOutButton_actionPerformed(ActionEvent e) {

      user.scale(1 / ZOOM_SCALE, 1 / ZOOM_SCALE);
  }

  void panAllButton_actionPerformed(ActionEvent e) {
  user.computeBoundingBox();
  user.scaleFitTarget();
//  xMirrorBox.setSelected(false);
//  yMirrorBox.setSelected(false);
//    System.out.println("view xform" + viewTransform);
//    System.out.println("boundingbox" + boundingBox);
    user.repaint();
    rotationCounter = 0;
    signX = 1;
    signY = -1;
    //    flip = false;

  }


  void panLeftButton_actionPerformed(ActionEvent e) {
     user.translate(SHIFT_FACTOR, 0);
  }

  void panRightButton_actionPerformed(ActionEvent e) {
      user.translate(- SHIFT_FACTOR, 0);
  }

  void panUpButton_actionPerformed(ActionEvent e) {
      user.translate(0, - SHIFT_FACTOR);
  }

  void panDownButton_actionPerformed(ActionEvent e) {
      user.translate(0, SHIFT_FACTOR);
  }

  void rotateCWButton_actionPerformed(ActionEvent e) {	  
	  user.rotate(-Math.PI / 2.);

  }

  void rotateCCWButton_actionPerformed(ActionEvent e) {
	  user.rotate(Math.PI / 2.);
  }
  
  public void mirrorHorizontal_actionPerformed(ActionEvent e) {
	  user.scale(-1,1);

    }
 
  public void mirrorVertical_actionPerformed(ActionEvent e) {
	  user.scale(1,-1);
    }


  void saveButton_actionPerformed(ActionEvent e) {
    try {
      int w = user.getWidth(), h = user.getHeight();
      BufferedImage image = new BufferedImage(w, h,
          BufferedImage.TYPE_3BYTE_BGR);
      Graphics2D g2 = image.createGraphics();
      g2.setBackground(Color.white);
      user.paint(g2);
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
    printJob.setPrintable((Printable) user);

//  Page dialog
    printJob.pageDialog(printJob.defaultPage());
//  Print dialog
    if(printJob.printDialog()){
      try { printJob.print(); } catch (Exception PrintException) { }
    }
//  No dialogs
//  try { printJob.print(); } catch (Exception PrintException) { }
  }

//  public void paintComponent (Graphics g) {
//
//    if (viewTransform == null) {
//      boundingBox = new Rectangle2D.Double
//        (-user.getWidth()/2, -user.getHeight(), user.getWidth(), user.getHeight());
//      viewTransform = user.getTransform();
//    }
//    super.paintComponent(g);
// }

}
