package mygeom;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import mygeom.Point2;

/**
 * Path.java
 *
 * @author <a href="mailto:gery.casiez@univ-lille1.fr">Gery Casiez</a>
 * @version
 */

public class Path {
	private ArrayList<Point2> listePoints;
	
	public Path() {
		listePoints = new ArrayList<Point2>();
	}
	
	public void add(Point2 p) {
		listePoints.add(p);
	}
	
	public void clear() {
		listePoints.clear();
	}
	
	public ArrayList<Point2> getPointList() {
		return listePoints;
	}
	
	public void draw(Graphics2D g) {
		Point2 p1,p2;
		int i = 0;
		
		g.setColor(Color.BLACK);
		if (listePoints.size()>1) {
			while (i < listePoints.size()-1) {
				p1 = new Point2(listePoints.get(i));
				p2 = new Point2(listePoints.get(i+1));
				g.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
				i++;
			}
		}
	}
}
