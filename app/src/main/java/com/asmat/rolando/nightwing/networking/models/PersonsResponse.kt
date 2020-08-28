package com.asmat.rolando.nightwing.networking.models

data class PersonsResponse(
        val page: Int,
        val total_results: Int,
        val total_pages: Int,
        val results: List<Person>?) {

    data class Person(
            val popularity: Double,
            val known_for_department: String?,
            val name: String,
            val id: Int?,
            val profile_path: String?)
}
