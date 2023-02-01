package tarc.edu.carpartsapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tarc.edu.carpartsapp.Model.MyCartModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.MyCartItemBinding


class MyCartAdapter: RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {

    public var myCartModelArrayList: ArrayList<MyCartModel>
    private val context: Context
    private lateinit var binding: MyCartItemBinding

    constructor(myCartModelArrayList: ArrayList<MyCartModel>, context: Context){
        this.myCartModelArrayList = myCartModelArrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartAdapter.MyViewHolder {

        binding = MyCartItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyCartAdapter.MyViewHolder, position:Int) {

        val myCartModel = myCartModelArrayList[position]
        val name = myCartModel.name
        val price = myCartModel.price
        val total_quantity = myCartModel.total_quantity
        val total_price = myCartModel.total_price
        val img_url = myCartModel.img_url

        holder.name.text = name
        holder.price.text = price
        holder.total_quantity.text = total_quantity
        holder.total_price.text = total_price
        Glide.with(context).load(img_url).into(holder.img_url)
    }

    override fun getItemCount(): Int {

        return myCartModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = binding.myCartItemName
        val price : TextView = binding.myCartItemPrice
        val total_quantity : TextView = binding.myCartItemTotalQuantity
        val total_price : TextView = binding.myCartItemTotalPrice
        val img_url : ImageView = binding.myCartItemImage
    }
}