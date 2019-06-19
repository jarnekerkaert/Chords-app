package com.hogent.tictac.persistence

import androidx.lifecycle.ViewModel
import com.hogent.tictac.persistence.database.App
import com.hogent.tictac.view.SongViewModel

abstract class InjectedViewModel : ViewModel() {

    init {
        inject()
    }

    private fun inject() {
        when (this) {
            is SongViewModel -> App.component.inject(this)

        }
    }
}