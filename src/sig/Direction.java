package sig;

import java.awt.event.KeyEvent;

public enum Direction {
	UP(KeyEvent.VK_UP),
	DOWN(KeyEvent.VK_DOWN),
	LEFT(KeyEvent.VK_Q),
	RIGHT(KeyEvent.VK_E);
	
	int keycode;
	
	Direction(int keycode) {
		this.keycode=keycode;
	}
}
