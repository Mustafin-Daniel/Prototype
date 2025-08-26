package com.example.prototype.backend;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CardReader {
    private Context context;
    public CardReader(Context context) {
        this.context = context;
    }

    /**
     * Progressively reads all items from a JSON array file in the assets folder.
     * This method reads the entire JSON array and returns a list of all Item objects.
     *
     * @param fileName The name of the JSON file in the assets folder
     * @return List of Item objects representing all JSON objects in the array
     * @throws IOException if file cannot be read
     * @throws JSONException if JSON is malformed
     */
    public List<Item> readAllItemsFromAssets(String fileName)
            throws IOException, JSONException {

        String jsonString = loadJsonFromAssets(fileName);
        JSONArray jsonArray = new JSONArray(jsonString);

        List<Item> allItems = new ArrayList<>();

        // Progressively read each item in the array
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Item item = jsonObjectToItem(jsonObject);
            allItems.add(item);
        }

        Log.d(TAG, "Successfully loaded " + allItems.size() + " items from " + fileName);
        return allItems;
    }
    /**
     * Reads a single JSON object from assets and returns it as a Item.
     *
     * @param fileName The name of the JSON file in the assets folder
     * @return Map representing the JSON object
     * @throws IOException if file cannot be read
     * @throws JSONException if JSON is malformed
     */
    public Item readJsonObjectFromAssets(String fileName)
            throws IOException, JSONException {

        String jsonString = loadJsonFromAssets(fileName);
        JSONObject jsonObject = new JSONObject(jsonString);

        return jsonObjectToItem(jsonObject);
    }

    /**
     * Reads the raw JSON string from assets folder.
     *
     * @param fileName The name of the JSON file in the assets folder
     * @return Raw JSON string
     * @throws IOException if file cannot be read
     */
    public String loadJsonFromAssets(String fileName) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(fileName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }

        inputStream.close();
        reader.close();

        return jsonBuilder.toString();
    }

    /**
     * Converts a JSONObject to an Item recursively.
     *
     * @param jsonObject The JSONObject to convert
     * @return Item representation of the JSONObject
     * @throws JSONException if JSON is malformed
     */
    private Item jsonObjectToItem(JSONObject jsonObject) throws JSONException {
        Item item = new Item();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);

            switch (key){
                case "rus_word":
                    item.setRus_word(value.toString());
                    break;
                case "eng_word":
                    item.setEng_word(value.toString());
                    break;
                case "transcript":
                    item.setTranscript(value.toString());
                    break;
                default:
                    item.setEng_word("Incorrect key while reading!");
                    break;
            }
        }

        return item;
    }
}
