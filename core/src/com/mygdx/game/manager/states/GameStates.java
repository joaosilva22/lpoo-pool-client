package com.mygdx.game.manager.states;

import com.mygdx.game.network.Message;
import com.mygdx.game.screens.GameScreen;

/**
 * Created by joaopsilva on 05-06-2016.
 */
public enum GameStates implements State<GameScreen> {
    ERROR() {
        @Override
        public void enter(GameScreen entity) {
            // Debugging
            // TODO: apagar quando nao for necessario
            System.out.println("Entered ERROR ...");
        }

        @Override
        public void update(GameScreen entity, float delta) {

        }

        @Override
        public void exit(GameScreen entity) {
            // Debugging
            // TODO: apagar quando nao for necessario
            System.out.println("Left ERROR ...");
        }

        @Override
        public void handleMessage(GameScreen entity, Message message) {

        }
    },

    WAITING_SERVER() {
        @Override
        public void enter(GameScreen entity) {
            // Debugging
            // TODO: apagar quando nao for necessario
            System.out.println("Entered WAITING_SERVER ...");
        }

        @Override
        public void update(GameScreen entity, float delta) {

        }

        @Override
        public void exit(GameScreen entity) {
            // Debugging
            // TODO: apagar quando nao for necessario
            System.out.println("Left WAITING_SERVER ...");
        }

        @Override
        public void handleMessage(GameScreen entity, Message message) {
            // Muda o estado para 'WAITING'
            // Da inicio ao jogo
            if (message.getTag().equals("ready"))
                entity.getStateManager().setState(WAITING_OPPONENT);
        }
    },

    WAITING_OPPONENT() {
        @Override
        public void enter(GameScreen entity) {
            // Debugging
            // TODO: apagar quando nao for necessario
            System.out.println("Entered WAITING_OPPONENT ...");
        }

        @Override
        public void update(GameScreen entity, float delta) {

        }

        @Override
        public void exit(GameScreen entity) {
            // Debugging
            // TODO: apagar quando nao for necessario
            System.out.println("Left WAITING_OPPONENT ...");
        }

        @Override
        public void handleMessage(GameScreen entity, Message message) {
            // Muda o estado para 'PLAYING'
            // Inicia a jogada deste jogador
            if (message.getTag().equals("begin-turn"))
                entity.getStateManager().setState(PLAYING);

            // Termina o jogo em caso de vitoria
            if (message.getTag().equals("won"))
                entity.getStateManager().setState(WON);

            // Termina o jogo em caso de derrota
            if (message.getTag().equals("lost"))
                entity.getStateManager().setState(LOST);
        }
    },

    PLAYING() {
        @Override
        public void enter(GameScreen entity) {
            // Atualiza a direcao da bola no servidor
            // (Para reflectir a direcao dada no cliente.)
            Message aim = new Message("aim");
            aim.addArgument("direction", entity.getCueBall().getDirection());
            entity.getClient().write(aim.toJson());

            // Debugging
            // TODO: apagar quando nao for necessario
            System.out.println("Entered PLAYING ...");
        }

        @Override
        public void update(GameScreen entity, float delta) {

        }

        @Override
        public void exit(GameScreen entity) {
            // Debugging
            // TODO: apagar quando nao for necessario
            System.out.println("Left PLAYING ...");
        }

        @Override
        public void handleMessage(GameScreen entity, Message message) {

        }
    },

    WON() {
        @Override
        public void enter(GameScreen entity) {

        }

        @Override
        public void update(GameScreen entity, float delta) {

        }

        @Override
        public void exit(GameScreen entity) {

        }

        @Override
        public void handleMessage(GameScreen entity, Message message) {

        }
    },

    LOST() {
        @Override
        public void enter(GameScreen entity) {

        }

        @Override
        public void update(GameScreen entity, float delta) {

        }

        @Override
        public void exit(GameScreen entity) {

        }

        @Override
        public void handleMessage(GameScreen entity, Message message) {

        }
    }
}
