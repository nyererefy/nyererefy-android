package com.konektedi.vs.statistics

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.konektedi.vs.R
import com.konektedi.vs.utilities.common.getRandomColor
import com.konektedi.vs.utilities.models.Stat

/**
 * Created by Sy on b/14/2018.
 */

class StatsAdapter(private val mContext: Context,
                   private val list: List<Stat>) :
        RecyclerView.Adapter<StatsAdapter.StatsViewHolder>() {


    inner class StatsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val title: TextView = view.findViewById(R.id.title)
        private val count: TextView = view.findViewById(R.id.count)
        private val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraintLayout)

        fun bind(candidate: Stat?, mContext: Context) {
            val color = getRandomColor(mContext)
            constraintLayout.setBackgroundColor(Color.parseColor(color))

            count.text = candidate!!.count
            title.text = candidate.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatsViewHolder {
        val view = LayoutInflater.from(mContext)
                .inflate(R.layout.z_stat, parent, false)

        return StatsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: StatsViewHolder, position: Int) {
        holder.bind(list[position], mContext)
    }


}