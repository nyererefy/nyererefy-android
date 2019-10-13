package com.nyererefy.data.repositories

import androidx.lifecycle.MutableLiveData
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException
import com.nyererefy.graphql.CandidatesQuery
import com.nyererefy.utilities.Resource
import com.nyererefy.utilities.common.NetworkState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CandidatesRepository
@Inject constructor(private val client: ApolloClient) {

    fun fetchCandidates(id: String): Resource<CandidatesQuery.Data> {
        val query = CandidatesQuery.builder().subcategoryId(id.toInt()).build()

        val networkState = MutableLiveData<NetworkState>()
        val data = MutableLiveData<CandidatesQuery.Data>()

        networkState.value = NetworkState.LOADING

        client.query(query).enqueue(object : ApolloCall.Callback<CandidatesQuery.Data>() {
            override fun onFailure(e: ApolloException) {
                networkState.postValue(NetworkState.error(e.localizedMessage))
            }

            override fun onResponse(response: Response<CandidatesQuery.Data>) {
                when {
                    response.hasErrors() -> {
                        networkState.postValue(NetworkState.error(response.errors()[0].message()))
                    }
                    else -> {
                        networkState.postValue(NetworkState.LOADED)
                        data.postValue(response.data())
                    }
                }
            }
        })
        return Resource(data, networkState)
    }
}