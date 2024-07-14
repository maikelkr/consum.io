package com.example.consumapp.model

import android.os.Parcelable
import com.example.consumapp.helper.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class VehicleModel(
    var id : String = "",
    var vehicleModel : String = "",
    var vehicleBrand : String = "",
    var vehicleCategory : String = "",
    var vehicleAge : String = "",
    var kmActual : String = "",
    var gasConsum : String = "",
    var gasSize : String = ""
) : Parcelable {
    init {
        this.id = FirebaseHelper.getDataBase().push().key ?: ""
    }
}
