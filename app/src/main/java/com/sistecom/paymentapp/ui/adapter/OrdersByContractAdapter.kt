package com.sistecom.paymentapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.model.DataNode
import com.sistecom.paymentapp.data.model.order.Order
import kotlinx.android.synthetic.main.item_order_layout.view.*

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 01:12 PM
 *
 */

class OrdersByContractAdapter(private val ordersList: ArrayList<Order>,
                              val clickListener: (Order) -> Unit)
    : RecyclerView.Adapter<OrdersByContractAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(order: Order, clickListener: (Order) -> Unit) {
            itemView.apply {
                val statusAndDate =  """${order.status}${order.requestDate}"""
                val alternId = "${order.alternId}"
                txtAlternId.text = alternId
                txtConcept.text = order.concept
                txtRequestDate.text = statusAndDate
                txtTransactionAmount.text = order.amount.toString()
                Glide.with(imgCardLogo.context)
                        .load(imgCardLogo.context.getDrawable(R.drawable.ic_icon_cc_card))
                        .into(imgCardLogo)
                Glide.with(imgLogoOrder.context)
                        .load(imgLogoOrder.context.getDrawable(R.drawable.ic_icon_invoice))
                        .into(imgLogoOrder)
                setOnClickListener { clickListener(order) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
            DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_order_layout, parent, false))

    override fun getItemCount(): Int = ordersList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(ordersList[position], clickListener)
    }

    fun addOrders(listOrders: List<Order>) {
        this.ordersList.apply {
            clear()
            addAll(listOrders)
        }
    }
}