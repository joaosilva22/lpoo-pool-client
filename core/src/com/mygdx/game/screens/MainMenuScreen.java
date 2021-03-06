package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.PoolGameClient;

/**
 * Created by joaopsilva on 03-06-2016.
 */
public class MainMenuScreen implements Screen {
    final PoolGameClient game;

    private Stage stage;
    private Table table;

    public MainMenuScreen(final PoolGameClient game) {
        this.game = game;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Tamanhos das fonts ------------------------------
        int fontTitleSize = (int) (Gdx.graphics.getHeight() * 0.06);
        BitmapFont fontTitle = game.generateFont(fontTitleSize);
        int fontMainSize = (int) (Gdx.graphics.getHeight() * 0.04);
        BitmapFont fontMain = game.generateFont(fontMainSize);

        // Styles dos actors ------------------------------
        Label.LabelStyle lblMainStyle = new Label.LabelStyle();
        lblMainStyle.font = fontMain;

        Label.LabelStyle lblTitleStyle = new Label.LabelStyle();
        lblTitleStyle.font = fontTitle;

        // TODO: fazer dispose destas texturas
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("text-button-up.png"))));
        btnStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("text-button-down.png"))));
        btnStyle.checked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("text-button-down.png"))));
        btnStyle.font = fontMain;

        // Criacao dos actors ------------------------------
        Label lblTitle = new Label("Pool Game", lblTitleStyle);

        TextButton btnPlay = new TextButton("Join Game", btnStyle);
        btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        TextButton btnOptions = new TextButton("Options", btnStyle);
        btnOptions.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new OptionsScreen(game));
            }
        });

        // Layout dos actors ------------------------------
        int spacing = (int) (Gdx.graphics.getHeight() * 0.015);
        int width = (int) (Gdx.graphics.getWidth() * 0.5), height = (int) (Gdx.graphics.getWidth() * 0.1);
        int titleSpacing = (int) (Gdx.graphics.getHeight() * 0.05);

        table.top();
        table.add(lblTitle).colspan(2).padTop(titleSpacing).padBottom(titleSpacing / 2);

        table.columnDefaults(0).pad(spacing).padRight(0).width(width).height(height);

        table.row();
        table.add(btnPlay);

        table.row();
        table.add(btnOptions);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.5f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
