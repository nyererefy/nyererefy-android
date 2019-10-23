package com.nyererefy.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.nyererefy.adapters.SexSpinnerAdapter
import com.nyererefy.adapters.sexList
import com.nyererefy.databinding.FragmentSetInfoBinding
import com.nyererefy.di.Injectable
import com.nyererefy.graphql.type.Sex
import com.nyererefy.graphql.type.UserSetupInput
import com.nyererefy.ui.MainActivity
import com.nyererefy.utilities.afterTextChanged
import com.nyererefy.utilities.common.BaseFragment
import com.nyererefy.viewmodels.SetInfoViewModel
import org.jetbrains.anko.clearTop
import org.jetbrains.anko.support.v4.intentFor
import javax.inject.Inject

class SetInfoFragment : BaseFragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: SetInfoViewModel by viewModels { viewModelFactory }
    private lateinit var binding: FragmentSetInfoBinding
    private lateinit var sex: Sex


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetInfoBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.fragment = this

        setUpSexSpinner()
        observeInputs()

        return binding.root
    }

    private fun observeInputs() {
        binding.name.afterTextChanged {
            viewModel.dataChanged(
                    name = it,
                    username = binding.username.text.toString(),
                    password = binding.password.text.toString(),
                    confPassword = binding.confPassword.text.toString()
            )
        }

        binding.username.afterTextChanged {
            viewModel.dataChanged(
                    name = binding.name.text.toString(),
                    username = it,
                    password = binding.password.text.toString(),
                    confPassword = binding.confPassword.text.toString()
            )
        }

        binding.password.afterTextChanged {
            viewModel.dataChanged(
                    name = binding.name.text.toString(),
                    username = binding.username.text.toString(),
                    password = it,
                    confPassword = binding.confPassword.text.toString()
            )
        }

        binding.confPassword.afterTextChanged {
            viewModel.dataChanged(
                    name = binding.name.text.toString(),
                    username = binding.username.text.toString(),
                    password = binding.password.text.toString(),
                    confPassword = it
            )
        }
    }

    fun submitData() {
        val input = UserSetupInput.builder()
                .name(binding.name.text.toString())
                .username(binding.username.text.toString())
                .password(binding.password.text.toString())
                .sex(sex)
                .build()

        viewModel.setInput(input)

        viewModel.data.observe(viewLifecycleOwner, Observer {
            if (it.setupAccount().isAccountSet) {
                startActivity(intentFor<MainActivity>().clearTop())
            }
        })
    }

    private fun setUpSexSpinner() {
        binding.sexSpinner.adapter = SexSpinnerAdapter(requireContext())
        binding.sexSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                sex = sexList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}