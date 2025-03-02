package com.example.smokecolumnapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val textInfo = findViewById<TextView>(R.id.textInfo)
        textInfo.text = """
            Guida all'uso:
            1. Inserisci le coordinate e gli angoli di ripresa.
            2. Seleziona se stai usando uno o due droni.
            3. Premi "Calcola" per ottenere la posizione del fumo.
            4. Premi "Mostra Mappa" per visualizzare il risultato.
        """.trimIndent()
    }
}
