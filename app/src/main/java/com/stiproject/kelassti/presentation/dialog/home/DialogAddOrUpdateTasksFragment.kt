package com.stiproject.kelassti.presentation.dialog.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.stiproject.kelassti.data.model.request.TasksRequest
import com.stiproject.kelassti.data.model.response.dosen.DosenData
import com.stiproject.kelassti.databinding.FragmentDialogAddOrUpdateTasksBinding
import java.util.Calendar
import com.stiproject.kelassti.R

class DialogAddOrUpdateTasksFragment : DialogFragment(R.layout.fragment_dialog_add_or_update_tasks) {

    private var _binding: FragmentDialogAddOrUpdateTasksBinding? = null
    private val binding get() = _binding!!

    private val dialogHomeViewModel: DialogHomeViewModel by activityViewModels()

    private lateinit var spinnerAdapter: ArrayAdapter<DosenData>
    private var pickedDosenData: DosenData? = null
    private var pickedDeadline: String? = null

    private lateinit var bindingButtonDeadlineTask: Button
    private lateinit var bindingTitleTaskInput: EditText
    private lateinit var bindingDeskripsiTaskInput: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDialogAddOrUpdateTasksBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = arguments
        val tasksId = args?.getInt("tasksId")

        dialogHomeViewModel.getAllDosen()
        dialogHomeViewModel.initelize(tasksId)

        bindingTitleTaskInput = binding.editTextTitleTugas
        bindingDeskripsiTaskInput = binding.editTextDeskripsiTugas
        bindingButtonDeadlineTask = binding.buttonDeadlineTugas

        val spinnerNamaDosen = binding.spinnerDosenName

        spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArrayList<DosenData>()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerNamaDosen.adapter = it
        }

        bindingButtonDeadlineTask.setOnClickListener{
            showDatePickerDialog()
        }

        dialogHomeViewModel.dialogHomeState.observe(viewLifecycleOwner){
            when(it){
                is DialogHomeViewModel.DialogHomeState.ApiFailed -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }
                is DialogHomeViewModel.DialogHomeState.ApiPostTasksSuccess -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    dismiss()
                }
                is DialogHomeViewModel.DialogHomeState.ValidationDataFailed -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                }

                is DialogHomeViewModel.DialogHomeState.ApiGetTasksSuccess -> {
                    val tasksData = it.data
                    binding.progressBarAddOrUpdateTasks.visibility = View.INVISIBLE
                    binding.constraintLayoutMainAddOrUpdateTasks.visibility = View.VISIBLE
                    if(tasksData != null){
                        bindingTitleTaskInput.setText(tasksData.title)
                        bindingDeskripsiTaskInput.setText(tasksData.description)
                        pickedDeadline = tasksData.deadline // TODO: ini di cek apakah benar format date nya
                        binding.textViewTambahkanTugas.text = "Update"

                        binding.textViewTambahkanTugas.setOnClickListener{
                            dialogHomeViewModel.updateTasksById(tasksData.id, createTasksRequest())
                        }

                    } else {
                        binding.textViewTambahkanTugas.setOnClickListener{
                            dialogHomeViewModel.createTasks(createTasksRequest())
                        }
                    }
                }
                DialogHomeViewModel.DialogHomeState.Loading -> {
                    binding.progressBarAddOrUpdateTasks.visibility = View.VISIBLE
                    binding.constraintLayoutMainAddOrUpdateTasks.visibility = View.INVISIBLE
                }
            }
        }

        dialogHomeViewModel.dosenState.observe(viewLifecycleOwner){
            spinnerAdapter.clear()
            it?.forEach {
                spinnerAdapter.addAll(it)
            }
            spinnerAdapter.notifyDataSetChanged()
        }

        spinnerNamaDosen.onItemSelectedListener = object : AdapterView. OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                pickedDosenData = parent?.getItemAtPosition(position) as DosenData?
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    fun createTasksRequest() = TasksRequest(
        pickedDosenData?.id ?: 0,
        bindingDeskripsiTaskInput.text.toString(),
        bindingTitleTaskInput.text.toString(),
        pickedDeadline ?: ""
    )

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                pickedDeadline = String.format(
                    "%d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1,
                    selectedDay
                )
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            950
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        dialogHomeViewModel.setHomeStateBackToLoading()
        _binding = null
    }

    companion object{
        fun newInstance(tasksId: Int): DialogAddOrUpdateTasksFragment{
            val args = Bundle()
            val fragment = DialogAddOrUpdateTasksFragment()
            args.putInt("tasksId",tasksId)
            fragment.arguments = args
            return fragment
        }
    }
}
