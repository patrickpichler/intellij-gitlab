package com.pichler.gitlabplugin.service

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.pichler.gitlabplugin.GitlabConfigurationStateService
import com.pichler.gitlabplugin.GitlabConfigurationStateService.GitlabConfigurationState
import com.pichler.gitlabplugin.api.GitlabAPIWithProjectID
import com.pichler.gitlabplugin.api.buildGitlabAPI
import org.funktionale.option.Option
import org.funktionale.option.optionTry
import org.funktionale.option.toOption

class GitlabAPIService(
        val project: Project
) {

    companion object {
        fun instance(project: Project) = ServiceManager.getService(project, GitlabAPIService::class.java)
    }

    val configurationStateService = GitlabConfigurationStateService.instance(project)

    val api: Option<GitlabAPIWithProjectID> = configurationStateService.state.toOption()
            .filter(GitlabConfigurationState::isEverythingSet)
            .flatMap { optionTry { buildGitlabAPI(it.gitlabURL!!, it.token!!, it.projectID!!) } }
}