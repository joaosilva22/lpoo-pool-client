package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PoolGameClient;
import com.mygdx.game.network.Client;
import com.mygdx.game.network.Message;
import com.mygdx.game.sprites.Cue;
import com.mygdx.game.sprites.CueBall;

/**
 * Created by joaopsilva on 02-06-2016.
 */
public class GameScreen implements Screen, InputProcessor {
    final PoolGameClient game;

    private OrthographicCamera camera;

    private CueBall cueBall;
    private Cue cue;

    private Vector3 touchPosition;
    private Vector3 firstTouch;
    private float directionRegionY, ballRegionY, cueRegionY;

    private Client client;

    // Debugging ------------------------------
    private ShapeRenderer shapeRenderer;

    public GameScreen(final PoolGameClient game) {
        this.game = game;

        camera = new OrthographicCamera(game.VIEWPORT_WIDTH, game.VIEWPORT_HEIGHT);
        camera.position.set(game.VIEWPORT_WIDTH / 2, game.VIEWPORT_HEIGHT / 2, 0);
        camera.update();

        cueBall = new CueBall(game.VIEWPORT_WIDTH / 2, game.VIEWPORT_HEIGHT / 2);
        cue = new Cue(game.VIEWPORT_WIDTH / 2, cueBall.getSprite().getY() - 32);

        touchPosition = new Vector3();
        firstTouch = new Vector3();
        directionRegionY = cueBall.getSprite().getY() + cueBall.getSprite().getHeight();
        ballRegionY = cueBall.getSprite().getY() - 32;

        try {
            client = new Client("localhost", 4444);
        } catch (GdxRuntimeException e) {
            System.out.println("Failed to connecto to server...");
        }

        Gdx.input.setInputProcessor(this);

        // Debugging ------------------------------
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        cueBall.update(delta);
        cue.update(delta);
        camera.update();

        Gdx.gl.glClearColor(0, 0.5f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        cueBall.render(game.batch);
        cue.render(game.batch);
        game.batch.end();

        // Debugging ------------------------------
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(255, 0, 0, 255);
        shapeRenderer.line(0, ballRegionY, Gdx.graphics.getWidth(), ballRegionY);
        shapeRenderer.line(0, directionRegionY, Gdx.graphics.getWidth(), directionRegionY);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.update();
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
        cueBall.dispose();
        cue.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchPosition.set(screenX, screenY, 0));

        // Direcao da tacada ------------------------------
        if (touchPosition.y > directionRegionY) {
            firstTouch.set(touchPosition);
            return true;
        }

        // Angulo de incidencia da tacada ------------------------------
        // TODO: implementar duplo clique para centrar o 'spin'
        if (touchPosition.y > ballRegionY && touchPosition.y < directionRegionY) {
            float angle;
            float step = (float) Math.PI / cueBall.getSprite().getWidth();

            if (touchPosition.x < cueBall.getSprite().getX()) {
                angle = (float) Math.PI;
                cueBall.setHitAngle(angle);
                return true;
            }

            if (touchPosition.x > cueBall.getSprite().getX() + cueBall.getSprite().getWidth()) {
                angle = (float) Math.PI * 2;
                cueBall.setHitAngle(angle);
                return true;
            }

            angle = (float) Math.PI + (touchPosition.x - cueBall.getSprite().getX()) * step;
            cueBall.setHitAngle(angle);
            return true;
        }

        // Forca da tacada ------------------------------
        if (touchPosition.y < ballRegionY) {
            firstTouch.set(touchPosition);
            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchPosition.set(screenX, screenY, 0));

        // Forca da tacada ------------------------------
        if (touchPosition.y < ballRegionY) {
            float impulseMultiplier = 1 - (touchPosition.y / ballRegionY);
            cue.setImpulseMultiplier(impulseMultiplier, false);

            // fazer a jogada se o impulseMultiplier for maior que um certo treshold
            // TODO: Substituir este threshold por uma constante
            if (impulseMultiplier > 0.1) {
                Message message = new Message("play");
                message.addArgument("impulse", impulseMultiplier);
                message.addArgument("direction", cueBall.getDirection());
                message.addArgument("spin", cueBall.getHitAngle());
                if (client.isConnected()) client.write(message.toJson());
            }
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 dragPosition = new Vector3(screenX, screenY, 0);
        camera.unproject(dragPosition);

        // Direcao da tacada ------------------------------
        if (firstTouch.y > directionRegionY && dragPosition.y > directionRegionY) {
            Vector3 delta = dragPosition.cpy().sub(firstTouch);
            cueBall.setDirection(cueBall.getDirection() + (delta.x / 2));
            firstTouch.set(dragPosition);

            // Informar o servidor da direcao atual da tacada
            // (Escrito sempre que o utilizador aponta numa nova direcao.)
            Message message = new Message("aiming");
            message.addArgument("direction", cueBall.getDirection());
            if (client.isConnected()) client.write(message.toJson());
            return true;
        }

        // Angulo de incidencia da tacada ------------------------------
        if (dragPosition.y > ballRegionY && dragPosition.y < directionRegionY) {
            float angle;
            float step = (float) Math.PI / cueBall.getSprite().getWidth();

            if (dragPosition.x < cueBall.getSprite().getX()) {
                angle = (float) Math.PI;
                cueBall.setHitAngle(angle);
                return true;
            }

            if (dragPosition.x > cueBall.getSprite().getX() + cueBall.getSprite().getWidth()) {
                angle = (float) Math.PI * 2;
                cueBall.setHitAngle(angle);
                return true;
            }

            angle = (float) Math.PI + (dragPosition.x - cueBall.getSprite().getX()) * step;
            cueBall.setHitAngle(angle);
            return true;
        }


        // Forca da tacada ------------------------------
        if (firstTouch.y < ballRegionY && dragPosition.y < ballRegionY) {
            Vector3 delta = dragPosition.cpy().sub(firstTouch);
            float impulseMultiplier = 1 - (dragPosition.y / ballRegionY);
            cue.setImpulseMultiplier(impulseMultiplier, true);
            firstTouch.set(dragPosition);
            return true;
        }

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
