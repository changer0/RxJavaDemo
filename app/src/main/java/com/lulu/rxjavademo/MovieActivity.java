package com.lulu.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lulu.rxjavademo.services.DoubanService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MovieActivity extends AppCompatActivity {

    private DoubanService mDoubanService;
    private static final String TAG = "MovieActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);


        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("https://api.douban.com/v2/");
        builder.addConverterFactory(ScalarsConverterFactory.create());
        //增加Call类型的转换器, 可以把Retrofit和RxJava联动
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        Retrofit retrofit = builder.build();

        mDoubanService = retrofit.create(DoubanService.class);


    }

    public void btnGetList(View view) {
        Observable<String> observable = mDoubanService.getMovieTop250(0, 10);
        observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new Observer<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onNext(String s) {
                                Log.d(TAG, "onNext: s ==> " + s);
                            }
                        }
//                        new Action1<String>() {
//                            @Override
//                            public void call(String s) {
//                                Log.d(TAG, "call: s ==>" + s);
//                            }
//                        }
                );
    }
}
