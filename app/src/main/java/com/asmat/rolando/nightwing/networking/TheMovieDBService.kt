package com.asmat.rolando.nightwing.networking

import com.asmat.rolando.nightwing.networking.models.*
import com.asmat.rolando.nightwing.tv_season_details.network.TvSeasonDetailsResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBService {

    //region movie/*
    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int): Single<MoviesResponse>

    @GET("movie/popular")
    suspend fun getPopularMoviesSuspend(@Query("page") page: Int): Response<MoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(@Query("page") page: Int): Single<MoviesResponse>

    @GET("movie/top_rated")
    suspend fun getTopRatedMoviesSuspend(@Query("page") page: Int): Response<MoviesResponse>

    @GET("movie/now_playing")
    fun getNowPlayingMovies(@Query("page") page: Int): Single<MoviesResponse>

    @GET("movie/now_playing")
    suspend fun getNowPlayingMoviesSuspend(@Query("page") page: Int): Response<MoviesResponse>

    @GET("movie/upcoming")
    fun getUpcomingMovies(@Query("page") page: Int): Single<MoviesResponse>

    @GET("movie/upcoming")
    suspend fun getUpcomingMoviesSuspend(@Query("page") page: Int): Response<MoviesResponse>

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

    @GET("movie/{movie_id}/recommendations")
    suspend fun getMovieRecommendationsSuspend(@Path("movie_id") id: Int): Response<MoviesResponse>

    @GET("movie/{movie_id}/similar")
    fun getSimilarMovies(@Path("movie_id") id: Int): Single<MoviesResponse>

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilarMoviesSuspend(@Path("movie_id") id: Int): Response<MoviesResponse>
    //endregion

    //region search/*
    @GET("search/movie")
    fun searchMovie(@Query("query") searchString: String, @Query("page") page: Int): Single<MoviesResponse>

    @GET("search/person")
    fun searchPerson(@Query("query") searchString: String, @Query("page") page: Int): Single<PersonsResponse>

    @GET("search/tv")
    fun searchTvShow(@Query("query") searchString: String, @Query("page") page: Int): Single<TvShowsResponse>
    //endregion

    //region people/*
    @GET("person/popular")
    fun getPopularPeople(@Query("page") page: Int): Single<PopularPeopleResponse>

    @GET("person/{person_id}")
    fun getPersonDetails(@Path("person_id") id: Int): Single<PersonDetailsResponse>

    @GET("person/{person_id}/movie_credits")
    suspend fun getPersonMovieCredits(@Path("person_id") id: Int): Response<PersonMovieCredits>
    //endregion

    //region TV Shows
    @GET("tv/popular")
    fun getPopularTvShows(@Query("page") page: Int): Single<TvShowsResponse>

    @GET("tv/popular")
    suspend fun getPopularTvShowsSuspend(@Query("page") page: Int): Response<TvShowsResponse>

    @GET("tv/top_rated")
    suspend fun getTopRatedTvShowsSuspend(@Query("page") page: Int): Response<TvShowsResponse>

    @GET("tv/on_the_air")
    suspend fun getOnTheAirTvShowsSuspend(@Query("page") page: Int): Response<TvShowsResponse>

    @GET("tv/top_rated")
    fun getTopRatedTvShows(@Query("page") page: Int): Single<TvShowsResponse>

    @GET("tv/on_the_air")
    fun getOnTheAirTvShows(@Query("page") page: Int): Single<TvShowsResponse>

    @GET("tv/{tv_id}")
    fun getTvShowDetails(@Path("tv_id") id: Int): Single<TvShowDetailsResponse>
    //endregion

    //region TV SEASONS
    @GET("tv/{tv_id}/season/{season_number}")
    suspend fun getTvSeasonDetails(
        @Path("tv_id") id: Int,
        @Path("season_number") season_number: Int): Response<TvSeasonDetailsResponse>
    //endregion
}