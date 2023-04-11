package com.example.findapotty.diary;

import java.io.Serializable;
//model class is used to set and get the data from database

public class DiaryEntry implements Serializable {
    String date, month, pottyType, stoolColor,urineColor, additionalNotes;
    Integer dayOfWeek,duration,painRating,stoolType;

    public DiaryEntry(){}

    public DiaryEntry(String date, String month, String pottyType, String stoolColor,
                      String urineColor, String additionalNotes, Integer dayOfWeek, Integer duration,
                      Integer painRating, Integer stoolType) {
        this.date = date;
        this.month = month;
        this.pottyType = pottyType;
        this.stoolColor = stoolColor;
        this.urineColor = urineColor;
        this.additionalNotes = additionalNotes;
        this.dayOfWeek = dayOfWeek;
        this.duration = duration;
        this.painRating = painRating;
        this.stoolType = stoolType;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPottyType() {
        return pottyType;
    }

    public void setPottyType(String pottyType) {
        this.pottyType = pottyType;
    }

    public String getStoolColor() {
        return stoolColor;
    }

    public void setStoolColor(String stoolColor) {
        this.stoolColor = stoolColor;
    }

    public String getUrineColor() {
        return urineColor;
    }

    public void setUrineColor(String urineColor) {
        this.urineColor = urineColor;
    }

    public String getAdditionalNotes() {
        return additionalNotes;
    }

    public void setAdditionalNotes(String additionalNotes) {
        this.additionalNotes = additionalNotes;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getPainRating() {
        return painRating;
    }

    public void setPainRating(Integer painRating) {
        this.painRating = painRating;
    }

    public Integer getStoolType() {
        return stoolType;
    }

    public void setStoolType(Integer stoolType) {
        this.stoolType = stoolType;
    }
}
