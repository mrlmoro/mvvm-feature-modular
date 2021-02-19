package com.mrlmoro.gitrepo.data.dto

import com.squareup.moshi.Json

data class PagingDto<T>(
    @field:Json(name = "total_count") val totalCount: Long,
    val items: List<T>
)