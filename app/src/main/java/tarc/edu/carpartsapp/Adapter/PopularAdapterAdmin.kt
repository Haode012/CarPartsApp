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
import tarc.edu.carpartsapp.Filter.PopularFilterAdmin
import tarc.edu.carpartsapp.Filter.PopularFilterCustomer
import tarc.edu.carpartsapp.Model.PopularModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.PopularCarPartsAdminBinding

class PopularAdapterAdmin: RecyclerView.Adapter<PopularAdapterAdmin.MyViewHolder> , Filterable {

    public var popularModelArrayList: ArrayList<PopularModel>
    private val context: Context
    private lateinit var binding: PopularCarPartsAdminBinding
    private var filterList: ArrayList<PopularModel>

    private var filter: PopularFilterAdmin? = null

    constructor(popularModelArrayList: ArrayList<PopularModel>, context: Context){
        this.popularModelArrayList = popularModelArrayList
        this.context = context
        this.filterList = popularModelArrayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapterAdmin.MyViewHolder {

        binding = PopularCarPartsAdminBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: PopularAdapterAdmin.MyViewHolder, position:Int) {

        val popularModel = popularModelArrayList[position]
        val id = popularModel.id
        val name = popularModel.name
        val description = popularModel.description
        val img_url = popularModel.img_url

        holder.id.text = id
        holder.name.text = name
        holder.description.text = description
        Glide.with(context).load(img_url).into(holder.img_url)

        holder.itemView.setOnClickListener{
            viewPopularCarPartsFragment(popularModel, holder)
        }
    }

    private fun viewPopularCarPartsFragment(popularModel: PopularModel, holder: PopularAdapterAdmin.MyViewHolder) {
        val id = popularModel.id
        val name = popularModel.name
        val description = popularModel.description
        val img_url = popularModel.img_url

        Navigation.findNavController(holder.itemView)
            .navigate(R.id.action_nav_popular_car_parts_admin_to_nav_edit_delete_popular_car_parts_admin, Bundle().apply {
                putString("id",id.toString())
                putString("name",name.toString())
            putString("description",description.toString())
            putString("img_url", img_url.toString())
                //set img_url
        })
    }

    override fun getItemCount(): Int {

        return popularModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val id: TextView = binding.popularId
        val name : TextView = binding.popularName
        val description : TextView = binding.popularDescription
        val img_url : ImageView = binding.popularImage
        var itemView : CardView = binding.popularCardView
    }

    override fun getFilter(): Filter {
        if(filter == null){
            filter = PopularFilterAdmin(filterList, this)
        }
        return filter as PopularFilterAdmin
    }
}