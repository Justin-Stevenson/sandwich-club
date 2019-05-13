package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JsonUtils {
    private static final String TAG = "JsonUtils";

    public static Sandwich parseSandwichJson(String json) {
        Sandwich sandwich;
        JSONObject name;
        String mainName = null;
        List<String> aka = null;
        String placeOfOrigin = null;
        String description = null;
        String image = null;
        List<String> ingredients = null;

        try {
            JSONObject jsonObject = new JSONObject(json);
            name = jsonObject.getJSONObject("name");
            mainName = name.getString("mainName");
            aka = getList(jsonObject, "alsoKnownAs");
            placeOfOrigin = jsonObject.getString("placeOfOrigin");
            description = jsonObject.getString("description");
            image = jsonObject.getString("image");
            ingredients = getList(jsonObject, "ingredients");

        } catch (JSONException jse) {
            Log.e(TAG, "parseSandwichJson: error parsing sandwich json", jse);
        }

        sandwich = new Sandwich(mainName, aka, placeOfOrigin, description, image, ingredients);

        return sandwich;
    }

    private static List<String> getList(JSONObject jsonObject, String key) throws JSONException {
        if (jsonObject.has(key)) {  // Check for value since empty value throws JSONException
            return convertToList(jsonObject.getJSONArray(key));
        }

        return Collections.emptyList();
    }

    private static List<String> convertToList(JSONArray array) throws JSONException {
        if (array == null) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<>(array.length());

        for (int index = 0; index < array.length(); index++) {
            String item = array.getString(index);
            list.add(item);
        }

        return list;
    }
}
