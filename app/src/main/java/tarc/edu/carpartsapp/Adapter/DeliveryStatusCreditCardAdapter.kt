package tarc.edu.carpartsapp.Adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Admin.AdminUpdateDeliveryStatus
import tarc.edu.carpartsapp.FragmentAdminUpdateDeliveryStatusCreditCard
import tarc.edu.carpartsapp.Model.DeliveryStatus
import tarc.edu.carpartsapp.R

class DeliveryStatusCreditCardAdapter(private val deliveryStatusCreditList: ArrayList<DeliveryStatus>) :

    RecyclerView.Adapter<DeliveryStatusCreditCardAdapter.MyViewHolder>() {
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database =
        Firebase.database("https://carsurusmobileapplication-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.delivery_status_credit, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = deliveryStatusCreditList[position]
        holder.deliveryStatusId.text = currentItem.deliveryStatusId
        holder.buttonUpdate.setOnClickListener{
            val bundle = Bundle()
            val fragment = FragmentAdminUpdateDeliveryStatusCreditCard()
            bundle.putString("deliveryStatusId", currentItem.deliveryStatusId)
            fragment.arguments = bundle
            Navigation.findNavController(it).navigate(R.id.action_fragmentAdminViewAllDeliveryStatusCreditCard_to_fragmentAdminUpdateDeliveryStatusCreditCard,bundle)
        }
    }


    override fun getItemCount(): Int {
        return deliveryStatusCreditList.size
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val deliveryStatusId : TextView = itemView.findViewById(R.id.delivStatusId)
        val buttonUpdate : Button = itemView.findViewById(R.id.updateStatus)
    }
}