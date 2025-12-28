package com.example.cryptotracker.data.utils

import com.example.cryptotracker.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

fun <T, R> safeApiCall(
    apiCall: suspend () -> T,
    mapper: (T) -> R
): Flow<Resource<R>> = flow {

    emit(Resource.Loading(true))

    try {
        val remoteData = apiCall()
        val domainData = mapper(remoteData)
        emit(Resource.Success(domainData))

    } catch (e: Exception) {
        val errorMessage = when (e) {
            is IOException -> "No internet connection. Please check your network."
            is HttpException -> "Server error (Code: ${e.code()}). Try again later."
            is SerializationException -> "Data parsing error. The app needs an update."
            else -> e.localizedMessage ?: "An unknown error occurred."
        }
        e.printStackTrace()
        emit(Resource.Error(errorMessage))

    } finally {
        emit(Resource.Loading(false))
    }
}