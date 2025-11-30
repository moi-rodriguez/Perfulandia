package com.example.perfulandia

import android.content.Context
import com.example.perfulandia.data.repository.AvatarRepository

// Esta clase actúa como un contenedor de dependencias.
class AppDependencies(context: Context) {

    val avatarRepository: AvatarRepository by lazy {
        AvatarRepository(context)
    }

    companion object {
        @Volatile
        private var INSTANCE: AppDependencies? = null

        fun getInstance(context: Context): AppDependencies {
            return INSTANCE ?: synchronized(this) {
                val instance = AppDependencies(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}