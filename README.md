[![Build Status](https://travis-ci.org/rolandoasmat/Movie-Night.svg?branch=master)](https://travis-ci.org/rolandoasmat/Movie-Night)

# Movie Night
Android application to browse, save and share movies to watch! originally a Udacity Android Nanodegree [project](ProjectRequirements.md).

<a href='https://play.google.com/store/apps/details?id=com.asmat.rolando.popularmovies&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

## About
- [The Movie Database API](https://developers.themoviedb.org/3/getting-started/introduction) client
- 100% Kotlin
- Architecture - MVVM with Repositories
- Using ViewModel and LiveData classes for View updates
- Dependency injection via Dagger 2
- RxJava 2 for async network tasks
- Retrofit 2 for network API requests

## Running Application
- Follow The Movie Database API getting started [instructions](https://developers.themoviedb.org/3/getting-started/introduction) to register and get an API key.
- Open the gradle.properties file.
- Update the	`THE_MOVIE_DB_API_KEY` value with your API key.
- Run!