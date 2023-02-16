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
import tarc.edu.carpartsapp.Model.CarPartsCategoryModel
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.Model.ReturnProductModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.ReturnProductAdminItemBinding
import tarc.edu.carpartsapp.databinding.ReturnProductCashOnDeliveryItemBinding


class ReturnProductAdminAdapter: RecyclerView.Adapter<ReturnProductAdminAdapter.MyViewHolder> ,
    Filterable {

    public var returnProductModelArrayList: ArrayList<ReturnProductModel>
    private val context: Context
    private lateinit var binding: ReturnProductAdminItemBinding
    private var filterList: ArrayList<ReturnProductModel>

    private var filter: ReturnProductAdminFilter? = null

    constructor(returnProductModelArrayList: ArrayList<ReturnProductModel>, context: Context){
        this.returnProductModelArrayList = returnProductModelArrayList
        this.context = context
        this.filterList = returnProductModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReturnProductAdminAdapter.MyViewHolder {

        binding = ReturnProductAdminItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ReturnProductAdminAdapter.MyViewHolder, position:Int) {

        val returnProductModel = returnProductModelArrayList[position]
        val uid = returnProductModel.uid
        val id = returnProductModel.id
        val name = returnProductModel.name
        val warranty = returnProductModel.warranty
        val total_quantity = returnProductModel.total_quantity
        val order_id = returnProductModel.orderID
        val order_date = returnProductModel.orderDate
        val order_time = returnProductModel.orderTime
        val img_url = returnProductModel.img_url

        holder.uid.text = uid
        holder.id.text = id
        holder.name.text = name
        holder.warranty.text = warranty
        holder.total_quantity.text = total_quantity
        holder.order_id.text = order_id
        holder.order_date.text = order_date
        holder.order_time.text = order_time
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            returnProductAdminFragment(returnProductModel, holder)
        }
    }

    private fun returnProductAdminFragment(returnProductModel: ReturnProductModel, holder: ReturnProductAdminAdapter.MyViewHolder) {
        val uid = returnProductModel.uid
        val id = returnProductModel.id
        val name = returnProductModel.name
        val warranty = returnProductModel.warranty
        val total_quantity = returnProductModel.total_quantity
        val order_id = returnProductModel.orderID
        val order_date = returnProductModel.orderDate
        val order_time = returnProductModel.orderTime
        val img_url = returnProductModel.img_url

       Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_returnProduct_admin_to_fragmentAdminReturnProductDecision, Bundle().apply {
               // putString("uid",uid.toString())
                putString("id",id.toString())
                putString("name",name.toString())
                putString("warranty",warranty.toString())
               // putString("total_quantity",total_quantity.toString())
                //putString("order_id",order_id.toString())
               // putString("order_date",order_date.toString())
               // putString("order_time",order_time.toString())
                putString("img_url",img_url.toString())
            })
    }

    override fun getItemCount(): Int {

        return returnProductModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        var uid : TextView = binding.returnProductAdminItemUid
        var id : TextView = binding.returnProductAdminItemId
        var name : TextView = binding.returnProductAdminItemName
        val warranty : TextView = binding.returnProductAdminItemWarranty
        val total_quantity : TextView = binding.returnProductAdminItemTotalQuantity
        var order_id : TextView = binding.returnProductAdminItemOrderId
        val order_date : TextView = binding.returnProductAdminItemOrderDate
        val order_time : TextView = binding.returnProductAdminItemOrderTime
        val img_url : ImageView = binding.returnProductAdminItemImage
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = ReturnProductAdminFilter(filterList, this)
        }
        return filter as ReturnProductAdminFilter
    }
}