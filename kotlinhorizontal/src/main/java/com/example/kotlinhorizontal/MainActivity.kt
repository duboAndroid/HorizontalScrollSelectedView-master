package com.example.kotlinhorizontal

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private var horizontal: HorizontalScrollView? = null
    private var list = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        horizontal = findViewById(R.id.horizontal_scroll) as HorizontalScrollView

        for (i in 1..10) {
            list!!.add(i.toString() + "000")
        }
        horizontal!!.dates(list!!)
    }
}
