package com.example.perfulandia.data.remote.api

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * UploadApi: Endpoint para subir imágenes de forma general
 * Base URL: /upload
 * Requiere autenticación (Token JWT)
 */
interface UploadApi {

    /**
     * POST /upload/image
     * Subir una imagen de forma general
     *
     * Este endpoint es útil cuando necesitas subir una imagen
     * sin asociarla inmediatamente a una entidad específica.
     *
     * @param image Archivo de imagen (multipart/form-data)
     * @return URL de la imagen subida
     *
     * Ejemplo de respuesta:
     * {
     *   "success": true,
     *   "imageUrl": "https://...",
     *   "thumbnailUrl": "https://..."
     * }
     */
    @Multipart
    @POST("upload/image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part
    ): Response<ResponseBody>
}