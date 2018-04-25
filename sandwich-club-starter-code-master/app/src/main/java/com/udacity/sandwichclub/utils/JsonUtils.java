package com.udacity.sandwichclub.utils;

import java.lang.reflect.Field;

import java.lang.reflect.InvocationTargetException;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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

    private static String _trimOutDoubleQuotes(String string) {

        string = string.trim();
        if (string.isEmpty() || string.equals("\"\"")) {
            return string;
        } else {

            if (string.charAt(0) == '\"') {
                string = string.substring(1);
            }

            if (string.charAt((string.length() - 1)) == '\"') {
                string = string.substring(0, (string.length() - 1));
            }

            return string;

        }

    }

    public static Map<String, String> _getCollectionsMap(StringBuilder stringBuilder)
           throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        String jsonString = stringBuilder.toString();
        Map<String, String> collectionsMap = new HashMap<String, String>();
        while (jsonString.indexOf("[") >= 0) {

            String collectionName = jsonString.substring(0, jsonString.indexOf("[")).trim();
            collectionName = collectionName.substring((collectionName.lastIndexOf(",") + 1)).trim();
            collectionName = collectionName.substring(0, collectionName.indexOf(":"));
            String collectionValues = jsonString.substring(jsonString.indexOf("["), (jsonString.indexOf("]") + 1));

            int startIndex = stringBuilder.indexOf(collectionName);
            int endIndex = (stringBuilder.indexOf(collectionValues) + collectionValues.length() + 1);
            stringBuilder.delete(startIndex, endIndex);

            collectionName = _trimOutDoubleQuotes(collectionName);
            collectionValues = _trimOutDoubleQuotes(collectionValues);

            collectionsMap.put(collectionName, collectionValues);

            jsonString = jsonString.substring((jsonString.indexOf("]") + 1));

        }

        return collectionsMap;

    }

    /**
     * Parses the provided JSON String into it's respective Object as represented provided Class.<br>
     * <u><b>NOTE:</b></u>&nbsp;&nbsp; This parse method DOES NOT SUPPORT nested Objects; ONLY Primitives and Collections.
     * @param jsonString JSON String to be parses into an Object.
     * @param clazz Class of the Object into which provided JSON String needs to be transformed into.
     * @return Transformed Object
     * @throws InstantiationException Instantiation Exception
     * @throws IllegalAccessException Illegal Access Exception
     * @throws NoSuchMethodException No Such Method Exception
     * @throws InvocationTargetException Invocation Target Exception
     */
    public static Object toObject(String jsonString, Class clazz) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        Object object = null;
        if (clazz !=null && jsonString != null && !(jsonString = jsonString.trim()).isEmpty()) {

            Field[] fieldsArr = clazz.getFields();
            Map<String, Field> fieldsMap = new HashMap<String, Field>();
            for (Field field : fieldsArr) {
                fieldsMap.put(field.getName(), field);
            }

            //Checking jsonString has enclosing braces or not i.e. { }
            if (jsonString.charAt(0) == '{' && jsonString.charAt((jsonString.length() - 1)) == '}') {

                object = clazz.newInstance();

                StringBuilder stringBuilder = new StringBuilder(jsonString);
                Map<String, String> collectionsMap = _getCollectionsMap(stringBuilder);
                if (collectionsMap != null && !collectionsMap.isEmpty()) {

                    Iterator<String> iterator = collectionsMap.keySet().iterator();
                    while (iterator.hasNext()) {

                        String collectionName = iterator.next();
                        String collectionValues = collectionsMap.get(collectionName);
                        if (collectionValues.charAt(0) == '[' && collectionValues.charAt((collectionValues.length() - 1)) == ']') {

                            collectionValues = collectionValues.substring(1);
                            collectionValues = collectionValues.substring(0, (collectionValues.length() - 1)).trim();
                            String[] collectionValuesArr = collectionValues.split(",");
                            clazz.getMethod(_getModifierMethodName(collectionName), List.class).invoke(object, Arrays.asList(collectionValuesArr));

                        }


                    }

                }

                String modifiedJson = stringBuilder.toString().substring(1).trim();
                modifiedJson = modifiedJson.substring(0, (modifiedJson.length() - 1)).trim();
                String[] jsonNodes = modifiedJson.split(",\"");
                for (String jsonNode: jsonNodes) {

                    jsonNode = jsonNode.trim();
                    if (jsonNode.indexOf(":") > 0) {

                        String attribute = jsonNode.substring(0, jsonNode.indexOf(":"));
                        attribute = _trimOutDoubleQuotes(attribute);
                        if (!attribute.isEmpty()) {

                            String value = jsonNode.substring((jsonNode.indexOf(":") + 1));
                            if (value.charAt(0) != '[') {
                                value = _trimOutDoubleQuotes(value);
                            }
                            clazz.getMethod(_getModifierMethodName(attribute), String.class).invoke(object, value);

                        }

                    }

                }

            }

        }

        return object;

    }

}