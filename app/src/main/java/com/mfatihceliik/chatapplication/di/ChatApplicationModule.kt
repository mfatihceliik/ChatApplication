package com.mfatihceliik.chatapplication.di

import android.content.Context
import com.google.gson.Gson
import com.mfatihceliik.chatapplication.BuildConfig
import com.mfatihceliik.chatapplication.data.local.SharedPreferenceManager
import com.mfatihceliik.chatapplication.data.remote.ChatApplicationApiService
import com.mfatihceliik.chatapplication.data.remote.RemoteDataSource
import com.mfatihceliik.chatapplication.data.remote.RemoteSocketSource
import com.mfatihceliik.chatapplication.data.remote.SocketService
import com.mfatihceliik.chatapplication.util.socket.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChatApplicationModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(chatApplicationApiService: ChatApplicationApiService): RemoteDataSource {
        return RemoteDataSource(chatApplicationApiService)
    }

    @Provides
    @Singleton
    @ChatApplicationApiRetrofit
    fun provideChatApplicationRetrofit(@ChatApplicationApiHttpClient okHttpClient: OkHttpClient, @ChatApplicationApiEndPoint url: String, @ChatApplicationApiGsonFactory gson: Gson) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @ChatApplicationApiHttpClient
    fun provideChatApplicationOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()
        builder.interceptors().add(HttpLoggingInterceptor().apply {
            level = if(BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        })
        builder.addInterceptor(Interceptor {
            val token = SharedPreferenceManager(context).getToken()
            val request = it.request().newBuilder().addHeader("authorization", "$token").build()
            it.proceed(request)
        })
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS))
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideChatApplicationApiService(@ChatApplicationApiRetrofit retrofit: Retrofit): ChatApplicationApiService {
        return retrofit.create(ChatApplicationApiService::class.java)
    }

    @Provides
    @Singleton
    @ChatApplicationApiEndPoint
    fun provideEndPoint(): String {
        //return "http://192.168.2.8:8080/api/"
        return "http://172.19.192.1:8080/api/"
    }

    @Provides
    @Singleton
    @ChatApplicationApiGsonFactory
    fun provideGsonFactory(): Gson {
        return Gson()
    }
}

@Module
@InstallIn(SingletonComponent::class)
object SocketIOModule {

    @Provides
    @Singleton
    fun provideSocket(@ChatApplicationSocketIoApiEndPoint url: String): Socket {
        val options = IO.Options.builder()
            .setTransports(arrayOf(WebSocket.NAME))
            .setReconnection(true)
            .setReconnectionAttempts(20)
            .setReconnectionDelay(4000)
            .build()
        return IO.socket(url, options).apply {
            connect()
        }
    }

    @Provides
    @Singleton
    fun provideRemoteSocketSource(socketService: SocketService): RemoteSocketSource {
        return RemoteSocketSource(socketService)
    }

    @Provides
    @Singleton
    @ChatApplicationSocketIoApiEndPoint
    fun provideSocketIoEndPoint(): String {
        //return "http://192.168.2.8:8080"
        return "http://172.19.192.1:8080"
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface SocketServiceModule {
    @Binds
    @Singleton
    fun bindMySocketService(socketIOManager: SocketIOManager): SocketService
}



@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatApplicationApiGsonFactory

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatApplicationApiEndPoint

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatApplicationApiRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatApplicationApiHttpClient

// SOCKET

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatApplicationSocketIoApiEndPoint

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ChatApplicationSocket
