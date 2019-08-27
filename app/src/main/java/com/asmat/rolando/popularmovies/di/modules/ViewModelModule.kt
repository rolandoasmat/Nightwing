package com.asmat.rolando.popularmovies.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.asmat.rolando.popularmovies.di.ViewModelFactory
import com.asmat.rolando.popularmovies.di.ViewModelKey
import com.asmat.rolando.popularmovies.ui.castdetails.CastDetailsViewModel
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsViewModel
import com.asmat.rolando.popularmovies.ui.favoritemovies.FavoriteMoviesViewModel
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsViewModel
import com.asmat.rolando.popularmovies.ui.nowplayingmovies.NowPlayingMoviesViewModel
import com.asmat.rolando.popularmovies.ui.popularmovies.PopularMoviesViewModel
import com.asmat.rolando.popularmovies.ui.search.SearchMoviesViewModel
import com.asmat.rolando.popularmovies.ui.topratedmovies.TopRatedMoviesViewModel
import com.asmat.rolando.popularmovies.ui.upcomingmovies.UpcomingMoviesViewModel
import com.asmat.rolando.popularmovies.ui.watchlatermovies.WatchLaterViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(PersonMovieCreditsViewModel::class)
    internal abstract fun bindPersonMovieCreditsViewModel(provideMovieCreditsViewModel: PersonMovieCreditsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CastDetailsViewModel::class)
    internal abstract fun bindCastDetailsViewModel(castDetailsViewModel: CastDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailsViewModel::class)
    internal abstract fun bindMovieDetailsViewModel(movieDetailsViewModel: MovieDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PopularMoviesViewModel::class)
    internal abstract fun bindPopularMoviesViewModel(popularMoviesViewModel: PopularMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopRatedMoviesViewModel::class)
    internal abstract fun bindTopRatedMoviesViewModel(topRatedMoviesViewModel: TopRatedMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteMoviesViewModel::class)
    internal abstract fun bindFavoriteMoviesViewModel(favoriteMoviesViewModel: FavoriteMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(WatchLaterViewModel::class)
    internal abstract fun bindWatchLaterViewModel(watchLaterViewModel: WatchLaterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NowPlayingMoviesViewModel::class)
    internal abstract fun bindNowPlayingMoviesViewModel(nowPlayingMoviesViewModel: NowPlayingMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(UpcomingMoviesViewModel::class)
    internal abstract fun bindUpcomingMoviesViewModel(upcomingMoviesViewModel: UpcomingMoviesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchMoviesViewModel::class)
    internal abstract fun bindSearchMoviesViewModel(searchMoviesViewModel: SearchMoviesViewModel): ViewModel

}