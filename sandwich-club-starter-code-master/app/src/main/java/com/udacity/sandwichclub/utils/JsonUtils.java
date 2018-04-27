package com.udacity.sandwichclub.utils;

import java.util.List;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.udacity.sandwichclub.model.Sandwich;

public class JsonUtils {

    private static List toList(JSONArray jsonArray) throws JSONException {

        ArrayList<String> arrayList = new ArrayList<String>();
        if (jsonArray != null && jsonArray.length() > 0) {

            for (int index = 0; index < jsonArray.length(); index++) {
                arrayList.add((String)jsonArray.get(index));
            }

        }

        return arrayList;

    }

    public static Sandwich parseSandwichJson(String json) {

        Sandwich sandwich = null;
        if (json != null && !(json = json.trim()).isEmpty()) {

            try {

                JSONObject jsonObject = new JSONObject(json);
                sandwich = new Sandwich(jsonObject.getString("mainName"), toList(jsonObject.getJSONArray("alsoKnownAs")),
                                        jsonObject.getString("placeOfOrigin"), jsonObject.getString("description"), jsonObject.getString("image"),
                                        toList(jsonObject.getJSONArray("ingredients")));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        System.out.println(sandwich);

        return sandwich;

    }

}