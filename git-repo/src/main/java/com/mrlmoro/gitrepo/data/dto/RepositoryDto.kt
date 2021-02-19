package com.mrlmoro.gitrepo.data.dto

import com.mrlmoro.gitrepo.domain.model.Owner
import com.mrlmoro.gitrepo.domain.model.Repository

sealed class RepositoryDto {

    data class Response(
        val id: Long,
        val name: String,
        val description: String?,
        val owner: OwnerDto.Response
    ) : RepositoryDto() {

        fun toRepository() = Repository(
            id = this.id,
            name = this.name,
            description = this.description ?: "",
            owner = Owner(
                username = this.owner.login,
                avatarUrl = this.owner.avatarUrl ?: ""
            )
        )

    }

}