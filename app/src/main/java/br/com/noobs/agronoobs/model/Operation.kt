package br.com.noobs.agronoobs.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Operation(
    val userId: String,
    @ServerTimestamp
    val createdDateTime: Date,
    var selectedLocation: String,
    var selectedOperation: Operations,
    var startPosition: Float,
    var imageStartPath: String,
    var finishPosition: Float,
    var imageFinishPath: String,
    var acquiredQuantity: Float?,
) {

    constructor() : this(
        "",
        Date(),
        "",
        Operations.HARVEST,
        0f,
        "",
        0f,
        "",
        0f
    )
}