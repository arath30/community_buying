package com.albertson.spark_poc.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.albertson.spark_poc.R
import com.albertson.spark_poc.databinding.FragmentViewInviteBottomSheetBinding
import com.albertson.spark_poc.presentation.viewmodel.ViewInviteBottomSheetViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ViewInviteBottomSheet : BottomSheetDialogFragment() {
    private var acceptListener: OnAcceptInviteListener? = null
    private lateinit var binding: FragmentViewInviteBottomSheetBinding
    private lateinit var viewModel: ViewInviteBottomSheetViewModel
    private var adminName = ""
    private var communityName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentViewInviteBottomSheetBinding.inflate(
            inflater, container, false
        )

        viewModel = ViewModelProvider(requireActivity())[ViewInviteBottomSheetViewModel::class.java]

        arguments?.let {
            adminName = it.getString(ADMIN_NAME).toString()
            communityName = it.getString(COMMUNITY_NAME).toString()
        }

        binding.apply {
            textView2.text = String.format(getString(R.string.community_invite_label), adminName, communityName)

            accept.setOnClickListener {
                acceptListener?.onAcceptInvite()
                dialog?.dismiss()
            }

            ivClose.setOnClickListener {
                dialog?.dismiss()
            }

            decline.setOnClickListener {
                acceptListener?.onRejectedInvite()
                dialog?.cancel()
            }
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog?.setOnShowListener { it ->
            val d = it as BottomSheetDialog
            val bottomSheet =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let {
                val behavior = BottomSheetBehavior.from(it)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        dialog?.setCancelable(false)
        return super.onCreateDialog(savedInstanceState)
    }

    fun setOnAcceptInviteListener(listener: OnAcceptInviteListener) {
        acceptListener = listener
    }

    companion object {
        const val ADMIN_NAME = "ADMIN_NAME"
        const val COMMUNITY_NAME = "COMMUNITY_NAME"
    }
}

interface OnAcceptInviteListener {
    fun onAcceptInvite()
    fun onRejectedInvite()
}