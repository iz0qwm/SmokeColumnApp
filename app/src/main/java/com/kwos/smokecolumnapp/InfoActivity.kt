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

A. utilizza due droni o un drone che si sposta in due posizioni differenti mentre punta il target.
Effettua una triangolazione.
            
B. utilizza il campo visivo (FOV) della camera del drone e la stima in metri della dimensione della colonna da parte dell'operatore.
            
        MODALITA' OPERATIVE
1. 
Sul display del radiocomando selezionare la modalità foto 4:3 e attivare la X sul display per facilitare il posizionamento della colonna di fumo al centro della ripresa.
2. 
Per l'algoritmo (A) scattare due fotografie facendo attenzione a centrare sempre il target, da due posizioni differenti rispetto allo stesso, lontane tra loro almeno 200m, cercando di girargli intorno.
Per l'algoritmo (B) scattare una fotografia possibilmente con l'obiettivo zoom (70mm) 
3. 
Per l'algoritmo (A) cliccare sulle icone immagine e far caricare le informazioni dalle due fotografie
Per l'algoritmo (b) cliccare sull'icona immagine e far caricare le informazioni, selezionare il FOV del vostro drone/camera utilizzati per la fotografia
4.
Premi "Calcola" per ottenere la posizione della colonna di fumo.
5. 
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
