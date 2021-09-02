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
import java.util.ArrayList;
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
	public static final Rectangle NAMESTART_REGION = new Rectangle(119,0,4,11);
	public static Map<String,List<Boolean>> PLOTSTATE = new HashMap<String,List<Boolean>>();
	
	public static final String[] REGIONS =  new String[] {"Kugane","Gridania","Uldah","Limsa"};
	public static final int CURRENT_REGION = 0;
	
	public static void main(String[] args) throws IOException, FontFormatException {
		try {
			r = new Robot();
			
			for (String REGION : REGIONS) {
				List<Boolean> house_list = new ArrayList<Boolean>();
				PLOTSTATE.put(REGION,house_list);
				for (int i=0;i<1440;i++) {
					house_list.add(true);
				}
			}
			
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
			if (OpenResidentialMenu()) {
				CaptureResidentialData();
			}
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	private static void CaptureResidentialData() {
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		BufferedImage i = null;
		for (int ward=0;ward<24;ward++) {
			for (int tab=0;tab<2;tab++) {
				try {
					i = CaptureScreen(env);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				for (int x=0;x<2;x++) {
					for (int y=0;y<15;y++) {
						BufferedImage plot_img = i.getSubimage(SCANSTART_REGION.x+SCANSTART_REGION.width*x,SCANSTART_REGION.y+SCANSTART_REGION.height*y,SCANSTART_REGION.width,NAMESTART_REGION.height);
						boolean found=false;
						for (int xx=0;xx<NAMESTART_REGION.width;xx++) {
							for (int yy=0;yy<NAMESTART_REGION.height;yy++) {
								Color col = new Color(plot_img.getRGB(NAMESTART_REGION.x+xx, NAMESTART_REGION.y+yy));
								if (col.getRed()>150||col.getGreen()>150) {
									found=true;
									break;
								}
							}
							if (found) {
								break;
							}
						}
						if (found) {
							//Ignore this plot, it is taken.
							PLOTSTATE.get(REGIONS[CURRENT_REGION]).set(x*15+y+ward*60+tab*30,found);
							System.out.println("Plot "+((x*15+y+tab*30)+1)+" in Ward "+(ward+1)+" is taken.");
						} else {
							//Ignore this plot, it is taken.
							if (PLOTSTATE.get(REGIONS[CURRENT_REGION]).get(x*15+y+ward*60+tab*30)) {
								AnnounceNewOpenPlot(REGIONS[CURRENT_REGION],ward+1,(x*15+y+tab*30)+1,"Small");
							}
							PLOTSTATE.get(REGIONS[CURRENT_REGION]).set(x*15+y+ward*60+tab*30,found);
						}
					}
				}
				PressKey(KeyEvent.VK_F10);r.delay(500);
			}
			PressKey(KeyEvent.VK_F9);r.delay(200);
			PressKeyWithModifier(KeyEvent.VK_SHIFT,KeyEvent.VK_F10);r.delay(200);
			PressKey(KeyEvent.VK_NUMPAD0);r.delay(1000);
		}
	}
	
	private static void AnnounceNewOpenPlot(String region,int ward,int plot,String size) {
		System.out.println("A new "+size+" plot has become available in "+region+", Ward "+ward+", Plot "+plot+"!");
	}
	
	private static void PressKeyWithModifier(int modifier,int keycode) {
		r.keyPress(modifier);
		r.keyPress(keycode);
		r.keyRelease(keycode);r.delay(100);
		r.keyRelease(modifier);
	}
	
	private static boolean OpenResidentialMenu() {
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
			return true;
		} else {
			System.out.println("Could not select Aetheryte!! Moving on...");
			return false;
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