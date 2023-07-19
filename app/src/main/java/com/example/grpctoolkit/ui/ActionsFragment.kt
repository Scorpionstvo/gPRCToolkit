package com.example.grpctoolkit.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.grpctoolkit.R
import com.example.grpctoolkit.data.Task
import com.example.grpctoolkit.databinding.FragmentActionsBinding
import com.example.grpctoolkit.viewmodel.ActionViewModel
import com.google.android.material.snackbar.Snackbar

class ActionsFragment : Fragment(R.layout.fragment_actions) {
    private val binding by viewBinding(FragmentActionsBinding::bind)
    private val viewModel: ActionViewModel by viewModels()
    private var adapter: ArrayAdapter<Task>? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initTaskAdapter()
        setupShowingEvents()
        initActions()
        binding.input.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                p0?.let {
                    if (p0.isNotEmpty()) binding.tiName.error = ""
                }
            }
        })
    }

    private fun initTaskAdapter() {

    }

    private fun setupShowingEvents() {
        viewModel.state.observe(viewLifecycleOwner) { event ->
            when (event) {
                is Event.CreateSuccessfully -> showSnackbar(
                    if (event.id == -1) resources.getString(
                        R.string.object_not_created
                    ) else "Объект ${event.id} успешно создан"
                )

                is Event.UpdateSuccessfully -> showSnackbar(resources.getString(R.string.object_update_success))
                is Event.UnSuccess -> showSnackbar(resources.getString(R.string.unsuccess_message))
                is Event.TaskList -> showTasks(event.list)
            }
        }
    }

    private fun initActions() {
        with(binding) {
            btnCreate.setOnClickListener {
                val name = input.text.toString()
                if (name.isNotEmpty()) viewModel.createTask(name)
                else showInputError()
            }

            btnRead.setOnClickListener { viewModel.readTask(0) }

            btnUpdate.setOnClickListener {
                val name = input.text.toString()
                if (name.isNotEmpty()) viewModel.updateTask(1, name)
                else showInputError()
            }

            btnDelete.setOnClickListener { viewModel.deleteTask(1) }
        }
    }

    private fun showInputError() {
        binding.tiName.error = resources.getString(R.string.input_empty_error_message)
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        )
        snackbar.setActionTextColor(Color.YELLOW)
        snackbar.show()
    }

    private fun showTasks(tasks: List<Task>) {
        binding.taskContainer.removeAllViews()
        for (task in tasks) {
            val taskTextView = TextView(requireContext())
            taskTextView.text = "ID: ${task.id} Name: ${task.name}"
            binding.taskContainer.addView(taskTextView)
        }
    }

}
