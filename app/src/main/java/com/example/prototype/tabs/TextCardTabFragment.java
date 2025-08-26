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

public class TextCardTabFragment extends Fragment {
    
    private List<Item> itemList;
    private CardReader cardReader;
    private LinearLayout rootLayout;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text_card_tab, container, false);
        
        rootLayout = view.findViewById(R.id.text_card_tab_layout);
        setupTextCardTab();
        
        return view;
    }
    
    private void setupTextCardTab() {
        if (getContext() == null) return;
        
        // Add title
        TextView titleText = new TextView(getContext());
        titleText.setText("Text & Card View");
        titleText.setTextSize(24f);
        titleText.setTextColor(getResources().getColor(android.R.color.black, null));
        titleText.setPadding(32, 32, 32, 16);
        rootLayout.addView(titleText);
        
        // Add description
        TextView descText = new TextView(getContext());
        descText.setText("This tab combines text content with interactive cards.\n\nScroll down to see the card functionality.");
        descText.setTextSize(16f);
        descText.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        descText.setPadding(32, 16, 32, 32);
        rootLayout.addView(descText);
        
        // Add separator
        View separator = new View(getContext());
        separator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray, null));
        separator.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            2
        ));
        separator.setPadding(32, 16, 32, 16);
        rootLayout.addView(separator);
        
        // Add card functionality
        setupCardSection();
    }
    
    private void setupCardSection() {
        if (getContext() == null) return;
        
        cardReader = new CardReader(getContext());
        try {
            itemList = cardReader.readAllItemsFromAssets("worddata.json");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return;
        }
        
        if (itemList.isEmpty()) return;
        
        // Add card section title
        TextView cardTitle = new TextView(getContext());
        cardTitle.setText("Interactive Card");
        cardTitle.setTextSize(20f);
        cardTitle.setTextColor(getResources().getColor(android.R.color.black, null));
        cardTitle.setPadding(32, 32, 32, 16);
        rootLayout.addView(cardTitle);
        
        // Track the current index for the click listener
        final int[] currentIndex = {0};
        
        // Create and add the CardDisplay
        CardDisplay cardDisplay = new CardDisplay(itemList.get(currentIndex[0]), getContext());
        rootLayout.addView(cardDisplay);
        
        // Add instruction text
        TextView instructionText = new TextView(getContext());
        instructionText.setText("Tap the card image to see the next word!");
        instructionText.setTextSize(14f);
        instructionText.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        instructionText.setPadding(32, 16, 32, 32);
        instructionText.setGravity(android.view.Gravity.CENTER);
        rootLayout.addView(instructionText);
        
        // Handle image click to advance to the next item
        ImageView image = cardDisplay.findViewById(R.id.imageView);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentIndex[0] = (currentIndex[0] + 1) % itemList.size();
                // Replace the current card with a new one for the next item
                rootLayout.removeView(cardDisplay);
                CardDisplay nextCard = new CardDisplay(itemList.get(currentIndex[0]), getContext());
                rootLayout.addView(nextCard, rootLayout.indexOfChild(instructionText));
                // Re-bind the click listener to the new card's image
                ImageView nextImage = nextCard.findViewById(R.id.imageView);
                nextImage.setOnClickListener(this);
            }
        });
    }
}
