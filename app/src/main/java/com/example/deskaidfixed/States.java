package com.example.deskaidfixed;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.Arrays;

//Created this enum just to facilitate the usage of the status in some aspects for the creating tickets form

public enum States {

    UNSOLVED("Unsolved"),
    SOLVED("Solved"),
    TESTING("Testing");

    private final String state;

    States(String value) {
        this.state = value;
    }

    @NonNull
    @Override
    public String toString() {
        return this.state;
    }

    public static States fromString(String str) {

        if(SOLVED.toString().equals(str)) {
            return States.SOLVED;
        } else if(TESTING.toString().equals(str)) {
            return States.TESTING;
        } else {
            return States.UNSOLVED;
        }

    }
}
