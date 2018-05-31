package com.pichler.gitlabplugin.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.time.Instant

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