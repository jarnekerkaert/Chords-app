package com.hogent.tictac.viewmodel

import androidx.lifecycle.ViewModel
import com.hogent.tictac.persistence.App

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