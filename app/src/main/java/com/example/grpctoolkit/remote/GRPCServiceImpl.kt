package com.example.grpctoolkit.remote

import com.example.grpctoolkit.data.Task
import com.example.grpctoolkit.util.Configuration
import grpc.crud.CrudServiceGrpc
import grpc.crud.TaskRequest
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GRPCServiceImpl : GRPCService {
    private val configuration = Configuration()
    private val channel: ManagedChannel =
        ManagedChannelBuilder.forAddress(configuration.getServerIp(), configuration.getServerPort())
            .usePlaintext().build()

    override fun createTask(name: String): Flow<Int> {
        return flow {
            val blockingStub = CrudServiceGrpc.newBlockingStub(channel)
            val request = TaskRequest.newBuilder().setName(name).setId(0).build()
            val response = blockingStub.addElement(request)
            emit(response.id)
        }
    }

    override fun readTask(taskId: Int): Flow<List<Task>> {
        return flow {
            val blockingStub = CrudServiceGrpc.newBlockingStub(channel)
            val request = TaskRequest.newBuilder().build()
            val command = blockingStub.listingElements(request)
            val taskList = command.arrayList.map { taskElement ->
                Task(taskElement.id, taskElement.name)
            }
            emit(taskList)
        }
    }

    override fun updateTask(taskId: Int, newName: String): Flow<Boolean> {
        return flow {
            val blockingStub = CrudServiceGrpc.newBlockingStub(channel)
            val request = TaskRequest.newBuilder().setId(taskId).setName(newName).build()
            val command = blockingStub.updateElementByID(request)
            emit(command != null)
        }
    }

    override fun deleteTask(id: Int): Flow<Boolean> {
        return flow {
            val blockingStub = CrudServiceGrpc.newBlockingStub(channel)
            val request = TaskRequest.newBuilder().setId(id).build()
            val command = blockingStub.removeElement(request)
            emit(command != null)
        }
    }

}
