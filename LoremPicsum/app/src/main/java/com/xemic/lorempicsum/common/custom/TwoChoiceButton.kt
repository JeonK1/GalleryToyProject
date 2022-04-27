package com.xemic.lorempicsum.common.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.xemic.lorempicsum.R
import com.xemic.lorempicsum.databinding.CustomTwoChoiceButtonBinding

// 둘 중 하나만 고를 수 있는 버튼
class TwoChoiceButton(context: Context, attrs: AttributeSet): ConstraintLayout(context, attrs) {
    companion object {
        lateinit var typedArray: TypedArray
        lateinit var binding: CustomTwoChoiceButtonBinding
        const val CHOICE_LEFT = 0
        const val CHOICE_RIGHT = 1
        var currentState = CHOICE_LEFT
    }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_two_choice_button, this)
        binding = CustomTwoChoiceButtonBinding.bind(view)

        typedArray = context.obtainStyledAttributes(attrs, R.styleable.TwoChoiceButton)
        initButtonText()
        invalidateUI(currentState)
    }

    private fun initButtonText() {
        binding.tcbLeft.text = typedArray.getString(R.styleable.TwoChoiceButton_leftButtonText)
        binding.tcbRight.text = typedArray.getString(R.styleable.TwoChoiceButton_rightButtonText)
    }

    fun invalidateUI(_currentState: Int) {
        currentState = _currentState
        when (currentState) {
            CHOICE_LEFT -> {
                Log.e("test","left")
                // Colored 활성화, Grayscale 비활성화
                binding.tcbLeft.backgroundTintList = typedArray.getColorStateList(R.styleable.TwoChoiceButton_leftButtonSelectedBackgroundTint)
                binding.tcbLeft.setTextColor(typedArray.getColorStateList(R.styleable.TwoChoiceButton_leftButtonSelectedTextColor))
                binding.tcbRight.backgroundTintList = typedArray.getColorStateList(R.styleable.TwoChoiceButton_rightButtonBackgroundTint)
                binding.tcbRight.setTextColor(typedArray.getColorStateList(R.styleable.TwoChoiceButton_rightButtonTextColor))
            }
            CHOICE_RIGHT -> {
                Log.e("test","right")
                // Colored 비활성화, Grayscale 활성화
                binding.tcbLeft.backgroundTintList = typedArray.getColorStateList(R.styleable.TwoChoiceButton_leftButtonBackgroundTint)
                binding.tcbLeft.setTextColor(typedArray.getColorStateList(R.styleable.TwoChoiceButton_leftButtonTextColor))
                binding.tcbRight.backgroundTintList = typedArray.getColorStateList(R.styleable.TwoChoiceButton_rightButtonSelectedBackgroundTint)
                binding.tcbRight.setTextColor(typedArray.getColorStateList(R.styleable.TwoChoiceButton_rightButtonSelectedTextColor))
            }
        }
    }

    fun setOnLeftButtonClickListener(l: OnClickListener?){
        l?.apply {
            binding.tcbLeft.setOnClickListener(l)
        }
    }

    fun setOnRightButtonClickListener(l: OnClickListener?){
        l?.apply {
            binding.tcbRight.setOnClickListener(l)
        }
    }
}