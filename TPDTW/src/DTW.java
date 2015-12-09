import java.awt.Point;
import java.security.InvalidParameterException;
import java.util.Vector;

public class DTW {

	private Vector<Point> RStroke;
	private Vector<Point> TStroke;

	/**
	 * Tableau des meilleurs prédecesseurs. Calculer dynamiquement.
	 */
	private Matrix DMatrix;

	private boolean empty = true;

	public DTW(Vector<Point> RStroke, Vector<Point> TStroke) {

		if (RStroke.isEmpty() || TStroke.isEmpty()) {
			empty = true;
			throw new InvalidParameterException("Liste vide en parametre.");
		}

		this.RStroke = RStroke;
		this.TStroke = TStroke;

		computeD();
	}

	private void computeD() {

		// i -> parcours des lignes (RStroke)
		// j -> parcours des colonne (TStroke)

		final int n = RStroke.size();
		final int m = TStroke.size();

		DMatrix = new Matrix(n, m, true);
		DMatrix.items[1][1] = RStroke.get(0).distance(TStroke.get(0));

		for (int i = 1; i < n; i++) {
			final double dist = RStroke.get(i).distance(TStroke.get(1));
			DMatrix.items[i][1] = dist + DMatrix.items[i - 1][1];
		}

		for (int j = 1; j < m; j++) {
			final double dist = RStroke.get(1).distance(TStroke.get(j));
			DMatrix.items[1][j] = dist + DMatrix.items[1][j - 1];
		}

		for (int i = 1; i < n; i++) {
			for (int j = 1; j < m; j++) {

				final Point ref = RStroke.get(i);
				final Point test = TStroke.get(j);
				final double dist = ref.distance(test);

				double minValue = Double.POSITIVE_INFINITY;
				int minX = -1;
				int minY = -1;

				if (minValue > DMatrix.items[i - 1][j]) {
					minValue = DMatrix.items[i - 1][j];
					minX = i - 1;
					minY = j;
				}

				if (minValue > DMatrix.items[i][j - 1]) {
					minValue = DMatrix.items[i][j - 1];
					minX = i;
					minY = j - 1;
				}

				if (minValue > DMatrix.items[i - 1][j - 1]) {
					minValue = DMatrix.items[i - 1][j - 1];
					minX = i - 1;
					minY = j - 1;
				}

				DMatrix.items[i][j] = dist + minValue;

				DMatrix.couple[i][j] = new Couple(minX, minY);
			}
		}
		
		empty = false;
	}

	public boolean isEmpty() {
		return empty;
	}

	public int getRows() {
		return DMatrix.nRows;
	}

	public int getCols() {
		return DMatrix.nCols;
	}

	public Couple getCouple(final int i, final int j) {
		return DMatrix.couple[i][j];
	}

}
