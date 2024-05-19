package br.com.noobs.agronoobs.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.noobs.agronoobs.R
import br.com.noobs.agronoobs.model.Operation
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class OperationAdapter(
    private val context: Context,
    private val operations: MutableList<Operation>,
    private val clickListener: ((Operation) -> Unit)? = null
) : RecyclerView.Adapter<OperationAdapter.OperationItemViewHolder>() {

    inner class OperationItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val location: TextView = itemView.findViewById(R.id.tv_location)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val operationType: TextView = itemView.findViewById(R.id.tv_operation_type)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperationItemViewHolder {
        val itemList = LayoutInflater
            .from(context)
            .inflate(R.layout.operation_item, parent, false)
        return OperationItemViewHolder(itemList)
    }

    override fun getItemCount(): Int {
        return operations.size
    }

    override fun onBindViewHolder(holder: OperationItemViewHolder, position: Int) {
        val item = operations[position]
        holder.location.text = item.selectedLocation
        holder.date.text = ZonedDateTime
            .ofInstant(item.createdDateTime.toInstant(), ZoneId.of("UTC"))
            .withZoneSameInstant(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("dd MMM',' yyyy"))
        holder.operationType.text = item.selectedOperation.value
    }

    fun setData(operations: List<Operation>) {
        this.operations.clear()
        this.operations.addAll(operations)
        notifyDataSetChanged()
    }
}