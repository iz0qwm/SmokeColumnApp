package com.kwos.smokecolumnapp

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.text.util.Linkify
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat


class InfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        val textInfo = findViewById<TextView>(R.id.textInfo)
        textInfo.text = HtmlCompat.fromHtml("""
<p>Questa App &egrave; di ausilio alle <strong>OdV di Protezione Civile</strong> <em>interessate nel monitoraggio delle colonne di fumo per la campagna A.I.B. Antincendio Boschivo.</em></p>
<p><strong><em>Grazie all'utilizzo dei droni &egrave; possibile stabilire le coordinate stimate della colonna di fumo.</em></strong></p>
<p>L'App si basa su <em>due algoritmi</em>:</p>
<ol style="list-style-type: upper-alpha;">
<li>(A) utilizza due droni o un drone che si sposta in due posizioni differenti mentre punta il target.<br />Effettua una triangolazione.</li>
<li>(B) utilizza il campo visivo (FOV) della camera del drone e la stima in metri della dimensione della colonna da parte dell'operatore.</li>
</ol>
<p style="text-align: center;"><strong>MODALITA' OPERATIVE</strong></p>
<ol>
<li>1) Sul display del radiocomando selezionare la modalit&agrave; foto 4:3 e attivare la X sul display per facilitare il posizionamento della colonna di fumo al centro della ripresa.</li>
<li>2) Per l'algoritmo (A) scattare due fotografie facendo attenzione a centrare sempre il target, da due posizioni differenti rispetto allo stesso, lontane tra loro almeno 200m, cercando di girargli intorno.<br />Per l'algoritmo (B) scattare una fotografia possibilmente con l'obiettivo zoom (70mm)</li>
<li>3) Per l'algoritmo (A) cliccare sulle icone immagine e far caricare le informazioni dalle due fotografie<br />Per l'algoritmo (B) cliccare sull'icona immagine e far caricare le informazioni, selezionare il FOV del vostro drone/camera utilizzati per la fotografia.</li>
<li>4) Solo per l'algoritmo (B), sull'immagine, caricata in basso, stabilire le dimensioni in pixel della colonna di fumo, puntando prima il dito a sinistra della stessa e poi a destra (si former&agrave; una linea e verr&agrave; letta la dimensione in pixel.)</li>
<li>5) Premi "Calcola" per ottenere la posizione della colonna di fumo.</li>
<li>6) Premi "Mostra Mappa" per visualizzare il risultato.</li>
</ol>
<p>Maggiori informazioni su: https://tinyurl.com/doescolonnafumo<br /><br /><em>Copyright</em><br /><em>@ 2025 Raffaello Di Martino KWOS</em></p>
        """.trimIndent(), HtmlCompat.FROM_HTML_MODE_LEGACY)

        supportActionBar?.hide()
        textInfo.autoLinkMask = Linkify.WEB_URLS
        textInfo.movementMethod = LinkMovementMethod.getInstance()
    }
}
