package com.example.prototype.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prototype.R;

public class EmptyTabFragment extends Fragment {
    
    private String tabName;
    
    public static EmptyTabFragment newInstance(String tabName) {
        EmptyTabFragment fragment = new EmptyTabFragment();
        Bundle args = new Bundle();
        args.putString("tab_name", tabName);
        fragment.setArguments(args);
        return fragment;
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tabName = getArguments().getString("tab_name", "Empty Tab");
        }
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty_tab, container, false);
        
        TextView textView = view.findViewById(R.id.empty_tab_text);
        textView.setText(tabName + "\n\nThis tab is currently empty.\nTap to add content later.");
        
        return view;
    }
}

