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
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.PopularCarPartsBinding


class PopularAdapter: RecyclerView.Adapter<PopularAdapter.MyViewHolder> {

    public var popularModelArrayList: ArrayList<PopularModel>
    private val context: Context
    private lateinit var binding: PopularCarPartsBinding

    constructor(popularModelArrayList: ArrayList<PopularModel>, context: Context){
        this.popularModelArrayList = popularModelArrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.MyViewHolder {

        binding = PopularCarPartsBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PopularAdapter.MyViewHolder, position:Int) {

        val popularModel = popularModelArrayList[position]
        val name = popularModel.name
        val description = popularModel.description
        val img_url = popularModel.img_url

        holder.name.text = name
        holder.description.text = description
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            viewAllPopularFragment(popularModel, holder)
        }
    }

    private fun viewAllPopularFragment(popularModel: PopularModel, holder: PopularAdapter.MyViewHolder) {
        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_home_main_to_nav_view_all_popular_main)
    }

    override fun getItemCount(): Int {

        return popularModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val name : TextView = binding.popularName
        val description : TextView = binding.popularDescription
        val img_url : ImageView = binding.popularImage
        var itemView : CardView = binding.popularCardView
    }
}