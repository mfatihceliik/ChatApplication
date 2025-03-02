package com.mfatihceliik.chatapplication.data.entity.enums

enum class FileType(val type: String, val value: Int) {
    TEXT_MESSAGE("Text Message", 1),
    IMAGE_MESSAGE("Image Message", 2),
    FILE_MESSAGE("File Message", 3)
}