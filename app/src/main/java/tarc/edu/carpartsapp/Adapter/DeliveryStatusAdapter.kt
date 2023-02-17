package tarc.edu.carpartsapp.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Admin.AdminUpdateDeliveryStatus
import tarc.edu.carpartsapp.Model.DeliveryStatus
import tarc.edu.carpartsapp.R

class DeliveryStatusAdapter(private val deliveryStatusList: ArrayList<DeliveryStatus>) :

    RecyclerView.Adapter<DeliveryStatusAdapter.MyViewHolder>() {
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database =
        Firebase.database("https://carsurusmobileapplication-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.delivery_status_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = deliveryStatusList[position]
        holder.deliveryStatusId.text = currentItem.deliveryStatusId
        holder.buttonUpdate.setOnClickListener{
            val bundle = Bundle()
            val fragment = AdminUpdateDeliveryStatus()
            bundle.putString("orderID", currentItem.deliveryStatusId)
            fragment.arguments = bundle
            Navigation.findNavController(it).navigate(R.id.action_fragmentAdminViewAllDeliveryStatus_to_adminUpdateDeliveryStatus,bundle)
        }
    }


    private fun retrieveDeliveryStatusId() {

    }

    override fun getItemCount(): Int {
        return deliveryStatusList.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
       val deliveryStatusId : TextView = itemView.findViewById(R.id.deliveryStatusId)
        val buttonUpdate : Button = itemView.findViewById(R.id.btnUpdateStatus)
    }
}