package com.nyererefy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nyererefy.adapters.ElectionsAdapter
import com.nyererefy.databinding.ElectionsFragmentBinding
import com.nyererefy.di.Injectable
import com.nyererefy.utilities.Status
import com.nyererefy.viewmodels.ElectionsViewModel
import org.jetbrains.anko.support.v4.longToast
import javax.inject.Inject


class ElectionsFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ElectionsViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val binding = ElectionsFragmentBinding.inflate(inflater, container, false)

        val adapter = ElectionsAdapter()
        binding.recyclerView.adapter = adapter
        subscribeUI(adapter)

        return binding.root
    }

    private fun subscribeUI(adapter: ElectionsAdapter) {
        viewModel.elections.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    adapter.submitList(it.data?.elections())
                }
                Status.ERROR -> {
                    longToast(it.message.toString())
                }
            }
        })
    }
}