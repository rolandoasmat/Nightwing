package com.asmat.rolando.popularmovies.networking

import com.asmat.rolando.popularmovies.BuildConfig
import com.asmat.rolando.popularmovies.database.Movie
import com.asmat.rolando.popularmovies.models.*
import com.asmat.rolando.popularmovies.networking.models.*
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

object TheMovieDBClient {

    //region Private
    private const val baseURL = "https://api.themoviedb.org/3/"

    private val service by lazy { createService() }

    /**
     * Creates an instance of [TheMovieDBService], all requests will contain the
     * following query parameters set: api_key, language, and region.
     */
    private fun createService(): TheMovieDBService {
        val okHttpClient = OkHttpClient()
                .newBuilder()
                .addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val httpURL = originalRequest.url()

                    val newHttpURLBuilder = httpURL.newBuilder()
                    newHttpURLBuilder.addQueryParameter("api_key", BuildConfig.API_KEY)
                    newHttpURLBuilder.addQueryParameter("language", Locale.getDefault().getLanguage())
                    newHttpURLBuilder.addQueryParameter("region", Locale.getDefault().getCountry())
                    val newHttpURL = newHttpURLBuilder.build()

                    val newRequest = originalRequest.newBuilder().url(newHttpURL).build()
                    chain.proceed(newRequest)
                }
                .build()

        return Retrofit.Builder()
                .baseUrl(baseURL)
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

    fun getMovieDetails(movieID: Int): Single<MovieResponse> {
        return service.getMovieDetails(movieID)
    }

    fun getMovieVideos(movieID: Int): Single<VideosResponse> {
        return service.getMovieVideos(movieID)
    }

    fun getMovieReviews(movieID: Int): Single<List<Review>> {
        return service.getMovieReviews(movieID)
    }

    fun getMovieCredits(movieID: Int): Single<Credit> {
        return service.getMovieCredits(movieID)
    }

    fun searchMovie(searchString: String, page: Int): Single<MoviesResponse> {
        return service.searchMovie(searchString, page)
    }
    //endregion
}
