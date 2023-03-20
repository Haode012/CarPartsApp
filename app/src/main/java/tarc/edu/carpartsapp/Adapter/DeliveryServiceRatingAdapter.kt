package tarc.edu.carpartsapp.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Model.DeliveryFeedback
import tarc.edu.carpartsapp.R

class DeliveryServiceRatingAdapter(private val deliveryFeedbackList: ArrayList<DeliveryFeedback>) :

    RecyclerView.Adapter<DeliveryServiceRatingAdapter.MyViewHolder>() {
    private lateinit var db : DatabaseReference
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database =
        Firebase.database("https://carsurusmobileapplication-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.delivery_service_rate, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = deliveryFeedbackList[position]
        holder.deliveryFeedbackId.text = currentItem.deliveryFeedbackId
        holder.ratingBarRateDelivery.rating = currentItem.ratingBarRateDelivery.toFloat()
        holder.deleteButn.setOnClickListener {
            deleteFeedback(holder.bindingAdapterPosition)
            Toast.makeText(it.context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
        }

    }


    override fun getItemCount(): Int {
        return deliveryFeedbackList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteFeedback(position: Int){
        //val feedbackID = feedback.feedbackId
        db = FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference().child("Admin View All Feedback Delivery")
            .child(deliveryFeedbackList.get(position).deliveryFeedbackId)
        db.removeValue()
        deliveryFeedbackList.removeAt(position)
        notifyItemRemoved(position)
        notifyDataSetChanged()
    }



    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val deliveryFeedbackId: TextView = itemView.findViewById(R.id.deliveryFeedbackId)
        val ratingBarRateDelivery : RatingBar = itemView.findViewById(R.id.ratingBarRateDelivery)
        val deleteButn : ImageView = itemView.findViewById(R.id.imageButtonDelete2)
    }
}