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


class ViewAllCategoryAdapter: RecyclerView.Adapter<ViewAllCategoryAdapter.MyViewHolder> {

    public var viewAllCategoryModelArrayList: ArrayList<ViewAllCategoryModel>
    private val context: Context
    private lateinit var binding: ViewAllCategoryBinding

    constructor(viewAllCategoryModelArrayList: ArrayList<ViewAllCategoryModel>, context: Context){
        this.viewAllCategoryModelArrayList = viewAllCategoryModelArrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllCategoryAdapter.MyViewHolder {

        binding = ViewAllCategoryBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewAllCategoryAdapter.MyViewHolder, position:Int) {

        val viewAllCategoryModel = viewAllCategoryModelArrayList[position]
        val name = viewAllCategoryModel.name
        val description = viewAllCategoryModel.description
        val price = viewAllCategoryModel.price
        val warranty = viewAllCategoryModel.warranty
        val img_url = viewAllCategoryModel.img_url

        holder.name.text = name
        holder.description.text = description
        holder.price.text = price
        holder.warranty.text = warranty
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            detailsFragment(viewAllCategoryModel, holder)
        }
    }

    private fun detailsFragment(viewAllCategoryModel: ViewAllCategoryModel, holder: ViewAllCategoryAdapter.MyViewHolder) {
         val name = viewAllCategoryModel.name
         val description = viewAllCategoryModel.description
        val price = viewAllCategoryModel.price
        val warranty = viewAllCategoryModel.warranty
        val img_url = viewAllCategoryModel.img_url


        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_view_all_category_customer_to_nav_details_customer, Bundle().apply {
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

        val name : TextView = binding.categoryItemName
        val description : TextView = binding.categoryItemDescription
        val price : TextView = binding.categoryItemPrice
        val warranty : TextView = binding.categoryItemWarranty
        val img_url : ImageView = binding.categoryItemImage


    }
}