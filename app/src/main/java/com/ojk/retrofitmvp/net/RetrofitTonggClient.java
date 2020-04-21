package com.ojk.retrofitmvp.net;



import android.util.Log;

import com.ojk.retrofitmvp.utils.phoneUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitTonggClient {

    public static String YHNJ_URL = "xxxxxxxxx";


    private static volatile RetrofitTonggClient instance;
    private APITonggService apiService;

    private RetrofitTonggClient() {
    }

    public static RetrofitTonggClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitTonggClient.class) {
                if (instance == null) {
                    instance = new RetrofitTonggClient();
                }
            }
        }
        return instance;
    }
    public static String TAG = "LogInterceptor";

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {
            try {
                String text = URLDecoder.decode(message, "utf-8");
                Log.e("OKHttp-----", text);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.e("OKHttp-----", message);
            }
        }
    });


    public APITonggService getApi() {
        //初始化一个client,不然retrofit会自己默认添加一个
        OkHttpClient client = new OkHttpClient().newBuilder()
                //设置拦截器
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        Request request = chain.request()
                                .newBuilder()
                                .addHeader("Connection", "Keep-Alive")
                                .addHeader("Accept", "application/json")
                                .addHeader("platform", "Android")
                                .addHeader("appversion", "1.0")
                               .addHeader("token", "aaaa")
                                .addHeader("deviceInfo", phoneUtils.getHeaders())
                                .addHeader("lang", "id")
                                .build();
                        long startTime = System.currentTimeMillis();
                        okhttp3.Response response = chain.proceed(chain.request());
                        long endTime = System.currentTimeMillis();
                        long duration=endTime-startTime;
                        okhttp3.MediaType mediaType = response.body().contentType();
                        String content = response.body().string();
                        Log.d(TAG,"\n");
                        Log.d(TAG,"----------Start----------------");
                        Log.d(TAG, "| "+request.toString());
                        String method=request.method();
                        if("POST".equals(method)){
                            StringBuilder sb = new StringBuilder();
                            if (request.body() instanceof FormBody) {
                                FormBody body = (FormBody) request.body();
                                for (int i = 0; i < body.size(); i++) {
                                    sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                                }
                                sb.delete(sb.length() - 1, sb.length());
                                Log.d(TAG, "| RequestParams:{"+sb.toString()+"}");
                            }
                        }
                        Log.d(TAG, "| Response:" + content);
                        Log.d(TAG,"----------End:"+duration+"毫秒----------");
                        return chain.proceed(request);
                    }
                })   .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                }).sslSocketFactory(getsslsocket()).addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                //设置网络请求的Url地址
                .baseUrl(YHNJ_URL)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                //设置网络请求适配器，使其支持RxJava与RxAndroid
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        //创建—— 网络请求接口—— 实例
        apiService = retrofit.create(APITonggService.class);
        return apiService;
    }



    private static SSLSocketFactory getsslsocket() {

        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }


}
