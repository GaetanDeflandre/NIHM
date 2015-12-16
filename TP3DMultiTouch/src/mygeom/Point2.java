package mygeom;

/**
 * Point2.java
 *
 * @author <a href="mailto:gery.casiez@univ-lille1.fr">Gery Casiez</a>
 * @version
 */

public class Point2 extends Tuple2 {

		public Point2() {
			super();
		}
	
		public Point2(Point2 a) {
			super(a);
		}
		
		public Point2(Point2 a,Point2 b) {
			super(b);
			this.sub(a);
		}
		
		public Point2(double x,double y) {
			super(x,y);
		}

		public double getY() {
			return y;
		}

		public double getX() {
			return x;
		}
		
		public String toString() {
			return "Point("+x+","+y+")";
		}
		
}
