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
import tarc.edu.carpartsapp.Filter.RecommendedFilterAdmin
import tarc.edu.carpartsapp.Filter.RecommendedFilterCustomer
import tarc.edu.carpartsapp.Model.RecommendedModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.RecommendedCarPartsAdminBinding

class RecommendedAdapterAdmin: RecyclerView.Adapter<RecommendedAdapterAdmin.MyViewHolder> ,
    Filterable {

    public var recommendedModelArrayList: ArrayList<RecommendedModel>
    private val context: Context
    private lateinit var binding: RecommendedCarPartsAdminBinding
    private var filterList: ArrayList<RecommendedModel>

    private var filter: RecommendedFilterAdmin? = null
    constructor(recommendedModelArrayList: ArrayList<RecommendedModel>, context: Context){
        this.recommendedModelArrayList = recommendedModelArrayList
        this.context = context
        this.filterList = recommendedModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):RecommendedAdapterAdmin.MyViewHolder {

        binding = RecommendedCarPartsAdminBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecommendedAdapterAdmin.MyViewHolder, position:Int) {

        val recommendedModel = recommendedModelArrayList[position]
        val id = recommendedModel.id
        val name = recommendedModel.name
        val description = recommendedModel.description
        val img_url = recommendedModel.img_url

        holder.id.text = id
        holder.name.text = name
        holder.description.text = description
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            viewRecommendedCarPartsFragment(recommendedModel, holder)
        }
    }

    private fun viewRecommendedCarPartsFragment(recommendedModel: RecommendedModel, holder: RecommendedAdapterAdmin.MyViewHolder) {
        val id = recommendedModel.id
        val name = recommendedModel.name
        val description = recommendedModel.description
        val img_url = recommendedModel.img_url

        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_recommended_car_parts_admin_to_nav_edit_delete_recommended_car_parts, Bundle().apply {
                putString("id",id.toString())
                putString("name",name.toString())
                putString("description",description.toString())
                putString("img_url",img_url.toString())
            })
    }

    override fun getItemCount(): Int {

        return recommendedModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val id : TextView = binding.recommendedId
        val name : TextView = binding.recommendedName
        val description : TextView = binding.recommendedDescription
        val img_url : ImageView = binding.recommendedImage
        var itemView : CardView = binding.recommendedCardView
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = RecommendedFilterAdmin(filterList, this)
        }
        return filter as RecommendedFilterAdmin
    }
}