package com.pichler.gitlabplugin

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.annotations.Transient

@State(name = "GitlabConfigurationStateService", storages = [(Storage("gitlab.xml"))], reloadable = false)
class GitlabConfigurationStateService(
        val project: Project
) : PersistentStateComponent<GitlabConfigurationStateService.GitlabConfigurationState> {

    companion object {
        fun instance(project: Project): GitlabConfigurationStateService =
                ServiceManager.getService(project, GitlabConfigurationStateService::class.java)
    }

    private val state: GitlabConfigurationState = GitlabConfigurationState()

    override fun getState(): GitlabConfigurationState = state

    override fun loadState(state: GitlabConfigurationState) {
        this.state.apply {
            gitlabURL = state.gitlabURL
            token = state.token
            projectState = state.projectState
        }
    }

    class GitlabConfigurationState(
            var gitlabURL: String? = null,
            var token: String? = null,
            var projectID: Long? = null,
            var projectName: String? = null
    ) {
        constructor(gitlabURL: String? = null,
                    token: String? = null,
                    projectState: ProjectState) : this(gitlabURL, token, projectState.id, projectState.name)

        var projectState: ProjectState?
            @Transient
            set(value) {
                projectID = value?.id
                projectName = value?.name
            }
            @Transient
            get() {
                val projectID = this.projectID
                val projectName = this.projectName

                return if (projectID != null && projectName != null)
                    ProjectState(projectID, projectName)
                else
                    null
            }

        fun isEverythingSet(): Boolean = projectName != null && projectID != null && gitlabURL != null && token != null
    }

    data class ProjectState(
            var id: Long,
            var name: String
    )
}