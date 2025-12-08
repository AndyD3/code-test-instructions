package com.douglas.andy.shortener_be.shorten

class Counter() {

    private var count=1;

    fun getValue() :Int {
        count++;
        return count;
    }
}