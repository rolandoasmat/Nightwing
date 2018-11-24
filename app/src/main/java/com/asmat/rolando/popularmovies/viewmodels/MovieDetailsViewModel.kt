package com.asmat.rolando.popularmovies.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.asmat.rolando.popularmovies.database.entities.FavoriteMovie
import com.asmat.rolando.popularmovies.database.entities.WatchLaterMovie
import com.asmat.rolando.popularmovies.model.MoviesRepository
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.CreditsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.MovieDetailsResponse
import com.asmat.rolando.popularmovies.networking.the.movie.db.models.VideosResponse

class MovieDetailsViewModel(moviesRepository: MoviesRepository,
                            movieID: Int) : ViewModel() {

    val movieDetails: LiveData<MovieDetailsResponse>
    val favoriteMovie: LiveData<FavoriteMovie>
    val watchLaterMovie: LiveData<WatchLaterMovie>
    val videos: LiveData<List<VideosResponse.Video>>
    val cast: LiveData<List<CreditsResponse.Cast>>


   init {
       movieDetails = MutableLiveData<MovieDetailsResponse>()
       moviesRepository.getMovieDetails(movieID).subscribe({ result ->
           movieDetails.value = result
       }, { _ ->
           movieDetails.value = null
       })

       favoriteMovie = moviesRepository.getFavoriteMovie(movieID)
       watchLaterMovie = moviesRepository.getWatchLaterMovie(movieID)

       videos = MutableLiveData<List<VideosResponse.Video>>()
       moviesRepository.getMovieVideos(movieID).subscribe({ result ->
           videos.value = result.results
       }, { _ ->
           videos.value = null
       })

       cast = MutableLiveData<List<CreditsResponse.Cast>>()
       moviesRepository.getMovieCredits(movieID).subscribe({ result ->
           cast.value = result.cast
       }, { _ ->
           cast.value = null
       })
   }

}