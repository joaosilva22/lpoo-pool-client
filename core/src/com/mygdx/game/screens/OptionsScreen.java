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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
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

        // Remover teclado do ecra (android) ------------------------------
        // Cria um botao invisivel, que ocupa o ecra inteiro.
        // Quando clicado, faz com que o teclado virtual desapareca
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = null;
        style.down = null;
        ImageButton cancelFocusButton = new ImageButton(style);
        cancelFocusButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                stage.unfocusAll();
            }
        });

        table = new Table();
        Stack stack = new Stack();
        stack.addActor(cancelFocusButton);
        stack.addActor(table);
        stack.setFillParent(true);

        stage.addActor(stack);

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
        fldStyle.cursor = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("cursor.png"))));

        // TODO: fazer dispose destas texturas
        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("text-button-up.png"))));
        btnStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("text-button-down.png"))));
        btnStyle.checked = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("text-button-down.png"))));
        btnStyle.font = fontMain;

        // Criacao dos actors ------------------------------
        Label lblTitle = new Label("Options Menu", lblTitleStyle);
        lblTitle.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                stage.unfocusAll();
            }
        });

        Label lblNickname = new Label("Nickname :", lblMainStyle);
        lblNickname.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                stage.unfocusAll();
            }
        });

        Label lblAdress = new Label("IP Adress :", lblMainStyle);
        lblAdress.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                stage.unfocusAll();
            }
        });

        Label lblPort = new Label("Port :", lblMainStyle);
        lblPort.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                stage.unfocusAll();
            }
        });

        Label lblSound = new Label("Sound : ", lblMainStyle);
        lblSound.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                stage.unfocusAll();
            }
        });

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
                if (btnSound.isChecked()) {
                    btnSound.setText("OFF");
                    game.volume = 0.0f;
                } else {
                    btnSound.setText("ON");
                    game.volume = 1.0f;
                }
            }
        });

        TextButton btnMainMenu = new TextButton("Main Menu", btnStyle);
        btnMainMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setOnscreenKeyboardVisible(false);
                stage.unfocusAll();
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
        table.add(lblSound);
        table.add(btnSound);

        table.row();
        table.add(btnMainMenu).colspan(2).padTop(titleSpacing).center().width(width).height(height * 1.5f);
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
        stage.dispose();
    }
}
