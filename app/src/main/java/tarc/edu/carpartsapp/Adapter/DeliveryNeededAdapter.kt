package tarc.edu.carpartsapp.Adapter

import android.content.SharedPreferences
import android.os.Bundle
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
import tarc.edu.carpartsapp.Admin.AdminUpdateDeliveryStatus
import tarc.edu.carpartsapp.Model.Deliveries
import tarc.edu.carpartsapp.Model.DeliveryStatus
import tarc.edu.carpartsapp.R

class DeliveryNeededAdapter(private val deliveriesList: ArrayList<Deliveries>) :

    RecyclerView.Adapter<DeliveryNeededAdapter.MyViewHolder>() {
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database =
        Firebase.database("https://carsurusmobileapplication-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.deliveries_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = deliveriesList[position]
        holder.orderId.text = currentItem.orderID
        holder.name.text = currentItem.name
        holder.quantity.text = currentItem.quantity
        holder.totalAmount.text = currentItem.totalAmount
        holder.address.text = currentItem.address
        holder.id.text = currentItem.id
        Glide.with(holder.img_url.context).load(currentItem.img_url).into(holder.img_url)

        holder.btnCreate.setOnClickListener {
            val bundle = Bundle()
            val fragment = AdminUpdateDeliveryStatus()
            bundle.putString("name", currentItem.name)
            fragment.arguments = bundle
            //Navigation.findNavController(it).navigate(R.id.action_fragmentViewAllDelivery_to_createDeliveryStatus2, bundle)
            }
        }


    override fun getItemCount(): Int {
        return deliveriesList.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val img_url : ImageView = itemView.findViewById(R.id.productPicture)
        val name : TextView = itemView.findViewById(R.id.name)
        val quantity : TextView = itemView.findViewById(R.id.quantityOutput)
        val totalAmount : TextView = itemView.findViewById(R.id.totalAmount)
        val address : TextView = itemView.findViewById(R.id.deliveryAddress)
        val orderId : TextView = itemView.findViewById(R.id.orderId)
        val id : TextView = itemView.findViewById(R.id.id)
        val btnCreate : Button = itemView.findViewById(R.id.btnCreate)
    }
}