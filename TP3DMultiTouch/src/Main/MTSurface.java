package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.nio.IntBuffer;
import java.util.ArrayList;

import mygeom.Point2;
import tuio.MTedt;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;

import event.BlobQueue;

/**
 * MTSurface.java
 * 
 * @author <a href="mailto:gery.casiez@univ-lille1.fr">Gery Casiez</a>
 * @version
 */

class RePaint implements ActionListener {
	MTSurface mtsurface;

	public RePaint(MTSurface mt) {
		mtsurface = mt;
	}

	public void actionPerformed(ActionEvent e) {
		mtsurface.repaint();
	}
}

class MTEventQueue {
	class MTEventData {
		public String type;
		public int id;
		public Point2 p;

		public MTEventData(String type, int id, Point2 p) {
			this.type = type;
			this.id = id;
			this.p = p;
		}
	}

	ArrayList<MTEventData> eventQueue;

	public MTEventQueue() {
		eventQueue = new ArrayList<MTEventData>();
	}

	public synchronized void addEvent(String type, int id, Point2 p) {
		eventQueue.add(new MTEventData(type, id, p));
	}

	public synchronized ArrayList<MTEventData> dump() {
		ArrayList<MTEventData> res = (ArrayList<MTEventData>) eventQueue
				.clone();
		eventQueue.clear();
		return res;
	}

}

