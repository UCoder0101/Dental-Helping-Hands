package com.dentalhelpinghands.Api

import com.dentalhelpinghands.common.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.*


class RetrofitBuilder {

    companion object {
        const val HEADER_KEY_VALUE = "ylvkgexwkqeuduutirtpumgvshofclch"
        private const val HEADER_KEY_KEY = "key"
        private const val HEADER_KEY_TOKEN = "token"
        private const val BASE_URL = "https://client.appmania.co.in/DentalHelpingHands/Api/"


        private var retrofit: Retrofit? = null

        fun getClient(token: String): Retrofit? {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }

        /* fun backendApiClient(token: String?): Retrofit? {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            return getRetrofitBuilder(token, gson)
        }

        //// base url with https
        private fun getRetrofitBuilder(token: String?, gson: Gson): Retrofit? {
            try {
                //// Create a trust manager that does not validate certificate chains
                val trustAllCerts = arrayOf<TrustManager>(
                    object : X509TrustManager {
                        override fun checkClientTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun checkServerTrusted(
                            chain: Array<X509Certificate>,
                            authType: String
                        ) {
                        }

                        override fun getAcceptedIssuers(): Array<X509Certificate> {
                            return arrayOf()
                        }
                    }
                )

                //// Install the all-trusting trust manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(null, trustAllCerts, SecureRandom())
                // // Create an ssl socket factory with our all-trusting manager
                val sslSocketFactory =
                    sslContext.socketFactory // : Info: base url https to uncomment this line.
                val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
                if (Constants.IS_SHOW_LOG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    builder.addInterceptor(logging)
                }
                builder.readTimeout(120, TimeUnit.SECONDS)
                builder.connectTimeout(2, TimeUnit.MINUTES)
                builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
                    val request: Request
                    request = if (token != null) {
                        chain.request().newBuilder()
                            .addHeader(
                                HEADER_KEY_KEY,
                                HEADER_KEY_VALUE
                            )
                            .addHeader(HEADER_KEY_TOKEN, token)
                            .build()
                    } else {
                        chain.request().newBuilder()
                            .addHeader(
                                HEADER_KEY_KEY,
                                HEADER_KEY_VALUE
                            )
                            .build()
                    }
                    chain.proceed(request)
                })
                builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                builder.hostnameVerifier(HostnameVerifier { hostname: String?, session: SSLSession? -> true })
                return Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(builder.build())
                    .build()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return getRetrofitBuilderWithoutHttps(token, gson)
        }

        //// base url without https
        private fun getRetrofitBuilderWithoutHttps(token: String?, gson: Gson): Retrofit? {
            //// this case use without https base url
            //// base url without https
            val builder: OkHttpClient.Builder = OkHttpClient().newBuilder()
            if (Constants.IS_SHOW_LOG) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                builder.addInterceptor(logging)
            }
            builder.readTimeout(120, TimeUnit.SECONDS)
            builder.connectTimeout(2, TimeUnit.MINUTES)
            builder.addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val request: Request
                request = if (token != null) {
                    chain.request().newBuilder()
                        .addHeader(
                            HEADER_KEY_KEY,
                            HEADER_KEY_VALUE
                        )
                        .addHeader(HEADER_KEY_TOKEN, token)
                        .build()
                } else {
                    chain.request().newBuilder()
                        .addHeader(
                            HEADER_KEY_KEY,
                            HEADER_KEY_VALUE
                        )
                        .build()
                }
                chain.proceed(request)
            })
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(builder.build())
                .build()
        }*/
    }
}