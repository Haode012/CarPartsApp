package tarc.edu.carpartsapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import tarc.edu.carpartsapp.Model.Feedback
import tarc.edu.carpartsapp.R

class FeedbackAdapter(private val feedbackList: ArrayList<Feedback>) :

    RecyclerView.Adapter<FeedbackAdapter.MyViewHolder>() {
    val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
    val database = Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.feedback_list, parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = feedbackList[position]
        holder.ratings.rating = currentItem.rating.toFloat()
        holder.comment.text = currentItem.comment

        val ref = database.getReference("Feedback")
        holder.delete.setOnClickListener(){
         deleteFeedback()
            }
        }


    private fun deleteFeedback(){
        val ref = database.getReference("Feedback")
        ref.removeValue().addOnCompleteListener { task ->
            if(task.isSuccessful){

            }
        }
    }

    override fun getItemCount(): Int {
        return feedbackList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val ratings : RatingBar = itemView.findViewById(R.id.ratingBarNew)
        val comment : TextView = itemView.findViewById(R.id.feedbackText)
        val delete : ImageView = itemView.findViewById(R.id.imageButtonDelete)
    }
}