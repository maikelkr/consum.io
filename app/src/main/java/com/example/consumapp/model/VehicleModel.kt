package com.example.consumapp.model

import android.os.Parcelable
import com.example.consumapp.helper.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class VehicleModel(
    var id : Int = 0,
    var key : String = "",
    var model : String = "",
    var brand : String = "",
    var category : String = "",
    var age : String = "",
    var kmActual : String = "",
    var consum : String = "",
    var gasSize : String = ""
) : Parcelable {
    init {
        this.key = FirebaseHelper.getDataBase().push().key ?: ""
    }
}
