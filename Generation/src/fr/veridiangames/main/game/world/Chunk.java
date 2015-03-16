package fr.veridiangames.main.game.world;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.veridiangames.main.Main;
import fr.veridiangames.main.rendering.Texture;
import fr.veridiangames.main.rendering.model.OBJModel;
import fr.veridiangames.utils.Vector3f;

public class Chunk {
	public static final int SIZE = 16;
	
	public int x, y;
	float[][] noise;
	Random rand;
	
	private int terrain;
	private List<OBJModel> trees;
	
	public Chunk(int x, int y, Noise noise) {
		this.x = x;
		this.y = y;
		this.noise = new float[SIZE + 1][SIZE + 1];
		rand = new Random();
		
		trees = new ArrayList<OBJModel>();
		for (int i = 0; i < rand.nextInt(5) + 5; i++) {
			trees.add(new OBJModel("/tree.obj"));
		}
		
		for (int xx = 0; xx < SIZE + 1; xx++) {
			for (int yy = 0; yy < SIZE + 1; yy++) {
				int nx = xx + x * SIZE;
				int ny = yy + y * SIZE;
				this.noise[xx][yy] = noise.getNoise(nx, ny);
			}
		}
		
		compile();
	}
	
	public void compile() {
		terrain = glGenLists(1);
		glNewList(terrain, GL_COMPILE);
		glBegin(GL_QUADS);
		for (int x = 0; x < SIZE; x++) {
			for (int y = 0; y < SIZE; y++) {
				float xx = x + this.x * SIZE;
				float yy = y + this.y * SIZE;
				
				float h00 = noise[x][y];
				float h10 = noise[x + 1][y];
				float h11 = noise[x + 1][y + 1];
				float h01 = noise[x][y + 1];
				
				glTexCoord2f(0, 0); glVertex3f(xx, h00, yy);
				glTexCoord2f(1, 0); glVertex3f(xx + 1, h10, yy);
				glTexCoord2f(1, 1); glVertex3f(xx + 1, h11, yy + 1);
				glTexCoord2f(0, 1); glVertex3f(xx, h01, yy + 1);
			}	
		}
		glEnd();
		glEndList();
	}
	
	public void update() {

	}
	
	public boolean isPlayerInside() {
		Vector3f cam = Main.i.cam;
		if (    cam.x >= x * SIZE && 
				cam.z >= y * SIZE && 
				cam.x < x * SIZE + SIZE && 
				cam.z < y * SIZE + SIZE) {
			return true;
		}
		return false;
	}
	
	public void render() {
		Texture.grass.bind();
		glCallList(terrain);
		
		Texture.tree.bind();
		Random r = new Random(SIZE);
		for (int i = 0; i < trees.size(); i++) {
			int nx = r.nextInt(SIZE);
			int ny = r.nextInt(SIZE);
			float x = this.x * SIZE + nx;
			float y = this.y * SIZE + ny;
			glPushMatrix();
			glTranslatef(x, noise[nx][ny], y);
			trees.get(i).render();
			glPopMatrix();
		}
	}
	
	public float getTerrainHeight(float xp, float yp) {
		float x = xp - this.x * SIZE;
		float y = yp - this.y * SIZE;
		
		int xx = (int) Math.floor(x);
		int yy = (int) Math.floor(y);
		float xc = (x % SIZE) % 1;
		float zc = (y % SIZE) % 1;

		float height;
		float a = noise[xx][yy];
		float b = noise[xx + 1][yy];
		float c = noise[xx][yy + 1];
		float d = noise[xx + 1][yy + 1];
		
		height = interpolate(a, b, c, d, xc, zc);
		
		return height;
	}
	
	private float lerp(float s, float e, float t) {
		return s + (e - s) * t;
	}
	
	private float interpolate(float x00, float x10, float x01, float x11, float tx, float ty) {
		return lerp(lerp(x00, x10, tx), lerp(x01, x11, tx), ty);
	}
	
	private float cos(float v) { return (float) Math.cos(v); }
	private float sin(float v) { return (float) Math.sin(v); }
	private float tan(float v) { return (float) Math.tan(v); }
}