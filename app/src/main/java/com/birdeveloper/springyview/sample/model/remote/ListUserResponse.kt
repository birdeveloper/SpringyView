package com.birdeveloper.springyview.sample.model.remote

import java.io.Serializable

data class ListUserResponse(
    val page: Int? = null,
    val per_page: Int? = null,
    val total: Int? = null,
    val total_pages:Int? = null,
    val data: MutableList<User?>? = null,
    val support: Support): Serializable