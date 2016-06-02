package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * Created by joaopsilva on 02-06-2016.
 */
public class CueBall {
    private Texture ballTex, hitmarkerTex;
    private Sprite ball, hitmarker;

    private Texture rotIndicatorTex, dirIndicatorTex;
    private Sprite rotIndicator, dirIndicator;

    private float hitAngle;
    private float direction;

    public CueBall(float x, float y) {
        ballTex = new Texture(Gdx.files.internal("cue-ball.png"));
        ball = new Sprite(ballTex);
        ball.setSize(192, 192);
        ball.setCenter(x, y + ball.getHeight() / 4);

        hitmarkerTex = new Texture(Gdx.files.internal("hit-mark.png"));
        hitmarker = new Sprite(hitmarkerTex);
        hitmarker.setSize(32, 32);
        hitmarker.setCenter(ball.getX() + ball.getWidth() / 2, ball.getY());

        rotIndicatorTex = new Texture(Gdx.files.internal("rotation-indicator.png"));
        rotIndicator = new Sprite(rotIndicatorTex);
        rotIndicator.setSize(300, 300);
        rotIndicator.setAlpha(0.3f);
        rotIndicator.setCenter(x, y + ball.getHeight() / 4);
        rotIndicator.setOrigin(rotIndicator.getWidth() / 2, rotIndicator.getHeight() / 2);

        dirIndicatorTex = new Texture(Gdx.files.internal("direction-indicator.png"));
        dirIndicator = new Sprite(dirIndicatorTex);
        dirIndicator.setAlpha(0.3f);
        dirIndicator.setPosition(x, y + ball.getHeight() / 2);

        hitAngle = (float) (3*Math.PI) / 2;
        direction = (float) Math.PI / 2;
    }

    public void update(float delta) {
        float xpos = ball.getX() + ball.getWidth()/2 + MathUtils.cos(hitAngle) * ball.getWidth()/2;
        float ypos = ball.getY() + ball.getHeight()/2 + MathUtils.sin(hitAngle) * ball.getHeight()/2;
        hitmarker.setCenter(xpos, ypos);

        rotIndicator.setRotation(direction);
    }

    public void render(SpriteBatch batch) {
        rotIndicator.draw(batch);
        dirIndicator.draw(batch);
        ball.draw(batch);
        hitmarker.draw(batch);
    }

    public Sprite getSprite() {
        return ball;
    }

    public void setHitAngle(float angle) {
        this.hitAngle = angle;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public void dispose() {
        ballTex.dispose();
        hitmarkerTex.dispose();
        rotIndicatorTex.dispose();
        dirIndicatorTex.dispose();
    }
}
