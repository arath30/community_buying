package com.albertson.spark_poc.presentation.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.navigation.fragment.findNavController
import com.albertson.spark_poc.R
import com.albertson.spark_poc.data.local.entity.Community
import com.albertson.spark_poc.data.local.entity.Member
import com.albertson.spark_poc.databinding.BottomsheetDeleteCommunityConfirmationBinding
import com.albertson.spark_poc.databinding.BottomsheetDeleteConfirmationBinding
import com.albertson.spark_poc.databinding.BottomsheetSuccessBinding
import com.albertson.spark_poc.presentation.viewmodel.MemberViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DeleteCommunityBottomSheetFragment: BottomSheetDialogFragment() {
    lateinit var binding: BottomsheetDeleteCommunityConfirmationBinding
    private var deleteListener: OnCommunityDeleteListener? = null
    private var community: Community? = null
    private var position: Int? = null

    fun setOnDeleteMemberListener(position: Int,community: Community,listener: OnCommunityDeleteListener){
        this.community = community
        this.position = position
        deleteListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding = BottomsheetDeleteCommunityConfirmationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnDelete.setOnClickListener {
                community?.let { it1 -> position?.let { it2 -> deleteListener?.onDelete(it1, it2) } }
                dismiss()
//                arguments?.apply { putBoolean(ProductListFragment.PRODUCT_ADDED, true) }
//                findNavController().navigate(
//                    R.id.action_homeFragment_to_myCommunityDashBoard,
//                    arguments
//                )
            }

            ivClose.setOnClickListener {
                dismiss()
            }

            tvCancel.setOnClickListener {
                dismiss()
            }

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setContentView(R.layout.bottomsheet_add_product)
            setOnShowListener {
                val bottomSheetView =
                    (it as? BottomSheetDialog)?.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                        ?: return@setOnShowListener
                BottomSheetBehavior.from(bottomSheetView).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                    skipCollapsed = true
                }
            }
        }
    }
}

interface OnCommunityDeleteListener{
    fun onDelete(community: Community,position: Int)
}