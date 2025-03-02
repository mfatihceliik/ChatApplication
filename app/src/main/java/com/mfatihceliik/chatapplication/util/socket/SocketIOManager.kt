package com.mfatihceliik.chatapplication.util.socket

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.mfatihceliik.chatapplication.data.entity.remote.typing.Typing
import com.mfatihceliik.chatapplication.data.entity.base.SocketResponse
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.response.ConversationResponse
import com.mfatihceliik.chatapplication.data.entity.remote.conversation.request.CreateConversationRequest
import com.mfatihceliik.chatapplication.data.entity.remote.message.Message
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.ImageMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.message.request.TextMessageRequest
import com.mfatihceliik.chatapplication.data.entity.remote.participant.Participant
import com.mfatihceliik.chatapplication.data.entity.remote.user.User
import com.mfatihceliik.chatapplication.data.remote.SocketService
import io.socket.client.Ack
import io.socket.client.Socket
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject
import kotlin.coroutines.resume

class SocketIOManager @Inject constructor(
    private val socket: Socket
): SocketService {

    companion object {
        private const val TAG = "SocketIOManager"
    }

    init {
        //reConnecting()
    }

    private fun reConnecting() {
        socket.on("disconnect") {
            Log.v(TAG, "disconnect")
        }

        socket.on("reconnecting") {
            Log.v(TAG, "reconnecting")
        }

        socket.on("close") {
            Log.v(TAG, "close")
        }

        socket.on("reconnect") {
            Log.v(TAG, "reconnect")
        }

        socket.on("reconnection_attempt") {
            Log.v(TAG, "reconnection_attempt")
        }

        //Fired when couldn't reconnect within reconnectionAttempts.
        socket.on("reconnect_failed") {
            Log.v(TAG, "reconnect_failed")
        }

        //Fired upon a reconnection attempt error.
        socket.on("reconnect_error") {
            Log.v(TAG, "reconnect_error")
        }
    }

    override fun establishConnection() {
        socket.connect()
    }

    override fun reConnect() {
        socket.open()
        socket.connect()
    }

    override fun disconnect() {
        socket.disconnect()
    }

    override suspend fun connectionSuccess(): Flow<Boolean> = callbackFlow {
        //establishConnection()
        socket.on(Socket.EVENT_CONNECT) {
            Log.v(TAG, "connect " + socket.connected().toString())
            trySend(true)
        }
        awaitClose() {
            socket.off("connect")
        }
    }

    override suspend fun connectionError(): Flow<Boolean> = callbackFlow {
        socket.on(Socket.EVENT_CONNECT_ERROR) {
            Log.v(TAG, "connect_error " + socket.connected().toString())
            trySend(false)
        }
        awaitClose()
    }

    override suspend fun onTextMessage(): Flow<SocketResponse<Message>> = callbackFlow {
        socket.on("onTextMessage") { args ->
            Log.v(TAG, "girdi babuş")
            val result = dataParser<Message>(args)
            Log.v(TAG, result.toString())
            trySend(result)
        }
        awaitClose {
            socket.off("onTextMessage")
        }
    }

    override suspend fun onImageMessage(): Flow<SocketResponse<Message>> = callbackFlow {
        socket.on("onImageMessage") { args ->
            val result = dataParser<Message>(args)
            trySend(result)
            Log.v(TAG, result.toString())
        }
        awaitClose {
            socket.off("onImageMessage")
        }
    }

    override suspend fun userTyping(): Flow<SocketResponse<Typing>> = callbackFlow {
        socket.on("onTyping") { args ->
            val result = dataParser<Typing>(args)
            trySend(result)
        }
        awaitClose {
            socket.off("onTyping")
        }
    }

    override suspend fun createConversation(createConversationRequest: CreateConversationRequest): Flow<SocketResponse<ConversationResponse>> = flow {
        establishConnection()
        val conversation = Gson().toJson(createConversationRequest)
        val acknowledgement = socket.awaitEmit("onCreateConversation", conversation)
        val socketResponse = dataParser<ConversationResponse>(acknowledgement)
        Log.v(TAG, "createConversation ${socketResponse.data.toString()}")
        emit(socketResponse)
    }

    override suspend fun sendTextMessage(textMessageRequest: TextMessageRequest): Flow<SocketResponse<Message>> = flow {
        establishConnection()
        val textMessage = Gson().toJson(textMessageRequest)
        val acknowledgement = socket.awaitEmit("onTextMessage", textMessage)
        val socketResponse = dataParser<Message>(acknowledgement)
        Log.v(TAG, "sendTextMessage ${socketResponse.data.toString()}")
        emit(socketResponse)
    }

    override suspend fun sendImageMessage(imageMessageRequest: ImageMessageRequest): Flow<SocketResponse<Message>> = flow {
        establishConnection()
        val imageMessage = Gson().toJson(imageMessageRequest)
        val acknowledgement = socket.awaitEmit("onImageMessage", imageMessage)
        val socketResponse = dataParser<Message>(acknowledgement)
        Log.v(TAG, "sendImageMessage ${socketResponse.data.toString()}")
        emit(socketResponse)
    }

    override suspend fun getAllUsers(userId: Int): Flow<SocketResponse<List<User>>> = flow {
        establishConnection()
        val acknowledgement = socket.awaitEmit("getAllUsers", userId)
        val socketResponse = dataParser<ArrayList<User>>(acknowledgement)
        emit(socketResponse)
    }

    override suspend fun onConversations(userId: Int): Flow<SocketResponse<List<ConversationResponse>>> = flow {
        establishConnection()
        val acknowledgement = socket.awaitEmit("onConversations", userId)
        val socketResponse = dataParser<List<ConversationResponse>>(acknowledgement)

        emit(socketResponse)
    }

    override suspend fun joinConversations(userId: Int): Flow<SocketResponse<List<Participant>>> = flow {
        establishConnection()
        val acknowledgement = socket.awaitEmit("joinConversations", userId)
        val socketResponse = dataParser<List<Participant>>(acknowledgement)
        emit(socketResponse)
    }

    private suspend fun Socket.awaitEmit(event: String, vararg arg: Any): Array<out Any?> = suspendCancellableCoroutine { continuation ->
        emit(event, *arg, Ack { args ->
            continuation.resume(args)
        })
    }

    private inline fun <reified T> dataParser(args: Array<out Any?>): SocketResponse<T> {
        return try {
            val response = args[0] as JSONObject
            val data = response.getString("data")
            val message = response.getString("message")
            val success = response.getBoolean("success")
            val convertedData = parseJsonData<T>(data)
            SocketResponse(convertedData, message, success)
        } catch (jsonException: JSONException) {
            val message = jsonException.localizedMessage
            jsonException.localizedMessage?.let {
                Log.v(TAG, it)
            }
            SocketResponse(null, message, false)
        }
    }

    private inline fun <reified T> parseJsonData(jsonString: String): T? {
        return try {
            val gson = Gson()
            val type = object : TypeToken<T>() {}.type
            gson.fromJson<T>(jsonString, type)
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "JsonSyntaxException: ${e.localizedMessage}")
            null
        }
    }

    /*override suspend fun sendMessage(sendMessage: SendMessage): Flow<SocketResponse<ConversationDetail>> {
        return flow {

            establishConnection()
            val message = Gson().toJson(sendMessage)
            val acknowledgement = socket.awaitEmit("onNewMessage", message)
            val socketResponse = dataParser<ConversationDetail>(acknowledgement)
            emit(socketResponse)
        }
    }*/

    /*private suspend fun Socket.awaitOn(event: String): Flow<Array<out Any?>> = callbackFlow {
        socket.on("connect") { argsConnect ->
            socket.on(event) { args ->
                CoroutineScope(Dispatchers.IO).launch {
                    send(args)
                }
            }
        }
        awaitClose()
    }*/
}