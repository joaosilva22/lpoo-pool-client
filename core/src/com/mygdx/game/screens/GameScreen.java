package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PoolGameClient;
import com.mygdx.game.manager.StateManager;
import com.mygdx.game.manager.states.GameStates;
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
    private Texture bannerTexture;
    private Sprite banner;
    private BitmapFont font, fontSmall;

    private Vector3 touchPosition;
    private Vector3 firstTouch;
    private float directionRegionY, ballRegionY;

    private StateManager<GameScreen, GameStates> stateManager;
    private Client client;

    // Debugging ------------------------------
    private ShapeRenderer shapeRenderer;

    public GameScreen(final PoolGameClient game) {
        this.game = game;
        stateManager = new StateManager<GameScreen, GameStates>(this, GameStates.WAITING_SERVER);

        camera = new OrthographicCamera(game.VIEWPORT_WIDTH, game.VIEWPORT_HEIGHT);
        camera.position.set(game.VIEWPORT_WIDTH / 2, game.VIEWPORT_HEIGHT / 2, 0);
        camera.update();

        // Objectos da cena ------------------------------
        // (Devem ser feitos os disposes necessarios.)
        cueBall = new CueBall(game.VIEWPORT_WIDTH / 2, game.VIEWPORT_HEIGHT / 2);
        cue = new Cue(game.VIEWPORT_WIDTH / 2, cueBall.getSprite().getY() - 32);

        bannerTexture = new Texture(Gdx.files.internal("banner.png"));
        banner = new Sprite(bannerTexture);
        float ratio = banner.getHeight() / banner.getWidth();
        banner.setSize(game.VIEWPORT_WIDTH, ratio * game.VIEWPORT_WIDTH);
        banner.setPosition(0, game.VIEWPORT_HEIGHT / 2);

        int fontSize = (int) (game.VIEWPORT_HEIGHT * 0.04);
        font = game.generateFont(fontSize);
        int fontSizeSmall = (int) (game.VIEWPORT_HEIGHT * 0.03);
        fontSmall = game.generateFont(fontSizeSmall);

        // Variaveis de input ------------------------------
        touchPosition = new Vector3();
        firstTouch = new Vector3();
        directionRegionY = cueBall.getSprite().getY() + cueBall.getSprite().getHeight();
        ballRegionY = cueBall.getSprite().getY() - 32;

        // Conectar ao servidor ------------------------------
        // Se a conexao falhar o state passa a um estado de erro.
        // E apresentada uma mensagem explicativa, e o utilizador pode voltar ao menu.
        try {
            client = new Client(game.IPAdress, game.port, this);
        } catch (GdxRuntimeException e) {
            System.out.println("Failed to connect to to server...");
            stateManager.setState(GameStates.ERROR);
        }

        // Informar o servidor do nickname escolhido
        Message message = new Message("connect");
        message.addArgument("name", game.name);
        if (client != null) client.write(message.toJson());

        Gdx.input.setInputProcessor(this);

        // Debugging ------------------------------
        // TODO: eliminar ou esconder quando nao for preciso
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        camera.update();

        stateManager.update(delta);
        cue.update(delta);
        cueBall.update(delta);

        Gdx.gl.glClearColor(0, 0.5f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        cueBall.render(game.batch);
        cue.render(game.batch);

        // Em caso de erro ao ligar ao servidor (!)
        // (Estado do jogo ERROR)
        if (stateManager.getState().equals(GameStates.ERROR)) {
            banner.draw(game.batch);
            float mainTextY = game.VIEWPORT_HEIGHT / 2 + banner.getHeight() * 0.65f;
            float smallTextY = game.VIEWPORT_HEIGHT / 2 + banner.getHeight() * 0.40f;
            font.draw(game.batch, "Failed to connect to server.", 0, mainTextY, game.VIEWPORT_WIDTH, Align.center, true);
            fontSmall.draw(game.batch, "Tap anywhere to go back", 0, smallTextY, game.VIEWPORT_WIDTH, Align.center, true);
        }

        // Enquanto espera que o oponente se conecte ao servidor
        // (Estado do jogo WAITING_SERVER)
        if (stateManager.getState().equals(GameStates.WAITING_SERVER)) {
            banner.draw(game.batch);
            float mainTextY = game.VIEWPORT_HEIGHT / 2 + banner.getHeight() * 0.65f;
            font.draw(game.batch, "Waiting for your opponent to connect.", 0, mainTextY, game.VIEWPORT_WIDTH, Align.center, true);
        }

        // Enquanto o cliente espera pela sua vez de jogar
        // (Estado do jogo WAITING_OPPONENT)
        if (stateManager.getState().equals(GameStates.WAITING_OPPONENT)) {
            banner.draw(game.batch);
            float mainTextY = game.VIEWPORT_HEIGHT / 2 + banner.getHeight() * 0.65f;
            float smallTextY = game.VIEWPORT_HEIGHT / 2 + banner.getHeight() * 0.40f;
            font.draw(game.batch, "Please wait.", 0, mainTextY, game.VIEWPORT_WIDTH, Align.center, true);
            fontSmall.draw(game.batch, "...", 0, smallTextY, game.VIEWPORT_WIDTH, Align.center, true);
        }

        // Caso tenha ganho o jogo
        // (Estado do jogo WON)
        if (stateManager.getState().equals(GameStates.WON)) {
            banner.draw(game.batch);
            float mainTextY = game.VIEWPORT_HEIGHT / 2 + banner.getHeight() * 0.65f;
            float smallTextY = game.VIEWPORT_HEIGHT / 2 + banner.getHeight() * 0.40f;
            font.draw(game.batch, "Victory!", 0, mainTextY, game.VIEWPORT_WIDTH, Align.center, true);
            fontSmall.draw(game.batch, "Tap anywhere to go back", 0, smallTextY, game.VIEWPORT_WIDTH, Align.center, true);
        }

        // Caso tenha perdido o jogo
        // (Estado do jogo LOST)
        if (stateManager.getState().equals(GameStates.LOST)) {
            banner.draw(game.batch);
            float mainTextY = game.VIEWPORT_HEIGHT / 2 + banner.getHeight() * 0.65f;
            float smallTextY = game.VIEWPORT_HEIGHT / 2 + banner.getHeight() * 0.40f;
            font.draw(game.batch, "Defeat ...", 0, mainTextY, game.VIEWPORT_WIDTH, Align.center, true);
            fontSmall.draw(game.batch, "Tap anywhere to go back", 0, smallTextY, game.VIEWPORT_WIDTH, Align.center, true);
        }

        // Caso perca a ligacao ao servidor
        // Muda o estado para ERROR
        if (client != null) {
            if (!client.isConnected() && !stateManager.getState().equals(GameStates.ERROR)) {
                client.disconnect();
                stateManager.setState(GameStates.ERROR);
            }
        }

        game.batch.end();

        // Debugging ------------------------------
        // TODO: eliminar ou esconder quando nao for preciso
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
        bannerTexture.dispose();
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
        if (touchPosition.y > directionRegionY && stateManager.getState().equals(GameStates.PLAYING)) {
            firstTouch.set(touchPosition);
            return true;
        }

        // Angulo de incidencia da tacada ------------------------------
        // TODO: implementar duplo clique para centrar o 'spin'
        if (touchPosition.y > ballRegionY && touchPosition.y < directionRegionY && stateManager.getState().equals(GameStates.PLAYING)) {
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
        if (touchPosition.y < ballRegionY && stateManager.getState().equals(GameStates.PLAYING)) {
            firstTouch.set(touchPosition);
            return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        camera.unproject(touchPosition.set(screenX, screenY, 0));

        // Forca da tacada ------------------------------
        if (touchPosition.y < ballRegionY && stateManager.getState().equals(GameStates.PLAYING)) {
            float impulseMultiplier = 1 - (touchPosition.y / ballRegionY);
            cue.setImpulseMultiplier(impulseMultiplier, false);

            // Fazer a jogada se o impulseMultiplier for maior que um certo treshold
            // Muda o estado para 'WAITING'
            // TODO: Substituir este threshold por uma constante
            if (impulseMultiplier > 0.1) {
                Message message = new Message("play");
                message.addArgument("impulse", impulseMultiplier);
                message.addArgument("direction", cueBall.getDirection());
                message.addArgument("spin", cueBall.getHitAngle());
                // TODO: checkar a conexao antes de fazer a jogada e mudar de estado se necessario
                if (client != null) client.write(message.toJson());
                stateManager.setState(GameStates.WAITING_OPPONENT);
            }
        }

        // Voltar ao menu principal
        // Em caso de erro (ERROR)
        if (stateManager.getState().equals(GameStates.ERROR)) {
            game.setScreen(new MainMenuScreen(game));
        }

        // Voltar ao menu principal
        // Em caso de vitoria (WON)
        if (stateManager.getState().equals(GameStates.WON)) {
            client.disconnect();
            game.setScreen(new MainMenuScreen(game));
        }

        // Voltar ao menu principal
        // Em caso de derrota (LOST)
        if (stateManager.getState().equals(GameStates.LOST)) {
            client.disconnect();
            game.setScreen(new MainMenuScreen(game));
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector3 dragPosition = new Vector3(screenX, screenY, 0);
        camera.unproject(dragPosition);

        // Direcao da tacada ------------------------------
        if (firstTouch.y > directionRegionY && dragPosition.y > directionRegionY && stateManager.getState().equals(GameStates.PLAYING)) {
            Vector3 delta = dragPosition.cpy().sub(firstTouch);
            cueBall.setDirection(cueBall.getDirection() + (delta.x / 100));
            firstTouch.set(dragPosition);

            // Informar o servidor da direcao atual da tacada
            // (Escrito sempre que o utilizador aponta numa nova direcao.)
            Message message = new Message("aim");
            message.addArgument("direction", cueBall.getDirection());
            if (client != null) client.write(message.toJson());
            return true;
        }

        // Angulo de incidencia da tacada ------------------------------
        if (dragPosition.y > ballRegionY && dragPosition.y < directionRegionY && stateManager.getState().equals(GameStates.PLAYING)) {
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
        if (firstTouch.y < ballRegionY && dragPosition.y < ballRegionY && stateManager.getState().equals(GameStates.PLAYING)) {
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

    public void handleMessage(Message message) {
        stateManager.handleMessage(message);
    }

    public StateManager<GameScreen, GameStates> getStateManager() {
        return stateManager;
    }

    public Client getClient() {
        return client;
    }

    public CueBall getCueBall() {
        return cueBall;
    }
}
