package com.lulu.rxjavademo.services;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Lulu on 2016/10/27.'
 * 豆瓣
 */
public interface DoubanService {
    @GET("movie/top250")
    Observable<String> getMovieTop250(@Query("start") int start, @Query("count") int count);
}
