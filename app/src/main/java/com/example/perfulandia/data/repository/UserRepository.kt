package com.example.perfulandia.data.repository

import com.example.perfulandia.data.mapper.UserMapper
import com.example.perfulandia.data.remote.api.UserApi
import com.example.perfulandia.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val userApi: UserApi
) {

    /**
     * Fetches a list of all users from the API.
     *
     * @return A [Result] containing a list of [User] on success, or an exception on failure.
     */
    suspend fun getUsers(): Result<List<User>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userApi.getUsers()
                if (response.isSuccessful && response.body() != null) {
                    val userDtos = response.body()!!.data
                    val users = userDtos.map { UserMapper.fromDto(it) } // Use the existing mapper
                    Result.success(users)
                } else {
                    Result.failure(Exception("Failed to fetch users: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}