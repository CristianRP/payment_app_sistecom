package com.sistecom.paymentapp.ui.adapter

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
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
                              private val clickListener: (Order) -> Unit)
    : RecyclerView.Adapter<OrdersByContractAdapter.DataViewHolder>() {

    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(order: Order, clickListener: (Order) -> Unit, isSelected: Boolean) {
            itemView.apply {
                val statusAndDate =  """${order.status}${order.requestDate}"""
                val alternId = "${order.alternId}"
                txtAlternId.text = alternId
                txtConcept.text = order.concept
                txtRequestDate.text = statusAndDate
                txtTransactionAmount.text = order.amount.toString()
                isActivated = isSelected
                Glide.with(imgCardLogo.context)
                        .load(imgCardLogo.context.getDrawable(R.drawable.ic_icon_cc_card))
                        .into(imgCardLogo)
                Glide.with(imgLogoOrder.context)
                        .load(imgLogoOrder.context.getDrawable(R.drawable.ic_icon_invoice))
                        .into(imgLogoOrder)
                setOnClickListener { clickListener(order) }
            }
        }
        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
                object: ItemDetailsLookup.ItemDetails<Long>() {
                    override fun getPosition(): Int = absoluteAdapterPosition
                    override fun getSelectionKey(): Long? = itemId
                }
    }

    class MyItemDetailsLookup(private val recyclerView: RecyclerView) :
        ItemDetailsLookup<Long>() {
        override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
            val view = recyclerView.findChildViewUnder(e.x, e.y)
            if (view != null) {
                return (recyclerView.getChildViewHolder(view) as DataViewHolder)
                        .getItemDetails()
            }
            return null
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
            DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_order_layout, parent, false))

    override fun getItemCount(): Int = ordersList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val pos = ordersList[position]
        tracker?.let {
            holder.bind(pos, clickListener, it.isSelected(position.toLong()))
        }
    }

    fun addOrders(listOrders: List<Order>) {
        this.ordersList.apply {
            clear()
            addAll(listOrders)
        }
    }
}