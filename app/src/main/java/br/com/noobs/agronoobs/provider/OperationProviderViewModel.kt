package br.com.noobs.agronoobs.provider

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.noobs.agronoobs.model.Operation

class OperationProviderViewModel : ViewModel() {

    private var listOperations: MutableLiveData<List<Operation>> = MutableLiveData()
    private var operationProvider: OperationProvider = OperationProvider()


    fun getListOperations(): MutableLiveData<List<Operation>> {
        operationProvider.searchOperations().addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                return@addSnapshotListener
            }

            val operations = mutableListOf<Operation>()
            snapshot?.documents?.forEach {
                val operation = it.toObject(Operation::class.java)
                operation?.let { it1 -> operations.add(it1) }
            }

            listOperations.value = operations
        }
        return listOperations
    }
}