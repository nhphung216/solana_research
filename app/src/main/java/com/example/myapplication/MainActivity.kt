package com.example.myapplication

import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.response.SolanaDataResponse
import com.solana.Solana
import com.solana.api.getAccountInfo
import com.solana.core.PublicKey
import com.solana.models.buffer.AccountInfo
import com.solana.networking.OkHttpNetworkingRouter
import com.solana.networking.RPCEndpoint
import com.solana.vendor.toLong
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var solana: Solana
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.poolInfo.setOnClickListener {
            getAccountInfoRPC()
        }

        val router = OkHttpNetworkingRouter(RPCEndpoint.devnetSolana)
        solana = Solana(router)
    }

    private fun getAccountInfo() {
        solana.api.getAccountInfo(
            PublicKey("DynqvMmxdh3ZddS3kczNMzjEAZuFHE8tpr8gFhCrTX2f"),
            AccountInfo::class.java,
            onComplete = { result ->
                result.onSuccess {
                    Log.e("onSuccess", "$it")
                }
                result.onFailure {
                    Log.e("onFailure", "$it")
                }
            }
        )
    }

    private fun getAccountInfoRPC() {
        binding.text.text = ""
        Single.fromCallable {
            val jsonEncode = JSONObject()
            jsonEncode.put("encoding", "base64")

            val arrJson = JSONArray()
            arrJson.put("DynqvMmxdh3ZddS3kczNMzjEAZuFHE8tpr8gFhCrTX2f")
            arrJson.put(jsonEncode)

            val jsonData = JSONObject()
            jsonData.put("jsonrpc", "2.0")
            jsonData.put("id", 1)
            jsonData.put("method", "getAccountInfo")
            jsonData.put("params", arrJson)

            val mediaType = "application/json".toMediaTypeOrNull()
            val body = jsonData.toString().toRequestBody(mediaType)

            val builder = OkHttpClient().newBuilder()

            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.interceptors().add(logging)

            val client: OkHttpClient = builder.build()
            val request: Request =
                Request.Builder().url("https://api.devnet.solana.com/")
                    .method("POST", body)
                    .addHeader("content-type", "application/json")
                    .build()
            val response: Response = client.newCall(request).execute()
            response
        }.map { response ->
            val responseBodyCopy: ResponseBody = response.peekBody(Long.MAX_VALUE)
            val responseBodyJson: String = responseBodyCopy.string()
            Log.e("tag()", "responseBodyJson: $responseBodyJson")
            val model = SolanaDataResponse.fromJSON(responseBodyJson)

            // decode info
            val encoded = model.result?.value?.data!![0]
            println("encoded: $encoded")

            println(
                "name: ${
                    byteArrayOf(
                        98,
                        105,
                        103,
                        114,
                        105,
                        99,
                        104,
                        115,
                        116,
                        97,
                        107,
                        105,
                        110,
                        103,
                        53,
                        56
                    ).toString(Charsets.UTF_8)
                }"
            )
            println("totalDepositedAmount: ${byteArrayOf(47, 101, 23, 44, 112, 0, 0, 0).toLong()}")
            println("minimumDepositedAmount: ${byteArrayOf(0, -31, -11, 5, 0, 0, 0, 0).toLong()}")
            println("penaltyRate: ${byteArrayOf(-72, 11, 0, 0, 0, 0, 0, 0).toLong()}")
            println("vestingDuration: ${byteArrayOf(-72, 11, 0, 0, 0, 0, 0, 0).toLong()}")
            println("rewardPeriod: ${byteArrayOf(-36, 5, 0, 0, 0, 0, 0, 0).toLong()}")
            println("startAt: ${byteArrayOf(88, 2, 0, 0, 0, 0, 0, 0).toLong()}")
            println(
                "totalAllTimeStakedAccount: ${
                    byteArrayOf(
                        -14,
                        -30,
                        39,
                        99,
                        0,
                        0,
                        0,
                        0
                    ).toLong()
                }"
            )
            println(
                "totalAllTimeWithdrawnAmount: ${
                    byteArrayOf(
                        1,
                        -128,
                        60,
                        72,
                        120,
                        0,
                        0,
                        0
                    ).toLong()
                }"
            )
            println("totalAllTimeDepositedAmount: ${byteArrayOf(0, 0, 0, 0, 0, 0, 0, 0).toLong()}")
            println(
                "diffDurationSameAccount: ${
                    byteArrayOf(
                        0,
                        59,
                        68,
                        44,
                        -77,
                        -111,
                        33,
                        87
                    ).toLong()
                }"
            )

            // decode the encoded data
            var bytes = ""
            var uBytes = ""
            val uByteArray = arrayListOf<UByte>()
            val decoded = Base64.decode(encoded, Base64.DEFAULT)
            decoded.forEach { bytes += ("$it, ") }
            decoded.forEach {
                uBytes += ("${it.toUByte()}, ")
                uByteArray.add(it.toUByte())
            }
            println("bytes: $bytes")
            println("uBytes: $uBytes")
            bytes + "\n\n" + uBytes
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe({ response ->
                binding.text.text = response
            }, {
                it.printStackTrace()
            })
    }
}