package com.example.demo01.world.life;

public class Human extends LifeBase {
    private String name;
    private int love;
    private boolean marry;
    private int marryTime;
    private Human spouse;

    public Human getSpouse() {
        return spouse;
    }

    public void setSpouse(Human spouse) {
        this.spouse = spouse;
    }

    public int getMarryTime() {
        return marryTime;
    }

    public void setMarryTime(int marryTime) {
        this.marryTime = marryTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public boolean isMarry() {
        return marry;
    }

    public void setMarry(boolean marry) {
        this.marry = marry;
    }
}
