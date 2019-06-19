package com.hogent.tictac.view

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hogent.tictac.R
import com.hogent.tictac.common.Model
import kotlinx.android.synthetic.main.fragment_song.view.*

class SongAdapter(
        private val songs: Array<Model.Song>,
        private val listener: View.OnClickListener
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = songs[position]
        holder.songKey.text = item.key.name
        holder.songTitle.text = item.title

        with(holder.parentLayout) {
            setOnClickListener {
                listener.onClick(holder.itemView)
            }
        }
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songKey: TextView = view.song_key
        val songTitle: TextView = view.song_title
        val parentLayout: RelativeLayout = view.parent_layout
    }
}
