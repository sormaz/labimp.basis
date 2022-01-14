package edu.ohiou.mfgresearch.labimp.basis;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JPanel;

public class TestDrawable implements Drawable2D {

	public void settCanvas(Draw2DPanel canvas) {
		// TODO Auto-generated method stub

	}

	public JPanel geettCanvas() {
		// TODO Auto-generated method stub
		return null;
	}

	public void paintComponent(Graphics g) {
		// TODO Auto-generated method stub

	}

	public void paintComponent(Graphics2D g) {
		// TODO Auto-generated method stub

	}

	public GraphicsConfiguration geetGraphicsConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	public void makeDrawSets() {
		// TODO Auto-generated method stub

	}

	public LinkedList<Shape> geetDrawList() {
		// TODO Auto-generated method stub
		return null;
	}

	public LinkedList<Shape> geetFillList() {
		// TODO Auto-generated method stub
		return null;
	}

	public LinkedList<DrawString> geetStringList() {
		// TODO Auto-generated method stub
		return null;
	}

	public void generateImageList() {
		// TODO Auto-generated method stub

	}

	public void addButtonOptions(int options) {
		// TODO Auto-generated method stub

	}

	public void removeButtonOptions(int options) {
		// TODO Auto-generated method stub

	}

	public void setNeedUpdate(boolean needUpdate) {
		// TODO Auto-generated method stub

	}

	public Collection giveSelectables() {
		// TODO Auto-generated method stub
		return null;
	}

	public Point2D geettPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	public void settPosition(Point2D point) {
		// TODO Auto-generated method stub

	}

	public String toToolTipString() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestDrawable td = new TestDrawable();
		Draw2DApplet d2a = new Draw2DApplet (td);
		td.display("Show test");
	}

	private void display(String string) {
		// TODO Auto-generated method stub
		
	}

}
