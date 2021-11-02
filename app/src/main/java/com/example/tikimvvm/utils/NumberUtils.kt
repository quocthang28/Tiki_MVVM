package com.example.tikimvvm.utils

import android.icu.text.NumberFormat
import java.util.*

class NumberUtils private constructor() {
    companion object {
        fun formatCurrency(price: Int?): String {
            val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                NumberFormat.getCurrencyInstance(Locale.US)
            } else {
                TODO("VERSION.SDK_INT < N")
            }
            return format.format(price).run { this.subSequence(1, this.length - 3).toString() + "đ" }
        }
    }
}