package com.example.androiddevfaq.ui.addquestion

import android.app.AlertDialog
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.androiddevfaq.App
import com.example.androiddevfaq.R
import com.example.androiddevfaq.base.BaseFragment
import com.example.androiddevfaq.base.observe
import com.example.androiddevfaq.databinding.FragmentAddQuestionBinding
import com.example.androiddevfaq.ui.addquestion.event.AddQuestionNavigationEvents
import com.example.androiddevfaq.ui.addquestion.interactor.AddQuestionInteractor
import com.example.androiddevfaq.ui.addquestion.viewmodel.AddQuestionFactory
import com.example.androiddevfaq.ui.addquestion.viewmodel.AddQuestionViewModel
import com.example.androiddevfaq.utils.onTextChanged
import com.example.androiddevfaq.utils.popBackStack

class AddQuestionFragment(
    layoutID: Int = R.layout.fragment_add_question
) : BaseFragment<FragmentAddQuestionBinding>(layoutID, FragmentAddQuestionBinding::inflate) {

    private lateinit var addQuestionViewModel: AddQuestionViewModel

    private val stateObserver = Observer<AddQuestionViewModel.ViewState> {
        binding.apply {
            progressBar.isVisible = it.progressBarVisibility
            answerInputLayout.isVisible = it.answerEditTextVisibility
            questionInputLayout.isVisible = it.questionEditTextVisibility
            sendQuestionButton.isVisible = it.sendQuestionButtonVisibility
            createQuestionToolbar.toolbar.apply {
                title = it.titleText
                subtitle = it.subtitleText
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val title = requireContext().resources.getString(R.string.add_answer_title)
        val interactor = AddQuestionInteractor(App.getApi())
        val categoryID = arguments?.getInt(TAG_FOR_CATEGORY_ID, 0) ?: 0
        val categoryName = arguments?.getString(TAG_FOR_CATEGORY_NAME, "") ?: ""
        val factory = AddQuestionFactory(title, categoryID, categoryName, interactor)
        addQuestionViewModel = ViewModelProviders
            .of(this, factory)
            .get(AddQuestionViewModel::class.java)
        observe(addQuestionViewModel.stateLiveData, stateObserver)
        initListeners()
        addQuestionViewModel.onActivityCreated(savedInstanceState == null)
        addQuestionViewModel.navigationEvents.observe(viewLifecycleOwner, {
            when (it) {
                is AddQuestionNavigationEvents.ShowSuccessDialog -> showSuccessDialog(it.message)
                is AddQuestionNavigationEvents.ShowFailureDialog -> showErrorDialog(it.failureMessage)
                is AddQuestionNavigationEvents.GoToBack -> popBackStack()
            }
        })
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

    private fun showSuccessDialog(message: String?) {
        val title = requireContext().resources.getString(R.string.add_answer_success_add_title)
        val finalMessage = message
            ?: requireContext().resources.getString(R.string.add_answer_success_add)
        val ok = requireContext().resources.getString(R.string.ok)

        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(finalMessage)
            .setCancelable(false)
            .setPositiveButton(ok) { i1, _ ->
                i1.dismiss()
                addQuestionViewModel.onDialogPositiveButtonsClicked()
            }
        dialog.show()
    }

    private fun showErrorDialog(message: String?) {
        val title = requireContext().resources.getString(R.string.add_answer_error_add_title)
        val finalMessage = message
            ?: requireContext().resources.getString(R.string.add_answer_error_add_message)
        val ok = requireContext().resources.getString(R.string.ok)

        val dialog = AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(finalMessage)
            .setCancelable(false)
            .setPositiveButton(ok) { i1, _ ->
                i1.dismiss()
                addQuestionViewModel.onDialogPositiveButtonsClicked()
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