public class MTSurface extends GLJPanel implements GLEventListener,
		MouseMotionListener {

	static MTedt mtEdt = null;
	private MTEventQueue mtqueue;
	private BlobQueue bqueue;
	private MyGlassPane myGlassPane;
	GLU glu;
	int buffSize = 6;
	IntBuffer pickingBuffer;
	int mouse_x, mouse_y;
	GLUT glut = new GLUT();

	public MTSurface(GLCapabilities caps) {
		super(caps);
		addGLEventListener(this);
		mtqueue = new MTEventQueue();
		bqueue = new BlobQueue();
		pickingBuffer = IntBuffer.allocate(buffSize);
		
		if (mtEdt == null) {
			mtEdt = new MTedt(this);
		}
		
		javax.swing.Timer t = new javax.swing.Timer(20, new RePaint(this));
		t.start();

		addMouseMotionListener(this);
	}

	public BlobQueue getBlobQueue() {
		return bqueue;
	}

	public void setGlassPane(MyGlassPane myGlassPane) {
		this.myGlassPane = myGlassPane;
	}

	public void addCursor(int id, Point2 p) {
		p.x *= getPreferredSize().getWidth();
		p.y *= getPreferredSize().getHeight();
		mtqueue.addEvent("add", id, p);
	}

	public void updateCursor(int id, Point2 p) {
		if (id == 2){
			System.out.println(p.x + " " + p.y);
		}
		p.x *= getPreferredSize().getWidth();
		p.y *= getPreferredSize().getHeight();
		mtqueue.addEvent("update", id, p);
	}

	public void removeCursor(int id, Point2 p) {
		mtqueue.addEvent("remove", id, p);
	}

	float mrx = 0;
	float mry = 0;
	float mpz = 0;
	
	public void drawScene(GL2 gl) {
		gl.glTranslatef(0, 0, -2);
		gl.glRotatef(mrx, mry, 0, 0);
		mrx += 0.2;
		mry -= 0.1;
		mpz -= 0.005;
		float amb[] = { 0.24725f, 0.1995f, 0.0745f, 1f };
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_AMBIENT, amb, 0);

		float diff[] = { 0.75164f, 0.60648f, 0.22648f };
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_DIFFUSE, diff, 0);

		float spec[] = { 0.628281f, 0.555802f, 0.366065f };
		gl.glMaterialfv(GL2.GL_FRONT, GL2.GL_SPECULAR, spec, 0);
		gl.glMaterialf(GL2.GL_FRONT, GL2.GL_SHININESS, (float) (0.4 * 128.0));

		//glut.glut
		glut.glutSolidTeapot(0.4);
	}

	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

		ArrayList<MTEventQueue.MTEventData> mtqueue2 = mtqueue.dump();

		for (MTEventQueue.MTEventData evt : mtqueue2) {
			if (evt.type == "add")
				bqueue.addCursor(evt.id, evt.p);
			if (evt.type == "update")
				bqueue.updateCursor(evt.id, evt.p);
			if (evt.type == "remove")
				bqueue.removeCursor(evt.id);
		}

		// Render the scene
		gl.glRenderMode(GL2.GL_RENDER);
		gl.glMatrixMode(GL2.GL_PROJECTION); // Set up the projection.
		gl.glLoadIdentity();
		glu.gluPerspective(60.0, 1.0, 1.0, 100.0);

		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity(); // Set up modelview transform.
		drawScene(gl);

		// Find objects touched
		for (MTEventQueue.MTEventData evt : mtqueue2) {
			int px = (int) evt.p.x;
			int py = (int) (evt.p.y);

			int buffsize = 512;
			int[] viewPort = new int[4];
			IntBuffer selectBuffer = Buffers.newDirectIntBuffer(buffsize);
			int hits = 0;

			gl.glGetIntegerv(GL2.GL_VIEWPORT, viewPort, 0);
			gl.glSelectBuffer(buffsize, selectBuffer);
			gl.glRenderMode(GL2.GL_SELECT);
			gl.glInitNames();
			gl.glLoadName(0);

			gl.glMatrixMode(GL2.GL_PROJECTION);
			gl.glLoadIdentity();
			glu.gluPickMatrix(px, (double) viewPort[3] - py, 1.0d, 1.0d,
					viewPort, 0);
			glu.gluPerspective(60.0, 1.0, 1.0, 100.0);

			gl.glMatrixMode(GL2.GL_MODELVIEW);
			gl.glLoadIdentity();
			gl.glPushName(0);
			drawScene(gl);

			hits = gl.glRenderMode(GL2.GL_RENDER);
			processHits(hits, selectBuffer);
		}

		myGlassPane.repaint();
	}

	public void processHits(int hits, IntBuffer buffer) {
		System.out.println("---------------------------------");
		System.out.println(" HITS: " + hits);
		int offset = 0;
		int names;
		float z1, z2;
		for (int i = 0; i < hits; i++) {
			System.out.println("- - - - - - - - - - - -");
			System.out.println(" hit: " + (i + 1));
			names = buffer.get(offset);
			offset++;
			z1 = (float) (buffer.get(offset) & 0xffffffffL) / 0x7fffffff;
			offset++;
			z2 = (float) (buffer.get(offset) & 0xffffffffL) / 0x7fffffff;
			offset++;
			System.out.println(" number of names: " + names);
			System.out.println(" z1: " + z1);
			System.out.println(" z2: " + z2);
			System.out.println(" names: ");

			for (int j = 0; j < names; j++) {
				System.out.print("       " + buffer.get(offset));
				if (j == (names - 1)) {
					System.out.println("<-");
				} else {
					System.out.println();
				}
				offset++;
			}
			System.out.println("- - - - - - - - - - - -");
		}
		System.out.println("---------------------------------");
	}

	public void init(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();
		float ambient[] = { 0.0f, 0.0f, 0.0f, 1.0f };
		float diffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };
		float position[] = { 0.0f, 3.0f, 3.0f, 0.0f };

		float lmodel_ambient[] = { 0.2f, 0.2f, 0.2f, 1.0f };
		float local_view[] = { 0.0f };

		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glDepthFunc(GL2.GL_LESS);

		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_AMBIENT, ambient, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, diffuse, 0);
		gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, position, 0);

		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, lmodel_ambient, 0);
		gl.glLightModelfv(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, local_view, 0);

		gl.glFrontFace(GL2.GL_CW);
		gl.glEnable(GL2.GL_LIGHTING);
		gl.glEnable(GL2.GL_LIGHT0);
		gl.glEnable(GL2.GL_AUTO_NORMAL);
		gl.glEnable(GL2.GL_NORMALIZE);

		glu = new GLU();
	}

	public void dispose(GLAutoDrawable drawable) {
		// called when the panel is being disposed
	}

	public void reshape(GLAutoDrawable drawable, int x, int y, int width,
			int height) {

	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		mouse_x = e.getX();
		mouse_y = e.getY();
	}

}
