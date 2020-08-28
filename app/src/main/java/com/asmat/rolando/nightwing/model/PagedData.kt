package com.asmat.rolando.nightwing.model

data class PagedData<T>(val items: List<T>,
                        val totalNumOfPages: Int)