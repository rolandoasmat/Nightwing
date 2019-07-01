package com.asmat.rolando.popularmovies.networking.the.movie.db

import com.asmat.rolando.popularmovies.BuildConfig
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.*
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Network requests to The Movie DB API.
 *
 * @see <a href="https://developers.themoviedb.org/3"</a>
 */
class TheMovieDBClient {

    //region Private
    private val service by lazy { createService() }

    /**
     * Creates an instance of [TheMovieDBService]. Note: all requests will at least
     * contain the query parameters specified in [TheMovieDBInterceptor]
     */
    private fun createService(): TheMovieDBService {
        val okHttpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor(TheMovieDBInterceptor())
                .build()

        return Retrofit.Builder()
                .baseUrl(BuildConfig.THE_MOVIE_DB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build()
                .create(TheMovieDBService::class.java)
    }
    //endregion

    //region API
    fun getPopularMovies(page: Int): Single<MoviesResponse> {
        return service.getPopularMovies(page)
    }

    fun getTopRatedMovies(page: Int): Single<MoviesResponse> {
        return service.getTopRatedMovies(page)
    }

    fun getNowPlayingMovies(page: Int): Single<MoviesResponse> {
        return service.getNowPlayingMovies(page)
    }

    fun getUpcomingMovies(page: Int): Single<MoviesResponse> {
        return service.getUpcomingMovies(page)
    }

    fun getMovieDetails(movieID: Int): Single<MovieDetailsResponse> {
        return service.getMovieDetails(movieID)
    }

    fun getMovieVideos(movieID: Int): Single<VideosResponse> {
        return service.getMovieVideos(movieID)
    }

    fun getMovieReviews(movieID: Int): Single<ReviewsResponse> {
        return service.getMovieReviews(movieID)
    }

    fun getMovieCredits(movieID: Int): Single<CreditsResponse> {
        return service.getMovieCredits(movieID)
    }

    fun searchMovie(searchString: String, page: Int): Single<MoviesResponse> {
        return service.searchMovie(searchString, page)
    }

    fun getPersonDetails(id: Int): Single<PersonDetailsResponse> {
        return service.getPersonDetails(id)
    }

    fun getPersonMovieCredits(personID: Int): Single<PersonMovieCredits> {
        return service.getPersonMovieCredits(personID)
    }
    //endregion
}
