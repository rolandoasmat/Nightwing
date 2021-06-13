package com.asmat.rolando.nightwing.networking

/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.asmat.rolando.nightwing.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
abstract class NetworkBoundResource<ResultType>(
    private val cache: Cache<ResultType>?
    ) {

    interface Cache<ResultType> {
        // Called to get the cached data from the database.
        @MainThread
        suspend fun loadFromDb(): ResultType

        // Called to save the result of the API response into the database
        @WorkerThread
        suspend fun saveResult(item: ResultType)

        // Called with the data in the database to decide whether to fetch
        // potentially updated data from the network.
        @MainThread
        fun shouldFetch(data: ResultType?): Boolean
    }

    // Called to create the API call.
    @MainThread
    protected abstract suspend fun fetchData(): NetworkResponse<ResultType>

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    protected open fun onFetchFailed() {}

    fun load(): Flow<Resource<ResultType>> = flow {
        val cachedValue = cache?.loadFromDb()
        val shouldFetch = cache?.shouldFetch(cachedValue) == true
        if (shouldFetch || cachedValue == null) {
            emit(Resource.Loading<ResultType>())
            try {
                when (val networkResponse = fetchData()) {
                    is NetworkResponse.Success -> {
                        emit(Resource.Success<ResultType>(networkResponse.data))
                    }
                    is NetworkResponse.Failure -> {
                        emit(Resource.Error<ResultType>(networkResponse.errorMessage))
                    }
                }
            } catch (e: Exception) {
                emit(Resource.Error<ResultType>(e.message ?: "Error fetching data."))
            }
        } else {
            emit(Resource.Success<ResultType>(cachedValue))
        }
    }.flowOn(Dispatchers.IO)

    sealed class NetworkResponse<ResultType> {
        class Success<ResultType>(val data: ResultType):NetworkResponse<ResultType>()
        class Failure<ResultType>(val errorMessage: String):NetworkResponse<ResultType>()
    }
}