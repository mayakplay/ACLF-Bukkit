package com.mayakplay.aclf.net;

import com.google.gson.JsonObject;

import java.util.function.Consumer;

/**
 * @author winterprison
 * @version 0.0.1
 * @since 06.07.2019.
 */
public class LinkQueue {

    private JsonObject data;

    private Consumer<JsonObject> callback;

    public LinkQueue(JsonObject data, Consumer<JsonObject> callback) {
        this.data = data;
        this.callback = callback;
    }

    public JsonObject getData() {
        return data;
    }

    public Consumer<JsonObject> getCallback() {
        return callback;
    }

}
