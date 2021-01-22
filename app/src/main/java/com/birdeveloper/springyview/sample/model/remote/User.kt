package com.birdeveloper.springyview.sample.model.remote

import java.io.Serializable

data class User(
    val id: Int? = null,
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val avatar: String? = null
): Serializable