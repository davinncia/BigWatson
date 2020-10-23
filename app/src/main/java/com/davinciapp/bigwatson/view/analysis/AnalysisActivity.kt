package com.davinciapp.bigwatson.view.analysis

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.davinciapp.bigwatson.R
import com.davinciapp.bigwatson.bind
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnalysisActivity : AppCompatActivity() {

    private lateinit var viewModel: AnalysisViewModel

    private val fab by bind<FloatingActionButton>(R.id.fab_analysis)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_analysis)

        //setRadarChart(List(5) {Pair("", 0)}) // Showing empty chart on start
        viewModel = ViewModelProvider(this)[AnalysisViewModel::class.java]

        if (intent.hasExtra(FAV_KEY)) {
            viewModel.setSearchStrategy(intent.getBooleanExtra(FAV_KEY, false))
        }

        activateFabClick()

        viewModel.message.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        })

        viewModel.userLiveData.observe(this, Observer {
            findViewById<TextView>(R.id.tv_user_name_profile_header).text = it.displayName
            Glide.with(this).load(it.imageUrl).circleCrop().into(findViewById(R.id.iv_profile_header))
            findViewById<ImageView>(R.id.iv_verified_badge_profile_header).apply {
                if (it.isVerified) visibility = View.VISIBLE
                else View.GONE
            }
        })

        viewModel.userStoredLiveData.observe(this) {
            fab.clearAnimation()
            activateFabClick()

            fab.apply {
                if (it) setImageResource(R.drawable.ic_people_full)
                else setImageResource(R.drawable.ic_people)
            }
        }

        viewModel.personalityLiveData.observe(this, Observer {
            setRadarChart(it)
        })


    }

    private fun activateFabClick() {
        fab.setOnClickListener {
            viewModel.handleUserStorage()
            fab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_clockwise))
            fab.setOnClickListener(null) //Waiting for db action to finish before activation
        }
    }


    private fun setRadarChart(watsonData: List<Pair<String, Int>>) {
        val radarChart = findViewById<RadarChart>(R.id.radar_chart_analysis)
        radarChart.visibility = View.VISIBLE

        val labels = Array(watsonData.size) { i -> watsonData[i].first}
        val entries = List(watsonData.size) { i -> RadarEntry(watsonData[i].second.toFloat())}

        // Options
        radarChart.legend.isEnabled = false
        radarChart.description.isEnabled = false
        radarChart.webAlpha = 80

        radarChart.yAxis.axisMinimum = 0F
        radarChart.yAxis.setDrawLabels(false)
        radarChart.yAxis.axisMaximum = 80F

        radarChart.xAxis.textSize = 8F
        radarChart.xAxis.typeface = ResourcesCompat.getFont(this, R.font.athiti_medium)
        radarChart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return labels[value.toInt()]
            }

        }

        val dataSet = RadarDataSet(entries, "")
        dataSet.lineWidth = 2F
        dataSet.setDrawFilled(true)

        val data = RadarData(dataSet)
        data.setValueTypeface(ResourcesCompat.getFont(this, R.font.athiti_medium))
        data.setValueTextSize(11F)

        radarChart.data = data

        radarChart.animateY(2000, Easing.EaseInSine)
    }

    companion object {

        private const val FAV_KEY = "fav_key"

        fun newIntent(context: Context, isFavorite: Boolean) =
            Intent(context, AnalysisActivity::class.java).apply {
                putExtra(FAV_KEY, isFavorite)
            }
    }

}