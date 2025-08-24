package com.example.prototype.backend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.prototype.R;

public class CardDisplay extends CardView {
    /** Word in English */
    private TextView eng_word;
    /** Word in Russian */
    private TextView rus_word;
    /** VectorDrawable */
    private ImageView image;
    /** Saved transcription instead of full Item */
    private String transcription;
    final boolean toggled = false; // Track toggle state

    public CardDisplay(@NonNull Context context) {
        super(context);
        init(context);
    }

    public CardDisplay(Item item, @NonNull Context context) {
        super(context);
        init(context);

        rus_word.setText(item.getRus_word());
        eng_word.setText(item.getEng_word()+"\uD83D\uDD08");
        transcription = item.getTranscript();

        eng_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggled) {
                    eng_word.setText(transcription);
                    //TODO ADD sound
                }else{
                    eng_word.setText(item.getEng_word()+"\uD83D\uDD08");
                }
            }
        });
    }

    private void init(Context context) {
        // Inflate the card layout
        LayoutInflater.from(context).inflate(R.layout.cardview, this, true);

        rus_word = findViewById(R.id.russianWordTxt);
        eng_word = findViewById(R.id.englishWordTxt);
        image = findViewById(R.id.imageView);
    }
}
