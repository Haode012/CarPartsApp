package tarc.edu.carpartsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import tarc.edu.carpartsapp.Adapter.ViewAllCategoryAdapter
import tarc.edu.carpartsapp.Adapter.ViewAllPopularAdapter
import tarc.edu.carpartsapp.Model.ViewAllCategoryModel
import tarc.edu.carpartsapp.Model.ViewAllPopularModel
import tarc.edu.carpartsapp.R
import tarc.edu.carpartsapp.databinding.FragmentViewAllCategoryBinding

class ViewAllCategoryFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAllCategoryModelArrayList : ArrayList<ViewAllCategoryModel>
    private lateinit var query : Query

    private var _binding: FragmentViewAllCategoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentViewAllCategoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewCategory)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        viewAllCategoryModelArrayList = arrayListOf<ViewAllCategoryModel>()

        getData()
    }

    private fun getData() {

        // get brake kit
        val type = requireArguments().getString("type").toString()

        if (type != null && type.equals("brake kit", ignoreCase = true)) {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            query = dbref.equalTo("brake kit", "type")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {
                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            if (category?.type?.equals("brake kit", ignoreCase = true) == true) {
                                viewAllCategoryModelArrayList.add(category)
                            }
                        }
                        val context = context
                        if (context != null) {
                            recyclerView.adapter =
                                ViewAllCategoryAdapter(
                                    viewAllCategoryModelArrayList,
                                    context
                                )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        //get engine
        else if (type != null && type.equals("engine", ignoreCase = true)) {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            query = dbref.equalTo("engine", "type")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {
                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            if (category?.type?.equals("engine", ignoreCase = true) == true) {
                                viewAllCategoryModelArrayList.add(category)
                            }
                        }
                        val context = context
                        if (context != null) {
                            recyclerView.adapter =
                                ViewAllCategoryAdapter(
                                    viewAllCategoryModelArrayList,
                                   context
                                )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        //get exhaust
        else if (type != null && type.equals("exhaust", ignoreCase = true)) {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            query = dbref.equalTo("exhaust", "type")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {
                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            if (category?.type?.equals("exhaust", ignoreCase = true) == true) {
                                viewAllCategoryModelArrayList.add(category)
                            }
                        }
                        val context = context
                        if (context != null) {
                            recyclerView.adapter =
                                ViewAllCategoryAdapter(viewAllCategoryModelArrayList, context)

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        //get floor car mat
        else if (type != null && type.equals("floor car mat", ignoreCase = true)) {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            query = dbref.equalTo("floor car mat", "type")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {
                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            if (category?.type?.equals(
                                    "floor car mat",
                                    ignoreCase = true
                                ) == true
                            ) {
                                viewAllCategoryModelArrayList.add(category)
                            }
                        }
                        val context = context
                        if (context != null) {
                            recyclerView.adapter =
                                ViewAllCategoryAdapter(
                                    viewAllCategoryModelArrayList,
                                    context
                                )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        //get head light
        else if (type != null && type.equals("head light", ignoreCase = true)) {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            query = dbref.equalTo("head light", "type")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {
                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            if (category?.type?.equals("head light", ignoreCase = true) == true) {
                                viewAllCategoryModelArrayList.add(category)
                            }
                        }
                        val context = context
                        if (context != null) {
                            recyclerView.adapter =
                                ViewAllCategoryAdapter(
                                    viewAllCategoryModelArrayList,
                                    context
                                )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        //get side mirror
        else if (type != null && type.equals("side mirror", ignoreCase = true)) {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            query = dbref.equalTo("side mirror", "type")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {
                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            if (category?.type?.equals("side mirror", ignoreCase = true) == true) {
                                viewAllCategoryModelArrayList.add(category)
                            }
                        }
                        val context = context
                        if (context != null) {
                            recyclerView.adapter =
                                ViewAllCategoryAdapter(
                                    viewAllCategoryModelArrayList,
                                    context
                                )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        //get step board
        else if (type != null && type.equals("step board", ignoreCase = true)) {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            query = dbref.equalTo("step board", "type")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {
                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            if (category?.type?.equals("step board", ignoreCase = true) == true) {
                                viewAllCategoryModelArrayList.add(category)
                            }
                        }
                        val context = context
                        if (context != null) {
                            recyclerView.adapter =
                                ViewAllCategoryAdapter(
                                    viewAllCategoryModelArrayList,
                                   context
                                )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        //get suspension
        else if (type != null && type.equals("suspension", ignoreCase = true)) {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            query = dbref.equalTo("suspension", "type")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {
                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            if (category?.type?.equals("suspension", ignoreCase = true) == true) {
                                viewAllCategoryModelArrayList.add(category)
                            }
                        }
                        val context = context
                        if (context != null) {
                            recyclerView.adapter =
                                ViewAllCategoryAdapter(
                                    viewAllCategoryModelArrayList,
                                    context
                                )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }

        //get wiper blade
        else if (type != null && type.equals("wiper blade", ignoreCase = true)) {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            query = dbref.equalTo("wiper blade", "type")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {
                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            if (category?.type?.equals("wiper blade", ignoreCase = true) == true) {
                                viewAllCategoryModelArrayList.add(category)
                            }
                        }
                        val context = context
                        if (context != null) {
                            recyclerView.adapter =
                                ViewAllCategoryAdapter(
                                    viewAllCategoryModelArrayList,
                                    context
                                )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        } else {
            dbref =
                FirebaseDatabase.getInstance("https://latestcarpartsdatabase-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("ViewAllCategory")

            dbref.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (categorySnapshot in snapshot.children) {

                            val category =
                                categorySnapshot.getValue(ViewAllCategoryModel::class.java)
                            viewAllCategoryModelArrayList.add(category!!)
                        }
                        val context = context
                        if (context != null) {
                            recyclerView.adapter =
                                ViewAllCategoryAdapter(
                                    viewAllCategoryModelArrayList,
                                    context
                                )
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}