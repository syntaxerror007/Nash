package com.android.nash.core.yesnocheckbox

import android.content.Context
import android.graphics.Typeface
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.android.nash.R
import com.rengwuxian.materialedittext.MaterialEditText
import com.rengwuxian.materialedittext.MaterialEditText.FLOATING_LABEL_HIGHLIGHT

class MultiCheckCheckGroup : LinearLayout {
    private lateinit var multiCheckCheckGroupData: MultiCheckCheckGroupData
    private val titleTextView = TextView(context)
    private val optionsCheckBox = mutableListOf<CheckBox>()
    private val editTextAdditionalInfo = MaterialEditText(context)
    private val additionalTextInputLayout = TextInputLayout(context)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun initView() {
        this.orientation = VERTICAL
        titleTextView.text = multiCheckCheckGroupData.title
        titleTextView.setTypeface(null, Typeface.BOLD)
        titleTextView.setTextColor(ContextCompat.getColor(context, R.color.black))
        addView(titleTextView)
        multiCheckCheckGroupData.options.forEach {
            val checkBox = CheckBox(context)
            checkBox.text = it
            checkBox.setOnClickListener { view ->  handleCheck(view) }
            optionsCheckBox.add(checkBox)
            addView(checkBox)
        }

        editTextAdditionalInfo.setFloatingLabel(FLOATING_LABEL_HIGHLIGHT)
        editTextAdditionalInfo.floatingLabelText = resources.getString(R.string.text_please_add_addtional_info)
        editTextAdditionalInfo.hint = resources.getString(R.string.text_please_add_addtional_info)
        addView(editTextAdditionalInfo)
    }

    private fun handleCheck(checkedCheckbox: View) {
        if (!multiCheckCheckGroupData.enableMultiCheck) {
            optionsCheckBox.forEach {
                it.isChecked = it == checkedCheckbox
            }
        }
    }

    fun setMultiCheckData(multiCheckCheckGroupData: MultiCheckCheckGroupData) {
        this.multiCheckCheckGroupData = multiCheckCheckGroupData
        initView()
    }
}