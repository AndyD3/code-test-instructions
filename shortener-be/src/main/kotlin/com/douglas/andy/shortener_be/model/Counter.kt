package com.douglas.andy.shortener_be.model

import org.springframework.data.annotation.Id;

data class Counter(
    @Id
    val id:Int,
    val count: Int
)
