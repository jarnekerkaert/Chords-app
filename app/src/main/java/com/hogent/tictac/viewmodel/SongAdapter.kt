package com.hogent.tictac.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.hogent.tictac.R
import com.hogent.tictac.persistence.Model
import kotlinx.android.synthetic.main.fragment_song.view.*

class SongAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val songViewModel: SongViewModel,
    private val mListener: OnSongClickListener
) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {

    private var songs: Array<Model.Song> = arrayOf()
    private val onClickListener: View.OnClickListener

    init {
        reloadData()

        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Model.Song
            mListener.onSongClick(item.id)
        }
    }

    fun reloadData() {
        songViewModel.songs.observe(lifecycleOwner, Observer {
            songs = it!!
            this.notifyDataSetChanged()
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_song, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = songs[position]
        holder.songKey.text = item.key.name
        holder.songTitle.text = item.title
        holder.songComposer.text = item.user

        with(holder.parentLayout) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val songKey: TextView = view.song_key
        val songTitle: TextView = view.song_title
        val songComposer: TextView = view.song_composer
        val parentLayout: MaterialCardView = view.parent_layout
    }

    interface OnSongClickListener {
        fun onSongClick(item: String)
    }
}
