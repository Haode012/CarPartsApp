package tarc.edu.carpartsapp.Admin

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.nav_header_admin.view.*
import tarc.edu.carpartsapp.MainActivity
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityAdminBinding
    private var firebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarAdmin.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_admin)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home_admin, R.id.nav_profile_admin, R.id.nav_aboutUs_admin
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        newUpdateHeader()
    }

    private fun updateNavHeader() {
        firebaseAuth = FirebaseAuth.getInstance()

        val user = firebaseAuth.currentUser
        val uid = user!!.uid

        val ref =
            Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users").child(uid)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val navView: NavigationView = binding.navView
                val view: View = navView.getHeaderView(0)
                val name = dataSnapshot.child("fullName").value as String?
                val email = dataSnapshot.child("emailAddress").value as String?
                view.textViewUsernameAdmin.setText(name)
                view.textViewEmailAdmin.setText(email)
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun newUpdateHeader(){
try {
    val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
    val ref =
        Firebase.database("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users/$userId")
    ref.get().addOnSuccessListener {
        val navView: NavigationView = binding.navView
        val view: View = navView.getHeaderView(0)
        view.textViewUsernameAdmin.text = it.child("fullName").getValue(String::class.java)
        view.textViewEmailAdmin.text = it.child("emailAddress").getValue(String::class.java)
    }
}catch (e:NullPointerException){

}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.admin, menu)
        return true
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
        val navController = findNavController(R.id.nav_host_fragment_content_admin)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}