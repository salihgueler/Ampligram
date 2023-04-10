package dev.salih.ampligram.model

data class Photo(
    val id: String,
    val url: String,
    val description: String,
    val username: String,
    val location: String,
    var comments: List<Comment>,
    var isFavorite: Boolean,
)
