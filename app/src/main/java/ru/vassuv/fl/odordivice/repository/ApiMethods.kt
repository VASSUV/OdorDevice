//package ru.vassuv.fl.odordivice.repository
//
//import io.reactivex.Single
//import retrofit2.http.*
//
//interface ApiMethods {
//    companion object {
//        const val BASE_URL = "https://api.instagram.com/"
//        const val ACCESS_TOKEN = "access_token"
//    }
//
//    @GET("v1/users/self/")
//    fun loadMyProfile(@Query(ACCESS_TOKEN) token: String): Single<String>
//}
