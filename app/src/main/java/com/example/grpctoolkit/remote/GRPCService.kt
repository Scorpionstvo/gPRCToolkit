package com.example.grpctoolkit.remote

import com.example.grpctoolkit.data.Task
import kotlinx.coroutines.flow.Flow

interface GRPCService {

   fun createTask(name: String): Flow<Int>

     fun readTask(taskId: Int): Flow<List<Task>>

    fun updateTask(taskId: Int, newName: String): Flow<Boolean>

    fun deleteTask(id: Int): Flow<Boolean>
}