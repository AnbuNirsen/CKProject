package com.example.ckproject.ui.shareHistory

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.ckproject.R
import com.example.ckproject.base.ShareHistoryViewModelFactory
import com.example.ckproject.data.api.ApiHelper
import com.example.ckproject.data.api.RetrofitBuilder
import com.example.ckproject.data.database.ShareHistory
import com.example.ckproject.data.database.ShareHistoryDatabase
import com.example.ckproject.ui.adapter.ShareHistoryAdapter
import com.example.ckproject.utils.Status

class ShareHistoryActivity : AppCompatActivity() {

    private lateinit var viewModel: ShareHistoryViewModel
    private lateinit var adapter: ShareHistoryAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_history)
        setUpViewModel()
        setUpUi()
        setUpObservers()
    }

    private fun setUpViewModel() {
        val shareHistoryDatabase = ShareHistoryDatabase.getInstance(applicationContext)
        viewModel = ViewModelProviders.of(
            this,
            ShareHistoryViewModelFactory(
                ApiHelper(RetrofitBuilder.apiService),
                shareHistoryDatabase
            )
        ).get(ShareHistoryViewModel::class.java)
    }

    private fun setUpUi() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        adapter = ShareHistoryAdapter(this, arrayListOf())
        recyclerView.adapter = adapter
    }

    private fun setUpObservers() {
        viewModel.getShareHistory().observe(this, Observer {
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

    private fun retrieveList(shareHistory: List<ShareHistory>) {
        adapter.apply {
            addShareHistory(shareHistory)
            notifyDataSetChanged()
        }
    }
}