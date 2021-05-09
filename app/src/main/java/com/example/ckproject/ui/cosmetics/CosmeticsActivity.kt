package com.example.ckproject.ui.cosmetics

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.ckproject.R
import com.example.ckproject.base.CosmeticsViewModelFactory
import com.example.ckproject.data.api.ApiHelper
import com.example.ckproject.data.api.RetrofitBuilder
import com.example.ckproject.data.database.ShareHistoryDatabase
import com.example.ckproject.data.model.CosmeticsItem
import com.example.ckproject.data.worker.NotificationWorker
import com.example.ckproject.ui.adapter.CosmeticsAdapter
import com.example.ckproject.ui.shareHistory.ShareHistoryActivity
import com.example.ckproject.utils.Status
import java.util.*
import kotlin.collections.ArrayList

class CosmeticsActivity : AppCompatActivity() {

    private lateinit var viewModel: CosmeticsViewModel
    private lateinit var adapter: CosmeticsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViewModel()
        setUpUi()
        setUpObservers()
        schduleNotificationWorker()

    }

    private fun schduleNotificationWorker() {
        val hourOfTheDay = 10
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfTheDay)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val currentTime = System.currentTimeMillis()
        val specificTimeToTrigger: Long = calendar.timeInMillis


        if(currentTime > specificTimeToTrigger){
            calendar.set(Calendar.HOUR_OF_DAY, 16)
            if(currentTime < calendar.timeInMillis)
            {
                NotificationWorker.runAt(16)
            }
            else{
                NotificationWorker.runAt(10)
            }
        }else{
            NotificationWorker.runAt(hourOfTheDay)
        }

    }

    private fun setUpUi() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        adapter = CosmeticsAdapter(this, arrayListOf())
        recyclerView.adapter = adapter
    }

    private fun setUpObservers() {
        viewModel.getCosmetics().observe(this, Observer {
            it?.let { resources ->
                when (resources.status) {
                    Status.SUCCESS -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        resources.data?.let { cosmetics -> retrieveList(cosmetics) }
                    }
                    Status.ERROR -> {
                        recyclerView.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                }
            }
        })
    }

    private fun retrieveList(cosmetics: ArrayList<CosmeticsItem>) {
        adapter.apply {
            addCosmetics(cosmetics)
            notifyDataSetChanged()
        }
    }

    private fun setUpViewModel() {
        val shareHistoryDatabase = ShareHistoryDatabase.getInstance(applicationContext)
        viewModel = ViewModelProviders.of(
            this,
            CosmeticsViewModelFactory(ApiHelper(RetrofitBuilder.apiService), shareHistoryDatabase)
        ).get(CosmeticsViewModel::class.java)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.history -> {
                openShareHistory()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openShareHistory() {
        startActivity(Intent(this, ShareHistoryActivity::class.java))
    }
}