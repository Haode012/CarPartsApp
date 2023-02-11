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
import tarc.edu.carpartsapp.Filter.ViewAllPopularFilterAdmin
import tarc.edu.carpartsapp.Filter.ViewAllRecommendedFilterAdmin
import tarc.edu.carpartsapp.Model.ViewAllPopularModel
import tarc.edu.carpartsapp.Model.ViewAllRecommendedModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.ViewAllRecommendedAdminBinding

class ViewAllRecommendedAdapterAdmin: RecyclerView.Adapter<ViewAllRecommendedAdapterAdmin.MyViewHolder> ,
    Filterable {

    public var viewAllRecommendedModelArrayList: ArrayList<ViewAllRecommendedModel>
    private val context: Context
    private lateinit var binding: ViewAllRecommendedAdminBinding
    private var filterList: ArrayList<ViewAllRecommendedModel>

    private var filter: ViewAllRecommendedFilterAdmin? = null

    constructor(viewAllRecommendedModelArrayList: ArrayList<ViewAllRecommendedModel>, context: Context){
        this.viewAllRecommendedModelArrayList = viewAllRecommendedModelArrayList
        this.context = context
        this.filterList = viewAllRecommendedModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewAllRecommendedAdapterAdmin.MyViewHolder {

        binding = ViewAllRecommendedAdminBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewAllRecommendedAdapterAdmin.MyViewHolder, position:Int) {

        val viewAllRecommendedModel = viewAllRecommendedModelArrayList[position]
        val id = viewAllRecommendedModel.id
        val name =  viewAllRecommendedModel.name
        val description =  viewAllRecommendedModel.description
        val price =  viewAllRecommendedModel.price
        val warranty = viewAllRecommendedModel.warranty
        val img_url =  viewAllRecommendedModel.img_url

        holder.id.text = id
        holder.name.text = name
        holder.description.text = description
        holder.price.text = price
        holder.warranty.text = warranty
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            editDeleteViewAllRecommendedFragment(viewAllRecommendedModel, holder)
        }
    }

    private fun editDeleteViewAllRecommendedFragment(viewAllRecommendedModel: ViewAllRecommendedModel, holder: ViewAllRecommendedAdapterAdmin.MyViewHolder) {
        val id = viewAllRecommendedModel.id
        val name =  viewAllRecommendedModel.name
        val description =  viewAllRecommendedModel.description
        val price =  viewAllRecommendedModel.price
        val warranty = viewAllRecommendedModel.warranty
        val img_url =  viewAllRecommendedModel.img_url


        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_view_all_recommended_car_parts_admin_to_nav_edit_delete_view_all_recommended_car_parts_admin, Bundle().apply {
                putString("id",id.toString())
                putString("name",name.toString())
                putString("description",description.toString())
                putString("price",price.toString())
                putString("warranty",warranty.toString())
                putString("img_url",img_url.toString())
            })
    }


    override fun getItemCount(): Int {

        return viewAllRecommendedModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val id : TextView = binding.recommendedItemId
        val name : TextView = binding.recommendedItemName
        val description : TextView = binding.recommendedItemDescription
        val price : TextView = binding.recommendedItemPrice
        val warranty : TextView = binding.recommendedItemWarranty
        val img_url : ImageView = binding.recommendedItemImage
        var itemView : CardView = binding.recommendedItemCardView
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = ViewAllRecommendedFilterAdmin(filterList, this)
        }
        return filter as ViewAllRecommendedFilterAdmin
    }
}