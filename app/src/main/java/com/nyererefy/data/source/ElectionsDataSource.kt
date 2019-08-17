package com.nyererefy.data.source

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.nyererefy.data.Api
import com.nyererefy.utilities.common.NetworkState
import com.nyererefy.utilities.models.Election
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

class ElectionsDataSource(private val retryExecutor: Executor) : PageKeyedDataSource<Int, Election>() {

    // keep a function reference for the retry event
    private var retry: (() -> Any)? = null
    private val apiClient = Api.create()

    /**
     * There is no sync on the state because paging will always call loadInitial first then wait
     * for it to return some success value before calling loadAfter.
     */
    val networkState = MutableLiveData<NetworkState>()

    val initialLoad = MutableLiveData<NetworkState>()

    fun retryAllFailed() {
        val prevRetry = retry
        retry = null
        prevRetry?.let {
            retryExecutor.execute {
                it.invoke()
            }
        }
    }

    override fun loadBefore(
            params: LoadParams<Int>,
            callback: LoadCallback<Int, Election>) {
        // ignored, since we only ever append to our initial load
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Election>) {
        networkState.postValue(NetworkState.LOADING)
        apiClient.getElections(params.key).enqueue(
                object : Callback<List<Election>> {
                    override fun onFailure(call: Call<List<Election>>, t: Throwable) {
                        retry = {
                            loadAfter(params, callback)
                        }
                        //TODO change error messages. DOnt expose api
                        networkState.postValue(NetworkState.error(t.message ?: "unknown err"))
                    }

                    override fun onResponse(
                            call: Call<List<Election>>,
                            response: Response<List<Election>>) {
                        when {
                            response.isSuccessful -> {
                                retry = null
                                callback.onResult(response.body() as MutableList<Election>, params.key + 10)
                                networkState.postValue(NetworkState.LOADED)
                            }
                            response.code() == 404 -> networkState.postValue(NetworkState.END)
                            else -> {
                                retry = {
                                    loadAfter(params, callback)
                                }
                                networkState.postValue(
                                        NetworkState.error("error code: ${response.code()}"))
                            }
                        }
                    }
                }
        )
    }

    override fun loadInitial(
            params: LoadInitialParams<Int>,
            callback: LoadInitialCallback<Int, Election>) {

        val request = apiClient.getElections(0)

        networkState.postValue(NetworkState.LOADING)
        initialLoad.postValue(NetworkState.LOADING)

        // triggered by a refresh, we better execute sync
        try {
            retry = null
            val response = request.execute()

            when {
                response.isSuccessful -> {
                    networkState.postValue(NetworkState.LOADED)
                    initialLoad.postValue(NetworkState.LOADED)
                    callback.onResult(response.body() as MutableList<Election>, 0, 10)
                }
                response.code() == 404 -> networkState.postValue(NetworkState.END)
                else -> networkState.postValue(
                        NetworkState.error("error code: ${response.code()}"))
            }

        } catch (ioException: IOException) {
            retry = {
                loadInitial(params, callback)
            }
            val error = NetworkState.error(ioException.message ?: "unknown error")
            networkState.postValue(error)
            initialLoad.postValue(error)
        }
    }

}