package tarc.edu.carpartsapp.Adapter

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
import org.w3c.dom.Text
import tarc.edu.carpartsapp.Model.DeliveryStatus
import tarc.edu.carpartsapp.R

class CustomerViewDeliveryStatusAdapter(private val customerDeliveryStatusList: ArrayList<DeliveryStatus>) :

    RecyclerView.Adapter<CustomerViewDeliveryStatusAdapter.MyViewHolder>() {
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database =
        Firebase.database("https://carsurusmobileapplication-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.customer_view_delivery_status_layout, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = customerDeliveryStatusList[position]
        holder.deliveryStatusText.text = currentItem.deliveryStatus
      //  holder.orderDate.text = currentItem.dateTime
        //holder.orderId.text = currentItem.orderID
        holder.houseAddress.text = currentItem.address
        holder.totalAmount.text = currentItem.TotalAmount
    }

    private fun retrieveDeliveryStatusId() {

    }

    override fun getItemCount(): Int {
        return customerDeliveryStatusList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val deliveryStatusText : TextView = itemView.findViewById(R.id.outputDeliveryStatusnew)
        //val orderDate : TextView = itemView.findViewById(R.id.outputPurchaseDate)
        val houseAddress : TextView = itemView.findViewById(R.id.outputHouseAddress)
        //val orderId : TextView = itemView.findViewById(R.id.outputOrderID)
        val totalAmount : TextView = itemView.findViewById(R.id.outputTotalAmount)

    }
}