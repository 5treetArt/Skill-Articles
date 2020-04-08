package ru.skillbranch.skillarticles.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.skillbranch.skillarticles.viewmodels.base.BaseViewModel
import ru.skillbranch.skillarticles.viewmodels.base.IViewModelState

class RootViewModel(handle: SavedStateHandle) : BaseViewModel<RootState>(handle, RootState()) {
}

class RootState : IViewModelState {
    override fun save(outState: SavedStateHandle) {
        //TODO
    }

    override fun restore(savedState: SavedStateHandle): IViewModelState {
        //TODO
        return this
    }
}
