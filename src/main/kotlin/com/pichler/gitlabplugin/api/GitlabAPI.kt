package com.pichler.gitlabplugin.api

import com.google.gson.*
import com.pichler.gitlabplugin.model.Discussion
import com.pichler.gitlabplugin.model.MergeRequest
import com.pichler.gitlabplugin.model.Note
import com.pichler.gitlabplugin.model.Project
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.java8.Java8CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.reflect.Type
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.concurrent.CompletableFuture

interface GitlabAPI {

    @GET("projects")
    fun listProjects(@Query("archived") archived: Boolean? = null,
                          @Query("visibility") visibility: String? = null,
                          @Query("order_by") orderBy: String? = null,
                          @Query("sort") sort: Sort? = null,
                          @Query("search") search: String? = null,
                          @Query("simple") simple: Boolean? = null,
                          @Query("owned") owned: Boolean? = null,
                          @Query("membership") membership: Boolean? = null,
                          @Query("starred") starred: Boolean? = null,
                          @Query("statistics") statistics: Boolean? = null,
                          @Query("with_custom_attributes") withCustomAttributes: Boolean? = null,
                          @Query("with_issues_enabled") withIssuesEnabled: Boolean? = null,
                          @Query("with_merge_request_enabled") withMergeRequestEnabled: Boolean? = null): Call<List<Project>>

    @GET("projects")
    fun listProjectsAsync(@Query("archived") archived: Boolean? = null,
                     @Query("visibility") visibility: String? = null,
                     @Query("order_by") orderBy: String? = null,
                     @Query("sort") sort: Sort? = null,
                     @Query("search") search: String? = null,
                     @Query("simple") simple: Boolean? = null,
                     @Query("owned") owned: Boolean? = null,
                     @Query("membership") membership: Boolean? = null,
                     @Query("starred") starred: Boolean? = null,
                     @Query("statistics") statistics: Boolean? = null,
                     @Query("with_custom_attributes") withCustomAttributes: Boolean? = null,
                     @Query("with_issues_enabled") withIssuesEnabled: Boolean? = null,
                     @Query("with_merge_request_enabled") withMergeRequestEnabled: Boolean? = null): CompletableFuture<List<Project>>

    @GET("projects/{id}/merge_requests")
    fun listMergeRequests(@Path("id") projectID: Int? = null,
                          @Query("source_branch") sourceBranch: String? = null,
                          @Query("target_branch") targetBranch: String? = null,
                          @Query("state") state: MergeRequestState? = null,
                          @Query("order_by") orderBy: OrderBy? = null): Call<List<MergeRequest>>

    @GET("projects/{id}/merge_requests/{merge_request_iid}/notes")
    fun listMergeRequestNotes(@Path("id") projectID: Long,
                              @Path("merge_request_iid") mergeRequestIID: Long,
                              @Query("sort") sort: Sort? = null,
                              @Query("order_by") orderBy: OrderBy? = null): Call<List<Note>>

    @DELETE("projects/{id}/merge_requests/{merge_request_iid}/notes/{note_id}")
    fun deleteMergeRequestNote(@Path("id") projectID: Long,
                               @Path("merge_request_iid") mergeRequestIID: Long,
                               @Path("note_id") noteId: Long): Call<Note>

    @GET("projects/{id}/merge_requests/{merge_request_iid}/discussions")
    fun listMergeRequestDiscussions(@Path("id") projectID: Long,
                                    @Path("merge_request_iid") mergeRequestIID: Long): Call<List<Discussion>>

    @GET("projects/{id}/merge_requests/{merge_request_iid}/discussions/{discussion_id}")
    fun getMergeRequestDiscussion(@Path("id") projectID: Long,
                                  @Path("merge_request_iid") mergeRequestIID: Long,
                                  @Path("discussion_id") discussionID: String): Call<Discussion>

    @GET("projects/{id}/merge_requests/{merge_request_iid}/discussions/{discussion_id}/notes")
    fun listMergeRequestDiscussionNotes(@Path("id") projectID: Long,
                                        @Path("merge_request_iid") mergeRequestIID: Long,
                                        @Path("discussion_id") discussionID: String,
                                        @Query("sort") sort: Sort? = null,
                                        @Query("order_by") orderBy: OrderBy? = null): Call<List<Note>>

