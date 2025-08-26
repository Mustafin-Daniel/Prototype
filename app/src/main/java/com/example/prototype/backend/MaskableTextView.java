package com.example.prototype.backend;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A TextView that can hide or color specific words.
 * Usage:
 *   maskableTextView.setMode(MaskableTextView.Mode.HIDE or COLOR);
 *   maskableTextView.setMaskColor(Color.RED);
 *   maskableTextView.setMaskedWords(Arrays.asList("cat", "dog"));
 *   maskableTextView.setMaskedText("A cat and a dog.");
 */
public class MaskableTextView extends AppCompatTextView {
	public enum Mode { HIDE, COLOR }

	private final List<String> maskedWords = new ArrayList<>();
	private Mode mode = Mode.COLOR;
	@ColorInt private int maskColor = Color.YELLOW;
	private boolean caseInsensitive = true;

	public MaskableTextView(Context context) {
		super(context);
	}

	public MaskableTextView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public MaskableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public void setMode(Mode mode) {
		this.mode = mode == null ? Mode.COLOR : mode;
		applyMaskToCurrentText();
	}

	public void setMaskColor(@ColorInt int color) {
		this.maskColor = color;
		applyMaskToCurrentText();
	}

	public void setCaseInsensitive(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
		applyMaskToCurrentText();
	}

	public void setMaskedWords(Collection<String> words) {
		this.maskedWords.clear();
		if (words != null) {
			this.maskedWords.addAll(words);
		}
		applyMaskToCurrentText();
	}

	public void setMaskedText(CharSequence text) {
		super.setText(text);
		applyMaskToCurrentText();
	}

	@Override
	public void setText(CharSequence text, BufferType type) {
		super.setText(text, type);
		applyMaskToCurrentText();
	}

	private void applyMaskToCurrentText() {
		CharSequence current = getText();
		if (current == null || current.length() == 0 || maskedWords.isEmpty()) {
			return;
		}

		SpannableString spannable = new SpannableString(current);

		for (String word : maskedWords) {
			if (word == null || word.isEmpty()) continue;
			String patternWord = caseInsensitive ? Pattern.quote(word.toLowerCase(Locale.getDefault()))
					: Pattern.quote(word);
			// Word boundaries to match whole words; adjust if partial matches are desired
			Pattern pattern = Pattern.compile("\\b" + patternWord + "\\b",
					caseInsensitive ? Pattern.CASE_INSENSITIVE : 0);
			Matcher matcher = pattern.matcher(current);
			while (matcher.find()) {
				int start = matcher.start();
				int end = matcher.end();
				int colorToApply = (mode == Mode.HIDE) ? Color.TRANSPARENT : maskColor;
				spannable.setSpan(new ForegroundColorSpan(colorToApply), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}

		super.setText(spannable, BufferType.SPANNABLE);
	}
}
