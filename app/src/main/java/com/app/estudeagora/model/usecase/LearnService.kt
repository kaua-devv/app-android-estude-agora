package com.app.estudeagora.model.usecase

import com.app.estudeagora.model.entity.Learn
import com.app.estudeagora.repository.LearnRepository
import javax.inject.Inject

class LearnService @Inject constructor(private val repository: LearnRepository){


    fun findRandomLearn(): Learn? {
        return repository.findRandom();
    }


}