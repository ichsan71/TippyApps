package com.marqumil.tippy

import android.animation.ArgbEvaluator
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat

private const val TAG = "MainActivity"
private const val INITIAL_TIP_PERCENT = 15


class MainActivity : AppCompatActivity() {
    private lateinit var etBaseAmount: EditText;
    private lateinit var seekBarTip: SeekBar;
    private lateinit var tvBaseLabel: TextView;
    private lateinit var tvTipPercentLabel: TextView;
    private lateinit var tvTipLabel: TextView;
    private lateinit var tvTotalLabel: TextView;
    private lateinit var tvTipAmount: TextView;
    private lateinit var tvTotalAmount: TextView;
    private lateinit var tvTipDescription: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etBaseAmount = findViewById(R.id.etBaseAmount)
        seekBarTip = findViewById(R.id.seekBarTip)
        tvBaseLabel = findViewById(R.id.tvBaseLabel)
        tvTipPercentLabel = findViewById(R.id.tvTipPercentLabel)
        tvTipLabel = findViewById(R.id.tvTipLabel)
        tvTotalLabel = findViewById(R.id.tvTotalLabel)
        tvTipAmount = findViewById(R.id.tvTipAmount)
        tvTotalAmount = findViewById(R.id.tvTotalAmount)
        tvTipDescription = findViewById(R.id.tvTipDescription)

        seekBarTip.progress = INITIAL_TIP_PERCENT
        tvTipPercentLabel.text = "$INITIAL_TIP_PERCENT%"
        updateTipDesc(INITIAL_TIP_PERCENT)

        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
               Log.i(TAG, "onProgressChange $progress")
               tvTipPercentLabel.text = "$progress%"
                computeTipAndTotal()
                updateTipDesc(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        etBaseAmount.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                Log.i(TAG, "afterTextChange $s")
                computeTipAndTotal()
            }

        })

    }

    private fun updateTipDesc(tipPercent: Int) {
        val tipDesc = when (tipPercent){
            in 0..9 -> "Pelit"
            in 10..14 -> "Sedikit"
            in 15..19 -> "Mayan lah"
            in 20..24 -> "Mantull"
            else -> "Sultann!"
        }
        tvTipDescription.text = tipDesc

        // update color based on percent
        val color = ArgbEvaluator().evaluate(
            tipPercent.toFloat() / seekBarTip.max,
            ContextCompat.getColor(this, R.color.merah),
            ContextCompat.getColor(this, R.color.ijo)
        ) as Int

        tvTipDescription.setTextColor(color)
    }

    private fun computeTipAndTotal() {
        if (etBaseAmount.text.isEmpty()){
            tvBaseLabel.text = ""
            tvTotalAmount.text = ""
            return
        }

        // get the value of base and tip percent
        val base = etBaseAmount.text.toString().toDouble()
        val tipPercent = seekBarTip.progress

        // compute the tip and total
        val tipAmount = base * tipPercent/100
        val totalAmount =  base + tipAmount

        // update ui
        tvTipAmount.text = "%.2f".format(tipAmount)
        tvTotalAmount.text = "%.2f".format(totalAmount)
    }


}