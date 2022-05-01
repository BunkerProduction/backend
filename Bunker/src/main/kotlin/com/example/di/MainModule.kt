package com.example.di

import com.example.room.RoomController
import org.koin.dsl.module

val mainModule = module {
    single {
        RoomController(get())
    }
}