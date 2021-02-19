package com.mrlmoro.gitrepo

import com.mrlmoro.gitrepo.data.dto.OwnerDto
import com.mrlmoro.gitrepo.data.dto.RepositoryDto
import com.mrlmoro.gitrepo.domain.model.Owner
import com.mrlmoro.gitrepo.domain.model.Repository

object Mocks {
    val repositoryDtoResponseMock = RepositoryDto.Response(
        id = 1,
        name = "mockRepo",
        description = "mock",
        owner = OwnerDto.Response(
            login = "mockName",
            avatarUrl = "http://mock.com"
        )
    )

    val repositoryMock = Repository(
        id = 1,
        name = "mockRepo",
        description = "mock",
        owner = Owner(
            username = "mockName",
            avatarUrl = "http://mock.com"
        )
    )
}