package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) throws JSONException {
            JSONObject mainObject = new JSONObject(json);
            JSONObject nameObject = mainObject.getJSONObject("name");
            String mainName = nameObject.getString("mainName");
            if(mainName.equals(""))
                    mainName = "Info Not Available";
            JSONArray alsoKnownAsArray = nameObject.getJSONArray("alsoKnownAs");
            String placeOfOrigin = mainObject.getString("placeOfOrigin");
            if(placeOfOrigin.equals(""))
                    placeOfOrigin = "Info Not Available";
            String description = mainObject.getString("description");
            if(description.equals(""))
                    description = "Info Not Available";
            String image = mainObject.getString("image");
            JSONArray ingredients = mainObject.getJSONArray("ingredients");

            List<String> akaStrings = new ArrayList<String>();
            if(alsoKnownAsArray.length() == 0)
                    akaStrings.add("Info Not Available");
            else {
                    for (int index = 0; index < alsoKnownAsArray.length(); index++) {
                            akaStrings.add(alsoKnownAsArray.getString(index).toString());
                    }
            }

            List<String> listOfIngredients = new ArrayList<String>();
            if(ingredients.length() == 0)
                    listOfIngredients.add("Info Not Available");
            else {
                    for (int index = 0; index < ingredients.length(); index++) {
                            listOfIngredients.add(ingredients.getString(index).toString());
                    }
            }
            Sandwich sandwich = new Sandwich(mainName, akaStrings, placeOfOrigin, description,
                    image, listOfIngredients);
            return sandwich;
    }
}
