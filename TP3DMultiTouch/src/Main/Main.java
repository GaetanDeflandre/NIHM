package Main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

import com.jogamp.opengl.GLCapabilities;

/**
 * Main.java
 * 
 * @author <a href="mailto:gery.casiez@univ-lille1.fr">Gery Casiez</a>
 * @version
 */

class MyGlassPane extends JComponent {
	MTSurface mtSurface;

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		mtSurface.getBlobQueue().draw((Graphics2D) g);
	}

	public MyGlassPane(Container contentPane, MTSurface mtSurface) {
		this.mtSurface = mtSurface;
	}
}

public class Main {

	public Main() {
		JFrame window = new JFrame(
				"Université Lille 1 - M2 IVI - NIHM - 3D MultiTouch interaction - G. Casiez");
		GLCapabilities caps = new GLCapabilities(null);
		MTSurface mtsurface = new MTSurface(caps);

		// Used to show the touch points
		MyGlassPane myGlassPane = new MyGlassPane(window.getContentPane(),
				mtsurface);
		window.setGlassPane(myGlassPane);
		mtsurface.setGlassPane(myGlassPane);
		myGlassPane.setVisible(true);

		mtsurface.setPreferredSize(new Dimension(1000, 1000));
		window.getContentPane().add(mtsurface);

		window.pack();
		window.setLocationRelativeTo(null);
		// window.setLocation(1000,20);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setVisible(true);

		window.setVisible(true);
		mtsurface.requestFocusInWindow();

	}

	public static void main(String[] args) {
		new Main();
	}

}
