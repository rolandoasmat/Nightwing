package com.asmat.rolando.nightwing.model.mappers

import com.asmat.rolando.nightwing.model.Movie
import com.asmat.rolando.nightwing.cast_details.MovieCreditUiModel
import com.asmat.rolando.nightwing.cast_details.PersonMovieCreditsUiModel
import com.asmat.rolando.nightwing.database.entities.PopularMovie
import com.asmat.rolando.nightwing.database.entities.SavedMovie
import com.asmat.rolando.nightwing.database.entities.SavedTvShow
import com.asmat.rolando.nightwing.movie_details.MovieDetailsUIModel
import com.asmat.rolando.nightwing.networking.models.*
import com.asmat.rolando.nightwing.popular_people_tab.PopularPersonUiModel
import com.asmat.rolando.nightwing.ui.moviegrid.MovieGridItemUiModel
import com.asmat.rolando.nightwing.search.SearchDataModelsMapper
import com.asmat.rolando.nightwing.tv_season_details.TvSeasonEpisodesUiModel
import com.asmat.rolando.nightwing.tv_season_details.domain.TvShowSeason
import com.asmat.rolando.nightwing.ui.grid.GridItemUiModel
import com.asmat.rolando.nightwing.ui.row_view.RowViewItemUiModel
import com.asmat.rolando.nightwing.utilities.DateUtils
import com.asmat.rolando.nightwing.utilities.URLUtils
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Maps data objects to UiModel classes.
 */
@Singleton
open class UiModelMapper @Inject constructor(private val searchDataModelsMapper: SearchDataModelsMapper) {

    fun map(movies: List<Movie>?): List<MovieGridItemUiModel>? {
        return movies?.map {
            val posterURL = it.posterPath?.let { url -> URLUtils.getImageURL342(url)}
            MovieGridItemUiModel(it.id, it.title, posterURL)
        }
    }

    fun mapToGridUiModel(data: MoviesResponse.Movie): MovieGridItemUiModel {
        val posterURL = data.poster_path?.let { url ->
            URLUtils.getImageURL342(url)
        }
        return MovieGridItemUiModel(data.id ?: 0, data.title ?: "", posterURL)
    }

    fun mapToGridUiModels(data: List<MoviesResponse.Movie>?): List<MovieGridItemUiModel>? {
        return data?.map {
            val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url)}
            MovieGridItemUiModel(it.id ?: 0, it.title ?: "", posterURL)
        }
    }

    fun map(data: PersonMovieCredits): PersonMovieCreditsUiModel {
        val mapped = data.cast?.filter {
            it.release_date?.isEmpty() == false
        }?.sortedByDescending {
            DateUtils.transform(it.release_date ?: "")
        }?.map {
            val posterURL = it.poster_path?.let { url -> URLUtils.getImageURL342(url) }
            val movieID = it.id ?: 0
            MovieCreditUiModel(movieID, posterURL, it.character, it.title)
        }

        val movieCreditsWithBackdropImage = data.cast?.filter { it.backdrop_path != null }

        val backdropURL = if (movieCreditsWithBackdropImage?.isEmpty() == true) {
            null
        } else {
            movieCreditsWithBackdropImage?.random()?.backdrop_path?.let { URLUtils.getImageURL780(it) }
        }
        return PersonMovieCreditsUiModel(backdropURL, mapped
                ?: emptyList())
    }

    fun mapMovies(response: List<MoviesResponse.Movie>) = searchDataModelsMapper.mapMovies(response)

    fun mapPersons(response: List<PersonsResponse.Person>) = searchDataModelsMapper.mapPersons(response)

    fun mapTvShows(response: List<TvShowsResponse.Item>) = searchDataModelsMapper.mapTvShows(response)

    fun map(movie: MovieDetailsResponse): MovieDetailsUIModel? {
        val posterURL = movie.poster_path?.let { url -> URLUtils.getImageURL342(url)}
        val backdropURL = movie.backdrop_path?.let { url -> URLUtils.getImageURL780(url)}
        val releaseDateText = DateUtils.formatDate(movie.release_date)
        val releaseDate = DateUtils.transform(movie.release_date)?.time
        val voteAverage = movie.vote_average?.times(10)?.toInt()?.toString()?.let { percent ->
            "$percent%"
        }
        val runtime = movie.runtime?.let { movieRuntime ->
            "$movieRuntime min"
        }
        return MovieDetailsUIModel(
                posterURL,
                movie.overview ?: "",
                releaseDateText,
                releaseDate,
                movie.id ?: 0,
                movie.title ?: "",
                backdropURL,
                voteAverage,
                runtime,
                movie.tagline)
    }

    fun map(data: PopularPeopleResponse): List<PopularPersonUiModel> {
        return data.results.map {
            val url = it.profile_path?.let { path ->
                URLUtils.getImageURL342(path)
            }
            val titles = it.known_for.mapNotNull { it.title }
            val subtitle = if (titles.isEmpty()) {
                ""
            } else {
                titles.reduce { acc, s -> "$acc, $s" }
            }
            PopularPersonUiModel(it.id, url, it.name, subtitle ?: "")
        }
    }

    fun map(data: TvShowsResponse): List<RowViewItemUiModel> {
        return data.results.map {
            val url = it.poster_path?.let { path ->
                URLUtils.getImageURL342(path)
            }
            RowViewItemUiModel(it.id, url, it.name ?: "")
        }
    }

    fun mapToGridItems(data: TvShowsResponse): List<GridItemUiModel> {
        return data.results.map {
            val url = it.poster_path?.let { path ->
                URLUtils.getImageURL342(path)
            }
            GridItemUiModel(it.id, url, it.name ?: "")
        }
    }

    fun mapSavedMoviesToGridItems(data: List<SavedMovie>): List<GridItemUiModel> {
        return data.map { movie ->
            GridItemUiModel(movie.id, movie.posterURL, movie.title)
        }
    }

    fun mapSavedTvShowsToGridItems(data: List<SavedTvShow>): List<GridItemUiModel> {
        return data.map { movie ->
            GridItemUiModel(movie.id, movie.posterURL, movie.title)
        }
    }

    fun mapTvShowSeason(data: TvShowSeason): TvSeasonEpisodesUiModel {
        return TvSeasonEpisodesUiModel(
            episodes = data.episodes.map { episode ->
                val backdropURL = episode.imageURL?.let { url ->
                    URLUtils.getImageURL780(url)
                }
                TvSeasonEpisodesUiModel.Item(
                    backdropURL = backdropURL,
                    title = "${episode.episodeNumber}. ${episode.name}",
                    body = episode.overview
                )
            }
        )
    }

    fun popularMovieToMovieGridItemUiModel(data: PopularMovie): MovieGridItemUiModel {
        val posterURL = data.posterPath?.let { path ->
            URLUtils.getImageURL342(path)
        }
        return MovieGridItemUiModel(
            id = data.id,
            title = data.title ?: "Unknown title",
            posterURL = posterURL
        )
    }

}