package com.example.prototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Card extends CardView {
    /** Word in English */
    private TextView eng_word;
    /** Word in Russian */
    private TextView rus_word;
    /** VectorDrawable */
    private ImageView image;


    public Card(@NonNull Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        // Inflate the card layout
        LayoutInflater.from(context).inflate(R.layout.cardview, this, true);

        rus_word = findViewById(R.id.russianWordTxt);
        eng_word = findViewById(R.id.englishWordTxt);
        image = findViewById(R.id.imageView);
    }

    public void setEng_word(TextView eng_word) {
        this.eng_word = eng_word;
    }

    public void setRus_word(TextView rus_word) {
        this.rus_word = rus_word;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    /** Gets the data of a Card using only the engword */
    public void readCard(String engWord){

    }
}
