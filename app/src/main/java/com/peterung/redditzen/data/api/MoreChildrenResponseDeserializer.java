package com.peterung.redditzen.data.api;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.peterung.redditzen.data.api.model.MoreChildrenResponse;
import com.peterung.redditzen.data.api.model.Thing;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by peter on 10/10/16.
 */

public class MoreChildrenResponseDeserializer implements JsonDeserializer<MoreChildrenResponse> {
    @Override
    public MoreChildrenResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonArray things = json.getAsJsonObject().get("json")
                .getAsJsonObject().get("data")
                .getAsJsonObject().get("things")
                .getAsJsonArray();

        MoreChildrenResponse moreChildrenResponse = new MoreChildrenResponse();
        moreChildrenResponse.things = new ArrayList<>();
        for (JsonElement jsonElement : things) {
            Thing thing = new Gson().fromJson(jsonElement, Thing.class);
            moreChildrenResponse.things.add(context.deserialize(thing.data, thing.kind.getRedditClass()));
        }

        return moreChildrenResponse;
    }
}
