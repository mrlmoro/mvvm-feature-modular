package com.mrlmoro.gitrepo.domain.model

data class Repository(
        val id: Long,
        val name: String,
        val description: String,
        val owner: Owner
)