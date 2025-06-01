package com.example.project

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
//import com.example.project.adapters.HistoryAdapter
//import com.example.project.models.UserProfile
//import com.example.project.sqlite.HistoryDatabaseHelper
import java.text.SimpleDateFormat
import java.util.*

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistoryAdapter
    private lateinit var emptyText: TextView
    private lateinit var spinner: Spinner

    //private lateinit var dbHelper: HistoryDatabaseHelper
    //private var historyList = mutableListOf<UserProfile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        /*setSupportActionBar(findViewById(R.id.history_toolbar))
        recyclerView = findViewById(R.id.history_recycler)
        emptyText = findViewById(R.id.empty_text)
        spinner = findViewById(R.id.filter_spinner)
        dbHelper = HistoryDatabaseHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter(this, historyList, onClick = { openProfile(it) }, onDelete = { deleteHistory(it) })
        recyclerView.adapter = adapter

        initSwipeToDelete()
        setupSpinner()
        fetchHistory()*/
    }
    /*

        private fun setupSpinner() {
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                    when (pos) {
                        0 -> fetchHistory()
                        1 -> fetchLast7Days()
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        }
     private fun fetchHistory() {
            historyList.clear()
            historyList.addAll(dbHelper.getAllHistorySortedByRecent())
            updateUI()
        }

        private fun fetchLast7Days() {
            historyList.clear()
            historyList.addAll(dbHelper.getLast7DaysHistory())
            updateUI()
        }

        private fun updateUI() {
            adapter.notifyDataSetChanged()
            emptyText.visibility = if (historyList.isEmpty()) View.VISIBLE else View.GONE
        }

        private fun openProfile(profile: UserProfile) {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("profile_id", profile.id)
            startActivity(intent)
        }

        private fun deleteHistory(profile: UserProfile) {
            dbHelper.deleteHistoryByUserId(profile.id)
            historyList.remove(profile)
            updateUI()
            Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show()
        }

        private fun initSwipeToDelete() {
            val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val profile = historyList[viewHolder.adapterPosition]
                    deleteHistory(profile)
                }
            })
            itemTouchHelper.attachToRecyclerView(recyclerView)
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.history_menu, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            if (item.itemId == R.id.action_clear_all) {
                confirmDeleteAll()
                return true
            }
            return super.onOptionsItemSelected(item)
        }

        private fun confirmDeleteAll() {
            AlertDialog.Builder(this)
                .setTitle("Clear All History")
                .setMessage("Are you sure you want to delete all your history?")
                .setPositiveButton("Yes") { _, _ ->
                    dbHelper.clearAllHistory()
                    historyList.clear()
                    updateUI()
                    Toast.makeText(this, "All history cleared", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }*/
}