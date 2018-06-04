package com.pichler.gitlabplugin.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.time.Instant

data class Project(
        val id: Long,
        val description: String,
        val name: String,
        @SerializedName("name_with_namespace")
        val nameWithNamespace: String,
        val path: String,
        @SerializedName("path_with_namespace")
        val pathWithNamespace: String,
        @SerializedName("created_at")
        val createdAt: Instant,
        @SerializedName("default_branch")
        val defaultBranch: String,
        @SerializedName("tag_list")
        val tagList: List<String>,
        @SerializedName("ssh_url_to_repo")
        val sshUrlToRepo: String,
        @SerializedName("http_url_to_repo")
        val httpUrlToRepo: String,
        @SerializedName("web_url")
        val webUrl: String,
        @SerializedName("avatar_url_")
        val avatarUrl: String,
        @SerializedName("star_count")
        val starCount: Long,
        @SerializedName("fork_count")
        val forkCount: Long,
        @SerializedName("last_activity_at")
        val lastActivityAt: Instant,
        @SerializedName("_links")
        val links: Links,
        val archived: Boolean,
        val visibility: String,
        val owner: Author,
        @SerializedName("resolve_outdated_diff_discussions")
        val resolveOutdatedDiffDiscussions: Boolean,
        @SerializedName("container_registry_enabled")
        val containerRegistryEnabled: Boolean,
        @SerializedName("issues_enabled")
        val issuesEnabled: Boolean,
        @SerializedName("merge_requests_enabled")
        val mergeRequestsEnabled: Boolean,
        @SerializedName("wiki_enabled")
        val wikiEnabled: Boolean,
        @SerializedName("jobs_enabled")
        val jobsEnabled: Boolean,
        @SerializedName("snippets_enabled")
        val snippetsEnabled: Boolean,
        @SerializedName("shared_runners_enabled")
        val sharedRunnersEnabled: Boolean,
        @SerializedName("lfs_enabled")
        val lfsEnabled: Boolean,
        @SerializedName("creator_id")
        val creatorID: Int,
        val namespace: Namespace,
        @SerializedName("import_status")
        val importStatus: String,
        @SerializedName("open_issues_count")
        val openIssuesCount: Int,
        @SerializedName("public_jobs")
        val publicJobs: Boolean,
        @SerializedName("ci_config_path")
        val ciConfigPath: String?,
        @SerializedName("shared_with_groups")
        val sharedWithGroups: List<String>,
        @SerializedName("only_allow_merge_if_pipeline_succeeds")
        val onlyAllowMergeIfPipelineSucceeds: Boolean,
        @SerializedName("request_access_enabled")
        val requestAccessEnabled: Boolean,
        @SerializedName("only_allow_merge_if_all_discussions_are_resolved")
        val onlyAllowMergeIfAllDiscussionsAreResolved: Boolean,
        @SerializedName("printing_merge_request_link_enabled")
        val printingMergeRequestLinkEnabled: Boolean,
        @SerializedName("merge_method")
        val mergeMethod: String
)

data class Namespace(
        val id: Long,
        val name: String,
        val path: String,
        val kind: String,
        @SerializedName("full_path")
        val fullPath: String,
        @SerializedName("parent_id")
        val parentID: Long?
)

data class Links(
        val self: String,
        val issues: String,
        @SerializedName("merge_requests")
        val mergeRequests: String,
        @SerializedName("repo_branches")
        val repoBranches: String,
        val labels: String,
        val events: String,
        val members: String
)

data class MergeRequest(
        val id: Long,
        val iid: Long,
        @SerializedName("project_id")
        val projectID: Long,
        val title: String,
        val description: String,
        val state: String,
        @SerializedName("created_at")
        val createdAt: Instant,
        @SerializedName("updated_at")
        val updatedAt: Instant,
        @SerializedName("target_branch")
        val targetBranch: String,
        @SerializedName("source_branch")
        val sourceBranch: String,
        val upvotes: Long,
        val downvotes: Long,
        val author: Author,
        val assignee: Author?,
        @SerializedName("work_in_progress")
        val workInProgress: Boolean,
        @SerializedName("merge_when_pipeline_succeeds")
        val mergeWhenPipelineSucceeds: Boolean,
        @SerializedName("merge_status")
        val mergeStatus: String,
        val sha: String,
        @SerializedName("merge_commit_sha")
        val mergeCommitSha: String?,
        @SerializedName("user_notes_count")
        val userNotesCount: Long,
        val webUrl: String
)

data class Discussion(
        val id: String,
        @SerializedName("individual_node")
        val individualNote: Boolean,
        val notes: List<Note>
)

data class Note(
        val id: Long,
        val type: String,
        val body: String,
        val author: Author,
        val createdAt: Instant,
        val updatedAt: Instant,
        val system: Boolean,
        @SerializedName("noteable_id")
        val noteableID: Long,
        @SerializedName("noteable_type")
        val noteableType: String,
        val position: Position,
        val resolveable: Boolean,
        val resolved: Boolean,
        @SerializedName("resolved_by")
        val resolvedBy: Author,
        @SerializedName("noteable_iid")
        val noteableIID: Long
)

data class Author(
        val id: Long,
        val name: String,
        val username: String,
        val state: String,
        @SerializedName("avatar_url")
        val avatarURL: String,
        @SerializedName("web_url")
        val webURL: String
)

data class Position(
        @SerializedName("base_sha")
        val baseSHA: String,
        @SerializedName("start_sha")
        val startSHA: String,
        @SerializedName("head_sha")
        val headSHA: String,
        @SerializedName("old_path")
        val oldPath: String?,
        @SerializedName("new_path")
        val newPath: String?,
        @SerializedName("position_type")
        val positionType: String,
        @SerializedName("old_line")
        val oldLine: Long?,
        @SerializedName("new_line")
        val newLine: Long?


) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}