package com.example.network.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
//import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


// Query Parameter :- https://android-kotlin-fun-mars-server.appspot.com/realestate?filter=buy

enum class MarsApiFilter(val value: String) {
    SHOW_RENT("rent"),
    SHOW_BUY("buy"),
    SHOW_ALL("all") }

private const val BASE_URL = " https://android-kotlin-fun-mars-server.appspot.com/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    //.addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface MarsApiService{

    @GET("realestate")
    // fun getProperties(): Call<String>
    fun getProperties(@Query("filter") type : String): Deferred<List<MarsProperty>> // Refer Glossary
            //Call<List<MarsProperty>>

}


object MarsApi{

    val retrofitService : MarsApiService by lazy{
        retrofit.create(MarsApiService::class.java)
    }
}


/*                      *----    Glossary    ----*
The Deferred interface defines a coroutine job that returns a result value (Deferred inherits from Job).
The Deferred interface includes a method called await(), which causes your code to wait without blocking
until the value is ready, and then that value is returned.
*/