package tarc.edu.carpartsapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import tarc.edu.carpartsapp.Admin.AdminActivity
import tarc.edu.carpartsapp.Customer.CustomerActivity
import tarc.edu.carpartsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAdmin.setOnClickListener{
            val intent = Intent(applicationContext, AdminActivity::class.java)
            startActivity(intent)
        }

        binding.buttonCustomer.setOnClickListener{
            val intent = Intent(
                applicationContext,
                CustomerActivity::class.java
            )
            startActivity(intent)
        }
    }
}