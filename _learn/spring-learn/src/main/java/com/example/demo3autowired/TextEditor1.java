package com.example.demo3autowired;

public class TextEditor1 {
    private SpellChecker spellChecker;
    private String note;

    public TextEditor1(SpellChecker spellChecker1, String note) {
        this.spellChecker = spellChecker1;
        this.note = note;
    }

    public SpellChecker getSpellChecker() {
        return spellChecker;
    }

    public void setSpellChecker(SpellChecker spellChecker) {
        this.spellChecker = spellChecker;
    }

    public void spellCheck() {
        spellChecker.check();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}