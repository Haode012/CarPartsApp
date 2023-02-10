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
import tarc.edu.carpartsapp.Filter.PopularFilter
import tarc.edu.carpartsapp.Model.CarPartsCategoryModel
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.CarPartsCategoryBinding


class CarPartsCategoryAdapter: RecyclerView.Adapter<CarPartsCategoryAdapter.MyViewHolder>,
    Filterable {

    public var carPartsCategoryModelArrayList: ArrayList<CarPartsCategoryModel>
    private val context: Context
    private lateinit var binding: CarPartsCategoryBinding
    private var filterList: ArrayList<CarPartsCategoryModel>

    private var filter: CarPartsCategoryFilter? = null

    constructor(carPartsCategoryModelArrayList: ArrayList<CarPartsCategoryModel>, context: Context){
        this.carPartsCategoryModelArrayList = carPartsCategoryModelArrayList
        this.context = context
        this.filterList = carPartsCategoryModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarPartsCategoryAdapter.MyViewHolder {

        binding = CarPartsCategoryBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CarPartsCategoryAdapter.MyViewHolder, position:Int) {

        val carPartsCategoryModel = carPartsCategoryModelArrayList[position]
        val name = carPartsCategoryModel.name
        val img_url = carPartsCategoryModel.img_url

        holder.name.text = name
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            viewAllCategoryFragment(carPartsCategoryModel, holder)
        }
    }

    private fun viewAllCategoryFragment(carPartsCategoryModel: CarPartsCategoryModel, holder: CarPartsCategoryAdapter.MyViewHolder) {
        val type = carPartsCategoryModel.type

        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_home_main_to_nav_view_all_category_main, Bundle().apply {

                putString("type",type.toString())

            })
    }


    override fun getItemCount(): Int {

        return carPartsCategoryModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = binding.carPartsCategoryName
        val img_url : ImageView = binding.carPartsCategoryImage


    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = CarPartsCategoryFilter(filterList, this)
        }
        return filter as CarPartsCategoryFilter
    }
}