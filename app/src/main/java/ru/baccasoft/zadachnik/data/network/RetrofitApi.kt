package ru.baccasoft.zadachnik.data.network

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*
import ru.baccasoft.zadachnik.data.network.models.*
import ru.baccasoft.zadachnik.features.mainScreen.TaskModel

interface RetrofitApi {

    // region AUTH

    @GET("")
    suspend fun beat(): Result<BeatModel>

    @POST("send-code")
    suspend fun sendCode(
        @Body body: SendCodeRequestModel
    ): Result<SendCodeResponseModel>

    @POST("login")
    suspend fun login(
        @Body body: LoginRequestModel
    ): Result<AuthResponseModel>

    @GET("refresh_token")
    fun refreshToken(
    ): Call<AuthResponseModel>

    // endregion

    // region TASKS

    @GET("api/v1/tasks")
    suspend fun getFilteredDocuments(
        @Query("search") search: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("page_size") size: Int,
        @Query("only_archive") archived: Int, // вот тут по сути bool: 0-false, 1-true
        @Query("only_active") active: Int,
        @Query("is_user_creator") is_user_creator: Int,
        @Query("is_user_responsible") is_user_responsible: Int,
    ): Result<TaskListModel>

    @PUT("api/v1/tasks/{id}/to-archive")
    suspend fun sendTaskToArchive(
        @Path("id") id: String,
        @Body body: TaskToArchiveModel
    ): Result<TaskModel>

    @GET("api/v1/tip")
    suspend fun getTips(): Result<HintModel>

    @GET("api/v1/tasks/{id}")
    suspend fun getTaskDetails(
        @Path("id") id: String
    ): Result<GetTaskModel>

    @POST("api/v1/comments")
    suspend fun sendComment(@Body body: AddCommentModel)

    @POST("api/v1/tasks")
    suspend fun createTask(@Body body: CreateTaskModel) : Result<GetTaskModel>

    @PUT("api/v1/tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body body: UpdateTaskModel) : Result<GetTaskModel>

    // endregion


    // region FILES

    @GET("api/v1/files/{id}")
    suspend fun getFilesList(
        @Path("id") id: String
    ): Result<ArrayList<FilesListModel>>

    @GET("api/v1/files/download/{hashed_data}")
    @Streaming
    suspend fun getFile(
        @Path("hashed_data") hashed_data: String?
    ): Result<ResponseBody>
    // endregion


}