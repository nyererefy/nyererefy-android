package com.nyererefy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nyererefy.adapters.CountsAdapter
import com.nyererefy.databinding.FragmentCountsBinding
import com.nyererefy.graphql.CountsQuery
import com.nyererefy.utilities.SpacesItemDecoration
import com.nyererefy.utilities.common.BaseFragment
import com.nyererefy.viewmodels.CountsViewModel
import com.nyererefy.viewmodels.SubcategoryViewViewModel
import javax.inject.Inject

class CountsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: CountsViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentCountsBinding
    private lateinit var adapter: CountsAdapter
    private lateinit var subcategoryViewViewModel: SubcategoryViewViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subcategoryViewViewModel = activity?.run {
            ViewModelProviders.of(this)[SubcategoryViewViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        subcategoryViewViewModel.subcategoryId.observe(this, Observer {
            viewModel.setSubcategoryId(it)
        })
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCountsBinding.inflate(inflater, container, false)

        adapter = CountsAdapter { viewModel.retry() }
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(SpacesItemDecoration(8))
        binding.lifecycleOwner = this
        subscribeUI()

        return binding.root
    }

    @Suppress("UNCHECKED_CAST")
    private fun subscribeUI() {
        viewModel.data.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.candidatesAndVotesCount())
        })

        viewModel.subscriptionData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.candidatesAndVotesCount() as List<CountsQuery.CandidatesAndVotesCount>)
        })

        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            adapter.setNetworkState(it)
        })
    }

}