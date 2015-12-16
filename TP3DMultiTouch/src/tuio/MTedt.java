package tuio;

import Main.MTSurface;
import TUIO.TuioClient;
import TUIO.TuioCursor;
import TUIO.TuioListener;
import TUIO.TuioObject;
import TUIO.TuioTime;
import mygeom.Point2;

/**
 * MTedt.java
 *
 * @author <a href="mailto:gery.casiez@univ-lille1.fr">Gery Casiez</a>
 * @version
 */

public class MTedt implements TuioListener {

	TuioClient client=null;
	MTSurface surface; // the surface that will receive the events.
	
	private void initConnexion() {
		int port=3333;
		client=new TuioClient(port);
		client.connect();
		if (!client.isConnected()) {
			System.exit(1);
		}
		System.out.println("connection...");
		client.addTuioListener(this);
	}
	
	public MTedt() {
		initConnexion();
	}
	
	public MTedt(MTSurface s) {
		initConnexion();
		surface=s;
	}
	
	public void stop() {
		client.disconnect();
		System.out.println("disconnected");
	}
	
		
	/** Listeners **/
	
	public void addTuioObject(TuioObject tobj) {
	}

	public void updateTuioObject(TuioObject tobj) {
	}
	
	public void removeTuioObject(TuioObject tobj) {
	}

	public void addTuioCursor(TuioCursor tcur) {
		surface.addCursor(tcur.getCursorID(), new Point2(tcur.getX(), tcur.getY()));
	}

	public void updateTuioCursor(TuioCursor tcur) {
		surface.updateCursor(tcur.getCursorID(), new Point2(tcur.getX(), tcur.getY()));
	}
	
	public void removeTuioCursor(TuioCursor tcur) {
		surface.removeCursor(tcur.getCursorID(), new Point2(tcur.getX(), tcur.getY()));
	}

	
	public void refresh(TuioTime frameTime) {
	}

}
