import io.hybrid.interaction.dollar.Dollar;
import io.hybrid.interaction.dollar.DollarListener;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

/**
 * Canvas2D.java
 * 
 * @author <a href="mailto:gery.casiez@lifl.fr">Gery Casiez</a>
 * @version
 */

public class Canvas2D extends Canvas implements MouseMotionListener,
		MouseListener, DollarListener {

	private static final long serialVersionUID = -6942619804206773938L;

	// Stroke of reference
	private Vector<Point> RStroke;

	// Stroke to test
	private Vector<Point> TStroke;

	// QUESTION 6
	// 1$ recognizer
	private Dollar dollarInst;
	private boolean validatedDollar = false;
	private final static float VALIDATION_THRES = 0.80f;

	private int shiftModifier = 17;

	public Canvas2D() {

		dollarInst = new Dollar(Dollar.GESTURES_DEFAULT);

		RStroke = new Vector<Point>();
		TStroke = new Vector<Point>();

		addMouseListener(this);
		addMouseMotionListener(this);

		dollarInst.setListener(this);
		dollarInst.setActive(true);

		setBackground(new Color(255, 255, 255));
	}

	public void paint(Graphics g) {
		g.drawString(
				"Drag avec le bouton gauche ou droit de la souris + Shift : creation d'une courbe de reference",
				10, 15);
		g.drawString(
				"Drag avec le bouton gauche ou droit de la souris : creation d'une courbe de test",
				10, 30);

		if (validatedDollar) {
			g.setFont(new Font("", Font.BOLD, 16));
			g.drawString("Recognize gesture: " + dollarInst.getName(), 10, 55);
		}

		int r = 5;

		if (!RStroke.isEmpty()) {
			g.setColor(Color.black);
			for (int i = 1; i < RStroke.size(); i++) {
				g.drawLine(RStroke.elementAt(i - 1).x,
						RStroke.elementAt(i - 1).y, RStroke.elementAt(i).x,
						RStroke.elementAt(i).y);
				g.drawArc(RStroke.elementAt(i - 1).x - r,
						RStroke.elementAt(i - 1).y - r, 2 * r, 2 * r, 0, 360);
			}
			g.drawArc(RStroke.elementAt(RStroke.size() - 1).x - r,
					RStroke.elementAt(RStroke.size() - 1).y - r, 2 * r, 2 * r,
					0, 360);
		}

		if (!TStroke.isEmpty()) {
			g.setColor(Color.orange);
			for (int i = 1; i < TStroke.size(); i++) {
				g.drawLine(TStroke.elementAt(i - 1).x,
						TStroke.elementAt(i - 1).y, TStroke.elementAt(i).x,
						TStroke.elementAt(i).y);
				g.drawArc(TStroke.elementAt(i - 1).x - r,
						TStroke.elementAt(i - 1).y - r, 2 * r, 2 * r, 0, 360);
			}
			g.drawArc(TStroke.elementAt(TStroke.size() - 1).x - r,
					TStroke.elementAt(TStroke.size() - 1).y - r, 2 * r, 2 * r,
					0, 360);
		}

		if (!RStroke.isEmpty() && !TStroke.isEmpty()) {

			final DTW dtw = new DTW(RStroke, TStroke);

			if (dtw.isEmpty() || dtw.getRows() <= 0 || dtw.getCols() <= 0) {
				System.err.println("Calcul de DTW invalide.");
				return;
			}

			int i = dtw.getRows() - 1;
			int j = dtw.getCols() - 1;
			Couple c = dtw.getCouple(i, j);
			Couple backCouple;
			
			// QUESTION 5
			boolean begin = false;

			if (c == null) {
				System.err.println("Couple est nulle.");
				return;
			}
			g.setColor(Color.magenta);

			// on remonte dans le tableau de programmation dynamique
			while (i > 0 && j > 0) {

				final Point TPoint = TStroke.elementAt(c.y);
				final Point RPoint = RStroke.elementAt(c.x);

				backCouple = dtw.getCouple(c.x, c.y);

				if (TPoint == null || RPoint == null || backCouple == null) {
					System.err.println("Points ou couple sont nulle.");
					break;
				}
				g.drawLine(RStroke.elementAt(i).x, RStroke.elementAt(i).y,
						TStroke.elementAt(j).x, TStroke.elementAt(j).y);

				if (TStroke.elementAt(backCouple.y) != TPoint
						&& RStroke.elementAt(backCouple.x) != RPoint)
					begin = true;

				if (begin) {
					System.out.println("Draw corresponding !");
					g.drawLine(TPoint.x, TPoint.y, RPoint.x, RPoint.y);
				}

				c = backCouple;
			}

			// remonter la matrice de couple par la fin (tableau dinamique)

		}

	}

	public void mouseMoved(MouseEvent e) {

	}

	public void mouseDragged(MouseEvent e) {
		if ((e.getModifiers() == shiftModifier) || RStroke.isEmpty())
			RStroke.add(e.getPoint());
		else
			TStroke.add(e.getPoint());

		dollarInst.pointerDragged(e.getX(), e.getY());

		repaint();
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		if ((e.getModifiers() == shiftModifier) || RStroke.isEmpty())
			RStroke.clear();
		else
			TStroke.clear();
		dollarInst.pointerPressed(e.getX(), e.getY());
	}

	public void mouseReleased(MouseEvent e) {
		dollarInst.pointerReleased(e.getX(), e.getY());
		repaint();
	}

	// QUESTION 6
	// One dollar functions

	public void dollarDetected(Dollar dollar) {

		if (isValideDollar()) {
			repaint();
		}
	}

	public boolean isValideDollar() {
		validatedDollar = dollarInst.getScore() > VALIDATION_THRES;
		return validatedDollar;
	}
}
