package fr.veridiangames.main.game;

import fr.veridiangames.main.game.world.World;

public class Game {

	World world = new World();

	public Game() {

	}

	public void update() {
		world.update();
	}

	public void render() {
		world.render();
	}
}
