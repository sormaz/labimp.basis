/**
 * 
 */
package edu.ohiou.mfgresearch.labimp.basis;

import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * @author sormaz
 *
 */
public interface Scalable {
	
	public AffineTransform getTransform ();
	public int getWidth();
	public int getHeight();
	public void paint(Graphics g);
	public Rectangle2D geetBoundigBox();
	public void scale (double sx, double sy);
	
	public void translate (double tx, double ty);
	public void rotate(double angle);
	
	public void repaint();
	public void togglePaintStrings();
	public void togglePaintImages();
	public void computeBoundingBox();
	public void scaleFitTarget();

}
