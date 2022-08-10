package com.rushabh.diceapp;
public class Dice {
    private String name;
    private int sides;
    private int sideUp;

    // Default Constructor
    //
    public Dice() {
        this.sides = 6;
        this.name = "d6";
        roll();
    }

    // Constructor with one argument ie. Sides
    //
    public Dice(int sides) {
        this.sides = sides;
        this.name = getDiceNameFor(sides);
        roll();
    }

    // Constructor with two argument ie. Sides and Name
    //
    public Dice(int sides, String diceName) {
        this.sides = sides;
        this.name = diceName;
        roll();
    }

    // This method rolls the dice and set a random number to side up
    //
    public void roll() {
        int roundRandom = (int) Math.round(Math.random() * 10);
        if (roundRandom == 0) {
            this.sideUp = 1;
        } else {
            this.sideUp = (int) Math.round((roundRandom * this.sides) / 10);
        }
    }
    // Getters or Accessors
    //
    public int getSides() {
        return this.sides;
    }

    public int getSideUp() {
        return this.sideUp;
    }

    public String getDiceName() {
        return this.name;
    }

    // Setters or Mutators
    //
    public void setSides(int sides) {
        this.sides = sides;
        this.name = getDiceNameFor(sides);
    }

    public void setName(String diceName) {
        this.name = diceName;
    }

    private String getDiceNameFor(int sides) {
        return this.name = "d" + sides;
    }

    // Get highest value
    //
    public void showHighestValue() {
        System.out.println("Highest value of dice: " + this.sides);
    }
}
