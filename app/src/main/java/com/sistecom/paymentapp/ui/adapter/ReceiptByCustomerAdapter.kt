package com.sistecom.paymentapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sistecom.paymentapp.R
import com.sistecom.paymentapp.data.model.receipt.Receipt
import kotlinx.android.synthetic.main.item_order_layout.view.*
import kotlinx.android.synthetic.main.item_receipt.view.*

/**
 * Created by: cristianramirez
 * On: 5/06/20 at: 08:50 PM
 *
 */

class ReceiptByCustomerAdapter(private val receiptList: ArrayList<Receipt>)
    : RecyclerView.Adapter<ReceiptByCustomerAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(receipt: Receipt) {
            itemView.apply {
                val transactionNumber = "Número de transacción: ${receipt.transactionNumber}"
                val cardData = "Tipo de tarjeta: ${receipt.cardType} Numeración: ${receipt.cardNumber.toString().replaceRange(0, 2, "*")}"
                val transactionDate = "Fecha y hora de transacción: ${receipt.transactionDate} ${receipt.transactionTime}"
                txtSystemId.text = receipt.systemId
                txtTransactionNumber.text = transactionNumber
                txtReceiptAmount.text = receipt.amount.toString()
                txtCardData.text = cardData
                txtCardName.text = receipt.cardName
                txtTransactionDate.text = transactionDate
                Glide.with(imgReceiptCardLogo.context)
                        .load(imgReceiptCardLogo.context.getDrawable(R.drawable.ic_icon_cc_card))
                        .into(imgReceiptCardLogo)
                Glide.with(imgReceiptLogo.context)
                        .load(imgReceiptLogo.context.getDrawable(R.drawable.ic_icon_invoice))
                        .into(imgReceiptLogo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
            DataViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_receipt, parent, false))

    override fun getItemCount(): Int = receiptList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(receiptList[position])
    }

    fun addReceipts(listReceipts: List<Receipt>) {
        this.receiptList.apply {
            clear()
            addAll(listReceipts)
        }
    }

}