package com.albertson.spark_poc.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.room.util.query
import com.albertson.spark_poc.data.local.entity.ProductCommunityUser
import com.albertson.spark_poc.databinding.AdapterCartProductListBinding
import com.albertson.spark_poc.presentation.customview.QuantityButtonClickListener
import com.albertson.spark_poc.presentation.customview.QuantityUpdateListener

class CartProductListAdapter(
    private val isAdmin: Boolean,
    private val memberId: Int,
    private val productList: List<ProductCommunityUser>, onClickListener: OnClickEvent
) : RecyclerView.Adapter<CartItemViewHolder>() {

    private lateinit var binding: AdapterCartProductListBinding
    private var listener = onClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        binding = AdapterCartProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        holder.binds(isAdmin, productList[position], position, listener, memberId)

    }

    override fun getItemCount(): Int {
        return productList.size
    }
}

class CartItemViewHolder(private val binding: AdapterCartProductListBinding) : ViewHolder(binding.root) {
    fun binds(isAdmin: Boolean,productState: ProductCommunityUser, position: Int, listener: OnClickEvent, memberId: Int) {
        binding.product = productState
        binding.position = position
        binding.tvDiscountedPrice.paintFlags = android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
        binding.isAdmin = isAdmin

        if (isAdmin) {
            binding.touchEnable = true
        } else {
            binding.touchEnable = productState.member_id == memberId
        }

        binding.cvMainView.setOnClickListener{
            binding.outsideTouch = true
        }

        binding.ivRemoveProduct.setOnClickListener {
            listener.onRemove(position)
        }

        binding.tvPrice.text =  "$" + String.format("%.2f",
            (productState.product_discount_price.toDouble().times(productState.product_quantity)))

        binding.stepperView.quantityUpdateListener = object : QuantityUpdateListener {
            override fun onQuantityUpdate(isIncremented: Boolean) {

            }

            override fun onRemoveItem(position: Int) {
                listener.onRemove(position)
            }
        }

        binding.stepperView.quantityBtnClickListener = object : QuantityButtonClickListener {
            override fun onPlusButtonClick(quantity: Int) {
                val updatedPrice = productState.product_discount_price.toDouble().times(quantity)

                binding.tvPrice.text =  "$" + String.format("%.2f", updatedPrice)

                listener.onQuantityAdd(productState, quantity)
            }

            override fun onMinusButtonClick(quantity: Int) {
                if (quantity > 0) {
                    val updatedPrice =
                        productState.product_discount_price.toDouble().times(quantity)

                    binding.tvPrice.text = "$" + String.format("%.2f", updatedPrice)

                    listener.onQuantityMinus(productState, quantity)
                }
            }
        }

        binding.executePendingBindings()
    }
}

interface OnClickEvent {
    fun onClick(product: ProductCommunityUser)
    fun onRemove(position: Int)
    fun onQuantityAdd(product: ProductCommunityUser, quantity: Int)
    fun onQuantityMinus(product: ProductCommunityUser, quantity: Int)
}
