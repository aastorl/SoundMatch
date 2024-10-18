package com.example.soundmatch.domain

data class PodcastShow(
    val id: String,
    val name: String,
    val imageUrlString: String,
    val nameOfPublisher: String,
    val htmlDescription: String
)