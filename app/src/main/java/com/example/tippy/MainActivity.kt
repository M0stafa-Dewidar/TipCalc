package com.example.tippy

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentage.text = "$INITIAL_TIP_PERCENT%"
        updateTipDescription(INITIAL_TIP_PERCENT)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                Log.i(TAG, "OnProgressChange $progress%")
                tvTipPercentage.text = "$progress%"
                updateTipDescription(progress)
                computeTipAndTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?){}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}

        })

        etBase.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChanged $s")
                computeTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })
    }

    private fun computeTipAndTotal(){
        if(etBase.text.toString().isEmpty()){
            tvTotalRight.text = " "
            tvTipRight.text = " "
            return
        }
        val baseAmount = etBase.text.toString().toDouble()
        val tipPercent = seekBarTip.progress.toDouble()
        val tipAmount = baseAmount * (tipPercent/100)
        val totalAmount = baseAmount + tipAmount
        tvTotalRight.text = "%2f".format(totalAmount)
        tvTipRight.text = "%2f".format(tipAmount)
    }

    private fun updateTipDescription(tipPercent: Int){
        var tipDescription : String = " "
        when(tipPercent){
            in 0..9 -> tipDescription = "Have you no sense of decency?"
            in 10..25 -> tipDescription = "Well, that's more like it."
            in 26..50 -> tipDescription = "Would you like to try our special dessert?"
            in 51..70 -> tipDescription = "You worked in the service industry before?"
            in 71..85 -> tipDescription = "You sure you didn't add an extra digit ?"
            in 85..100 -> tipDescription = "GOD BLESS THE UNITED STATES OF AMERICA!"
        }
        tvTipDescription.text = tipDescription
        val color = ArgbEvaluator().evaluate(tipPercent.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this, R.color.colorWorstTip),
            ContextCompat.getColor(this, R.color.colorBestTip)) as Int
        tvTipDescription.setTextColor(color)
    }
}
