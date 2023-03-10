package com.example.retrofitapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.example.retrofitapi.databinding.ActivityMainBinding
import com.example.retrofitapi.network.Aatrox
import com.example.retrofitapi.network.Data
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

        val logging = HttpLoggingInterceptor()

        val authorizationInterceptor = AuthorizationInterceptor()

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authorizationInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("https://league-of-legends-api1.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)


        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(R.layout.activity_main)

            logging.level = HttpLoggingInterceptor.Level.BODY
            retrieveDetails()

        }


        private fun setDetails(champResult: Data) {
            binding.name.text = getString(R.string.name, champResult.Aatrox.name.toString())
            binding.title.text = getString(R.string.title, champResult.Aatrox.title.toString())
        }


        private fun retrieveDetails() {
            lifecycleScope.launch {
                try {
                    val details = apiService.getDetails()
                    setDetails(details)
                } catch (e: Exception) {
                    Snackbar.make(
                        binding.mainView,
                        "Error",
                        Snackbar.LENGTH_LONG
                    ).setAction("Retry") { retrieveDetails() }.show()
                }
            }
        }
    }

    const val API_AUTHORIZATION_HEADER = "X-RapidAPI-Key"

    class AuthorizationInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val newRequest = request.newBuilder().addHeader(
                API_AUTHORIZATION_HEADER,
                "6439f2fb65msh5482b0824188f41p13825bjsn75b6f84e247a"
            )
                .build()

            return chain.proceed(newRequest)

        }
    }

