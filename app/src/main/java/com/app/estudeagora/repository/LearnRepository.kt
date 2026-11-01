package com.app.estudeagora.repository


import android.content.Context
import com.app.estudeagora.model.entity.Learn
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearnRepository @Inject constructor(@ApplicationContext private val context: Context){


    // usa cache para ler mais rapido da proxima vez
    private val learns: List<Learn> by lazy {
        val json = context.assets
            .open("learns.json")
            .bufferedReader()
            .use { it.readText() }

        Json.decodeFromString<List<Learn>>(json)
    }

    fun findAll(): List<Learn> = learns

    // safe call
    fun findRandom(): Learn? {
        return findAll().randomOrNull();
    }


}