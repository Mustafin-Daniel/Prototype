package com.example.prototype.backend;

public class Item {
    private String eng_word, rus_word, transcript;

    public Item(String eng_word, String rus_word, String transcript) {
        this.eng_word = eng_word;
        this.rus_word = rus_word;
        this.transcript = transcript;
    }

    public Item() {
        this.eng_word = "Error"; this.rus_word = "Ошибка"; this.transcript = "Эррор";
    }

    public String getEng_word() {
        return eng_word;
    }

    public String getRus_word() {
        return rus_word;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setEng_word(String eng_word) {
        this.eng_word = eng_word;
    }

    public void setRus_word(String rus_word) {
        this.rus_word = rus_word;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }
}
