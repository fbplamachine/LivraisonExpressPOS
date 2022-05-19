package com.israel.livraisonexpresspos.data.services.inventory_management;

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

public class InventoryManagementClient {

    //    private final static String baseUrl = BuildConfig.API_URL;

    /*todo : move it into build variant*/
    private final static String baseUrl = "https://api.stock.staging.livraison-express.net/api/"; //preprod


    private static Retrofit retrofit;
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