    @PUT("projects/{id}/merge_requests/{merge_request_iid}/discussions/{discussion_id}")
    fun resolveMergeRequestDiscussion(@Path("id") projectID: Long,
                                      @Path("merge_request_iid") mergeRequestIID: Long,
                                      @Path("discussion_id") discussionID: String,
                                      @Query("resolved") resolved: Boolean): Call<Discussion>

    @POST("projects/{id}/merge_requests/{merge_request_iid}/discussions/{discussion_id}/notes")
    fun addMergeRequestDiscussionNote(@Path("id") projectID: Long,
                                      @Path("merge_request_iid") mergeRequestIID: Long,
                                      @Path("discussion_id") discussionID: String,
                                      @Query("body") body: String): Call<Note>

    @DELETE("projects/{id}/merge_requests/{merge_request_iid}/discussions/{discussion_id}/notes/{note_id}")
    fun deleteMergeRequestDiscussionNote(@Path("id") projectID: Long,
                                         @Path("merge_request_iid") mergeRequestIID: Long,
                                         @Path("discussion_id") discussionID: String,
                                         @Path("note_id") noteId: Long): Call<Note>

    @POST("projects/{id}/merge_requests/{merge_request_iid}/discussions")
    fun createDiscussion(@Path("id") projectID: Long,
                         @Path("merge_request_iid") mergeRequestIID: Long,
                         @Query("body") body: String): Call<Discussion>

    @POST("projects/{id}/merge_requests/{merge_request_iid}/discussions")
    fun createDiscussion(@Path("id") projectID: Long,
                         @Path("merge_request_iid") mergeRequestIID: Long,
                         @Query("body") body: String,
                         @Query("position[base_sha]") baseSHA: String,
                         @Query("position[start_sha]") startSHA: String,
                         @Query("position[head_sha]") headSHA: String,
                         @Query("position[old_path]") oldPath: String? = null,
                         @Query("position[new_path]") newPath: String? = null,
                         @Query("position[old_line]") oldLine: Long? = null,
                         @Query("position[new_line]") newLine: Long? = null,
                         @Query("position[position_type]") positionType: PositionType = PositionType.TEXT): Call<Discussion>


}

enum class PositionType(val value: String) {
    TEXT("text"),
    IMAGE("image");

    override fun toString(): String = value
}

enum class Sort(val value: String) {
    ASC("asc"),
    DESC("desc");

    override fun toString(): String = value
}

enum class OrderBy(val value: String) {
    CREATED_AT("created_at"),
    UPDATED_AT("updated_at");

    override fun toString(): String = value
}

enum class MergeRequestState(val value: String) {
    OPENED("opened"),
    CLOSED("closed"),
    MERGED("merged");

    override fun toString(): String = value
}

fun buildGitlabAPI(gitlabURL: String,
                   gitlabToken: String): GitlabAPI {
    val gson = GsonBuilder()
            .registerTypeAdapter(Instant::class.java, InstantTypeAdapter())
            .create()

    return Retrofit.Builder()
            .baseUrl("$gitlabURL/api/v4/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(Java8CallAdapterFactory.create())
            .client(OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()

                        val request = original.newBuilder()
                                .header("PRIVATE-TOKEN", gitlabToken)
                                .build()

                        chain.proceed(request)
                    }
                    .build())
            .build()
            .create(GitlabAPI::class.java)
}

private class InstantTypeAdapter : JsonDeserializer<Instant?>, JsonSerializer<Instant?> {

    override fun serialize(src: Instant?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement =
            JsonPrimitive(DateTimeFormatter.ISO_INSTANT.format(src))

    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Instant? {
        if (json == null)
            return null

        if (!json.isJsonPrimitive || !json.asJsonPrimitive.isString)
            throw JsonParseException("Element has to be a string")


        return Instant.parse(json.asJsonPrimitive.asString)
    }
}