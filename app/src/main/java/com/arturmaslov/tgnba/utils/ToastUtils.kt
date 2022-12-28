package com.arturmaslov.tgnba.utils

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty

object ToastUtils {

    private var mainToast: Toast? = null

    fun updateShort(ctx: Context?, text: String) {
        mainToast = Toasty.normal(ctx!!, text, Toast.LENGTH_SHORT)
        mainToast?.show()
    }

    fun updateError(ctx: Context?, text: String) {
        mainToast = Toasty.error(ctx!!, text, Toast.LENGTH_SHORT)
        mainToast?.show()
    }

    fun updateSuccess(ctx: Context?, text: String) {
        mainToast = Toasty.success(ctx!!, text, Toast.LENGTH_SHORT)
        mainToast?.show()
    }

    fun updateInfo(ctx: Context?, text: String) {
        mainToast = Toasty.info(ctx!!, text, Toast.LENGTH_SHORT)
        mainToast?.show()
    }

    fun updateWarning(ctx: Context?, text: String) {
        mainToast = Toasty.warning(ctx!!, text, Toast.LENGTH_SHORT)
        mainToast?.show()
    }

    fun updateNormal(ctx: Context?, text: String) {
        mainToast = Toasty.normal(ctx!!, text, Toast.LENGTH_SHORT)
        mainToast?.show()
    }

    fun updateLong(ctx: Context?, text: String) {
        mainToast = Toasty.normal(ctx!!, text, Toast.LENGTH_LONG)
        mainToast?.show()
    }
}