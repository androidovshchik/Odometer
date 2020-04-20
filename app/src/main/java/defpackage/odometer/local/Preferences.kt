package defpackage.odometer.local

import android.content.Context
import com.chibatching.kotpref.KotprefModel

class Preferences(context: Context) : KotprefModel(context) {

    override val kotprefName = "${context.packageName}_preferences"

    var trainNumber by nullableStringPref(null, "train_number")

    var mainSpeed by nullableStringPref(null, "main_speed")

    var conditionalWidth by nullableStringPref(null, "conditional_width")
}