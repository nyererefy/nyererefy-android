package com.nyererefy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.nyererefy.data.repositories.ReviewsRepository
import javax.inject.Inject

data class ReviewsArgs(val subcategoryId: Int, val offset: Int?)

class ReviewsViewModel
@Inject constructor(private val repository: ReviewsRepository) : ViewModel() {
    private val _args = MutableLiveData<ReviewsArgs>()
    private val _subcategoryId = MutableLiveData<Int>()

    private val _resource = Transformations.map(_args) {
        repository.fetchReviews(it)
    }

    private val _subscriptionResource = Transformations.map(_subcategoryId) {
        repository.subscribeToReviews(it)
    }

    val data = Transformations.switchMap(_resource) { it.data }
    val networkState = Transformations.switchMap(_resource) { it.networkState }
    val review = Transformations.switchMap(_subscriptionResource) { it.data }
    val subState = Transformations.switchMap(_subscriptionResource) { it.subscriptionState }


    fun setArgs(subcategoryId: Int, offset: Int? = null) {
        val args = ReviewsArgs(subcategoryId, offset)

        //Setting args
        if (_args.value != args) {
            _args.value = args
        }

        //Setting subcategoryId
        if (_subcategoryId.value != subcategoryId) {
            _subcategoryId.value = subcategoryId
        }
    }

    fun retry() {
        _args.value?.let {
            _args.value = it
        }
    }

    fun reconnect() {
        _subcategoryId.value?.let {
            _subcategoryId.value = it
        }
    }
}

