package ru.skillbranch.devintensive.extensions

import ru.skillbranch.devintensive.models.BaseMessage
import ru.skillbranch.devintensive.models.ImageMessage
import ru.skillbranch.devintensive.models.TextMessage

fun shortMessage(message: BaseMessage?): Pair<String, String?> = when(message){
    null -> "" to null
    is ImageMessage -> "${message.from.firstName} - отправил фото" to null
    is TextMessage -> message.text.orEmpty() to message.from.firstName
    else -> error("not expected message type")
}