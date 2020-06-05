package com.sistecom.paymentapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.model.DataNode
import kotlinx.android.synthetic.main.item_contract_by_customer_layout.view.*

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 12:45 AM
 *
 */

class ContractByCustomerAdapter(private val dataNode: ArrayList<DataNode>)
    : RecyclerView.Adapter<ContractByCustomerAdapter.DataViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(dataSingleNode: DataNode) {
            itemView.apply {
                txtContractTitle.text = dataSingleNode.detail[0].contractDetail.description
                Glide.with(imgLogoContract.context)
                        .load(imgLogoContract.context.getDrawable(R.drawable.ic_icon_contract))
                        .into(imgLogoContract)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
            DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contract_by_customer_layout, parent, false))

    override fun getItemCount(): Int = dataNode.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(dataNode[position])
        val childLayoutManager =
                LinearLayoutManager(holder.itemView.recyclerViewProductsRelated.context,
                                    RecyclerView.VERTICAL, false)
        holder.itemView.recyclerViewProductsRelated.apply {
            layoutManager = childLayoutManager
            addItemDecoration(DividerItemDecoration(
                    holder.itemView.recyclerViewProductsRelated.context,
                    (holder.itemView.recyclerViewProductsRelated.layoutManager as LinearLayoutManager).orientation))
            adapter = ProductRelatedAdapter(dataNode[position].detail[0].listProductRelated)
            setRecycledViewPool(viewPool)
        }
    }

    fun addDataNodes(dataNode: List<DataNode>) {
        this.dataNode.apply {
            clear()
            addAll(dataNode)
        }
    }
}