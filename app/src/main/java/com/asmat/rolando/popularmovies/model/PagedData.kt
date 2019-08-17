package com.asmat.rolando.popularmovies.model

data class PagedData<T>(val items: List<T>,
                        val totalNumOfPages: Int)