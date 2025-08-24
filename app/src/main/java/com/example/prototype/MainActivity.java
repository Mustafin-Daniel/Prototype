package com.example.prototype;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.prototype.backend.CardDisplay;
import com.example.prototype.backend.CardReader;
import com.example.prototype.backend.Item;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Item> itemList;
    private CardReader cardReader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cardReader = new CardReader(this);
        try {
            itemList=cardReader.readAllItemsFromAssets("worddata.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        CardDisplay cardDisplay = new CardDisplay(itemList.get(0), this);
    }
}