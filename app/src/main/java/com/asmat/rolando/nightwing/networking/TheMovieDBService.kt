package com.asmat.rolando.nightwing.networking

import com.asmat.rolando.nightwing.networking.models.*
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
    fun getMovieDetails(@Path("movie_id") id: Int): Single<MovieDetailsResponse>

    @GET("movie/{movie_id}/videos")
    fun getMovieVideos(@Path("movie_id") id: Int): Single<VideosResponse>

    @GET("movie/{movie_id}/reviews")
    fun getMovieReviews(@Path("movie_id") id: Int): Single<ReviewsResponse>

    @GET("movie/{movie_id}/credits")
    fun getMovieCredits(@Path("movie_id") id: Int): Single<CreditsResponse>

    @GET("movie/{movie_id}/recommendations")
    fun getMovieRecommendations(@Path("movie_id") id: Int): Single<MoviesResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(@Path("movie_id") id: Int): Single<MoviesResponse>
    //endregion

    //region search/*

    @GET("search/movie")
    fun searchMovie(@Query("query") searchString: String, @Query("page") page: Int): Single<MoviesResponse>

    @GET("search/person")
    fun searchPerson(@Query("query") searchString: String, @Query("page") page: Int): Single<PersonsResponse>

    //endregion

    //region people/*

    @GET("person/{person_id}")
    fun getPersonDetails(@Path("person_id") id: Int): Single<PersonDetailsResponse>

    @GET("person/{person_id}/movie_credits")
    fun getPersonMovieCredits(@Path("person_id") id: Int): Single<PersonMovieCredits>

    //endregion
}