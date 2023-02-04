package tarc.edu.carpartsapp.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tarc.edu.carpartsapp.Model.ViewAllCategoryModel
import tarc.edu.carpartsapp.Model.ViewAllPopularModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.ViewAllPopularBinding
import tarc.edu.carpartsapp.databinding.ViewAllPopularCustomerBinding

class ViewAllPopularAdapterCustomer: RecyclerView.Adapter<ViewAllPopularAdapterCustomer.MyViewHolder> {

    public var viewAllPopularModelArrayList: ArrayList<ViewAllPopularModel>
    private val context: Context
    private lateinit var binding: ViewAllPopularCustomerBinding

    constructor(viewAllPopularModelArrayList: ArrayList<ViewAllPopularModel>, context: Context){
        this.viewAllPopularModelArrayList= viewAllPopularModelArrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllPopularAdapterCustomer.MyViewHolder {

        binding = ViewAllPopularCustomerBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewAllPopularAdapterCustomer.MyViewHolder, position:Int) {

        val viewAllPopularModel = viewAllPopularModelArrayList[position]
        val id = viewAllPopularModel.id
        val name = viewAllPopularModel.name
        val description = viewAllPopularModel.description
        val price = viewAllPopularModel.price
        val warranty = viewAllPopularModel.warranty
        val img_url = viewAllPopularModel.img_url

        holder.id.text = id
        holder.name.text = name
        holder.description.text = description
        holder.price.text = price
        holder.warranty.text = warranty
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            detailsFragment(viewAllPopularModel, holder)
        }
    }

    private fun detailsFragment(viewAllPopularModel: ViewAllPopularModel, holder: ViewAllPopularAdapterCustomer.MyViewHolder) {
        val id = viewAllPopularModel.id
        val name = viewAllPopularModel.name
        val description = viewAllPopularModel.description
        val price = viewAllPopularModel.price
        val warranty = viewAllPopularModel.warranty
        val img_url = viewAllPopularModel.img_url


        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_view_all_popular_customer2_to_nav_details_customer2, Bundle().apply {
                putString("id",id.toString())
                putString("name",name.toString())
                putString("description",description.toString())
                putString("price",price.toString())
                putString("warranty",warranty.toString())
                putString("img_url",img_url.toString())
            })
    }


    override fun getItemCount(): Int {

        return viewAllPopularModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val id: TextView = binding.popularItemId
        val name : TextView = binding.popularItemName
        val description : TextView = binding.popularItemDescription
        val price : TextView = binding.popularItemPrice
        val warranty : TextView = binding.popularItemWarranty
        val img_url : ImageView = binding.popularItemImage
        var itemView : CardView = binding.popularItemCardView
    }
}