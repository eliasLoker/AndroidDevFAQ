package com.example.androiddevfaq.ui.addquestion

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.androiddevfaq.App
import com.example.androiddevfaq.R
import com.example.androiddevfaq.api.FakeApiImpl
import com.example.androiddevfaq.databinding.FragmentAddQuestionBinding
import com.example.androiddevfaq.ui.addquestion.event.ShowAddResultDialogEvents
import com.example.androiddevfaq.ui.addquestion.interactor.AddQuestionInteractor
import com.example.androiddevfaq.ui.addquestion.viewmodel.AddQuestionFactory
import com.example.androiddevfaq.ui.addquestion.viewmodel.AddQuestionViewModel
import com.example.androiddevfaq.ui.addquestion.viewmodel.AddQuestionViewModelImpl
import com.example.androiddevfaq.ui.base.BaseFragment
import com.example.androiddevfaq.utils.onTextChanged

class AddQuestionFragment(
    layoutID: Int = R.layout.fragment_add_question
) : BaseFragment<FragmentAddQuestionBinding>(layoutID, FragmentAddQuestionBinding::inflate) {

    private lateinit var addQuestionViewModel: AddQuestionViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val interactor = AddQuestionInteractor(App.getApi())
        val categoryID = arguments?.getInt(TAG_FOR_CATEGORY_ID, 0) ?: 0
        val categoryName = arguments?.getString(TAG_FOR_CATEGORY_NAME, "") ?: ""
        val factory = AddQuestionFactory(categoryID, categoryName, interactor)
        addQuestionViewModel = ViewModelProviders
            .of(this, factory)
            .get(AddQuestionViewModelImpl::class.java)
        addQuestionViewModel.onActivityCreated()
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.questionInputEditText.onTextChanged {
            addQuestionViewModel.onQuestionTextChanged(it)
        }

        binding.answerEditText.onTextChanged {
            addQuestionViewModel.onAnswerTextChanged(it)
        }

        binding.sendQuestionButton.setOnClickListener {
            addQuestionViewModel.onSendQuestionButtonClicked()
        }
    }

    private fun initObservers() {
        addQuestionViewModel.setToolbar.observe(viewLifecycleOwner, {
            binding.createQuestionToolbar.toolbar.apply {
                title = it
                subtitle = "Добавить вопрос"
            }
        })

        addQuestionViewModel.showAddResultDialogEvents.observe(viewLifecycleOwner, {
            when (it) {
                is ShowAddResultDialogEvents.Success -> showSuccessDialog(it.message)

                is ShowAddResultDialogEvents.Error -> showErrorDialog(it.errorMessage)
            }
        })

        addQuestionViewModel.goToBack.observe(viewLifecycleOwner, {
            findNavController().popBackStack()
        })
    }

    private fun showSuccessDialog(message: String) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Вопрос добавлен!")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok") { i1, _ ->
                i1.dismiss()
                addQuestionViewModel.onDialogPositiveButtonClicked()
            }
        dialog.show()
    }

    private fun showErrorDialog(message: String) {
        val dialog = AlertDialog.Builder(context)
            .setTitle("Вопрос не добавлен!")
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Ok") { i1, _ ->
                i1.dismiss()
                addQuestionViewModel.onDialogPositiveButtonClicked()
            }
        dialog.show()
    }

    companion object {

        private const val TAG_FOR_CATEGORY_ID = "TAG_FOR_CATEGORY_ID"
        private const val TAG_FOR_CATEGORY_NAME = "TAG_FOR_CATEGORY_NAME"

        @JvmStatic
        fun getBundle(categoryID: Int, categoryName: String) = Bundle().apply {
            putInt(TAG_FOR_CATEGORY_ID, categoryID)
            putString(TAG_FOR_CATEGORY_NAME, categoryName)
        }
    }
}