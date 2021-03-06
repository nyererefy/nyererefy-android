package com.nyererefy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.nyererefy.data.repositories.CandidatesRepository
import com.nyererefy.graphql.type.VoteInput
import javax.inject.Inject

class CandidatesViewModel
@Inject constructor(private val repository: CandidatesRepository) : ViewModel() {
    private val _subcategoryId = MutableLiveData<String>()
    private val _voteInput = MutableLiveData<VoteInput>()
    val isVoteBtnEnabled = MutableLiveData(false)

    private val _candidatesResource = Transformations.map(_subcategoryId) {
        repository.fetchCandidates(it)
    }

    private val _votingResource = Transformations.map(_voteInput) {
        repository.submitVote(it)
    }

    val data = Transformations.switchMap(_candidatesResource) { it.data }
    val networkState = Transformations.switchMap(_candidatesResource) { it.networkState }
    val votingNetworkState = Transformations.switchMap(_votingResource) { it.networkState }

    fun setSubcategoryId(electionId: String) {
        if (_subcategoryId.value != electionId) {
            _subcategoryId.value = electionId
        }
    }

    fun retry() {
        _subcategoryId.value?.let {
            _subcategoryId.value = it
        }
    }

    fun submitVote(input: VoteInput) {
        _voteInput.value = input
    }
}