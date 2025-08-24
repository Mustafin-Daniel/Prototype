package com.example.prototype.backend;

import android.content.Context;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.prototype.R;

public class CardDisplay extends CardView {
    public CardDisplay(@NonNull Context context) {
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

    public void update(Context context){

    }

}
