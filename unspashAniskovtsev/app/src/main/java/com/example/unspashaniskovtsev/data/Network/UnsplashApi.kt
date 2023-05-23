package com.example.unspashaniskovtsev.data.Network

import androidx.room.util.query
import com.example.unspashaniskovtsev.Models.*
import net.openid.appauth.ResponseTypeValues
import okhttp3.Call
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface UnsplashApi {

    @GET(value = "me")
    suspend fun getInfoAboutCurrentUser(): CurrentUser

    @GET(value = "users/{owner}")
    suspend fun getImage(
        @Path(value = "owner") userName: String
    ): User

    @GET(value = "photos")
    suspend fun getListOfPhotos(
        @Query(value = "per_page") itemPerPage: Int, @Query(value = "page") pageCount: Int
    ): List<Photo>

    @POST(value = "photos/{id}/like")
    suspend fun likePhoto(
        @Path(value = "id") userID: String
    )
    @DELETE(value = "photos/{id}/like")
    suspend fun unlikePhoto(
        @Path(value = "id") id: String
    )

    @GET(value = "photos/{id}")
    suspend fun getDeatilPhoto(
        @Path(value = "id") id:String
    ):DetailPhoto

    @GET
    suspend fun getFile(
        @Url url: String
    ): ResponseBody


    @GET(value = "collections")
    suspend fun getCollections(
        @Query(value = "per_page") itemPerPage: Int, @Query(value = "page") pageCount: Int
    ): List<Collections>

    @GET(value = "photos")
    suspend fun getPhotos(
        @Query(value = "per_page") itemPerPage: Int, @Query(value = "page") pageCount: Int
    ): List<Photo>


    @GET(value = "collections/{id}/photos")
    suspend fun getPhotosFromCollection(
        @Path(value = "id") id: String,
        @Query(value = "per_page") itemPerPage: Int, @Query(value = "page") pageCount: Int
    ): List<Photo>

    @GET(value = "search/photos")
    suspend fun searchPhotos(
        @Query(value = "query") query: String,
        @Query(value = "per_page") itemPerPage: Int, @Query(value = "page") pageCount: Int
    ): SearchResponseWrapper

    @GET(value = "users/{username}/likes")
    suspend fun getLikedPhotos(
        @Path(value = "username") userName: String,
        @Query(value = "per_page") itemPerPage: Int, @Query(value = "page") pageCount: Int
    ): List<Photo>

}