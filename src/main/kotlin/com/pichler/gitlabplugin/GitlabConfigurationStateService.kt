package com.pichler.gitlabplugin

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "GitlabConfigurationStateService", storages = [(Storage("gitlab.xml"))])
class GitlabConfigurationStateService : PersistentStateComponent<GitlabConfigurationStateService.GitlabConfigurationState> {

    companion object {
        val instance: GitlabConfigurationStateService
            get() = ServiceManager.getService(GitlabConfigurationStateService::class.java)
    }

    private val state: GitlabConfigurationState = GitlabConfigurationState()

    override fun getState(): GitlabConfigurationState = state

    override fun loadState(state: GitlabConfigurationState) {
        this.state.apply {
            gitlabURL = state.gitlabURL
            token = state.token
        }
    }

    class GitlabConfigurationState(
            var gitlabURL: String? = null,
            var token: String? = null
    )
}