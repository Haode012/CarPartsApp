package tarc.edu.carpartsapp.Adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tarc.edu.carpartsapp.Model.CarPartsCategoryModel
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.Model.RecommendedModel
import tarc.edu.carpartsapp.Model.ViewAllCategoryModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.ViewAllCategoryBinding
import tarc.edu.carpartsapp.databinding.ViewAllCategoryCustomerBinding


class ViewAllCategoryAdapterCustomer: RecyclerView.Adapter<ViewAllCategoryAdapterCustomer.MyViewHolder> {

    public var viewAllCategoryModelArrayList: ArrayList<ViewAllCategoryModel>
    private val context: Context
    private lateinit var binding: ViewAllCategoryCustomerBinding

    constructor(viewAllCategoryModelArrayList: ArrayList<ViewAllCategoryModel>, context: Context){
        this.viewAllCategoryModelArrayList = viewAllCategoryModelArrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllCategoryAdapterCustomer.MyViewHolder {

        binding = ViewAllCategoryCustomerBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewAllCategoryAdapterCustomer.MyViewHolder, position:Int) {

        val viewAllCategoryModel = viewAllCategoryModelArrayList[position]
        val id = viewAllCategoryModel.id
        val name = viewAllCategoryModel.name
        val description = viewAllCategoryModel.description
        val price = viewAllCategoryModel.price
        val warranty = viewAllCategoryModel.warranty
        val img_url = viewAllCategoryModel.img_url

        holder.id.text = id
        holder.name.text = name
        holder.description.text = description
        holder.price.text = price
        holder.warranty.text = warranty
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            detailsFragment(viewAllCategoryModel, holder)
        }
    }

    private fun detailsFragment(viewAllCategoryModel: ViewAllCategoryModel, holder: ViewAllCategoryAdapterCustomer.MyViewHolder) {
         val id = viewAllCategoryModel.id
        val name = viewAllCategoryModel.name
         val description = viewAllCategoryModel.description
        val price = viewAllCategoryModel.price
        val warranty = viewAllCategoryModel.warranty
        val img_url = viewAllCategoryModel.img_url


        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_view_all_category_customer2_to_nav_details_customer2, Bundle().apply {
                putString("id",id.toString())
                putString("name",name.toString())
                putString("description",description.toString())
                putString("price",price.toString())
                putString("warranty",warranty.toString())
                putString("img_url",img_url.toString())
            })
    }

    override fun getItemCount(): Int {

        return viewAllCategoryModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val id : TextView = binding.categoryItemId
        val name : TextView = binding.categoryItemName
        val description : TextView = binding.categoryItemDescription
        val price : TextView = binding.categoryItemPrice
        val warranty : TextView = binding.categoryItemWarranty
        val img_url : ImageView = binding.categoryItemImage


    }
}