package com.pichler.gitlabplugin.service

import com.intellij.openapi.project.Project
import com.pichler.gitlabplugin.GitlabConfigurationStateService
import com.pichler.gitlabplugin.GitlabConfigurationStateService.GitlabConfigurationState
import com.pichler.gitlabplugin.api.GitlabAPI
import com.pichler.gitlabplugin.head
import com.pichler.gitlabplugin.model.MergeRequest
import git4idea.repo.GitRepositoryManager
import org.funktionale.option.Option
import org.funktionale.option.toOption
import retrofit2.Response

class GitlabMergeRequestSyncer(
        val project: Project
) {
    private val stateService: GitlabConfigurationStateService = GitlabConfigurationStateService.instance(project)

    private val mergeRequestSyncerMap: MutableMap<Branch, MergeRequestSyncer> = mutableMapOf()

    fun getMergeRequestSyncer(branch: Branch) = mergeRequestSyncerMap.computeIfAbsent(branch, { MergeRequestSyncer(project, stateService.state, branch) })
}

class MergeRequestSyncer(
        val project: Project,
        val gitlabConfigurationState: GitlabConfigurationState,
        val branch: Branch
) {
    var mergeRequest: Option<MergeRequest> = Option.None
    val gitRepositoryManager = GitRepositoryManager.getInstance(project)
    val gitlabAPIService = GitlabAPIService.instance(project)

    init {
        mergeRequest = gitlabAPIService.api.get().listMergeRequests(sourceBranch = branch.name).execute().toOption()
                .filter { it.isSuccessful }
                .map(Response<List<MergeRequest>>::body)
                .filterNot { it == null }
                .flatMap { it!!.head() }
    }
}

data class Branch(
        val name: String
)