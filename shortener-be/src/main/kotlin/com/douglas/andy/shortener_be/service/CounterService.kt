package com.douglas.andy.shortener_be.service

interface CounterService {
    fun get() : Int
    fun set(count: Int): Int
}