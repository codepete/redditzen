package com.peterung.redditzen.data.api;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.peterung.redditzen.data.api.model.RedditObject;
import com.peterung.redditzen.data.api.model.Thing;

import java.lang.reflect.Type;

public class RedditObjectDeserializer implements JsonDeserializer<RedditObject> {
    @Override
    public RedditObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) {
            return null;
        }

        try {
            Thing thing = new Gson().fromJson(json, Thing.class);
            return context.deserialize(thing.data, thing.kind.getRedditClass());
        } catch (Exception e) {
            return null;
        }
    }
}
