package com.asmat.rolando.popularmovies.networking

import com.asmat.rolando.popularmovies.database.Movie
import com.asmat.rolando.popularmovies.models.Cast
import com.asmat.rolando.popularmovies.models.Review
import com.asmat.rolando.popularmovies.models.Video
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBService {

    //region movie/*
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<List<Movie>>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int): Single<List<Movie>>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("page") page: Int): Single<List<Movie>>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Single<List<Movie>>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<Movie>

    @GET("movie/{movie_id/videos}")
    fun getMovieVideos(@Path("movie_id") id: Int): Single<List<Video>>

    @GET("movie/{movie_id/reviews}")
    fun getMovieReviews(@Path("movie_id") id: Int): Single<List<Review>>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") id: Int): Single<List<Cast>>
    //endregion

    //region search/*
    @GET("search/movie")
    fun searchMovie(@Query("query") searchString: String, @Query("page") page: Int): Single<List<Movie>>
    //endregion
}