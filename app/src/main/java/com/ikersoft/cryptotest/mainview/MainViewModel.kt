package com.ikersoft.cryptotest.mainview

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ikersoft.cryptotest.api.RetrofitClient
import com.ikersoft.cryptotest.model.Crypto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson


class MainViewModel : ViewModel() {
    private val retrofit = RetrofitClient()
    var coinlist = MutableLiveData<ArrayList<Crypto>>()
    var callInProgress = false
    var error = MutableLiveData<Boolean>()

    fun getCoinList() {
        if (callInProgress){
            return
        }
        callInProgress = true

        CoroutineScope(Dispatchers.IO).launch {
            retrofit.apiCall(
                {
                    retrofit.getCoinList()
                },
                object : RetrofitClient.RemoteEmitter {
                    override fun onResponse(response: Response<Any>) {
                        if (response.code() == 200){
                            val gson = Gson()
                            val token: TypeToken<ArrayList<Crypto?>?> =
                                object : TypeToken<ArrayList<Crypto?>?>() {}

                            //nota, el array que devuelve el rest api se encuentra dentro del hashmap "data", lo que hace que no haya podido usar el gsonfactory
                            //para hacer el cast primero transformo a json y luego a arraylist<crypto>, aunque seguro que hay alguna manera para optimizar esta parte
                            val responseData = response.body() as Map<*, *>
                            val unformattedList = responseData["data"] as ArrayList<*>
                            val unformattedJson = gson.toJson(unformattedList)

                            coinlist.value = gson.fromJson(unformattedJson, token.type)
                            callInProgress = false
                        }
                        else{
                            Log.e("Error", "Error")
                            error.value = true
                            callInProgress = false
                        }
                    }

                    override fun onError(errorType: RetrofitClient.ErrorType, msg: String) {
                        Log.e("Api errortype", errorType.toString())
                        Log.e("Api message", msg)
                        error.value = true
                        callInProgress = false
                    }
                }
            )
        }
    }
}