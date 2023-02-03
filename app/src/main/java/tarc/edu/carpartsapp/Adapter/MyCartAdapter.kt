package tarc.edu.carpartsapp.Adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Model.MyCartModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.MyCartItemBinding


class MyCartAdapter: RecyclerView.Adapter<MyCartAdapter.MyViewHolder> {

    public var myCartModelArrayList: ArrayList<MyCartModel>
    private val context: Context
    private lateinit var binding: MyCartItemBinding

    constructor(myCartModelArrayList: ArrayList<MyCartModel>, context: Context){
        this.myCartModelArrayList = myCartModelArrayList
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCartAdapter.MyViewHolder {

        binding = MyCartItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return MyViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MyCartAdapter.MyViewHolder, position:Int) {

        val myCartModel = myCartModelArrayList[position]
        val name = myCartModel.name
        val warranty = myCartModel.warranty
        val price = myCartModel.price
        val total_quantity = myCartModel.total_quantity
        val total_price = myCartModel.total_price
        val img_url = myCartModel.img_url

        holder.name.text = name
        holder.warranty.text = warranty
        holder.price.text = price
        holder.total_quantity.text = total_quantity
        holder.total_price.text = total_price
        Glide.with(context).load(img_url).into(holder.img_url)


        holder.deleteBtn.setOnClickListener{
                    deleteCartItem(myCartModel, holder)
        }
    }

    private fun deleteCartItem(myCartModel: MyCartModel, holder: MyCartAdapter.MyViewHolder) {

        val name = myCartModel.name

        val ref = FirebaseDatabase.getInstance().getReference("CartItem")
        ref.child(name)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun getItemCount(): Int {

        return myCartModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var name : TextView = binding.myCartItemName
        val warranty : TextView = binding.myCartItemWarranty
        val price : TextView = binding.myCartItemPrice
        val total_quantity : TextView = binding.myCartItemTotalQuantity
        val total_price : TextView = binding.myCartItemTotalPrice
        val img_url : ImageView = binding.myCartItemImage
        var deleteBtn : ImageView = binding.imageViewDelete
    }
}