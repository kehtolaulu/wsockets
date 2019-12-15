package com.kehtolaulu.wsockets.web

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST

interface Api {
    @POST("login")
    fun login(@Body userDevice: UserDevice): Single<Response>
}
