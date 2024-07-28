package com.example.skillcinema.presentation.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.skillcinema.R
import com.example.skillcinema.databinding.DialogNewCollectionBinding

class AddNewCollectionDialog : DialogFragment() {
    private var _binding: DialogNewCollectionBinding? = null
    private val binding get() = _binding!!

    override fun getTheme(): Int = R.style.AppDialogTheme


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNewCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.closeButton.setOnClickListener {
            dismiss()
        }
        binding.doneButton.setOnClickListener {
            val newCollectionName = binding.newCollectionName.text.toString()
            if (newCollectionName.isBlank())
                Toast.makeText(
                    requireContext(),
                    getString(R.string.name_new_collection_is_not_blank),
                    Toast.LENGTH_SHORT
                ).show()
            else {
                setFragmentResult(
                    GET_NEW_COLLECTION_NAME_TAG,
                    bundleOf(RETURN_NAME_COLLECTION_TAG to newCollectionName)
                )
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val GET_NEW_COLLECTION_NAME_TAG = "new_collection_dialog"
        const val RETURN_NAME_COLLECTION_TAG = "name_collection"
    }
}