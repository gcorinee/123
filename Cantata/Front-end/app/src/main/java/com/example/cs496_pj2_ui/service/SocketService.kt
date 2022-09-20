package com.example.cs496_pj2_ui.service

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketService {

    lateinit var mSocket: Socket

    private const val BASE_URL = "http://192.249.18.210"

    @Synchronized
    fun setSocket() {
        try {
            mSocket = IO.socket(BASE_URL)
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

}