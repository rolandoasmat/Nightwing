package com.asmat.rolando.popularmovies.networking

import com.asmat.rolando.popularmovies.models.Credit
import com.asmat.rolando.popularmovies.models.Review
import com.asmat.rolando.popularmovies.networking.models.*
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBService {

    //region movie/*
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<MoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int): Single<MoviesResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("page") page: Int): Single<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Single<MoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieResponse>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideos(@Path("movie_id") id: Int): Single<VideosResponse>

    @GET("movie/{movie_id}/reviews")
    fun getMovieReviews(@Path("movie_id") id: Int): Single<List<Review>>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") id: Int): Single<Credit>
    //endregion

    //region search/*
    @GET("search/movie")
    fun searchMovie(@Query("query") searchString: String, @Query("page") page: Int): Single<MoviesResponse>
    //endregion
}