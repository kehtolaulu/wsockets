package com.kehtolaulu.wsockets.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.gson.GsonBuilder
import com.kehtolaulu.wsockets.web.Api
import com.kehtolaulu.wsockets.R
import com.kehtolaulu.wsockets.web.UserDevice
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private val api = Retrofit.Builder()
        .baseUrl("https://backend-chat.cloud.technokratos.com/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build()
        .create(Api::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btn_login.setOnClickListener { loginUsingData() }
    }

    private fun loginUsingData() {
        loginDevice(UserDevice(et_name.text.toString(), et_device_id.text.toString()))
    }

    private fun loginDevice(device: UserDevice) {
        val subscribe = api.login(device)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ r ->
                if (r.status == "ok") {
                    val sharedPref =
                        getSharedPreferences("SHARED_PREFERENCES", Context.MODE_PRIVATE)
                    sharedPref.edit()
                        .putString("device_id", device.device_id)
                        .putString("username", device.username).apply()
                    startActivity(Intent(this, ChatActivity::class.java))
                }
            }, Throwable::printStackTrace)
    }
}
