package sig;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

public class HousingTracker{
	
	public static Robot r = null;
	public static final Point WHITE_UI_SELECTION_PIXEL = new Point(650,73);
	public static final Point NONBLACK_UI_PIXEL = new Point(843,967); 
	public static final int SCANSTART_X = 601;
	public static final int SCANSTART_Y = 415;
	public static final Rectangle SCANSTART_REGION = new Rectangle(601,415,330,24);
	public static final Rectangle NAMESTART_REGION = new Rectangle(119,0,16,11);
	public static Map<String,List<Boolean>> PLOTSTATE = new HashMap<String,List<Boolean>>();
	
	public static final String[] REGIONS =  new String[] {"Kugane","Gridania","Uldah","Limsa"};
	public static final int CURRENT_REGION = 0;
	
	public static void main(String[] args) throws IOException, FontFormatException {
		try {
			r = new Robot();
			
			r.delay(3000);
			//Starts at 601,415
			/*BufferedImage i = CaptureScreen(env, r);
			for (int x=0;x<2;x++) {
				for (int y=0;y<15;y++) {
					ImageIO.write(i.getSubimage(SCANSTART_REGION.x+SCANSTART_REGION.width*x,SCANSTART_REGION.y+SCANSTART_REGION.height*y,SCANSTART_REGION.width,NAMESTART_REGION.height),"png",new File("screenshot_"+x+"_"+y+".png"));
				}
			}*/
			//CompleteALoadingPhase();
			//Walk(Direction.RIGHT,1000);
			//OpenResidentialMenu();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	private static void CaptureResidentialData() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		BufferedImage i = null;
		try {
			i = CaptureScreen(env);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		for (int x=0;x<2;x++) {
			for (int y=0;y<15;y++) {
				Image plot_img = i.getSubimage(SCANSTART_REGION.x+SCANSTART_REGION.width*x,SCANSTART_REGION.y+SCANSTART_REGION.height*y,SCANSTART_REGION.width,NAMESTART_REGION.height);
			}
		}
	}
	
	private static void OpenResidentialMenu() {
		PressKey(KeyEvent.VK_ESCAPE);r.delay(500);
		PressKey(KeyEvent.VK_ESCAPE);r.delay(500);
		PressKey(KeyEvent.VK_NUMPAD0);
		r.delay(500);
		Color screen_col = r.getPixelColor(WHITE_UI_SELECTION_PIXEL.x, WHITE_UI_SELECTION_PIXEL.y);
		if (screen_col.getRed()>=200&&screen_col.getGreen()>=200&&screen_col.getBlue()>=200) {
			System.out.println("Aetherythe Selected....Begin Processing.");
			PressKey(KeyEvent.VK_NUMPAD0);r.delay(2000);
			PressKey(KeyEvent.VK_NUMPAD7);r.delay(200);
			PressKey(KeyEvent.VK_NUMPAD0);r.delay(2000);
			PressKey(KeyEvent.VK_NUMPAD7);r.delay(200);
			PressKey(KeyEvent.VK_NUMPAD0);r.delay(2000);
		} else {
			System.out.println("Could not select Aetheryte!! Moving on...");
		}
	}

	private static void CompleteALoadingPhase() {
		while (!IsLoading()) {
			r.delay(200);
		}
		while (IsLoading()) {
			r.delay(200);
		}
	}
	
	private static boolean IsLoading() {
		return r.getPixelColor(NONBLACK_UI_PIXEL.x,NONBLACK_UI_PIXEL.y).equals(Color.BLACK);
	}
	
	private static void PressKey(int keycode) {
		r.keyPress(keycode);
		r.keyRelease(keycode);
	}
	
	private static void Walk(Direction dir,int ms) {
		r.keyPress(dir.keycode);
		r.delay(ms);
		r.keyRelease(dir.keycode);
	}

	private static BufferedImage CaptureScreen(GraphicsEnvironment env) throws IOException {
		BufferedImage screenshot = r.createScreenCapture(env.getMaximumWindowBounds());
		ImageIO.write(screenshot,"png",new File("screenshot.png"));
		return screenshot;
	}
}