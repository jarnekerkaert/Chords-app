package com.hogent.tictac.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hogent.tictac.R
import com.hogent.tictac.SongChordsFragment

class ChordAdapter(
        private val chords: List<String>,
        private val mListener: SongChordsFragment.OnSongChordsFragmentInteractionListener?
) : RecyclerView.Adapter<ChordAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as String
            mListener?.onSongChordsFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chord_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chords[position]

        holder.chord.text = item

        holder.chord.apply {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = chords.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chord: TextView = view.findViewById(R.id.chord)
    }
}
