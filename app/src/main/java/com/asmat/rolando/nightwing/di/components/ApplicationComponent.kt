package com.asmat.rolando.nightwing.di.components

import com.asmat.rolando.nightwing.cast_details.CastDetailsFragment
import com.asmat.rolando.nightwing.di.modules.ContextModule
import com.asmat.rolando.nightwing.di.modules.DatabaseModule
import com.asmat.rolando.nightwing.di.modules.NetworkModule
import com.asmat.rolando.nightwing.di.modules.RepositoriesModule
import com.asmat.rolando.nightwing.cast_details.CastMovieCreditsFragment
import com.asmat.rolando.nightwing.ui.moviegrid.BaseMovieGridFragment
import com.asmat.rolando.nightwing.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.nightwing.movie_details.MovieDetailsFragment
import com.asmat.rolando.nightwing.movies_tab.MoviesTabFragment
import com.asmat.rolando.nightwing.popular_people_tab.PopularPeopleTabFragment
import com.asmat.rolando.nightwing.ui.popularmovies.PopularMoviesFragment
import com.asmat.rolando.nightwing.search.SearchFragment
import com.asmat.rolando.nightwing.tv_show_details.TvShowDetailsFragment
import com.asmat.rolando.nightwing.tv_shows_tab.TvShowsTabFragment
import com.asmat.rolando.nightwing.tv_shows_tab.on_the_air.OnTheAirTvShowsFragment
import com.asmat.rolando.nightwing.tv_shows_tab.popular.PopularTvShowsFragment
import com.asmat.rolando.nightwing.tv_shows_tab.top_rated.TopRatedTvShowsFragment
import com.asmat.rolando.nightwing.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ContextModule::class,
    DatabaseModule::class,
    NetworkModule::class,
    RepositoriesModule::class])
interface ApplicationComponent {

    fun inject(app: MainActivity)
    fun inject(fragment: PopularMoviesFragment)
    fun inject(castDetailsActivity: CastMovieCreditsFragment)
    fun inject(paginatedMovieGridFragment: PaginatedMovieGridFragment)
    fun inject(baseMovieGridFragment: BaseMovieGridFragment)

    fun inject(searchFragment: SearchFragment)
    fun inject(movieDetailsFragment: MovieDetailsFragment)
    fun inject(castDetailsFragment: CastDetailsFragment)
    fun inject(moviesTabFragment: MoviesTabFragment)
    fun inject(fragment: PopularPeopleTabFragment)
    fun inject(fragment: TvShowsTabFragment)
    fun inject(fragment: PopularTvShowsFragment)
    fun inject(fragment: TopRatedTvShowsFragment)
    fun inject(fragment: OnTheAirTvShowsFragment)
    fun inject(fragment: TvShowDetailsFragment)
}