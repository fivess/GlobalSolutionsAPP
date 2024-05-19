package br.com.noobs.agronoobs.ui.operation.create

import androidx.lifecycle.ViewModel
import br.com.noobs.agronoobs.model.Operations

class CreateOperationViewModel : ViewModel() {

    private var _locationsOptions = listOf("Fazenda São Jorge", "Area Rural AB2")
    val locationsOptions: List<String>
        get() = _locationsOptions
    private var _operationsOptions = Operations.values().map { it.value }
    val operationsOptions: List<String>
        get() = _operationsOptions
}