package com.example.coroutines1scopes.model

import kotlinx.coroutines.delay

class UserRepository {

    suspend fun getUsers(): List<User> {

        delay(8000)
        return listOf(
            User(1, "Sachin"),
            User(2, "Saurav"),
            User(3, "Rahul"),
            User(4, "Ajay")
        )
    }
}