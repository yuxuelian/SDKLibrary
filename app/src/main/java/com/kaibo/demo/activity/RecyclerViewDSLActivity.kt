package com.kaibo.demo.activity

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.kaibo.core.adapter.advancedText
import com.kaibo.core.adapter.withItems
import com.kaibo.demo.R
import kotlinx.android.synthetic.main.activity_indicator_manger_test.*
import org.jetbrains.anko.textColor

class RecyclerViewDSLActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view_dsl)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.withItems {
            repeat(50) {
                advancedText("SingleTextItemV2$it") {
                    if (it % 2 == 0) {
                        textColor = Color.RED
                        textSize = 20f
                    } else {
                        textColor = Color.BLUE
                        textSize = 14f
                    }
                }
            }
        }
    }
}
