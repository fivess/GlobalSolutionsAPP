package br.com.noobs.agronoobs.dto

import br.com.noobs.agronoobs.model.Operation
import br.com.noobs.agronoobs.model.Operations
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

data class CreateOperationDto(
    val userId: String,
    val createdDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("UTC")),
    var selectedLocation: String? = null,
    var selectedOperation: Operations? = null,
    var startPosition: Float? = null,
    var imageStart: ByteArray? = null,
    var finishPosition: Float? = null,
    var imageFinish: ByteArray? = null,
    var acquiredQuantity: Float? = null,

    ) {
    fun isDefineLocationValid(): Boolean {
        return !selectedLocation.isNullOrEmpty() && selectedOperation != null
    }

    fun isDefineStartValid(): Boolean {
        return startPosition != null && imageStart != null
    }

    fun isDefineFinishValid(): Boolean {
        return finishPosition != null && imageFinish != null
    }

    fun isAcquiredQuantityValid(): Boolean {
        return acquiredQuantity != null
    }

    fun buildImageStartUri(): String {
        return "images/$userId/$createdDateTime/start"
    }

    fun buildImageFinishUri(): String {
        return "images/$userId/$createdDateTime/finish"
    }

    fun toModel(): Operation {
        return Operation(
            userId = userId,
            createdDateTime = Date(createdDateTime.toInstant().toEpochMilli()),
            selectedLocation = selectedLocation!!,
            selectedOperation = selectedOperation!!,
            startPosition = startPosition!!,
            imageStartPath = buildImageStartUri(),
            finishPosition = finishPosition!!,
            imageFinishPath = buildImageFinishUri(),
            acquiredQuantity = acquiredQuantity
        )
    }


}