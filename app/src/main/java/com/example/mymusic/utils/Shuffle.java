package com.example.mymusic.utils;


import androidx.annotation.IntDef;

@IntDef({
        Shuffle.ON,
        Shuffle.OFF
})
public @interface Shuffle {
    int ON = 0;
    int OFF = 1;
}
