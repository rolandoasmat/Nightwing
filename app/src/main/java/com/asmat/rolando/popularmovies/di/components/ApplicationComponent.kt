package com.asmat.rolando.popularmovies.di.components

import com.asmat.rolando.popularmovies.di.modules.*
import com.asmat.rolando.popularmovies.ui.castdetails.CastDetailsActivity
import com.asmat.rolando.popularmovies.ui.castdetails.personmoviecredits.PersonMovieCreditsFragment
import com.asmat.rolando.popularmovies.ui.common.BaseActivity
import com.asmat.rolando.popularmovies.ui.moviedetails.MovieDetailActivity
import com.asmat.rolando.popularmovies.ui.moviegrid.BaseMovieGridFragment
import com.asmat.rolando.popularmovies.ui.moviegrid.paginated.PaginatedMovieGridFragment
import com.asmat.rolando.popularmovies.ui.popularmovies.PopularMoviesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ContextModule::class,
    DatabaseModule::class,
    NetworkModule::class,
    RepositoriesModule::class,
    ViewModelModule::class
])
interface ApplicationComponent {

    fun inject(app: BaseActivity)
    fun inject(fragment: PopularMoviesFragment)
    fun inject(movieDetailActivity: MovieDetailActivity)
    fun inject(castDetailsActivity: CastDetailsActivity)
    fun inject(castDetailsActivity: PersonMovieCreditsFragment)
    fun inject(paginatedMovieGridFragment: PaginatedMovieGridFragment)
    fun inject(baseMovieGridFragment: BaseMovieGridFragment)

}