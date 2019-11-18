package com.example.demo3autowired;

public class SpellChecker {
    private String checkerName;

    public String getCheckerName() {
        return checkerName;
    }

    public void setCheckerName(String checkerName) {
        this.checkerName = checkerName;
    }

    public void check() {
        System.out.println(checkerName + ": Inside checkSpelling!!");
    }
}