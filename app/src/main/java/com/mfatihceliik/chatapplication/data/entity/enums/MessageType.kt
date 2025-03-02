package com.mfatihceliik.chatapplication.data.entity.enums

enum class MessageType(val key: Int, val value: String) {
    TEXT_MESSAGE(1, "Text Message"),
    IMAGE_MESSAGE(2, "Image Message"),
    FILE_MESSAGE(3, "File Message"),
    EMPTY_MESSAGE(404, "Empty Message")
}