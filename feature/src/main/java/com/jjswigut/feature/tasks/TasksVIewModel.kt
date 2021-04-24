package com.jjswigut.feature.tasks

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.jjswigut.core.base.BaseViewModel
import com.jjswigut.core.utils.State
import com.jjswigut.data.local.SwipeEvent
import com.jjswigut.data.local.entities.TaskEntity
import com.jjswigut.data.local.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksVIewModel @Inject constructor(
    private val repo: Repository
) : BaseViewModel() {

    private val tasksEventChannel = Channel<SwipeEvent>()
    val tasksEvent = tasksEventChannel.receiveAsFlow()

    fun getListOfTasks(listId: Long) = liveData(Dispatchers.IO) {
        emit(State.Loading)
        try {
            repo.getListOfTasks(listId).collect { lists ->
                emit(State.Success(lists))
            }
        } catch (exception: Exception) {
            emit(State.Failed(exception.toString()))
            Log.d(TAG, ":${exception.message} ")
        }
    }

    fun deleteList(listId: Long) {
        viewModelScope.launch { repo.deleteList(listId) }
    }

    fun onTaskSwiped(task: TaskEntity) = viewModelScope.launch {

        repo.deleteTask(task)
        tasksEventChannel.send(SwipeEvent.ShowUndoDeleteTaskMessage(task))
    }

    fun onUndoDeleteClick(task: TaskEntity) = viewModelScope.launch {
        repo.addTask(task)
    }
}