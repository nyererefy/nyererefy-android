package com.nyererefy.utilities.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.nyererefy.R


/**
 * A View Holder that can display a loading or have click action.
 * It is used to show the network state of paging.
 */
class NetworkStateItemViewHolder(view: View,
                                 private val retryCallback: () -> Unit)
    : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
    private val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar)
    private val retry = view.findViewById<Button>(R.id.retry_button)
    private val errorMsg = view.findViewById<TextView>(R.id.error_msg)
    private val endMsg = view.findViewById<TextView>(R.id.end_msg)

    init {
        retry.setOnClickListener {
            retryCallback()
        }
    }

    fun bindTo(networkState: NetworkState?) {
        progressBar.visibility = toVisibility(networkState?.status == Status.LOADING)
        retry.visibility = toVisibility(networkState?.status == Status.FAILED)
        endMsg.visibility = toVisibility(networkState?.status == Status.EMPTY)

        errorMsg.visibility = toVisibility(networkState?.msg != null)
        errorMsg.text = networkState?.msg
    }

    companion object {
        fun create(parent: ViewGroup, retryCallback: () -> Unit): NetworkStateItemViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.network_state_item, parent, false)
            return NetworkStateItemViewHolder(view, retryCallback)
        }

        fun toVisibility(constraint: Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}