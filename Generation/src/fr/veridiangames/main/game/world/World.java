package fr.veridiangames.main.game.world;

import java.util.Random;

import fr.veridiangames.main.Main;
import fr.veridiangames.utils.Vector3f;

public class World {
	public static final float GRAVITY = 10f / 120.0f;
	
	int size = 5;
	Chunk[][] chunks;
	Noise noise;
	Chunk playerChunk;
	
	public World() {
		noise = new Noise(new Random().nextLong(), 10, 5);
		chunks = new Chunk[size][size];
		for (int x = 0; x < chunks.length; x++) {
			for (int y = 0; y < chunks.length; y++) {
				chunks[x][y] = new Chunk(x, y, noise);
			}
		}
	}

	public void update() {
		Vector3f cam = Main.i.cam;
		for (int x = 0; x < chunks.length; x++) {
			for (int y = 0; y < chunks.length; y++) {
				chunks[x][y].update();
				if (chunks[x][y].isPlayerInside()) {
					playerChunk = chunks[x][y];
				}
			}
		}
		cam.y -= GRAVITY;
		if (playerChunk != null) {
			if (cam.y < playerChunk.getTerrainHeight(cam.x, cam.z)) {
				cam.y = playerChunk.getTerrainHeight(cam.x, cam.z);
			}
		}
	}

	public void render() {
		for (int x = 0; x < chunks.length; x++) {
			for (int y = 0; y < chunks.length; y++) {
				chunks[x][y].render();
			}
		}
	}
}
