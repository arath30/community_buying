package com.albertson.spark_poc.presentation.customview

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.albertson.spark_poc.R
import com.albertson.spark_poc.databinding.CustomStepperViewBinding


class CustomStepperView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : BaseStepperView(context, attrs, defStyleAttr) {

    var quantityUpdateListener: QuantityUpdateListener? = null
    var closeDialogListner: CloseDialogListener? = null
    var quantityBtnClickListener: QuantityButtonClickListener? = null

    private var primaryColor = ContextCompat.getColor(context, R.color.secondary)
    private var disabledColor = ContextCompat.getColor(context, android.R.color.darker_gray)
    private var loading = false
    private var collapsedWidth = 0
    private var expandedWidth = 0
    private var isExpanded = false

    var touchEnable = true

    var position = 0

    var outsideTouch = false
        set(value) {
            field = value
            if (value) {
                dismiss()
            }
        }

    var binding: CustomStepperViewBinding

    init {
        val inflater: LayoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.custom_stepper_view,
            this,
            true,
        )
        readAttributes(attrs)
        loading = false
        initViews()
        updateValues()
    }

    override fun readAttributes(attrs: AttributeSet?) = attrs?.let {
        val typedArray = context.obtainStyledAttributes(it, R.styleable.StepperView, 0, 0)
        apiTime = typedArray.getInt(R.styleable.BaseStepperView_apiTime, 0)
        quantity = typedArray.getInt(R.styleable.BaseStepperView_quantity, 1)
        maxQuantity = try {
            typedArray.getInt(R.styleable.BaseStepperView_maxQuantity, 10)
        } catch (e: NumberFormatException) {
            10
        }
        displayType = typedArray.getInt(R.styleable.BaseStepperView_displayType, -1)

        collapsedWidth = typedArray.getDimensionPixelOffset(
            R.styleable.StepperView_collapsedWidth,
            110,
        )

        expandedWidth = typedArray.getDimensionPixelOffset(
            R.styleable.StepperView_expandedWidth,
            300,
        )

        updateValues()
        typedArray.recycle()
    }

    override fun callApi() {
        TODO("Not yet implemented")
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initViews() {
        binding.clStepper.apply {
            setBackgroundResource(R.drawable.bg_stepper_rounded)
            layoutParams.width = collapsedWidth
            layoutParams.height = collapsedWidth
            requestLayout()
        }

        binding.clStepper.setOnClickListener {
            if (isExpanded.not() && touchEnable) {
                expand()
            } else {
                collapse()
            }
        }

        binding.ivStepperPlus.setOnClickListener {
            lastPressedButtonId = it.id
            if (quantity < maxQuantity) {
                quantity++
                quantityUpdateListener?.onQuantityUpdate(true)
            }
            quantityBtnClickListener?.onPlusButtonClick(quantity)
            updateStepperStates()
        }

        binding.ivStepperMinus.setOnClickListener {
            lastPressedButtonId = it.id
            closeDialogListner?.closeDialog(quantity)
            if (quantity > 0) {
                quantity--
                quantityUpdateListener?.onQuantityUpdate(false)
            }
            updateStepperStates()
            quantityBtnClickListener?.onMinusButtonClick(quantity)
        }

        binding.ivStepperPlus.apply {
            isClickable = true
        }
        binding.ivStepperMinus.apply {
            isClickable = true
        }
    }

    private fun updateStepperStates() {
        val isItemNotExistInCart = quantity == 1

        val maxQuantityReached = quantity == maxQuantity
        when {
            isItemNotExistInCart -> {
                binding.ivStepperPlus.isEnable = true
                //binding.ivStepperMinus.isEnable = false    //for showing delete icon
            }

            maxQuantityReached -> {
                binding.ivStepperPlus.isEnable = false
                binding.ivStepperMinus.isEnable = true
            }

            else -> {
                binding.ivStepperPlus.isEnable = true
                binding.ivStepperMinus.isEnable = true
            }
        }
    }

    override fun onApiFinished() {
    }

    /**
     * Update the text
     */
    override fun updateValues(outsideTouch: Boolean) {
        if (quantity > 0) {
            binding.tvStepperQuantity.apply {
                text = quantity.toString()
            }
            if (quantity == 1) {
                binding.ivStepperMinus.setImageResource(R.drawable.ic_delete)
            } else {
                binding.ivStepperMinus.setImageResource(R.drawable.ic_remove)
            }
            updateStepperStates()
        } else {
            quantityUpdateListener?.onRemoveItem(position)
            // product remove from list
        }
    }

    /**
     * Method to call listeners quantity update with new quantity
     * in case of api failure
     * */
    override fun callApiWithQuantity(quantityToUpdate: Int, weightToUpdate: Float) {
    }

    fun setListener(listener: QuantityUpdateListener) {
        quantityUpdateListener = listener
    }

    fun setCloseListener(listener: CloseDialogListener) {
        closeDialogListner = listener
    }


    fun setQtyButtonClickListener(listener: QuantityButtonClickListener) {
        quantityBtnClickListener = listener
    }

    private var AppCompatImageView.isEnable: Boolean
        get() {
            return isEnabled
        }
        set(value) {
            isEnabled = value
            imageTintList = if (value) {
                ColorStateList.valueOf(primaryColor)
            } else {
                ColorStateList.valueOf(disabledColor)
            }
        }

    private fun expand() {
        binding.clStepper.changeSize(true)
        isExpanded = true
    }

    private fun collapse() {
        isExpanded = false
        binding.clStepper.changeSize(false)
    }

    private fun View.changeSize(expand: Boolean) {
        var newWidth = collapsedWidth
        if (expand) {
            newWidth = expandedWidth
        }
        layoutParams.width = newWidth
        changeStepperState(expand)
    }

    private fun changeStepperState(isExpanded: Boolean) {
        binding.ivStepperMinus.isVisible = isExpanded
        binding.ivStepperPlus.isVisible = isExpanded

        binding.ivStepperPlus.apply {
            isClickable = isExpanded
        }
        binding.ivStepperMinus.apply {
            isClickable = isExpanded
        }
    }

    private fun dismiss() {
        if (isExpanded) {
            collapse()
        }
    }

}

interface QuantityUpdateListener {
    fun onQuantityUpdate(isIncremented: Boolean)
    fun onRemoveItem(position: Int)
}

interface QuantityButtonClickListener {
    fun onPlusButtonClick(quantity: Int)
    fun onMinusButtonClick(quantity: Int)
}

interface CloseDialogListener {
    fun closeDialog(quantity: Int)
}
