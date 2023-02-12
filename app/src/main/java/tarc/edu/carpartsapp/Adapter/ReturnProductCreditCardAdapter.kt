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
import tarc.edu.carpartsapp.Filter.ReturnProductCreditCardFilter
import tarc.edu.carpartsapp.Model.CarPartsCategoryModel
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.ReturnProductCashOnDeliveryItemBinding
import tarc.edu.carpartsapp.databinding.ReturnProductCreditCardItemBinding


class ReturnProductCreditCardAdapter: RecyclerView.Adapter<ReturnProductCreditCardAdapter.MyViewHolder> ,
    Filterable {

    public var myOrderModelArrayList: ArrayList<MyOrderModel>
    private val context: Context
    private lateinit var binding: ReturnProductCreditCardItemBinding
    private var filterList: ArrayList<MyOrderModel>

    private var filter: ReturnProductCreditCardFilter? = null

    constructor(myOrderModelArrayList: ArrayList<MyOrderModel>, context: Context){
        this.myOrderModelArrayList = myOrderModelArrayList
        this.context = context
        this.filterList = myOrderModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnProductCreditCardAdapter.MyViewHolder {

        binding = ReturnProductCreditCardItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ReturnProductCreditCardAdapter.MyViewHolder, position:Int) {

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
            returnProductCreditCardFragment(myOrderModel, holder)
        }
    }

    private fun returnProductCreditCardFragment(myOrderModel: MyOrderModel, holder: ReturnProductCreditCardAdapter.MyViewHolder) {
        val id = myOrderModel.id
        val name = myOrderModel.name
        val warranty = myOrderModel.warranty
        val order_id = myOrderModel.orderID
        val order_date = myOrderModel.currentDate
        val order_time = myOrderModel.currentTime
        val img_url = myOrderModel.img_url

        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_returnProductCreditCard_customer_to_nav_requestReturnProduct_customer, Bundle().apply {
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
        var id : TextView = binding.returnProductCreditCardItemId
        var name : TextView = binding.returnProductCreditCardItemName
        val warranty : TextView = binding.returnProductCreditCardItemWarranty
        var order_id : TextView = binding.returnProductCreditCardItemOrderId
        val order_date : TextView = binding.returnProductCreditCardItemOrderDate
        val order_time : TextView = binding.returnProductCreditCardItemOrderTime
        val img_url : ImageView = binding.returnProductCreditCardItemImage
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = ReturnProductCreditCardFilter(filterList, this)
        }
        return filter as ReturnProductCreditCardFilter
    }
}