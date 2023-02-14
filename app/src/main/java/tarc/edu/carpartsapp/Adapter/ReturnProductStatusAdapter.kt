package tarc.edu.carpartsapp.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tarc.edu.carpartsapp.Filter.CarPartsCategoryFilter
import tarc.edu.carpartsapp.Filter.ReturnProductAdminFilter
import tarc.edu.carpartsapp.Filter.ReturnProductCashOnDeliveryFilter
import tarc.edu.carpartsapp.Filter.ReturnProductStatusFilter
import tarc.edu.carpartsapp.Model.CarPartsCategoryModel
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.Model.ReturnProductModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.ReturnProductAdminItemBinding
import tarc.edu.carpartsapp.databinding.ReturnProductCashOnDeliveryItemBinding
import tarc.edu.carpartsapp.databinding.ReturnProductItemStatusBinding


class ReturnProductStatusAdapter: RecyclerView.Adapter<ReturnProductStatusAdapter.MyViewHolder> ,
    Filterable {

    public var returnProductModelArrayList: ArrayList<ReturnProductModel>
    private val context: Context
    private lateinit var binding: ReturnProductItemStatusBinding
    private var filterList: ArrayList<ReturnProductModel>

    private var filter: ReturnProductStatusFilter? = null

    constructor(returnProductModelArrayList: ArrayList<ReturnProductModel>, context: Context){
        this.returnProductModelArrayList = returnProductModelArrayList
        this.context = context
        this.filterList = returnProductModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnProductStatusAdapter.MyViewHolder {

        binding = ReturnProductItemStatusBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ReturnProductStatusAdapter.MyViewHolder, position:Int) {

        val returnProductModel = returnProductModelArrayList[position]
        val id = returnProductModel.id
        val name = returnProductModel.name
        val warranty = returnProductModel.warranty
        val total_quantity = returnProductModel.total_quantity
        val order_id = returnProductModel.orderID
        val order_date = returnProductModel.orderDate
        val order_time = returnProductModel.orderTime
        val status = returnProductModel.status
        val img_url = returnProductModel.img_url

        holder.id.text = id
        holder.name.text = name
        holder.warranty.text = warranty
        holder.total_quantity.text = total_quantity
        holder.order_id.text = order_id
        holder.order_date.text = order_date
        holder.order_time.text = order_time
        holder.status.text = status
        Glide.with(context).load(img_url).into(holder.img_url)

    }

    override fun getItemCount(): Int {

        return returnProductModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var id : TextView = binding.returnProductItemStatusId
        var name : TextView = binding.returnProductItemStatusName
        val warranty : TextView = binding.returnProductItemStatusWarranty
        val total_quantity : TextView = binding.returnProductItemStatusTotalQuantity
        var order_id : TextView = binding.returnProductItemStatusOrderId
        val order_date : TextView = binding.returnProductItemStatusOrderDate
        val order_time : TextView = binding.returnProductItemStatusOrderTime
        val img_url : ImageView = binding.returnProductItemStatusImage
        val status : TextView = binding.returnProductItemStatus
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = ReturnProductStatusFilter(filterList, this)
        }
        return filter as ReturnProductStatusFilter
    }
}