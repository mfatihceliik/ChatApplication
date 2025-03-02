package com.mfatihceliik.chatapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mfatihceliik.chatapplication.data.entity.local.Conversation
import com.mfatihceliik.chatapplication.data.entity.local.ImageMessage
import com.mfatihceliik.chatapplication.data.entity.local.Message
import com.mfatihceliik.chatapplication.data.entity.local.TextMessage
import com.mfatihceliik.chatapplication.data.entity.remote.message.MessageContentType
import com.mfatihceliik.chatapplication.data.entity.remote.participant.Participant
import com.mfatihceliik.chatapplication.data.entity.remote.user.User

@Database(entities =
[
    User::class,
    Participant::class,
    Conversation::class,
    Message::class,
    TextMessage::class,
    ImageMessage::class,
    MessageContentType::class
], version = 2, exportSchema = false)
abstract class ChatApplicationDatabase: RoomDatabase() {
    abstract fun localDao(): ChatAppDao
}