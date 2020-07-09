package com.asmat.rolando.popularmovies.di.components

import com.asmat.rolando.popularmovies.di.modules.ContextModule
import com.asmat.rolando.popularmovies.di.modules.DatabaseModule
import com.asmat.rolando.popularmovies.di.modules.NetworkModule
import com.asmat.rolando.popularmovies.di.modules.RepositoriesModule
import com.asmat.rolando.popularmovies.ui.castdetails.CastDetailsActivity
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsFragment
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridFragment
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailsActivity
import com.asmat.rolando.popularmovies.ui.popularmovies.PopularMoviesFragment
import com.asmat.rolando.popularmovies.search.SearchFragment
import com.asmat.rolando.popularmovies.ui.discover.MainActivity
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
    fun inject(movieDetailsActivity: MovieDetailsActivity)
    fun inject(castDetailsActivity: CastDetailsActivity)
    fun inject(castDetailsActivity: PersonMovieCreditsFragment)
    fun inject(paginatedMovieGridFragment: PaginatedMovieGridFragment)
    fun inject(baseMovieGridFragment: BaseMovieGridFragment)

    fun inject(searchFragment: SearchFragment)

}