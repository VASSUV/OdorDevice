//package ru.vassuv.fl.odordivice.repository
//
//import io.reactivex.schedulers.Schedulers
//import okhttp3.OkHttpClient
//import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.createWithScheduler
//import retrofit2.converter.scalars.ScalarsConverterFactory
//import ru.vassuv.fl.odordivice.App.Companion.log
//
//val Retrofit: ApiMethods by lazy({
//    retrofit2.Retrofit.Builder()
//            .baseUrl(ApiMethods.BASE_URL)
//            .client(OkHttpClient
//                    .Builder()
//                    .followRedirects(true)
//                    .addNetworkInterceptor {
//                        val request = it.request().newBuilder().build()
//                        log("===> SERVER", "url =", request.url())
//                        log("===> SERVER", "method =", request.method())
//                        log("===> SERVER", "headers =", request.headers()
//                                .toMultimap()
//                                .map { "\n            ${it.key}:${it.value.toList().first()}" }
//                                .joinToString(prefix = "", postfix = ""))
//                        val response = it.proceed(request)
//                        log("<<<< SERVER", response)
//                        response
//                    }.build())
//            .addConverterFactory(ScalarsConverterFactory.create())
//            .addCallAdapterFactory(createWithScheduler(Schedulers.io()))
//            .build()
//            .create(ApiMethods::class.java)
//})
