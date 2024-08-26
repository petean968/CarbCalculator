package com.example.carbcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.StateSet.TAG
private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var etGreutateAliment: EditText
    private lateinit var seekBarGlucidePercent: SeekBar
    private lateinit var percent: TextView
    private lateinit var tvcataGlucidaBag: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etGreutateAliment = findViewById(R.id.etGreutateAliment)
        seekBarGlucidePercent = findViewById(R.id.seekBarGlucidePercent)
        percent = findViewById(R.id.percent)
        tvcataGlucidaBag = findViewById(R.id.tvcataGlucidaBag)

        seekBarGlucidePercent.setOnSeekBarChangeListener ( object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {


                //Log.i(TAG,"onProgressChanged $p1")//
                Log.i(TAG , "onProgressChanged $p1")


                percent.text = "$p1%"
                computeTotalCarb()


            }


            override fun onStartTrackingTouch(p0: SeekBar?) {}


            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
        etGreutateAliment.addTextChangedListener(object: TextWatcher{

            override fun beforeTextChanged(b0: CharSequence? , b1: Int, b2: Int, b3: Int){}

            override fun onTextChanged(b0: CharSequence? ,b1: Int, b2: Int, b3: Int) {}

            override fun afterTextChanged(b0: Editable?){


             Log.i(TAG,"afterTextChanged $b0")

                computeTotalCarb()



            }


        })


    }
    private fun computeTotalCarb() {
    if (etGreutateAliment.text.isEmpty()) {
        tvcataGlucidaBag.text=""
        return
    }
        val GreutateAliment=etGreutateAliment.text.toString().toDouble()
        val percent=seekBarGlucidePercent.progress
        val cataGlucidaBag=GreutateAliment*percent/100

        tvcataGlucidaBag.text=cataGlucidaBag.toString()
    }
}
