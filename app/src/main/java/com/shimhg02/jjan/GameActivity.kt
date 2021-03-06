package com.shimhg02.jjan


import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.enablex.jjan.R
import com.google.gson.JsonObject
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONObject
import java.net.URISyntaxException


@Suppress("DEPRECATION")
class GameActivity : AppCompatActivity() {

    val PREFERENCE = "com.shimhg02.honbab"
    private lateinit var mSocket: Socket
    val socketURI = "https://jjan.andy0414.com"
    var isVip: Boolean = false
    var sex: Boolean = false
    lateinit var uuid: String
    var arrMessages: ArrayList<String> = ArrayList()
    var position = 0
    private val FINISH_INTERVAL_TIME: Long = 2000
    private var backPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        try {
            mSocket = IO.socket(socketURI)
        } catch (e: URISyntaxException) {
            Log.e("OnebyoneActivity", e.reason)
        }
        mSocket.connect()
        mSocket.on(Socket.EVENT_CONNECT, onConnect)
    }

    internal var onMatched: Emitter.Listener = Emitter.Listener { args ->
        runOnUiThread(Runnable {
            Toast.makeText(this@GameActivity,"게임 시작!!!!",Toast.LENGTH_SHORT).show()
            val data = args[0] as JSONArray
            try {
                Log.d("asdasd1",data.toString())
                mSocket.disconnect()
                mSocket.off(Socket.EVENT_DISCONNECT, onConnect)
            } catch (e: Exception) {
                return@Runnable
            }
        })
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStop() {
        super.onStop()
//        mSocket.disconnect()
//        mSocket.off()
//        mSocket.off(Socket.EVENT_CONNECT, onConnect)
//        mSocket.off(Socket.EVENT_DISCONNECT, onMatched)
//        mSocket.close()
        Log.d("SOCKET LIFECYCLE","DISCONNECT ONSTOP")
    }

    override fun onPause() {
        super.onPause()
        mSocket.disconnect()
        mSocket.off()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off(Socket.EVENT_DISCONNECT, onMatched)
        mSocket.close()
        Log.d("SOCKET LIFECYCLE","DISCONNECT ONPAUSE")
    }

    override fun onDestroy() {
        super.onDestroy()
        mSocket.disconnect()
        mSocket.off()
        mSocket.off(Socket.EVENT_CONNECT, onConnect)
        mSocket.off(Socket.EVENT_DISCONNECT, onMatched)
        mSocket.close()
        Log.d("SOCKET LIFECYCLE","DISCONNECT ONDESTROY")
    }

    val onConnect: Emitter.Listener = Emitter.Listener {
        val pref = getSharedPreferences(PREFERENCE, MODE_PRIVATE)
        System.out.println("LOGD GAME:"+ pref.getString("meetRoom",""))
        mSocket.emit("startRoulette", JSONObject(pref.getString("meetRoom","")))
        mSocket.on("startRoulette", onMatched)
        mSocket.on("endRoulette", onMatched)
    }

    override fun onBackPressed() {
        val tempTime = System.currentTimeMillis()
        val intervalTime: Long = tempTime - backPressedTime
        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            mSocket.disconnect()
            mSocket.off()
            mSocket.off(Socket.EVENT_CONNECT, onConnect)
            mSocket.off(Socket.EVENT_DISCONNECT, onMatched)
            mSocket.close()
            Log.d("SOCKET","DISCONNECT CUT")
            finish()
        } else {
            backPressedTime = tempTime
            Toast.makeText(applicationContext, "한번 더 누르면 1대 1 매칭이 종료됩니다..", Toast.LENGTH_SHORT).show()
        }
    }

//
//    val newGroup: Emitter.Listener = Emitter.Listener {
//        groupUUID = it[0].toString()
//        val intent = Intent(this@OnebyoneActivity, ChatActivity::class.java)
//        intent.putExtra("chatUUID", groupUUID)
//        startActivity(intent)
//    }
}