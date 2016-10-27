package com.lulu.rxjavademo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import rx.Observable;
import rx.Observer;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //简单使用 RxJava 方式进行编程
    public void btnFirstTest(View view) {
        //step1: 创建Observable

        //step2: 创建Observer

        String[] devices = {"iPhone 6s", "MI Note2", "Huawei P9"};
        Observable<String> observable = Observable.from(devices);

        // 只要被观察者 Observable 设置 观察者订阅 subscribe
        // 那么 观察者 就可以接收到相应的数据进行响应和处理

        //把观察者对象, 订阅到被观察者对象上
        observable.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {
                Log.d(TAG, "onCompleted: 观察者执行完成");
            }

            @Override
            public void onError(Throwable e) {
               e.printStackTrace();
            }
            /**
             * 观察者接收到一条新的数据或者事件
             * @param s
             */
            @Override
            public void onNext(String s) {
                Log.d(TAG, "onNext:观察者收到新的数据 " + s);
                if ("MI Note2".equals(s)){
                    throw new RuntimeException(TAG + "我炸了!!!!!!");
                }
            }
        });
    }
}
