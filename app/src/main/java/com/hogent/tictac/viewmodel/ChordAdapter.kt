package com.hogent.tictac.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.hogent.tictac.R
import com.hogent.tictac.persistence.Model
import kotlinx.android.synthetic.main.chord_list_item.view.*
import org.jetbrains.anko.backgroundColor
import java.util.*

class ChordAdapter(
        private var lifecycleOwner: LifecycleOwner,
        private var songViewModel: SongViewModel,
        private val scaleOfSongKey: Boolean,
        private val mListener: OnChordClickListener,
        private val chordColor: Int?
) : RecyclerView.Adapter<ChordAdapter.ViewHolder>() {

    private var chords: List<String> = listOf()
    private val onClickListener: View.OnClickListener

    init {
        reloadData()

        onClickListener = View.OnClickListener { v ->
            val item = v.tag as String
            mListener.onChordClick(item)
        }
    }

    fun reloadData() {
        songViewModel.songSelected.observe(lifecycleOwner, Observer {
            chords = if (scaleOfSongKey)
                this.scaleOfKey(it?.key?.name ?: "C")
            else
                it?.chords?.map { c -> c.name } ?: listOf()
            this.notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chord_list_item, parent, false)
        view.chord.backgroundColor =
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chords[position]

        holder.chord.text = item

        holder.chord.apply {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int = chords.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chord: TextView = view.findViewById(R.id.chord)
    }

    interface OnChordClickListener {
        fun onChordClick(item: String)
    }

    private fun scaleOfKey(key: String): List<String> {
        val chords = Model.NoteMajor.values().map { c -> c.name }
        Collections.rotate(chords, -(Model.NoteMajor.valueOf(key.replace("M", "")).ordinal))
        if (key.contains("M"))
            return listOf(
                    "${chords[0]}M",
                    chords[2],
                    chords[4],
                    "${chords[5]}M",
                    "${chords[7]}M",
                    chords[9],
                    chords[11]
            )
        else
            return listOf(
                    chords[0],
                    "${chords[2]}M",
                    "${chords[4]}M",
                    chords[5],
                    chords[7],
                    "${chords[9]}M",
                    "${chords[11]}M"
            )
    }
}
