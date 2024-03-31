package ex.company.productlistvk.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ex.company.productlistvk.data.network.retrofit.RetrofitApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    fun providesBaseUrl(): String = "https://dummyjson.com/"

    @Singleton
    @Provides
    fun providesRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofitService(retrofit: Retrofit): RetrofitApi = retrofit.create(RetrofitApi::class.java)
}