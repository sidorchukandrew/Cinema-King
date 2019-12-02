package com.isc329;

import javafx.scene.text.Text;

public class LoginViewValues {

	private static double logoYPos;
	private static double logoXPos;
	
	public static double getLogoY() {
		return Main.height - (Main.height * .9);
	}
	
	public static double getLogoX(Text t) {
		return (Main.width / 2) - (t.getLayoutBounds().getWidth() / 2);
	}
}
