package org.example;

import java.util.Arrays;

public class Movie {
    public int id;
    public String title;
    public String description;
    public int[] actors;



    @Override
    public String toString() {
        return "Фильм id="+this.id+" "+this.title+" "+this.description+" "+ Arrays.toString(actors);
    }
}
