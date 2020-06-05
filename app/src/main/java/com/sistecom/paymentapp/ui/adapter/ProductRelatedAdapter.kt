package com.sistecom.paymentapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.model.ProductRelated
import kotlinx.android.synthetic.main.item_contract_by_customer_layout.view.*
import kotlinx.android.synthetic.main.item_contract_products_related.view.*

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 02:11 AM
 *
 */

class ProductRelatedAdapter(private val listProductRelated: ArrayList<ProductRelated>)
    : RecyclerView.Adapter<ProductRelatedAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(productRelated: ProductRelated) {
            itemView.apply {
                txtProductRelated.text = productRelated.description
                Glide.with(imgProductRelated.context)
                        .load(imgProductRelated.context.getDrawable(R.drawable.ic_tv_tmp))
                        .into(imgProductRelated)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
            DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_contract_products_related, parent, false))

    override fun getItemCount(): Int = listProductRelated.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(listProductRelated[position])
    }

    fun addProductsRelated(listProductRelated: List<ProductRelated>) {
        this.listProductRelated.apply {
            clear()
            addAll(listProductRelated)
        }
    }
}