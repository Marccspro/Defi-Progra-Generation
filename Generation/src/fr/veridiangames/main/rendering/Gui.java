package fr.veridiangames.main.rendering;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;

public class Gui {
	
	private static String chars = "" + //
								  "ABCDEFGHIJKLMNOPQRSTUVWXYZ      " + //
								  "0123456789.,!?'\"-+=/\\%()<>:;     " + //
								  "";
	
	
	public static void drawText(String text, int x, int y, int size) {
		String msg = text.toUpperCase();
		glColor4f(1, 1, 1, 1);
		Texture.font.bind();
		glBegin(GL_QUADS);
		for (int i = 0; i < msg.length(); i++) {
			int ix = chars.indexOf(msg.charAt(i));
			int iy = 0;
			if (ix >= 32) iy = 1;
			if (iy >= 0) {
				if (ix >= 0) {
					float xx = x + i * size;
					float yy = y;
					
					float yo = iy;
					float xo = ix % 32;
					float texSize = 32.0f;
					
					glTexCoord2f((1 + xo) / texSize, (0 + yo) / 2.0f); glVertex2f(xx + size, yy);
					glTexCoord2f((0 + xo) / texSize, (0 + yo) / 2.0f); glVertex2f(xx, yy);
					glTexCoord2f((0 + xo) / texSize, (1 + yo) / 2.0f); glVertex2f(xx, yy + size);
					glTexCoord2f((1 + xo) / texSize, (1 + yo) / 2.0f); glVertex2f(xx + size, yy + size);
				}
			}
		}
		
		glEnd();
	}
	
	public static boolean button(String text, int x, int y) {
		boolean pressed = false;
		
		int mx = Mouse.getX();
		int my = Display.getHeight() - Mouse.getY();
		
		int h = 32;
		int w = text.length() * 24 + 100;
		
		drawText(text, x + w / 2 - (text.length() * 24) / 2, y + h / 2 - 12, 24);
		Texture.unbind();

		glColor4f(0.5f, 0.5f, 0.5f, 1);
		if (mx > x && my > y && mx <= x + w && my <= y + h) {
			glColor4f(0.8f, 0.5f, 0.5f, 1);
			if (Mouse.isButtonDown(0)) {
				glColor4f(1, 1, 1, 1);
				pressed = true;
			}
		}
		
		glBegin(GL_QUADS);
			glVertex2f(x, y);
			glVertex2f(x + w, y);
			glVertex2f(x + w, y + h);
			glVertex2f(x, y + h);

			glColor4f(0, 0, 0, 1);
			glVertex2f(x - 3, y - 3);
			glVertex2f(x + w + 3, y - 3);
			glVertex2f(x + w + 3, y + h + 3);
			glVertex2f(x - 3, y + h + 3);
		glEnd();
		
		glColor4f(1, 1, 1, 1);
		
		return pressed;
	}
}
