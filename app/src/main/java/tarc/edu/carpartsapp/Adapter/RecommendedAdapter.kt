package tarc.edu.carpartsapp.Adapter

import android.content.Context
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
import tarc.edu.carpartsapp.Filter.PopularFilter
import tarc.edu.carpartsapp.Filter.RecommendedFilter
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.Model.RecommendedModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.RecommendedCarPartsBinding

class RecommendedAdapter: RecyclerView.Adapter<RecommendedAdapter.MyViewHolder> , Filterable {

    public var recommendedModelArrayList: ArrayList<RecommendedModel>
    private val context: Context
    private lateinit var binding: RecommendedCarPartsBinding
    private var filterList: ArrayList<RecommendedModel>

    private var filter: RecommendedFilter? = null

    constructor(recommendedModelArrayList: ArrayList<RecommendedModel>, context: Context){
        this.recommendedModelArrayList = recommendedModelArrayList
        this.context = context
        this.filterList = recommendedModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedAdapter.MyViewHolder {

        binding = RecommendedCarPartsBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecommendedAdapter.MyViewHolder, position:Int) {

        val recommendedModel = recommendedModelArrayList[position]
        val name = recommendedModel.name
        val description = recommendedModel.description
        val img_url = recommendedModel.img_url

        holder.name.text = name
        holder.description.text = description
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            viewAllRecommendedFragment(recommendedModel, holder)
        }
    }

    private fun viewAllRecommendedFragment(recommendedModel: RecommendedModel, holder: RecommendedAdapter.MyViewHolder) {
        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_home_main_to_nav_view_all_recommended_main)
    }


    override fun getItemCount(): Int {

        return recommendedModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = binding.recommendedName
        val description : TextView = binding.recommendedDescription
        val img_url : ImageView = binding.recommendedImage

    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = RecommendedFilter(filterList, this)
        }
        return filter as RecommendedFilter
    }
}