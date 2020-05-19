package com.kireev.shop

import android.text.SpannableString
import android.text.style.StrikethroughSpan
import java.text.DecimalFormat

/**
 * returns spannable with strikethrough text
 */
val String.strikethroughSpannable: SpannableString
    get() {
        val spannable = SpannableString(this)
        spannable.setSpan(StrikethroughSpan(), 0, this.length, 0)
        return spannable
    }

/**
 * price in common format for catalog, detailed etc
 */
val Double.commonPriceFormat: String
    get() {
        val currencySign = "₽"
        val formatted = DecimalFormat("#,###")
            .format(this)
            .replace(",", " ")
        return "$formatted $currencySign"
    }

/**
 * price with two decimal numbers for order checkout
 */
val Double.checkoutPriceFormat: String
    get() {
        val currencySign = "₽"
        val formattedPrice = if (this > 0.0) {
            DecimalFormat("#,###.00").format(this)
        } else "0"
        return "$formattedPrice $currencySign"
    }