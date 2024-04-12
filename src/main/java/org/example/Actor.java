package org.example;

import java.util.Arrays;

public class Actor {
    public int id;
    public String firstName;
    public String lastName;
    public int[] movies;

    public Actor(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Актер id="+this.id+" "+this.firstName+" "+this.lastName+" "+ Arrays.toString(movies);
    }
}
