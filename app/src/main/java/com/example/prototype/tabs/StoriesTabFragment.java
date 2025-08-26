package com.example.prototype.tabs;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prototype.R;
import com.example.prototype.backend.DictionaryHighlighter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StoriesTabFragment extends Fragment {
    
    private LinearLayout rootLayout;
    private List<String> storyFiles;
    private Map<String, DictionaryHighlighter.DictEntry> dictionary;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stories_tab, container, false);
        
        rootLayout = view.findViewById(R.id.stories_tab_layout);
        setupStoriesTab();
        
        return view;
    }
    
    private void setupStoriesTab() {
        if (getContext() == null) return;
        
        // Add title
        TextView titleText = new TextView(getContext());
        titleText.setText("Stories");
        titleText.setTextSize(24f);
        titleText.setTextColor(getResources().getColor(android.R.color.black, null));
        titleText.setPadding(32, 32, 32, 16);
        titleText.setGravity(android.view.Gravity.CENTER);
        rootLayout.addView(titleText);
        
        // Add description
        TextView descText = new TextView(getContext());
        descText.setText("Tap on a story to read it with highlighted dictionary words.");
        descText.setTextSize(16f);
        descText.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
        descText.setPadding(32, 16, 32, 32);
        descText.setGravity(android.view.Gravity.CENTER);
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
        
        // Load dictionary
        dictionary = DictionaryHighlighter.loadDictionaryFromAssets(getContext(), "dictionary.json");

        // List story files
        listStoryFiles();
    }
    
    private void listStoryFiles() {
        if (getContext() == null) return;
        
        storyFiles = getStoryFilesFromAssets();
        
        if (storyFiles.isEmpty()) {
            TextView noStoriesText = new TextView(getContext());
            noStoriesText.setText("No story files found in assets/stories/");
            noStoriesText.setTextSize(16f);
            noStoriesText.setTextColor(getResources().getColor(android.R.color.darker_gray, null));
            noStoriesText.setPadding(32, 32, 32, 32);
            noStoriesText.setGravity(android.view.Gravity.CENTER);
            rootLayout.addView(noStoriesText);
            return;
        }
        
        // Add stories list title
        TextView listTitle = new TextView(getContext());
        listTitle.setText("Available Stories:");
        listTitle.setTextSize(18f);
        listTitle.setTextColor(getResources().getColor(android.R.color.black, null));
        listTitle.setPadding(32, 32, 32, 16);
        rootLayout.addView(listTitle);
        
        // Create clickable story items
        for (String storyFile : storyFiles) {
            createStoryItem(storyFile);
        }
    }
    
    private void createStoryItem(String storyFileName) {
        if (getContext() == null) return;
        
        // Create clickable story item
        TextView storyItem = new TextView(getContext());
        storyItem.setText(storyFileName);
        storyItem.setTextSize(16f);
        storyItem.setTextColor(getResources().getColor(android.R.color.holo_blue_dark, null));
        storyItem.setPadding(32, 16, 32, 16);
        storyItem.setBackgroundColor(getResources().getColor(android.R.color.white, null));
        storyItem.setClickable(true);
        storyItem.setFocusable(true);
        
        // Add click listener to read the story
        storyItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readStory(storyFileName);
            }
        });
        
        rootLayout.addView(storyItem);
        
        // Add small separator between items
        View itemSeparator = new View(getContext());
        itemSeparator.setBackgroundColor(getResources().getColor(android.R.color.background_light, null));
        itemSeparator.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            1
        ));
        itemSeparator.setPadding(32, 0, 32, 0);
        rootLayout.addView(itemSeparator);
    }
    
    private void readStory(String storyFileName) {
        if (getContext() == null) return;

        // Clear current view and show story
        rootLayout.removeAllViews();

        // Add back button
        TextView backButton = new TextView(getContext());
        backButton.setText("← Back to Stories");
        backButton.setTextSize(16f);
        backButton.setTextColor(getResources().getColor(android.R.color.holo_blue_dark, null));
        backButton.setPadding(32, 32, 32, 16);
        backButton.setClickable(true);
        backButton.setFocusable(true);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Recreate the stories list
                rootLayout.removeAllViews();
                setupStoriesTab();
            }
        });
        rootLayout.addView(backButton);

        // Add story title
        TextView storyTitle = new TextView(getContext());
        storyTitle.setText(storyFileName);
        storyTitle.setTextSize(20f);
        storyTitle.setTextColor(getResources().getColor(android.R.color.black, null));
        storyTitle.setPadding(32, 16, 32, 16);
        storyTitle.setGravity(android.view.Gravity.CENTER);
        rootLayout.addView(storyTitle);

        // Add separator
        View separator = new View(getContext());
        separator.setBackgroundColor(getResources().getColor(android.R.color.darker_gray, null));
        separator.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
        ));
        separator.setPadding(32, 16, 32, 16);
        rootLayout.addView(separator);

        // Read and display story with dictionary highlighting

        String storyContent = DictionaryHighlighter.readTextAsset(getContext(), "stories/" + storyFileName);
        TextView storyText = new TextView(getContext());
        storyText.setText(DictionaryHighlighter.buildFullTextWithDefaultWhite(storyContent, dictionary));
        storyText.setTextSize(16f);
        storyText.setPadding(32, 16, 32, 32);
        storyText.setLineSpacing(0, 1.2f);
        rootLayout.addView(storyText);
    }
    
    private List<String> getStoryFilesFromAssets() {
        List<String> files = new ArrayList<>();
        try {
            String[] assetFiles = getContext().getAssets().list("stories");
            if (assetFiles != null) {
                for (String file : assetFiles) {
                    if (file.endsWith(".txt")) {
                        files.add(file);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }
}

