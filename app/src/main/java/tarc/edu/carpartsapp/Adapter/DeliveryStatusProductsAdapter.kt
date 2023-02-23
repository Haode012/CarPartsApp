package tarc.edu.carpartsapp.Adapter


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Model.DeliveryStatus
import tarc.edu.carpartsapp.R

class DeliveryStatusProductsAdapter(private val deliveryStatusProducts: ArrayList<DeliveryStatus>) :

    RecyclerView.Adapter<DeliveryStatusProductsAdapter.MyViewHolder>() {
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database =
        Firebase.database("https://carsurusmobileapplication-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.delivery_status_products, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = deliveryStatusProducts[position]
        holder.names.text = currentItem.names
        holder.TotalAmount.text = "RM" + currentItem.TotalAmount
        holder.quantity.text = "X" + currentItem.quantity
        Glide.with(holder.img_url.context).load(currentItem.img_url).into(holder.img_url)
    }


    override fun getItemCount(): Int {
        return deliveryStatusProducts.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val img_url : ImageView = itemView.findViewById(R.id.productImageView)
        val names : TextView = itemView.findViewById(R.id.outputProductName)
        val TotalAmount : TextView = itemView.findViewById(R.id.outputPrice)
        val quantity : TextView = itemView.findViewById(R.id.outputQuantity)
    }
}