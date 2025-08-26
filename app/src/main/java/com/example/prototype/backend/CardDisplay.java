package com.example.prototype.backend;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.MediaPlayer;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.prototype.R;

import java.lang.reflect.Field;

public class CardDisplay extends CardView {
    /** Word in English */
    private TextView eng_word;
    /** Word in Russian */
    private TextView rus_word;
    /** VectorDrawable */
    private ImageView image;
    /** Saved transcription instead of full Item */
    private String transcription;
    boolean toggled = false; // Track toggle state
    private MediaPlayer mediaPlayer;
    private static final String TAG = "CardDisplay";

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

        // Load image from assets based on English word
        loadImageFromAssets(item.getEng_word());

        eng_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!toggled) {
                    eng_word.setText(transcription);
                    playSound(item.getEng_word());
                }else{
                    eng_word.setText(item.getEng_word()+"\uD83D\uDD08");
                }
                toggled=!toggled;
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

    /**
     * Loads an image based on the English word.
     * Dynamically searches for drawable resources by name.
     */
    private void loadImageFromAssets(String englishWord) {
        int drawableId = getDrawableIdForWord(englishWord);
        if (drawableId != 0) {
            image.setImageResource(drawableId);
            Log.d(TAG, "Successfully loaded image for: " + englishWord);
        } else {
            // Fallback to default image
            image.setImageResource(R.drawable.glitch);
            Log.d(TAG, "No image found for: " + englishWord + ", using default image");
        }
    }
    
    /**
     * Dynamically finds drawable resource ID by name
     */
    private int getDrawableIdForWord(String englishWord) {
        String drawableName = englishWord.toLowerCase() + "_icon";
        try {
            // Use reflection to get the drawable resource ID
            Field field = R.drawable.class.getField(drawableName);
            return field.getInt(null);
        } catch (Exception e) {
            // Drawable not found, return 0
            return 0;
        }
    }

    /**
     * Public method to manually load an image from assets.
     * Useful for updating the image after the card is created.
     */
    public void setImageFromAssets(String englishWord) {
        loadImageFromAssets(englishWord);
    }

    private void playSound(String englishWord) {
        try {
            if (mediaPlayer != null) {
                try {
                    mediaPlayer.stop();
                } catch (IllegalStateException ignored) {}
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
            }

            String fileName = "audio/" + englishWord + ".mp3";
            AssetFileDescriptor afd = getContext().getAssets().openFd(fileName);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            afd.close();
            mediaPlayer.setOnCompletionListener(mp -> {
                try {
                    mp.stop();
                } catch (IllegalStateException ignored) {}
                mp.release();
                if (mediaPlayer == mp) {
                    mediaPlayer = null;
                }
            });
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "Failed to play sound for: " + englishWord, e);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
            } catch (IllegalStateException ignored) {}
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
