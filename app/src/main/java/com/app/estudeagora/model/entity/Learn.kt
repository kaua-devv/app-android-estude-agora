package com.app.estudeagora.model.entity

import com.app.estudeagora.model.enums.DifficultLearnEnum
import kotlinx.serialization.Serializable


@Serializable
data class Learn (
    val id: Int,
    val title: String,
    val description: String,
    val whatLearn: List<String>,
    val difficult: DifficultLearnEnum

)