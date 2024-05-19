package br.com.noobs.agronoobs.ui.operation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.noobs.agronoobs.R
import br.com.noobs.agronoobs.model.Operation
import br.com.noobs.agronoobs.provider.OperationProviderViewModel
import br.com.noobs.agronoobs.ui.adapter.OperationAdapter

class ListOperation : Fragment() {

    private val operationProviderViewModel: OperationProviderViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val inflate = inflater.inflate(R.layout.fragment_list_operation, container, false)

        val operationAdapter = OperationAdapter(inflater.context, mutableListOf())


        operationProviderViewModel.getListOperations().observe(viewLifecycleOwner) {
            operationAdapter.setData(it)
        }


        recyclerView = inflate.findViewById(R.id.rv_operations)
        recyclerView.layoutManager = LinearLayoutManager(inflater.context)
        recyclerView.adapter = operationAdapter

        return inflate
    }

}