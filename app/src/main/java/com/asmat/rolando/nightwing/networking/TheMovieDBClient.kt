package com.asmat.rolando.nightwing.networking

import com.asmat.rolando.nightwing.BuildConfig
import com.asmat.rolando.nightwing.networking.models.*
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
open class TheMovieDBClient {

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

    open fun getPopularMovies(page: Int): Single<MoviesResponse> {
        return service.getPopularMovies(page)
    }

    open fun getTopRatedMovies(page: Int): Single<MoviesResponse> {
        return service.getTopRatedMovies(page)
    }

    open fun getNowPlayingMovies(page: Int): Single<MoviesResponse> {
        return service.getNowPlayingMovies(page)
    }

    open fun getUpcomingMovies(page: Int): Single<MoviesResponse> {
        return service.getUpcomingMovies(page)
    }

    open fun getMovieDetails(movieID: Int): Single<MovieDetailsResponse> {
        return service.getMovieDetails(movieID)
    }

    open fun getMovieVideos(movieID: Int): Single<VideosResponse> {
        return service.getMovieVideos(movieID)
    }

    open fun getMovieReviews(movieID: Int): Single<ReviewsResponse> {
        return service.getMovieReviews(movieID)
    }

    open fun getMovieCredits(movieID: Int): Single<CreditsResponse> {
        return service.getMovieCredits(movieID)
    }

    open fun getSimilarMovies(movieID: Int) = service.getSimilarMovies(movieID)

    open fun getMovieRecommendations(movieID: Int) = service.getMovieRecommendations(movieID)

    open fun searchMovie(searchString: String, page: Int): Single<MoviesResponse> {
        return service.searchMovie(searchString, page)
    }

    open fun searchPerson(searchString: String, page: Int): Single<PersonsResponse> {
        return service.searchPerson(searchString, page)
    }

    open fun getPopularPeople(page: Int) = service.getPopularPeople(page)

    open fun getPersonDetails(id: Int): Single<PersonDetailsResponse> {
        return service.getPersonDetails(id)
    }

    open fun getPersonMovieCredits(personID: Int): Single<PersonMovieCredits> {
        return service.getPersonMovieCredits(personID)
    }

    //region TV Shows
    fun getPopularTvShows(page: Int) = service.getPopularTvShows(page)
    fun getTopRatedTvShows(page: Int) = service.getTopRatedTvShows(page)
    fun getOnTheAirTvShows(page: Int) = service.getOnTheAirTvShows(page)
    //endregion
}
