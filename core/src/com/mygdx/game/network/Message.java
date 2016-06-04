package com.mygdx.game.network;

import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;

/**
 * Created by joaopsilva on 03-06-2016.
 */
public class Message {
    private String tag;
    ArrayList<MessageArgument> args;

    public Message(String tag) {
        this.tag = tag;
        args = new ArrayList<MessageArgument>();
    }

    public void addArgument(String name, Object value) {
        args.add(new MessageArgument(name, value));
    }

    public String toJson() {
        Json json = new Json();
        return json.toJson(this);
    }

    private class MessageArgument {
        private String name;
        private Object value;

        public MessageArgument(String name, Object value) {
            this.name = name;
            this.value = value;
        }
    }
}
