package com.example.myapplication.model

data class UserResponse(
    val email: String?="",
    val isRegister:Boolean,
    val message: String
)