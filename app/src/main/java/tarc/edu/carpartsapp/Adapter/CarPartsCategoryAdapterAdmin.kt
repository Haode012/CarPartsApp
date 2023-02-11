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
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tarc.edu.carpartsapp.Filter.CarPartsCategoryFilterAdmin
import tarc.edu.carpartsapp.Model.CarPartsCategoryModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.CarPartsCategoryAdminBinding


class CarPartsCategoryAdapterAdmin: RecyclerView.Adapter<CarPartsCategoryAdapterAdmin.MyViewHolder> , Filterable{

    public var carPartsCategoryModelArrayList: ArrayList<CarPartsCategoryModel>
    private val context: Context
    private lateinit var binding: CarPartsCategoryAdminBinding
    private var filterList: ArrayList<CarPartsCategoryModel>

    private var filter: CarPartsCategoryFilterAdmin? = null

    constructor(carPartsCategoryModelArrayList: ArrayList<CarPartsCategoryModel>, context: Context){
        this.carPartsCategoryModelArrayList = carPartsCategoryModelArrayList
        this.context = context
        this.filterList = carPartsCategoryModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarPartsCategoryAdapterAdmin.MyViewHolder {

        binding = CarPartsCategoryAdminBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CarPartsCategoryAdapterAdmin.MyViewHolder, position:Int) {

        val carPartsCategoryModel = carPartsCategoryModelArrayList[position]
        val id = carPartsCategoryModel.id
        val name = carPartsCategoryModel.name
        val type = carPartsCategoryModel.type
        val img_url = carPartsCategoryModel.img_url

        holder.id.text = id
        holder.name.text = name
        holder.type.text = type

        Glide.with(context).load(img_url).into(holder.img_url)
        holder.itemView.setOnClickListener{
            viewCarPartsCategoryFragment(carPartsCategoryModel, holder)
        }
    }

    private fun viewCarPartsCategoryFragment(carPartsCategoryModel: CarPartsCategoryModel, holder: CarPartsCategoryAdapterAdmin.MyViewHolder) {
        val id = carPartsCategoryModel.id
        val name = carPartsCategoryModel.name
        val type = carPartsCategoryModel.type
        val img_url = carPartsCategoryModel.img_url

        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_car_parts_category_admin_to_nav_edit_delete_car_parts_category_admin, Bundle().apply {
                putString("id",id.toString())
                putString("name",name.toString())
                putString("type",type.toString())
                putString("img_url",img_url.toString())
            })
    }

    override fun getItemCount(): Int {

        return carPartsCategoryModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val id : TextView = binding.carPartsCategoryId
        val name : TextView = binding.carPartsCategoryName
        val img_url : ImageView = binding.carPartsCategoryImage
        val type : TextView = binding.carPartsCategoryType
        var itemView : CardView = binding.carPartsCategoryCardView
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = CarPartsCategoryFilterAdmin(filterList, this)
        }
        return filter as CarPartsCategoryFilterAdmin
    }
}