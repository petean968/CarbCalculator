/*PRIMA APLICATIE FACUTA VREODATA CU ANDROID STUDIO*/


//AppCompatActivity → pentru activități cu compatibilitate UI.
//Bundle → pentru stocarea și restaurarea stării activității.
//Editable și TextWatcher → pentru a asculta modificările textului.
//EditText → câmp unde utilizatorul introduce date.
//SeekBar → slider pentru procent.
//TextView → afișare text.
//round → pentru a rotunji valorile.

package com.example.carbcalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import kotlin.math.round

class MainActivity : AppCompatActivity() {
    private lateinit var etGreutateAliment: EditText  // Câmpul pentru greutatea alimentului
    private lateinit var seekBarGlucidePercent: SeekBar  // SeekBar-ul pentru procentul de carbohidrați
    private lateinit var percent: TextView  // TextView pentru afișarea procentului
    private lateinit var tvcataGlucidaBag: TextView  // TextView pentru afișarea cantității de carbohidrați

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // Setează layout-ul principal,foloseste activity_main.xml pt. UI
        //legam variabilele de mai sus cu elemente reale din layout(UI)
        etGreutateAliment = findViewById(R.id.etGreutateAliment)  // Găsește EditText-ul pentru greutatea alimentului
        seekBarGlucidePercent = findViewById(R.id.seekBarGlucidePercent)  // Găsește SeekBar-ul pentru procentul de glucide
        percent = findViewById(R.id.percent)  // Găsește TextView-ul pentru procent
        tvcataGlucidaBag = findViewById(R.id.tvcataGlucidaBag)  // Găsește TextView-ul pentru cantitatea de carbohidrați
        //Ascultam modificarile slider-ului(SeekBar) pentru procentul de carbohidrati:
        seekBarGlucidePercent.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            //Această funcție se apelează când utilizatorul schimbă slider-ul:
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //Calculăm procentul actual al slider-ului.progress → valoarea curentă a slider-ului.
                //max → valoarea maximă a slider-ului.Rotunjim procentul la o zecimală pentru afișare mai clară.
                val maxValue = seekBarGlucidePercent.max
                val percentage = progress.toDouble() / maxValue * 100
                val roundedPercentage = round(percentage * 10) / 10.0  // Rotunjim la o zecimală
                //Afișăm procentul în TextView.
                //Apelăm funcția care calculează cantitatea de carbohidrați.

                percent.text = "%.1f%%".format(roundedPercentage)  // Afișăm procentul cu o zecimală
                computeTotalCarb()  // Calculează cantitatea totală de carbohidrați
            }
            //Nu facem nimic când utilizatorul începe sau termină să miște slider-ul,
            // dar metodele trebuie implementate.
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
            //Partea de UI – EditText:
        //Ascultam modificarile din campul de text:
        etGreutateAliment.addTextChangedListener(object : TextWatcher {
            //Nu facem nimic înainte sau în timpul modificării textului;
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}
            //După ce textul s-a schimbat, recalculăm cantitatea de carbohidrați:
            override fun afterTextChanged(editable: Editable?) {
                computeTotalCarb()  // Calculează cantitatea totală de carbohidrați
            }
        })
    }

    //Logica calculului carbohidraților
    private fun computeTotalCarb() {
        //Dacă câmpul pentru greutate este gol, nu afișăm nimic și ieșim din funcție.
        if (etGreutateAliment.text.isEmpty()) {
            tvcataGlucidaBag.text = ""
            return
        }
            //Convertim textul în număr. Dacă nu e număr valid, ieșim din funcție.
        val greutateAliment = etGreutateAliment.text.toString().toDoubleOrNull() ?: return
            //Calculăm procentul de carbohidrați pe baza slider-ului.
        val percent = seekBarGlucidePercent.progress.toDouble() / seekBarGlucidePercent.max * 100
            //Formula pentru cantitatea de carbohidrați:
            //cantitate carbohidrați = greutate aliment × procent / 100
        val cataGlucidaBag = greutateAliment * percent / 100

        // Afișează cantitatea totală de carbohidrați in TextView, cu două zecimale
        tvcataGlucidaBag.text = String.format("%.2f g", cataGlucidaBag)
    }
}
//UI:
//EditText pentru greutatea alimentului
//SeekBar pentru procentul de carbohidrați
//TextView pentru procent și cantitatea calculată

//Logică:
//Când se schimbă EditText sau SeekBar, se apelează computeTotalCarb().
//Se calculează cantitatea de carbohidrați și se afișează.

