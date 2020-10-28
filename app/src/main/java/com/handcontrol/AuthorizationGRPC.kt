package com.handcontrol

import android.content.Context
import android.telephony.TelephonyManager
import io.grpc.ManagedChannel
import java.io.Closeable
import java.util.concurrent.TimeUnit

class AuthorizationGRPC(private val channel: ManagedChannel) : Closeable {
    var login_name = ""
    var password = ""
    var imei = ""
    var user_name = ""

    private val stub: HandleRequestCoroutineStub = HandleRequestCoroutineStub(channel) {
        suspend fun loginAuth() {
            val request = login.newBuilder().setImei(imei).setLogin(login_name).setPassword(password).build()
            val response = stub.login(request)
            println("Received: ${response.message}")
        }

        suspend fun clientAuth() {
            val request = ProRequest.newBuilder().setImei(imei).setRequest(user_name).build()
            val response = stub.ProRequest(request)
            println("Received: ${response.message}")
        }
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    fun getIMEI(context: Context): String? {
        val mngr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        imei = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mngr.imei
        } else ({
            mngr.deviceId
        }).toString()
        return imei;
    }

    fun login_user(login: String, password: String) {
        this.login_name = login
        this.password = password
    }

    fun registration(user: String) {
        this.user_name = user;
    }
}
