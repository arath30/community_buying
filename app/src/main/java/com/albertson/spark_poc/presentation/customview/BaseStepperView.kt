package com.albertson.spark_poc.presentation.customview

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter

abstract class BaseStepperView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    protected var apiTime = 0
    var lastPressedButtonId: Int? = null
    var displayType: Int = -1
    var originalDisplayType: Int = -1
    var hasOriginalDisplayTypeBeenSet = false

    open var quantity = 0
        set(value) {
            field = value
            updateValues()
        }

    var maxQuantity = 10
        set(value) {
            field = if (value > 1) value else 1
            updateValues()
        }

    var stepperFor = ""
        set(value) {
            field = value
            updateValues()
        }

    var showToastMsg = false
        set(value) {
            field = value
            updateValues()
        }

    protected abstract fun updateValues(outsideTouch: Boolean = false)
    protected abstract fun readAttributes(attrs: AttributeSet?): Unit?
    protected abstract fun callApi()
    abstract fun onApiFinished()
    open fun onApiFinishedTalkBack(accessibilityText: String) {
        // No Operation
    }

    open fun callApiWithQuantity(quantityToUpdate: Int = -1, weightToUpdate: Float = -1f) {
        // No Operation
    }

    interface StepperListener {
        fun enableQuantityUpdate() = true
        fun onAmountUpdate(stepper: BaseStepperView, quantity: Int, weight: Float)
        fun onStepperVisibilityChange(isStepperVisible: Boolean) {
            // No Operation
        }

        fun showMtoQuantityBottomSheet() {}

        fun getCartCount(quantity: Int, maxQuantity: Int): Boolean = false
    }
}

@BindingAdapter("quantity")
fun setQuantity(view: BaseStepperView, value: Int) {
    view.quantity = value
}

@BindingAdapter("maxQuantity")
fun setMaxQuantity(view: BaseStepperView, value: Int) {
    view.maxQuantity = value
}

@BindingAdapter("displayType")
fun setDisplayType(view: BaseStepperView, display: Int) {
    view.displayType = display
    if (view.hasOriginalDisplayTypeBeenSet.not()) {
        view.originalDisplayType = display
        view.hasOriginalDisplayTypeBeenSet = true
    }
}

@BindingAdapter("stepperFor")
fun setStepperFor(view: BaseStepperView, value: String?) {
    view.stepperFor = value ?: ""
}

@BindingAdapter("showToast")
fun setShowToast(view: BaseStepperView, value: Boolean?) {
    view.showToastMsg = value ?: false
}
