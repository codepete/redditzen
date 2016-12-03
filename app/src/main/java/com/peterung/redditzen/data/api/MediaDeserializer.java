package com.peterung.redditzen.data.api;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.peterung.redditzen.data.api.model.Media;

import java.lang.reflect.Type;

public class MediaDeserializer implements JsonDeserializer<Media> {

    @Override
    public Media deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) {
            return null;
        }

        try {
            Media media = new Media();
            media.source = json.getAsJsonObject().get("type").getAsString();
            JsonObject oembed = json.getAsJsonObject().get("oembed").getAsJsonObject();
            media.providerUrl = oembed.get("provider_url").getAsString();
            media.description = oembed.get("description").getAsString();
            media.title = oembed.get("title").getAsString();
            media.url = oembed.get("url").getAsString();
            media.authorName = oembed.get("author_name").getAsString();
            media.height = oembed.get("height").getAsInt();
            media.width = oembed.get("width").getAsInt();
            media.thumbnailHeight = oembed.get("thumbnail_height").getAsInt();
            media.thumbnailWidth = oembed.get("thumbnail_width").getAsInt();
            media.providerName = oembed.get("provider_name").getAsString();
            media.thumbnailUrl = oembed.get("thumbnail_url").getAsString();
            media.type = oembed.get("type").getAsString();
            media.authorUrl = oembed.get("author_url").getAsString();

            return media;
        } catch (Exception e) {
            return null;
        }
    }
}
