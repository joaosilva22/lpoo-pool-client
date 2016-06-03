package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.mygdx.game.screens.MainMenuScreen;
import com.mygdx.game.screens.OptionsScreen;

public class PoolGameClient extends Game {
	public SpriteBatch batch;
	public FreeTypeFontGenerator generator;

	public static final float VIEWPORT_WIDTH = 360;
	public static final float VIEWPORT_HEIGHT = 640;

	public String name;
	public String IPAdress;
	public int port;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/AlexandriaFLF-Bold.ttf"));

		name = "Player";
		IPAdress = "XXX.XX.XX.XX";
		port = 4444;

		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}

	public BitmapFont generateFont(int size) {
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = size;
		return generator.generateFont(parameter);
	}

	public void dispose() {
		batch.dispose();
		generator.dispose();
	}
}
