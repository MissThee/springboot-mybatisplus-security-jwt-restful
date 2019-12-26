package com.example.demo3autowired;

public class TextEditor {
    private SpellChecker spellChecker1;

    private String note;

    public SpellChecker getSpellChecker1() {
        return spellChecker1;
    }

    public void setSpellChecker1(SpellChecker spellChecker1) {
        this.spellChecker1 = spellChecker1;
    }

    public void spellCheck() {
        spellChecker1.check();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}