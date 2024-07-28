package com.example.skillcinema.di

import com.example.skillcinema.BuildConfig
import com.example.skillcinema.data.network.FilmsApi
import com.example.skillcinema.data.network.StaffApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule {

    @Singleton
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            // Создаём перехватчик, добавляющий всем запросам header
            .client(
                OkHttpClient.Builder().addInterceptor {
                    val request =
                        it.request().newBuilder().addHeader("Content-Type", "application/json")
                            .addHeader("X-API-KEY", API_KEY).build()
                    it.proceed(request)
                }.build()
            ).build()
    }

    @Provides
    fun provideFilmApi(): FilmsApi = retrofit.create(FilmsApi::class.java)

    @Provides
    fun provideStaffApi(): StaffApi = retrofit.create(StaffApi::class.java)

    companion object {
        // Ключ для отправки запроса
        private const val API_KEY = BuildConfig.API_KEY

        // Url сервера
        private const val BASE_URL = "https://kinopoiskapiunofficial.tech"
    }
}