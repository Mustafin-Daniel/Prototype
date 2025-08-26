package com.example.prototype.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prototype.R;
import com.example.prototype.backend.CardDisplay;
import com.example.prototype.backend.CardReader;
import com.example.prototype.backend.Item;
import com.example.prototype.backend.DictionaryHighlighter;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainTabFragment extends Fragment {
    
    private List<Item> itemList;
    private CardReader cardReader;
    private LinearLayout rootLayout;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tab, container, false);
        
        rootLayout = view.findViewById(R.id.main_tab_layout);
        setupMainTab();
        
        return view;
    }
    
    private void setupMainTab() {
        if (getContext() == null) return;
        
        cardReader = new CardReader(getContext());
        try {
            itemList = cardReader.readAllItemsFromAssets("worddata.json");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return;
        }
        
        if (itemList.isEmpty()) return;
        
        // Track the current index for the click listener
        final int[] currentIndex = {0};
        
        // Create and add the initial CardDisplay
        CardDisplay cardDisplay = new CardDisplay(itemList.get(currentIndex[0]), getContext());
        rootLayout.addView(cardDisplay);
        
        // Add a TextView below the card for dictionary highlighting
        TextView dictTextView = new TextView(getContext());
        dictTextView.setTextSize(22f);
        rootLayout.addView(dictTextView);
        
        // Load dictionary and text assets and display full text with white default and colored dictionary words
        Map<String, DictionaryHighlighter.DictEntry> dict = DictionaryHighlighter.loadDictionaryFromAssets(getContext(), "dictionary.json");
        String rawText = DictionaryHighlighter.readTextAsset(getContext(), "text.txt");
        dictTextView.setText(DictionaryHighlighter.buildFullTextWithDefaultWhite(rawText, dict));
        
        // Handle image click to advance to the next item
        ImageView image = cardDisplay.findViewById(R.id.imageView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex[0] = (currentIndex[0] + 1) % itemList.size();
                // Replace the current card with a new one for the next item
                rootLayout.removeAllViews();
                CardDisplay nextCard = new CardDisplay(itemList.get(currentIndex[0]), getContext());
                rootLayout.addView(nextCard);
                // Re-add the dictionary text view under the new card
                TextView nextDictText = new TextView(getContext());
                nextDictText.setTextSize(22f);
                rootLayout.addView(nextDictText);
                nextDictText.setText(DictionaryHighlighter.buildFullTextWithDefaultWhite(rawText, dict));
                // Re-bind the click listener to the new card's image
                ImageView nextImage = nextCard.findViewById(R.id.imageView);
                nextImage.setOnClickListener(this);
            }
        });
    }
}
