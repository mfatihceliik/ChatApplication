package com.mfatihceliik.chatapplication.data.entity.enums

enum class SenderType(val key: Int, val value: String) {
    TEXT_MESSAGE_SENDER(1, "Text Message"),
    IMAGE_MESSAGE_SENDER(2, "Image Message"),
    FILE_MESSAGE_SENDER(3, "File Message")
}