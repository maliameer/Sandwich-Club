package com.udacity.sandwichclub.utils;

import java.lang.reflect.InvocationTargetException;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.udacity.sandwichclub.model.Sandwich;

public class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        Sandwich sandwich = null;
        try {
            sandwich = (Sandwich)toObject(json, Sandwich.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(sandwich);

        return sandwich;

    }

    private static String _capitalize(final String string) {
        return (Character.toUpperCase(string.charAt(0)) + string.substring(1));
    }

    private static String _getModifierMethodName(final String attributeName) {
        return ("set" + _capitalize(attributeName));
    }

    /**
     * Parses the provided JSON String into it's respective Object as represented provided Class.<br>
     * <u><b>NOTE:</b></u>&nbsp;&nbsp; This parse method DOES NOT SUPPORT nested Objects; ONLY Primitives and Collections.
     * @param jsonString JSON String to be parses into an Object.
     * @param clazz Class of the Object into which provided JSON String needs to be transformed into.
     * @return Transformed Object
     * @throws InstantiationException Instantiation Exception
     * @throws IllegalAccessException Illegal Access Exception
     * @throws InvocationTargetException Invocation Target Exception
     */
    public static Object toObject(String jsonString, Class clazz)
            throws JSONException, InstantiationException, IllegalAccessException, InvocationTargetException {

        Object clazzInstance = null;
        if (clazz != null && jsonString != null && !(jsonString = jsonString.trim()).isEmpty()) {

            clazzInstance = clazz.newInstance();

            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {

                String fieldName = iterator.next();
                Class attributeClass = null;
                Object object = jsonObject.get(fieldName);
                if (object.getClass().equals(String.class)) {
                    attributeClass = String.class;
                } else {

                    attributeClass = List.class;
                    JSONArray jsonArray = (JSONArray)object;
                    ArrayList<String> arrayList = new ArrayList<String>();

                    for (int index = 0; index < jsonArray.length(); index++) {
                        arrayList.add((String)jsonArray.get(index));
                    }
                    object = arrayList;

                }

                String modifierMethodName = _getModifierMethodName(fieldName);
                try {
                    clazz.getMethod(modifierMethodName, attributeClass).invoke(clazzInstance, object);
                } catch (NoSuchMethodException noSuchMethodException) {
                    System.out.println("Modifier method \"" + modifierMethodName + "\" NOT FOUND for attribute \"" + fieldName +
                            "\", so skipping setting JSON value to any instance attribute.");
                }

            }

        }

        return clazzInstance;

    }

}