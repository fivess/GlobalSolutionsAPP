package br.com.noobs.agronoobs.provider

import br.com.noobs.agronoobs.dto.CreateOperationDto
import br.com.noobs.agronoobs.model.Entity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage

class OperationProvider(
    private val fireStore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val fireStorage: FirebaseStorage = FirebaseStorage.getInstance(),
    private val user: FirebaseUser = FirebaseAuth.getInstance().currentUser!!
) {

    fun createOperation(
        createOperationDto: CreateOperationDto,
        successListener: () -> Unit,
        failureListener: () -> Unit

    ) {
        fireStorage.reference.child(createOperationDto.buildImageStartUri())
            .putBytes(createOperationDto.imageStart!!)
            .addOnSuccessListener {
                fireStorage.reference.child(createOperationDto.buildImageFinishUri())
                    .putBytes(createOperationDto.imageFinish!!)
                    .addOnSuccessListener {
                        saveOperation(createOperationDto, successListener, failureListener)
                    }
                    .addOnFailureListener {
                        failureListener()
                    }
            }
            .addOnFailureListener {
                failureListener()
            }

    }

    fun searchOperations(): Query {
        return fireStore.collection(Entity.OPERATION.name)
            .where(Filter.equalTo("userId", user.uid))
    }

    private fun saveOperation(
        createOperationDto: CreateOperationDto,
        successListener: () -> Unit,
        failureListener: () -> Unit
    ) {
        fireStore.collection(Entity.OPERATION.name)
            .add(createOperationDto.toModel())
            .addOnSuccessListener {
                successListener()
            }
            .addOnFailureListener {
                failureListener()
            }
    }
}