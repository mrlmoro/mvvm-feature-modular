package com.mrlmoro.gitrepo.data.dto

import com.squareup.moshi.Json

sealed class OwnerDto {

    data class Response(
        val login: String,
        @field:Json(name = "avatar_url") val avatarUrl: String?
    ) : OwnerDto()

}