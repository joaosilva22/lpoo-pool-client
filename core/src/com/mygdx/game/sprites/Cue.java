package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by joaopsilva on 03-06-2016.
 */
public class Cue {
    private Texture texture;
    private Sprite sprite;

    private Vector2 defaultPosition;
    private float impulseMultiplier;
    private float positionMultiplier;
    private boolean dragging;
    private float cueSpeed;
    private float impulseMultiplierTreshold;

    public Cue(float x, float y) {
        texture = new Texture(Gdx.files.internal("cue.png"));
        sprite = new Sprite(texture);
        sprite.setPosition(x - sprite.getWidth() / 2, y - sprite.getHeight());

        defaultPosition = new Vector2(sprite.getX(), sprite.getY());
        impulseMultiplier = 0.0f;
        positionMultiplier = 0.0f;
        dragging = false;
        cueSpeed = 7.5f;
        impulseMultiplierTreshold = 0.1f;
    }

    public void update(float delta) {
        // Desenhar o taco na posicao determinada por positionMultiplier
        sprite.setPosition(defaultPosition.x, defaultPosition.y - positionMultiplier * (defaultPosition.y + sprite.getHeight()));

        // Largar o taco sem efetuar a jogada.
        // (Porque o impulseMultiplier esta abaixo de um certo treshold.)
        if (!dragging && impulseMultiplier < impulseMultiplierTreshold) {
            if (positionMultiplier - cueSpeed * delta >= 0)
                positionMultiplier = positionMultiplier - cueSpeed * delta;
            else {
                positionMultiplier = 0;
                impulseMultiplier = 0;
            }
        }

        // Largar o taco e efetuar a jogada.
        // (O impulseMultiplier e suficiente para fazer a tacada.)
        if (!dragging && impulseMultiplier > impulseMultiplierTreshold) {
            if (positionMultiplier - cueSpeed * delta >= 0)
                positionMultiplier = positionMultiplier - cueSpeed * delta;
            else {
                positionMultiplier = 0;
                impulseMultiplier = 0;
            }
        }
    }

    public void render(SpriteBatch batch) {
        sprite.draw(batch);
    }

    public void setImpulseMultiplier(float impulseMultiplier, boolean dragging) {
        this.dragging = dragging;
        this.positionMultiplier = this.impulseMultiplier;
        this.impulseMultiplier = impulseMultiplier;
    }

    public void dispose() {
        texture.dispose();
    }

    public float getImpulseMultiplier() {
        return impulseMultiplier;
    }
}
