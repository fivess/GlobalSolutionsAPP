package br.com.noobs.agronoobs.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import br.com.noobs.agronoobs.R

class HomeFragment : Fragment() {

    private lateinit var addOperationCard: CardView
    private lateinit var openOperationsCard: CardView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val inflate = inflater.inflate(
            R.layout.fragment_home,
            container,
            false
        )

        addOperationCard = inflate.findViewById(R.id.add_operation_card_view)
        openOperationsCard = inflate.findViewById(R.id.open_operation_card_view)

        addOperationCard.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.nav_add_operation)
        }
        openOperationsCard.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.nav_list_operations)
        }

        return inflate


    }

}