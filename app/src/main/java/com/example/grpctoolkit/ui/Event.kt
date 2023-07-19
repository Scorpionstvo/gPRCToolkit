package com.example.grpctoolkit.ui

import com.example.grpctoolkit.data.Task

interface Event {

    data class TaskList(
        val list: List<Task>
    ) : Event

    object UpdateSuccessfully : Event

    data class CreateSuccessfully(val id: Int): Event

    object UnSuccess : Event

}