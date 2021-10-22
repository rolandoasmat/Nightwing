[![rolandoasmat](https://circleci.com/gh/rolandoasmat/nightwing.svg?style=svg)](https://app.circleci.com/pipelines/github/rolandoasmat/Nightwing)

# Nightwing
Android application to browse, save and share movies or tv shows!

<a href='https://play.google.com/store/apps/details?id=com.asmat.rolando.nightwing&pcampaignid=MKT-Other-global-all-co-prtnr-py-PartBadge-Mar2515-1'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png'/></a>

## About
- [The Movie Database API](https://developers.themoviedb.org/3/getting-started/introduction) client
- 100% Kotlin
- Architecture - MVVM with Repositories
- Using ViewModel and LiveData classes for View updates
- Dependency injection via Dagger 2
- RxJava 2 for async network tasks (migrating to coroutines)
- Retrofit 2 for network API requests

## Running Application
- Download and install the latest version of [Android Studio](https://developer.android.com/studio)
- Follow The Movie Database API getting started [instructions](https://developers.themoviedb.org/3/getting-started/introduction) to register and get an API key.
- Open the `gradle.properties` file.
- Update the	`THE_MOVIE_DB_API_KEY` value with your API key.
- Run!