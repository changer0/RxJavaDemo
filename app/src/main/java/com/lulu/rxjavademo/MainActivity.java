package com.lulu.rxjavademo;

import android.content.pm.ApplicationInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ArrayAdapter<String> mAdapter;

    private List<String> mPackages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.app_list_view);
        mPackages = new ArrayList<>();
        if (listView != null) {
            mAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, mPackages
            );
            listView.setAdapter(mAdapter);
        }

        EditText editText = (EditText) findViewById(R.id.main_edit_text);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "onTextChanged: s=>" + s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


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
        //当被观察者调用了 subscribe 之后, 内部实际上就是在发出数据, 交给观察者的onNext(T)
        // 全部发完就调用 onCompleted(),如果onNext出错 就会自动调用onError(Throwable e)
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
                if ("MI Note2".equals(s)) {
                    throw new RuntimeException(TAG + "我炸了!!!!!!");
                }
            }
        });
    }

    public void btnSecondTest(View view) {
        // 案例: 获取手机中安装的软件的包名
        getPackages()
                .observeOn(AndroidSchedulers.mainThread()) //指定执行的线程, 通常就是Android中的UI要设定的
                .subscribe(
                new Observer<ApplicationInfo>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: 完成");
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: 又炸了!!!!" + TAG);

                        mPackages.clear();
                        mAdapter.notifyDataSetInvalidated();
                    }

                    @Override
                    public void onNext(ApplicationInfo info) {
                        String msg = info.toString();
                        Log.d(TAG, "onNext: info => " + msg);
                        mPackages.add(msg);
                    }
                }
        );
    }

    private Observable<ApplicationInfo> getPackages() {
        //创建一个Observable对象, 并且指定 当这个可以被观察者, 调用 subscribe的时候, 对应的接口回调
        return Observable.create(new Observable.OnSubscribe<ApplicationInfo>(){
            @Override
            public void call(Subscriber<? super ApplicationInfo> subscriber) {
                try{
                    List<ApplicationInfo> list = getPackageManager().getInstalledApplications(0);
                    for (ApplicationInfo info : list) {
                        //获取数据, 并且发送到观察者Observable 如果出现错误 onError 完成 onComplete
                        if (!subscriber.isUnsubscribed()) {
                            //发送数据
                            subscriber.onNext(info);
                        }
                    }
                    if (!subscriber.isUnsubscribed()) {
                        // 结束
                        subscriber.onCompleted();
                    }
                } catch (Exception e){
                    if (!subscriber.isUnsubscribed()) {
                        subscriber.onError(e);
                    }
                }
            }
        });
    }


}
