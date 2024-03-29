package com.hogent.tictac.viewmodel

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.hogent.tictac.R
import com.hogent.tictac.persistence.Model
import org.jetbrains.anko.backgroundColor
import java.util.*


/**
 * RecyclerViewAdapter for displaying a list of chords
 *
 * @param lifecycleOwner
 * @param songViewModel
 * @param scaleOfSongKey decides if the adapter uses the chords from the key of the selected song in songViewModel
 * @param mListener for defining the onChordClickListener in the fragment
 *
 * @property chords list of chords in the recyclerView
 *
 * @constructor calls reloadData and sets the onclicklistener for the individual chords
 */
class ChordAdapter(
    private var lifecycleOwner: LifecycleOwner,
    private var songViewModel: SongViewModel,
    private val scaleOfSongKey: Boolean,
    private val mListener: OnChordClickListener
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

    /**
     * Reloads the chords shown in the recyclerview
     *
     * Used for refreshing the data from inside a fragment.
     * Observes the selected song in the songViewModel and updates the chords in the recyclerView when the data set changes
     */
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
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = chords[position]

        holder.chord.text = item

        holder.setColor()

        holder.chord.apply {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int = chords.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chord: TextView = view.findViewById(R.id.chord)

        /**
         * Sets the background colour of the chord
         *
         * Uses the hash encoded chord to generate rgb values
         */
        fun setColor() {
            val hash = chord.text.hashCode()
            chord.backgroundColor = Color.rgb(
                hash and 0xFF0000 shr 16,
                hash and 0x00FF00 shr 8,
                hash and 0x0000FF
            )
        }
    }

    interface OnChordClickListener {
        fun onChordClick(item: String)
    }

    /**
     * Used for generating chords of a given key
     *
     * An array with all existing chords is initialized, the array is shifted left so that the given chord is placed
     * first in the array.
     * If the given chord is a minor, the first, fourth and fifth chords are set to minor chords respectively.
     * If the given chord is a major, the second, third, sixth and seventh chords are set to minor chords respectively.
     *
     * @param key the given key
     * @return all 7 chords in the given key
    */
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
