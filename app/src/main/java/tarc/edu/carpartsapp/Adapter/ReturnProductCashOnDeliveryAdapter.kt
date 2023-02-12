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
import tarc.edu.carpartsapp.Filter.ReturnProductCashOnDeliveryFilter
import tarc.edu.carpartsapp.Model.CarPartsCategoryModel
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.ReturnProductCashOnDeliveryItemBinding


class ReturnProductCashOnDeliveryAdapter: RecyclerView.Adapter<ReturnProductCashOnDeliveryAdapter.MyViewHolder> ,
    Filterable {

    public var myOrderModelArrayList: ArrayList<MyOrderModel>
    private val context: Context
    private lateinit var binding: ReturnProductCashOnDeliveryItemBinding
    private var filterList: ArrayList<MyOrderModel>

    private var filter: ReturnProductCashOnDeliveryFilter? = null

    constructor(myOrderModelArrayList: ArrayList<MyOrderModel>, context: Context){
        this.myOrderModelArrayList = myOrderModelArrayList
        this.context = context
        this.filterList = myOrderModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnProductCashOnDeliveryAdapter.MyViewHolder {

        binding = ReturnProductCashOnDeliveryItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ReturnProductCashOnDeliveryAdapter.MyViewHolder, position:Int) {

        val myOrderModel = myOrderModelArrayList[position]
        val id = myOrderModel.id
        val name = myOrderModel.name
        val warranty = myOrderModel.warranty
        val order_id = myOrderModel.orderID
        val order_date = myOrderModel.currentDate
        val order_time = myOrderModel.currentTime
        val img_url = myOrderModel.img_url

        holder.id.text = id
        holder.name.text = name
        holder.warranty.text = warranty
        holder.order_id.text = order_id
        holder.order_date.text = order_date
        holder.order_time.text = order_time
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            returnProductCashOnDeliveryFragment(myOrderModel, holder)
        }
    }

    private fun returnProductCashOnDeliveryFragment(myOrderModel: MyOrderModel, holder: ReturnProductCashOnDeliveryAdapter.MyViewHolder) {
        val id = myOrderModel.id
        val name = myOrderModel.name
        val warranty = myOrderModel.warranty
        val order_id = myOrderModel.orderID
        val order_date = myOrderModel.currentDate
        val order_time = myOrderModel.currentTime
        val img_url = myOrderModel.img_url

        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_returnProductCashOnDelivery_customer_to_nav_requestReturnProduct_customer, Bundle().apply {
                putString("id",id.toString())
                putString("name",name.toString())
                putString("warranty",warranty.toString())
                putString("order_id",order_id.toString())
                putString("order_date",order_date.toString())
                putString("order_time",order_time.toString())
                putString("img_url",img_url.toString())
            })
    }

    override fun getItemCount(): Int {

        return myOrderModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var id : TextView = binding.returnProductCashOnDeliveryItemId
        var name : TextView = binding.returnProductCashOnDeliveryItemName
        val warranty : TextView = binding.returnProductCashOnDeliveryItemWarranty
        var order_id : TextView = binding.returnProductCashOnDeliveryItemOrderId
        val order_date : TextView = binding.returnProductCashOnDeliveryItemOrderDate
        val order_time : TextView = binding.returnProductCashOnDeliveryItemOrderTime
        val img_url : ImageView = binding.returnProductCashOnDeliveryItemImage
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = ReturnProductCashOnDeliveryFilter(filterList, this)
        }
        return filter as ReturnProductCashOnDeliveryFilter
    }
}