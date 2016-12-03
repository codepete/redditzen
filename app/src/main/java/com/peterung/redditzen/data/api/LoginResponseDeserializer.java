package com.peterung.redditzen.data.api;


import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.peterung.redditzen.data.api.model.LoginResponse;

import java.lang.reflect.Type;

public class LoginResponseDeserializer implements JsonDeserializer<LoginResponse> {
    @Override
    public LoginResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject data = json.getAsJsonObject().get("json").getAsJsonObject().get("data").getAsJsonObject();

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.cookie = data.get("cookie").getAsString();
        loginResponse.modhash = data.get("modhash").getAsString();
        loginResponse.needHttps = data.get("need_https").getAsBoolean();

        return loginResponse;
    }
}
