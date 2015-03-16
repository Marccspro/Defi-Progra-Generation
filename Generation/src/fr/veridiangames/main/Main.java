package fr.veridiangames.main;

import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import fr.veridiangames.main.game.Game;
import fr.veridiangames.main.rendering.Gui;
import fr.veridiangames.utils.Vector3f;

public class Main {
	
	public static Main i;
	Game game;
	
	public Vector3f cam = new Vector3f(64, 32, 32);
	public Vector3f camRot = new Vector3f();
	
	public Main() {
		i = this;
		Mouse.setGrabbed(true);
		game = new Game();
	}
	
	float speed;
	
	public void update() {
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && Mouse.isGrabbed()) Mouse.setGrabbed(false);
		//if (Mouse.isButtonDown(0) && !Mouse.isGrabbed()) Mouse.setGrabbed(true);
		if (!Mouse.isGrabbed()) return;
		
		game.update();
		
		camRot.x -= Mouse.getDY() * 0.5f;
		camRot.y += Mouse.getDX() * 0.5f;
		
		if (camRot.x >= 90) {
			camRot.x = 90;
		}
		if (camRot.x <= -90) {
			camRot.x = -90;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
		{
			speed = 0.8f;
		}
		else
		{
			speed = 0.1f;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_Z)) {
			cam.z -= Math.cos(Math.toRadians(camRot.y)) * speed;
			cam.x += Math.sin(Math.toRadians(camRot.y)) * speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			cam.z += Math.cos(Math.toRadians(camRot.y)) * speed;
			cam.x -= Math.sin(Math.toRadians(camRot.y)) * speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
			cam.z += Math.cos(Math.toRadians(camRot.y + 90)) * speed;
			cam.x -= Math.sin(Math.toRadians(camRot.y + 90)) * speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			cam.z -= Math.cos(Math.toRadians(camRot.y + 90)) * speed;
			cam.x += Math.sin(Math.toRadians(camRot.y + 90)) * speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			cam.y+=speed;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
			cam.y-=speed;
		}
	}
	
	public void render() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		GLU.gluPerspective(70.0f, (float)Display.getWidth() / (float)Display.getHeight(), 0.1f, 1000.0f);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glRotatef(camRot.x, 1, 0, 0);
		glRotatef(camRot.y, 0, 1, 0);
		glTranslatef(-cam.x, -cam.y - 1, -cam.z);
		
		game.render();
		
		if (Mouse.isGrabbed()) return;
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		int mid = Display.getWidth() / 2;
		Gui.drawText("Pause", mid - 160, Display.getHeight() / 4, 64);
		if (Gui.button("Reprendre", mid - 160, 90 + Display.getHeight() / 4)) {
			Mouse.setGrabbed(true);
		}
	}
	
	//---
	private boolean running = false;
	
	private void start() {
		running = true;
		loop();
	}
	
	private void loop() {
		long before = System.nanoTime();
		double ns = 1000000000.0 / 60.0;
		
		int ticks = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while (running) {
			if (Display.isCloseRequested()) break;
			
			long now = System.nanoTime();
			if (now - before > ns) {
				update();
				ticks++;
				before += ns;
			}else {
				glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
				glClearColor(222.0f / 255.0f, 244.0f / 255.0f, 250.0f / 255.0f, 1.0f);
				if (Display.wasResized()) {
					glViewport(0, 0, Display.getWidth(), Display.getHeight());
				}
				render();
				frames++;
				
				Display.update();
			}
			
			if (System.currentTimeMillis() - timer > 1000) {
				Display.setTitle("Defi Generation || " + ticks + " ticks, " + frames + " fps");
				ticks = 0;
				frames = 0;
				timer += 1000;
			}
		}
		Display.destroy();
		System.exit(0);
	}
	
	public static void main(String[] args) {
		display();
		new Main().start();
	}
	
	private static void display() {
		try {
			Display.setTitle("Defi Generation");
			Display.setResizable(true);
			Display.setDisplayMode(new DisplayMode(1280, 720));
			Display.create();
			
			glEnable(GL_DEPTH_TEST);
			glEnable(GL_ALPHA_TEST);
			glAlphaFunc(GL_GREATER, 0);
			
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			glEnable(GL_TEXTURE_2D);
			
			float[] fogFloatColor = new float[] {218.0f / 255.0f, 235.0f / 255.0f, 240.0f / 255.0f, 1.0f};
			FloatBuffer fogColor = (FloatBuffer) BufferUtils.createFloatBuffer(4).put(fogFloatColor).flip();
			
			glEnable(GL_FOG);
			glFog(GL_FOG_COLOR, fogColor);
			glFogf(GL_FOG_MODE, GL_LINEAR);
			glFogf(GL_FOG_START, 0);
			glFogf(GL_FOG_END, 50);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
}
