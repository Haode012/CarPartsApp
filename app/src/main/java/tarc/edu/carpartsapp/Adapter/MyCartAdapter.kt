package tarc.edu.carpartsapp.Adapter

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
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
        val id = myCartModel.id
        val name = myCartModel.name
        val warranty = myCartModel.warranty
        val price = myCartModel.price
        val total_quantity = myCartModel.total_quantity
        val total_price = myCartModel.total_price
        val img_url = myCartModel.img_url

        holder.id.text = id
        holder.name.text = name
        holder.warranty.text = warranty
        holder.price.text = price
        holder.total_quantity.text = total_quantity
        holder.total_price.text = total_price
        Glide.with(context).load(img_url).into(holder.img_url)

        var total_amount = 0.0
        for (myCartModel in myCartModelArrayList) {
            total_amount += myCartModel.total_price.toDouble()
        }

        val intent = Intent("totalAmount")
        intent.putExtra("totalAmount", total_amount)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)

        holder.deleteBtn.setOnClickListener{
            val builder = AlertDialog.Builder(context)
            val view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_layout, null)
            val messageTextView = view.findViewById<TextView>(R.id.messageTextView)
            messageTextView.text = "Are you sure you want to delete this cart item?"
            messageTextView.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))

            builder.setTitle("Delete")
                .setView(view)
                .setPositiveButton("Confirm"){a,d->
                    deleteCartItem(myCartModel, holder)
                }
                .setNegativeButton("Cancel"){a,d->
                    a.dismiss()
                }
                .show()
        }
    }

    private fun deleteCartItem(myCartModel: MyCartModel, holder: MyCartAdapter.MyViewHolder) {

        val id = myCartModel.id

        val ref = FirebaseDatabase.getInstance().getReference("CartItem")
        ref.child(id)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    myCartModelArrayList.remove(myCartModel)
                    notifyDataSetChanged()
                    Toast.makeText(context, "You item have been deleted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Delete failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun getItemCount(): Int {

        return myCartModelArrayList.size
    }

    inner class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        var id : TextView = binding.myCartItemId
        var name : TextView = binding.myCartItemName
        val warranty : TextView = binding.myCartItemWarranty
        val price : TextView = binding.myCartItemPrice
        val total_quantity : TextView = binding.myCartItemTotalQuantity
        val total_price : TextView = binding.myCartItemTotalPrice
        val img_url : ImageView = binding.myCartItemImage
        var deleteBtn : ImageView = binding.imageViewDelete
    }
}