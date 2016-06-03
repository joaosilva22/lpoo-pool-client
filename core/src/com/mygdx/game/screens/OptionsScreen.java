package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.PoolGameClient;

/**
 * Created by joaopsilva on 03-06-2016.
 */
public class OptionsScreen implements Screen {
    final PoolGameClient game;

    private Stage stage;
    private Table table;

    public OptionsScreen(final PoolGameClient game) {
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

        TextField.TextFieldStyle fldStyle = new TextField.TextFieldStyle();
        fldStyle.fontColor = Color.BLACK;
        fldStyle.font = fontMain;
        fldStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("text-field.png"))));
        fldStyle.background.setLeftWidth(fldStyle.background.getLeftWidth() + 10);
        fldStyle.background.setRightWidth(fldStyle.background.getRightWidth() + 10);

        // TODO: fazer dispose destas texturas
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("text-button-up.png"))));
        btnStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("text-button-down.png"))));
        btnStyle.checked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("text-button-down.png"))));
        btnStyle.font = fontMain;

        // Criacao dos actors ------------------------------
        Label lblTitle = new Label("Options Menu", lblTitleStyle);
        Label lblNickname = new Label("Nickname :", lblMainStyle);
        Label lblAdress = new Label("IP Adress :", lblMainStyle);
        Label lblPort = new Label("Port :", lblMainStyle);
        Label fldSound = new Label("Sound : ", lblMainStyle);

        // TODO: gravar as opcoes entre sessoes (?)
        final TextField fldNickname = new TextField(game.name, fldStyle);
        fldNickname.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.name = fldNickname.getText();
            }
        });

        final TextField fldAdress = new TextField(game.IPAdress, fldStyle);
        fldAdress.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.IPAdress = fldAdress.getText();
            }
        });

        final TextField fldPort = new TextField(Integer.toString(game.port), fldStyle);
        fldPort.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (fldPort.getText().length() > 0) game.port = Integer.parseInt(fldPort.getText());
            }
        });

        //TODO: fazer o toggle do som de verdade
        final TextButton btnSound = new TextButton("ON", btnStyle);
        btnSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (btnSound.isChecked()) btnSound.setText("OFF");
                else btnSound.setText("ON");
            }
        });

        TextButton btnMainMenu = new TextButton("Main Menu", btnStyle);
        btnMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Layout dos actors ------------------------------
        int spacing = (int) (Gdx.graphics.getHeight() * 0.015);
        int width = (int) (Gdx.graphics.getWidth() * 0.5), height = (int) (Gdx.graphics.getWidth() * 0.1);
        int titleSpacing = (int) (Gdx.graphics.getHeight() * 0.05);

        table.top();
        table.add(lblTitle).colspan(2).padTop(titleSpacing).padBottom(titleSpacing / 2);

        table.columnDefaults(0).pad(spacing).padRight(0).right();
        table.columnDefaults(1).pad(spacing).width(width).height(height);

        table.row();
        table.add(lblNickname);
        table.add(fldNickname);

        table.row();
        table.add(lblAdress);
        table.add(fldAdress);

        table.row();
        table.add(lblPort);
        table.add(fldPort);

        table.row();
        table.add(fldSound);
        table.add(btnSound);

        table.row();
        table.add(btnMainMenu).colspan(2).padTop(titleSpacing).center().width(width).height(height * 1.5f);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
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
        stage.dispose();
    }
}
