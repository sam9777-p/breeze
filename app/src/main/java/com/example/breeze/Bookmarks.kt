
package com.example.breeze
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.breeze.Data
import com.example.breeze.MyData
import com.google.firebase.database.FirebaseDatabase
import retrofit2.Call
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View

import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class Bookmarks : Fragment(R.layout.bookmarks_fragment) {
   // private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
   // private val newsList = ArrayList<Data>()
    private var list= ArrayList<Data>()
    private lateinit var auth: FirebaseAuth
    //private lateinit var swipeRefreshLayout: SwipeRefreshLayout


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //val view = inflater.inflate(, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        //swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        myAdapter = MyAdapter(requireContext(), list)
        recyclerView.adapter = myAdapter

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: return
        val databaseRef = FirebaseDatabase.getInstance().getReference("Bookmarks").child(userId)

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val bookmarkList = ArrayList<Data>()
                // Loop through all bookmarks

                for (bookmarkSnapshot in snapshot.children) {
                    val data = bookmarkSnapshot.getValue(Data::class.java)
                    if (data != null) {
                        bookmarkList.add(data)
                    }
                }
                myAdapter.updateData(bookmarkList)

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "Failed to load bookmarks: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

    }
}
