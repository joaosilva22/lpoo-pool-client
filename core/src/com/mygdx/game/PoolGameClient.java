package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.screens.GameScreen;

public class PoolGameClient extends Game {
	public SpriteBatch batch;

	public static final float VIEWPORT_WIDTH = 360;
	public static final float VIEWPORT_HEIGHT = 640;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public void dispose() {
		batch.dispose();
	}
}
