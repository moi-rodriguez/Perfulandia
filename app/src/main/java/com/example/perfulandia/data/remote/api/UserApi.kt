package com.example.perfulandia.data.remote.api

import com.example.perfulandia.data.remote.dto.UserDto
import retrofit2.Response
import retrofit2.http.GET

/**
 * Wrapper for the user list response
 */
data class UserListResponse(
    val success: Boolean,
    val data: List<UserDto>,
    val message: String?
)

/**
 * UserApi: Endpoints for user management (Admin)
 * Base URL: /api
 */
interface UserApi {

    /**
     * GET /users
     * Get all registered users.
     * Requires: Admin role, JWT in Authorization header
     *
     * @return A list of all users.
     */
    @GET("users") // Assuming the endpoint is /users
    suspend fun getUsers(): Response<UserListResponse>

    // Future endpoints for CRUD operations can be added here
    // e.g., @DELETE("users/{id}")
}
