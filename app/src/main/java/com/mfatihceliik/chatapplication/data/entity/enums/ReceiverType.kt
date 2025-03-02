package com.mfatihceliik.chatapplication.data.entity.enums

enum class ReceiverType(val key: Int, val value: String) {
    TEXT_MESSAGE_RECEIVER(-1, "Text Message"),
    IMAGE_MESSAGE_RECEIVER(-2, "Text Message"),
    FILE_MESSAGE_RECEIVER(-3, "Text Message")
}