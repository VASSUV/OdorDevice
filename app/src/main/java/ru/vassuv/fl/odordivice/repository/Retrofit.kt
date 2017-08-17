package ru.vassuv.fl.odordivice.repository

import okhttp3.OkHttpClient
import retrofit2.converter.scalars.ScalarsConverterFactory

val VASSUV_URL_STATISTICS = "https://vassuv.ru/api/"

val Retrofit: IRequest by lazy({
    retrofit2.Retrofit.Builder()
            .baseUrl(VASSUV_URL_STATISTICS)
            .client(OkHttpClient
                    .Builder()
                    .followRedirects(true)
                    .addNetworkInterceptor {
                        val request = it.request().newBuilder().build()
                        println("===> SERVER url =" + request.url())
                        println("===> SERVER method =" + request.method())
                        println("===> SERVER headers =" + request.headers()
                                .toMultimap()
                                .map { "\n            ${it.key}:${it.value.toList().first()}" }
                                .joinToString(prefix = "", postfix = ""))
                        val response = it.proceed(request)
                        println("<<<< SERVER" + response)
                        response
                    }.build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(IRequest::class.java)
})
