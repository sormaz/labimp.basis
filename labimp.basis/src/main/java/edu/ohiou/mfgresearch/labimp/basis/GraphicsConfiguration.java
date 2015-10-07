package edu.ohiou.mfgresearch.labimp.basis;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Stroke;
import java.util.Properties;
import java.util.StringTokenizer;

/* class that allows customization of drwing shapes
 * 
 */
public class GraphicsConfiguration {

//	public static final int 	CAP_BUTT 	0
//	public static final int 	CAP_ROUND 	1
//	public static final int 	CAP_SQUARE 	2

//	public static final int 	JOIN_BEVEL 	2
//	public static final int 	JOIN_MITER 	0
//	public static final int 	JOIN_ROUND 	1

	public static final String [] endCapStrings = {"CAP_BUTT", "CAP_ROUND", "CAP_SQUARE"};
	public static final String [] endJoinStrings = {"JOIN_MITER", "JOIN_ROUND", "JOIN_BEVEL"};

	int color;

	String fontName;
	String fontSize;

	float lineWidth;
	int lineJoin;
	int endCap;
	float miterLimit=1.0f;

	float [] dashArray;
	float dashPhase;

	public Color makeColor () {
		return new Color (color);
	}

	public String toString() {
		return "Color: " + color + 
		", Line width: " + lineWidth + 
		", End Cap: " + endCapStrings[endCap] + 
		", Line Join: " + endJoinStrings[lineJoin] +
		", Miter Limit: " + miterLimit;
	}

	public Stroke makeStroke () {
		return new BasicStroke(lineWidth, endCap, lineJoin, miterLimit, dashArray, dashPhase);
	}
	

	public void configure (Properties properties) {

		String colorStr = properties.getProperty("edu.ohiou.labimp.basis.GraphicsConfiguration.color", "");		
		String lineWidthStr = properties.getProperty("edu.ohiou.labimp.basis.GraphicsConfiguration.lineWidth", "");
		String lineJoinStr = properties.getProperty("edu.ohiou.labimp.basis.GraphicsConfiguration.lineJoin", "");
		String endCapStr = properties.getProperty("edu.ohiou.labimp.basis.GraphicsConfiguration.endCap", "");
		String miterLimitStr = properties.getProperty("edu.ohiou.labimp.basis.GraphicsConfiguration.miterLimit", "");
		String dashArrayStr = properties.getProperty("edu.ohiou.labimp.basis.GraphicsConfiguration.dashArray", "");
		String dashPhaseStr = properties.getProperty("edu.ohiou.labimp.basis.GraphicsConfiguration.dashPhase", "");

		try {
			color = Integer.parseInt(colorStr, 16);
		} catch (NumberFormatException e) {

		System.out.println("Color not specified, using default 000000 (black)");
		color = 000000;
	}
		try {
			lineWidth = Float.parseFloat(lineWidthStr);
		} catch (NumberFormatException e) {

			System.out.println("Line width not specified, using default 1.0");
			lineWidth = 1.0f;
		}

		try {
			lineJoin = Integer.parseInt(lineJoinStr);
		} catch (NumberFormatException e) {

			System.out.println("Line join not specified, using default 1");
			lineJoin = BasicStroke.JOIN_ROUND;
		}

		try {
			endCap = Integer.parseInt(endCapStr);
		} catch (NumberFormatException e) {

			System.out.println("End Cap not specified, using default 0 (CAP_BUTT)");
			endCap = BasicStroke.CAP_BUTT;
		}
		try {
			miterLimit = Float.parseFloat(miterLimitStr);
		} catch (NumberFormatException e) {

			System.out.println("Miter Limit not specified, using default 10.0");
			miterLimit = 10.0f;
		}
		try {
			StringTokenizer dashArrayTokenizer = new StringTokenizer(dashArrayStr, ",");
			int numTokens = dashArrayTokenizer.countTokens();
			if ( numTokens > 0) {
				dashArray = new float[numTokens];
				int i = 0;
			while (dashArrayTokenizer.hasMoreTokens()) {
			dashArray[i] = Float.parseFloat(dashArrayTokenizer.nextToken());
			i++;
			}
			}
		} catch (NumberFormatException e) {

			System.out.println("Dash Array not specified completely, using default null (solid line)");
			dashArray = null;
		}
		try {
			dashPhase = Float.parseFloat(dashPhaseStr);
		} catch (NumberFormatException e) {

			System.out.println("Dash phase not specified, using default 0.0");
			dashPhase = 0.0f;
		}
	}
	
	public void configure () {
		configure (this);
	}

	public void configure (Object target) {

		String className = target.getClass().getName();
//		System.out.println("Configuring Graphics for " + className);
		String colorStr = ViewObject.properties.getProperty(className + ".color", "");
		String lineWidthStr = ViewObject.properties.getProperty(className + ".lineWidth", "");
		String lineJoinStr = ViewObject.properties.getProperty(className + ".lineJoin", "");
		String endCapStr = ViewObject.properties.getProperty(className + ".endCap", "");
		String miterLimitStr = ViewObject.properties.getProperty(className + ".miterLimit", "");
		String dashArrayStr = ViewObject.properties.getProperty(className + ".dashArray", "");
		String dashPhaseStr = ViewObject.properties.getProperty(className + ".dashPhase", "");

		try {
			color = Integer.parseInt(colorStr, 16);
		} catch (NumberFormatException e) {

		System.out.println("Color not specified, using default 000000 (black)");
		color = 000000;
	}
		try {
			lineWidth = Float.parseFloat(lineWidthStr);
		} catch (NumberFormatException e) {

			System.out.println("Line width not specified, using default 1.0");
			lineWidth = 1.0f;
		}

		try {
			lineJoin = Integer.parseInt(lineJoinStr);
		} catch (NumberFormatException e) {

			System.out.println("Line join not specified, using default 1");
			lineJoin = BasicStroke.JOIN_ROUND;
		}

		try {
			endCap = Integer.parseInt(endCapStr);
		} catch (NumberFormatException e) {

			System.out.println("End Cap not specified, using default 0 (CAP_BUTT)");
			endCap = BasicStroke.CAP_BUTT;
		}
		try {
			miterLimit = Float.parseFloat(miterLimitStr);
		} catch (NumberFormatException e) {

			System.out.println("Miter Limit not specified, using default 10.0");
			miterLimit = 10.0f;
		}
		try {
			StringTokenizer dashArrayTokenizer = new StringTokenizer(dashArrayStr, ",");
			int numTokens = dashArrayTokenizer.countTokens();
			if ( numTokens > 0) {
				dashArray = new float[numTokens];
				int i = 0;
			while (dashArrayTokenizer.hasMoreTokens()) {
			dashArray[i] = Float.parseFloat(dashArrayTokenizer.nextToken());
			i++;
			}
			}
		} catch (NumberFormatException e) {

			System.out.println("Dash Array not specified completely, using default null (solid line)");
			dashArray = null;
		}
		try {
			dashPhase = Float.parseFloat(dashPhaseStr);
		} catch (NumberFormatException e) {

			System.out.println("Dash phase not specified, using default 0.0");
			dashPhase = 0.0f;
		}

	}


}
