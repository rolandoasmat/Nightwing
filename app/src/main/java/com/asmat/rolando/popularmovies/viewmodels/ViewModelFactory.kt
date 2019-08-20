package com.asmat.rolando.popularmovies.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asmat.rolando.popularmovies.repositories.MoviesRepository
import com.asmat.rolando.popularmovies.repositories.PeopleRepository
import com.asmat.rolando.popularmovies.ui.castdetails.CastDetailsViewModel
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsViewModel
import com.asmat.rolando.popularmovies.ui.favoritemovies.FavoriteMoviesViewModel
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsViewModel
import com.asmat.rolando.popularmovies.ui.nowplayingmovies.NowPlayingMoviesViewModel
import com.asmat.rolando.popularmovies.ui.popularmovies.PopularMoviesViewModel
import com.asmat.rolando.popularmovies.ui.topratedmovies.TopRatedMoviesViewModel
import com.asmat.rolando.popularmovies.ui.upcomingmovies.UpcomingMoviesViewModel
import com.asmat.rolando.popularmovies.ui.watchlatermovies.WatchLaterViewModel

class ViewModelFactory(private val moviesRepository: MoviesRepository,
                       private val peopleRepository: PeopleRepository) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PopularMoviesViewModel::class.java) -> PopularMoviesViewModel(moviesRepository) as T
            modelClass.isAssignableFrom(TopRatedMoviesViewModel::class.java) -> TopRatedMoviesViewModel(moviesRepository) as T
            modelClass.isAssignableFrom(NowPlayingMoviesViewModel::class.java) -> NowPlayingMoviesViewModel(moviesRepository) as T
            modelClass.isAssignableFrom(UpcomingMoviesViewModel::class.java) -> UpcomingMoviesViewModel(moviesRepository) as T
            modelClass.isAssignableFrom(MovieDetailsViewModel::class.java) -> MovieDetailsViewModel(moviesRepository) as T
            modelClass.isAssignableFrom(CastDetailsViewModel::class.java) -> CastDetailsViewModel(peopleRepository) as T
            modelClass.isAssignableFrom(PersonMovieCreditsViewModel::class.java) -> PersonMovieCreditsViewModel(peopleRepository) as T
            modelClass.isAssignableFrom(FavoriteMoviesViewModel::class.java) -> FavoriteMoviesViewModel(moviesRepository) as T
            modelClass.isAssignableFrom(WatchLaterViewModel::class.java) -> WatchLaterViewModel(moviesRepository) as T
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

}