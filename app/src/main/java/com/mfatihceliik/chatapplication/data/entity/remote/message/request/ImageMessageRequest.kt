package com.mfatihceliik.chatapplication.data.entity.remote.message.request

import java.util.UUID

data class ImageMessageRequest(
    val uuId: String = UUID.randomUUID().toString(),
    val conversationId: Int,
    val userId: Int,
    val messageContentTypeId: Int,
    val imageBlob: ByteArray,
    val text: String?
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ImageMessageRequest

        if (!imageBlob.contentEquals(other.imageBlob)) return false

        return true
    }
    override fun hashCode(): Int {
        return imageBlob.contentHashCode()
    }
}
