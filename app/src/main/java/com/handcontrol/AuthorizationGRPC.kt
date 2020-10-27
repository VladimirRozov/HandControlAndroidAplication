package com.handcontrol

import android.content.Context
import android.telephony.TelephonyManager
import io.grpc.ManagedChannel
import java.io.Closeable
import java.util.concurrent.TimeUnit

class AuthorizationGRPC(private val channel: ManagedChannel) : Closeable {
    var login_name = ""
    var password = ""
    var imei = "";

    private val stub: HandleRequestCoroutineStub = HandleRequestCoroutineStub(channel) {
        suspend fun greet(name: String) {
            val request = login.newBuilder().build()
            val response = stub.login(request)
            println("Received: ${response.message}")
        }



        fun getIMEI(context: Context): String? {
            val mngr = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            imei = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                mngr.getImei()
            } else ({
                // mngr.getDeviceId()
                0
            }).toString()
            return imei;
        }

        fun login(login: String, password: String) {
            this.login_name = login
            this.password = password
        }

        fun registration() {

        }
    }

    override fun close() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }
}
