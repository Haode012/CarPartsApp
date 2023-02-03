package tarc.edu.carpartsapp.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.Model.RecommendedModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.RecommendedCarPartsBinding
import tarc.edu.carpartsapp.databinding.RecommendedCarPartsCustomerBinding

class RecommendedAdapterCustomer: RecyclerView.Adapter<RecommendedAdapterCustomer.MyViewHolder> {

    public var recommendedModelArrayList: ArrayList<RecommendedModel>
    private val context: Context
    private lateinit var binding: RecommendedCarPartsCustomerBinding

    constructor(recommendedModelArrayList: ArrayList<RecommendedModel>, context: Context){
        this.recommendedModelArrayList = recommendedModelArrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendedAdapterCustomer.MyViewHolder {

        binding = RecommendedCarPartsCustomerBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: RecommendedAdapterCustomer.MyViewHolder, position:Int) {

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

    private fun viewAllRecommendedFragment(recommendedModel: RecommendedModel, holder: RecommendedAdapterCustomer.MyViewHolder) {
        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_home_customer_to_nav_view_all_recommended_customer2)
    }


    override fun getItemCount(): Int {

        return recommendedModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = binding.recommendedName
        val description : TextView = binding.recommendedDescription
        val img_url : ImageView = binding.recommendedImage

    }
}