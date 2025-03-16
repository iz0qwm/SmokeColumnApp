package com.kwos.smokecolumnapp

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val textInfo = findViewById<TextView>(R.id.textInfo)
        textInfo.text = """
            Questa App è di ausilio alle OdV di Protezione Civile interessate nel monitoraggio delle colonne di fumo per la campagna A.I.B. Antincendio Boschivo. 
            
            Grazie all'utilizzo dei droni è possibile stabilire le coordinate stimate della colonna di fumo.
            
            L'App si basa su due algoritmi:
            1. utilizza due droni o un drone che si sposta in due posizioni differenti mentre punta il target.
            Effettua una triangolazione.
            
            2. utilizza il campo visivo (FOV) della camera del drone e la stima in metri della dimensione della colonna da parte dell'operatore.
            
            Per entrambi gli algoritmi è necessario inserire anche le coordinate del o dei droni e l'angolo rispetto al nord verso cui si osserva la colonna di fumo (heading).
            Sul display del radiocomando selezionare la modalità foto 4:3 e attivare la X sul display per facilitare il posizionamento della colonna di fumo al centro della ripresa.
            
            Premi "Calcola" per ottenere la posizione della colonna di fumo.
            Premi "Mostra Mappa" per visualizzare il risultato.
            
            Maggiori informazioni su: https://tinyurl.com/doescolonnafumo
            
            Copyright
            @ 2025 Raffaello Di Martino KWOS
        """.trimIndent()

        supportActionBar?.hide()
        textInfo.autoLinkMask = Linkify.WEB_URLS
        textInfo.movementMethod = LinkMovementMethod.getInstance()
    }
}
