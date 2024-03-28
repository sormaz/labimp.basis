package edu.ohiou.mfgresearch.labimp.basis;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.JPanel;

public class TestDrawable implements Drawable2D, Viewable {

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

	public JPanel geettPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getPanelLocation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getPanelOrientation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void settApplet(GUIApplet applet) {
		// TODO Auto-generated method stub
		
	}

	public GUIApplet geettApplet() {
		// TODO Auto-generated method stub
		return null;
	}

	public void settPanel(JPanel inPanel) {
		// TODO Auto-generated method stub
		
	}

	public JPanel makePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addPanel() {
		// TODO Auto-generated method stub
		
	}

	public Color geettColor() {
		// TODO Auto-generated method stub
		return null;
	}

	public void settColor(Color color) {
		// TODO Auto-generated method stub
		
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public void display() {
		// TODO Auto-generated method stub
		
	}

	public void display(String inTitle) {
		// TODO Auto-generated method stub
		
	}

	public void display(String inTitle, Dimension inSize) {
		// TODO Auto-generated method stub
		
	}

	public void display(String inTitle, int inWidth, int inHeight) {
		// TODO Auto-generated method stub
		
	}

	public void display(String inTitle, Dimension inSize, int appletClosing) {
		// TODO Auto-generated method stub
		
	}

	public void toggleVisible() {
		// TODO Auto-generated method stub
		
	}

	public Dimension geetAppletSize() {
		// TODO Auto-generated method stub
		return null;
	}

	public Viewable geettGuiObject() {
		// TODO Auto-generated method stub
		return null;
	}

	public void settGuiObject(Viewable guiObject) {
		// TODO Auto-generated method stub
		
	}

}
