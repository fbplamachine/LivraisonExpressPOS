package com.israel.livraisonexpresspos.data;

import com.israel.livraisonexpresspos.BuildConfig;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
//    private final static String baseUrl = "https://api.v1.livraison-express.net/api/v1.1/";//prod
//    private final static String baseUrl = "https://api.test.livraison-express.net/api/v1.1/";//test
//    private final static String baseUrl = "https://api.staging.livraison-express.net/api/v1.1/";//pre-prod
    private final static String baseUrl = BuildConfig.API_URL;
    private static  Retrofit retrofit;
    private final static int connectionTimeOut = 20;
    private final static int writeTimeOut = 30;
    private final static int readTimeOut = 90;

    public  static Retrofit getClient(){
        if (retrofit == null){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .writeTimeout(writeTimeOut, TimeUnit.SECONDS)
                    .readTimeout(readTimeOut, TimeUnit.SECONDS)
                    .connectTimeout(connectionTimeOut, TimeUnit.SECONDS)
                    .addNetworkInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Interceptor.Chain chain) throws IOException {
                            Request.Builder builder = chain.request()
                                    .newBuilder()
                                    .addHeader("Connection", "close");
                            return chain.proceed(builder.build());
                        }
                    })
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
