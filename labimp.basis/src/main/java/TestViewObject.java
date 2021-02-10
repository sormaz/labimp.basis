import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;

import edu.ohiou.mfgresearch.labimp.basis.Draw2DApplet;
import edu.ohiou.mfgresearch.labimp.basis.ViewObject;

public class TestViewObject extends ViewObject {
	LinkedList<Point2D> points = new LinkedList<Point2D>();

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestViewObject tvo = new TestViewObject();
		tvo.points.add(new Point2D.Double(1,2));
		tvo.points.add(new Point2D.Double(0,1));
		tvo.points.add(new Point2D.Double(1,0));
		tvo.points.add(new Point2D.Double(1,1));
		Draw2DApplet d2a = new Draw2DApplet (tvo);
		tvo.display("Show test");
	}
	
	public LinkedList geetDrawList () {
		Path2D fillShape;

		fillShape = new Path2D.Double();
		for (int i = 0; i < points.size(); i++) {

			double x, y;

			x = ((Point2D) points.get(i)).getX();
			y = ((Point2D) points.get(i)).getY();

			if (i == 0) {
				fillShape.moveTo(x, y);   
			} 
			else{
				fillShape.lineTo(x, y);
			}
		}

		fillShape.closePath();

		LinkedList<Shape> list = new LinkedList<Shape>();
		list.add(fillShape);

		return list;
  }


}
