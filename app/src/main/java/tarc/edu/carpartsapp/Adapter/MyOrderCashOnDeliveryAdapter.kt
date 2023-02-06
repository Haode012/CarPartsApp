package tarc.edu.carpartsapp.Adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Model.MyOrderModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.MyOrderCashOnDeliveryItemBinding


class MyOrderCashOnDeliveryAdapter: RecyclerView.Adapter<MyOrderCashOnDeliveryAdapter.MyViewHolder> {

    public var myOrderModelArrayList: ArrayList<MyOrderModel>
    private val context: Context
    private lateinit var binding: MyOrderCashOnDeliveryItemBinding

    constructor(myOrderModelArrayList: ArrayList<MyOrderModel>, context: Context){
        this.myOrderModelArrayList = myOrderModelArrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrderCashOnDeliveryAdapter.MyViewHolder {

        binding = MyOrderCashOnDeliveryItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyOrderCashOnDeliveryAdapter.MyViewHolder, position:Int) {

        val myOrderModel = myOrderModelArrayList[position]
        val id = myOrderModel.id
        val name = myOrderModel.name
        val warranty = myOrderModel.warranty
        val price = myOrderModel.price
        val total_quantity = myOrderModel.total_quantity
        val total_price = myOrderModel.total_price
        val img_url = myOrderModel.img_url

        holder.id.text = id
        holder.name.text = name
        holder.warranty.text = warranty
        holder.price.text = price
        holder.total_quantity.text = total_quantity
        holder.total_price.text = total_price
        Glide.with(context).load(img_url).into(holder.img_url)
    }

    override fun getItemCount(): Int {

        return myOrderModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var id : TextView = binding.myOrderCashOnDeliveryItemId
        var name : TextView = binding.myOrderCashOnDeliveryItemName
        val warranty : TextView = binding.myOrderCashOnDeliveryItemWarranty
        val price : TextView = binding.myOrderCashOnDeliveryItemPrice
        val total_quantity : TextView = binding.myOrderCashOnDeliveryItemTotalQuantity
        val total_price : TextView = binding.myOrderCashOnDeliveryItemTotalPrice
        val img_url : ImageView = binding.myOrderCashOnDeliveryItemImage
    }
}