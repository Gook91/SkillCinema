package com.example.skillcinema.presentation.screen.abstractTemplates

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.skillcinema.presentation.dialogs.ErrorDialogFragment
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class AbstractFragmentWithErrorMessageAndInsets : Fragment() {
    protected abstract val viewModel: AbstractViewModelWithErrorChannel
    private var isMessageNotShow = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.isErrorChannel.filter { it }.onEach {
            if (isMessageNotShow) {
                ErrorDialogFragment().show(childFragmentManager, "Error dialog")
                isMessageNotShow = false
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)

        updateTopPaddingFromInsets(view)
    }

    protected open fun updateTopPaddingFromInsets(rootView: View) {
        val actionBarHeight = getActionBarHeight()
        activity?.window?.decorView?.let {
            ViewCompat.setOnApplyWindowInsetsListener(it) { _, insets ->
                val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top
                rootView.updatePadding(top = actionBarHeight + statusBarHeight)
                insets
            }
        }
    }

    protected open fun getActionBarHeight(): Int {
        val tv = TypedValue()
        return if (requireActivity().theme.resolveAttribute(
                android.R.attr.actionBarSize,
                tv,
                true
            )
        ) {
            TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        } else 0
    }
}