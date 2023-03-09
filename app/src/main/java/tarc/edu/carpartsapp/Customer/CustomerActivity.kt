package tarc.edu.carpartsapp.Customer

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.nav_header_customer.*
import kotlinx.android.synthetic.main.nav_header_customer.view.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import tarc.edu.carpartsapp.MainActivity
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.ActivityCustomerBinding

class CustomerActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityCustomerBinding
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarCustomer.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_customer)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_customer, R.id.loginFragment, R.id.nav_profile_customer, R.id.nav_myCart_customer, R.id.nav_my_order_cash_on_delivery_customer, R.id.nav_my_order_credit_card_customer, R.id.nav_my_order_delivery_tracker,R.id.nav_feedback_customer, R.id.nav_aboutUs_customer, R.id.nav_itemDelivered_customer, R.id.nav_status_customer
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        newUpdateHeader()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.customer, menu)
        //after updating new profile pic, will change the profile picture in nav drawer
        newUpdateHeader()
        return true

    }


    private fun updateNavHeader() {
        firebaseAuth = FirebaseAuth.getInstance()

        val user = firebaseAuth.currentUser
        val uid = user!!.uid

        val ref =
            Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(uid)

        ref.get().addOnSuccessListener {
            val name = it.child("fullName").value as String
            val email = it.child("emailAddress").value as String
            val navView: NavigationView = binding.navView
           // val profilePic = it.child("profilePicture").child("url").value.toString()
            val view: View = navView.getHeaderView(0)
            view.textViewFullName.setText(name)
            view.textViewEmail.setText(email)
            //Glide.with(this).load(profilePic).into(imageView)
        }
    }

    @SuppressLint("CheckResult")
    private fun newUpdateHeader(){
try {
    val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
    val ref =
        Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users/$userId")
    ref.get().addOnSuccessListener {
        val navView: NavigationView = binding.navView
        val view: View = navView.getHeaderView(0)
        val profilePic = it.child("profilePicture").child("url").value.toString()
        view.textViewUsernameCustomer.text = it.child("fullName").getValue(String::class.java)
        view.textViewEmailCustomer.text = it.child("emailAddress").getValue(String::class.java)
        var requestOptions = RequestOptions()
        requestOptions.signature(ObjectKey(System.currentTimeMillis()))
        Glide.with(this).load(profilePic).apply(requestOptions).into(imageView)
    }
}catch (e:Exception){

}
        }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_log_out -> {
                firebaseAuth.signOut().apply {
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_customer)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}