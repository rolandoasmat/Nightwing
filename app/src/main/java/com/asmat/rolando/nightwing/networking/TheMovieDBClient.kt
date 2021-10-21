package com.asmat.rolando.nightwing.networking

import com.asmat.rolando.nightwing.BuildConfig
import com.asmat.rolando.nightwing.networking.models.*
import com.asmat.rolando.nightwing.tv_season_details.network.TvSeasonDetailsResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Response
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

    suspend fun getPopularMoviesSuspend(page: Int) = service.getPopularMoviesSuspend(page)

    open fun getTopRatedMovies(page: Int): Single<MoviesResponse> {
        return service.getTopRatedMovies(page)
    }

    suspend fun getTopRatedMoviesSuspend(page: Int) = service.getTopRatedMoviesSuspend(page)


    open fun getNowPlayingMovies(page: Int): Single<MoviesResponse> {
        return service.getNowPlayingMovies(page)
    }

    suspend fun getNowPlayingMoviesSuspend(page: Int) = service.getNowPlayingMoviesSuspend(page)


    open fun getUpcomingMovies(page: Int): Single<MoviesResponse> {
        return service.getUpcomingMovies(page)
    }

    suspend fun getUpcomingMoviesSuspend(page: Int) = service.getUpcomingMoviesSuspend(page)


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

    suspend fun getSimilarMoviesSuspend(movieID: Int) = service.getSimilarMoviesSuspend(movieID)

    open fun getMovieRecommendations(movieID: Int) = service.getMovieRecommendations(movieID)

    suspend fun getMovieRecommendationsSuspend(movieID: Int) = service.getMovieRecommendationsSuspend(movieID)

    // region Search
    open fun searchMovie(searchString: String, page: Int): Single<MoviesResponse> {
        return service.searchMovie(searchString, page)
    }

    open fun searchPerson(searchString: String, page: Int): Single<PersonsResponse> {
        return service.searchPerson(searchString, page)
    }

    open fun searchTvShow(searchString: String, page: Int) = service.searchTvShow(searchString, page)
    //endregion

    open fun getPopularPeople(page: Int) = service.getPopularPeople(page)

    open fun getPersonDetails(id: Int): Single<PersonDetailsResponse> {
        return service.getPersonDetails(id)
    }

    suspend fun getPersonMovieCredits(personID: Int) =  service.getPersonMovieCredits(personID)

    //region TV Shows
    fun getPopularTvShows(page: Int) = service.getPopularTvShows(page)

    suspend fun popularTvShowsSinglePage() = service.getPopularTvShowsSuspend(1)
    suspend fun topRatedTvShowsSinglePage() = service.getTopRatedTvShowsSuspend(1)
    suspend fun onTheAirTvShowsSinglePage() = service.getOnTheAirTvShowsSuspend(1)

    fun getTopRatedTvShows(page: Int) = service.getTopRatedTvShows(page)

    fun getOnTheAirTvShows(page: Int) = service.getOnTheAirTvShows(page)

    fun getTvShowDetails(id: Int) = service.getTvShowDetails(id)
    //endregion

    //region TV Season
    suspend fun getTvSeasonDetails(
        showId: Int,
        seasonNumber: Int): Response<TvSeasonDetailsResponse> {
        return service.getTvSeasonDetails(showId, seasonNumber)
    }
    //endregion
}
