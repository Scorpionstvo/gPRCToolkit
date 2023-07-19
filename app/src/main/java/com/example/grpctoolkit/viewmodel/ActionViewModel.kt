package com.example.grpctoolkit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grpctoolkit.ui.Event
import com.example.grpctoolkit.remote.GRPCService
import com.example.grpctoolkit.remote.GRPCServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class ActionViewModel() : ViewModel() {
    private val crud: GRPCService = GRPCServiceImpl()
    private val _state = MutableLiveData<Event>()
    val state: LiveData<Event> = _state

    fun createTask(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            crud.createTask(name).flowOn(Dispatchers.Main)
                .catch { _state.postValue(Event.UnSuccess) }
                .collect { _state.postValue(Event.CreateSuccessfully(it)) }
        }
    }

    fun readTask(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            crud.readTask(id).flowOn(Dispatchers.Main).catch { _state.postValue(Event.UnSuccess) }
                .collect { _state.postValue(Event.TaskList(it)) }
        }
    }

    fun updateTask(id: Int, name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            crud.updateTask(id, name).flowOn(Dispatchers.Main)
                .catch { _state.postValue(Event.UnSuccess) }.collect {
                val event = if (it) Event.UpdateSuccessfully else Event.UnSuccess
                _state.postValue(event)
            }
        }
    }

    fun deleteTask(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            crud.deleteTask(id).flowOn(Dispatchers.Main).catch { _state.postValue(Event.UnSuccess) }
                .collect {
                    val event = if (it) Event.UpdateSuccessfully else Event.UnSuccess
                    _state.postValue(event)
                }
        }
    }

}
