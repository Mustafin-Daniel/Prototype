package com.example.prototype.backend;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DictionaryHighlighter {
	public static class DictEntry {
		public final String word;
		@ColorInt public final int color;
		public DictEntry(String word, @ColorInt int color) {
			this.word = word;
			this.color = color;
		}
	}

	public static Map<String, DictEntry> loadDictionaryFromAssets(@NonNull Context context, @NonNull String assetFileName) {
		Map<String, DictEntry> map = new HashMap<>();
		try (InputStream is = context.getAssets().open(assetFileName)) {
			String json = readAll(is);
			JSONArray arr = new JSONArray(json);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				String name = obj.optString("name", "");
				String colorStr = obj.optString("color", "#000000");
				int color = safeParseColor(colorStr, Color.BLACK);
				if (!name.isEmpty()) {
					map.put(name.toLowerCase(Locale.getDefault()), new DictEntry(name, color));
				}
			}
		} catch (Exception ignored) {}
		return map;
	}

	public static String readTextAsset(@NonNull Context context, @NonNull String assetFileName) {
		try (InputStream is = context.getAssets().open(assetFileName)) {
			return readAll(is);
		} catch (Exception e) {
			return "";
		}
	}

	public static Spannable buildColoredOnlyText(@NonNull String input,
												 @NonNull Map<String, DictEntry> dictionary) {
		SpannableStringBuilder builder = new SpannableStringBuilder();
		if (input.isEmpty() || dictionary.isEmpty()) return builder;

		// Tokenize words while preserving order; we'll include only dictionary words
		Pattern wordPattern = Pattern.compile("[A-Za-z']+");
		Matcher matcher = wordPattern.matcher(input);
		int index = 0;
		boolean first = true;
		while (matcher.find(index)) {
			String word = matcher.group();
			DictEntry entry = dictionary.get(word.toLowerCase(Locale.getDefault()));
			if (entry != null) {
				if (!first) builder.append(' ');
				int start = builder.length();
				builder.append(entry.word);
				int end = builder.length();
				builder.setSpan(new ForegroundColorSpan(entry.color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (entry.color != Color.BLACK) {
					builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				first = false;
			}
			index = matcher.end();
		}
		return builder;
	}

	// New: keep full text, color everything white by default, overlay dictionary colors
	public static Spannable buildFullTextWithDefaultWhite(@NonNull String input,
															@NonNull Map<String, DictEntry> dictionary) {
		SpannableStringBuilder builder = new SpannableStringBuilder(input);
		if (input.isEmpty()) return builder;

		// Set default color to white across entire text (so non-matching words are invisible but preserve spacing)
		builder.setSpan(new ForegroundColorSpan(Color.WHITE), 0, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		if (dictionary.isEmpty()) return builder;

		Pattern wordPattern = Pattern.compile("[A-Za-z']+");
		Matcher matcher = wordPattern.matcher(input);
		int index = 0;
		while (matcher.find(index)) {
			String word = matcher.group();
			DictEntry entry = dictionary.get(word.toLowerCase(Locale.getDefault()));
			if (entry != null) {
				int start = matcher.start();
				int end = matcher.end();
				builder.setSpan(new ForegroundColorSpan(entry.color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				if (entry.color != Color.BLACK) {
					builder.setSpan(new StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			index = matcher.end();
		}
		return builder;
	}

	private static String readAll(InputStream is) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = br.readLine()) != null) {
			sb.append(line).append('\n');
		}
		return sb.toString();
	}

	private static int safeParseColor(String color, @ColorInt int fallback) {
		try {
			return Color.parseColor(color);
		} catch (Exception e) {
			return fallback;
		}
	}
}
