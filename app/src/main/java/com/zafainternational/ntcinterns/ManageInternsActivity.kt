package com.zafainternational.ntcinterns

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ManageInternsActivity : AppCompatActivity() {

    private lateinit var database: DatabaseHelper
    private lateinit var internList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_manage_interns
        )

        database =
            DatabaseHelper(this)

        internList =
            findViewById(R.id.internList)
    }

    override fun onResume() {

        super.onResume()

        loadInterns()
    }

    private fun loadInterns() {

        val interns =
            database.getInterns()

        val adapter =
            ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                interns
            )

        internList.adapter =
            adapter
    }
}
