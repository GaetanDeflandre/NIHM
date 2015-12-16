package event;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;

import mygeom.Path;
import mygeom.Point2;

/**
 * BlobQueue.java
 * 
 * @author <a href="mailto:gery.casiez@univ-lille1.fr">Gery Casiez</a>
 * @version
 */

public class BlobQueue {
	HashMap<Integer, Path> cursor;

	public BlobQueue() {
		cursor = new HashMap<Integer, Path>();
	}

	public void addCursor(int id, Point2 p) {
		Path path;
		// Suppose to have a new blob but we double check
		path = cursor.get(id);
		if (path == null) { // we have a new blob, we add it
			path = new Path();
			path.add(p);
			cursor.put(id, path);
		} else { // update blob information
			updateCursor(id, p);
		}
	}

	public void updateCursor(int id, Point2 p) {
		Path path = cursor.get(id);
		if (path != null)
			path.add(p);
		else
			// It's a new blob
			addCursor(id, p);
	}

	public void removeCursor(int id) {
		cursor.remove(id);
	}

	public int getNbFingers() {
		return cursor.keySet().size();
	}

	public void draw(Graphics2D g) {
		int size = 30;
		try {
			for (int i : cursor.keySet()) {
				Path p = cursor.get(i);
				// p.draw(g);

				ArrayList<Point2> listePoints = p.getPointList();
				// Draw Ellipse for each point
				Ellipse2D ellipse = new Ellipse2D.Float(0, 0, size, size);
				AffineTransform at = AffineTransform.getTranslateInstance(0, 0);
				at.translate(listePoints.get(listePoints.size() - 1).x - size
						/ 2, listePoints.get(listePoints.size() - 1).y - size
						/ 2);
				g.setColor(Color.PINK);
				((Graphics2D) g).fill(at.createTransformedShape(ellipse));

				// Text with id
				g.setColor(Color.DARK_GRAY);
				g.drawString("" + i,
						(int) listePoints.get(listePoints.size() - 1).x - 3,
						(int) listePoints.get(listePoints.size() - 1).y + 4);

			}
		} catch (ConcurrentModificationException e) {
			System.out
					.println("Exception ConcurrentModificationException to fix!");
		}
	}

	public boolean checkId(int id) {
		return (cursor.keySet().contains(id));
	}
}
