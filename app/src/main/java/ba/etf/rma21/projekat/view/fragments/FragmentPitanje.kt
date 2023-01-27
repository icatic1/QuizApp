package ba.etf.rma21.projekat.view.fragments


import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import androidx.core.view.children
import androidx.core.view.size

import androidx.fragment.app.Fragment


import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.view.ItemIzabran
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FragmentPitanje(private val pitanje: Pitanje): Fragment() {

    private lateinit var tekstPitanja: TextView
    private lateinit var odgovori: ListView
    private lateinit var adapter: ArrayAdapter<String>
    private var pitanjeKvizViewModel = PitanjeKvizViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_pitanje, container, false)
        tekstPitanja = view.findViewById(R.id.tekstPitanja)
        odgovori = view.findViewById(R.id.odgovoriLista)
        tekstPitanja.text = pitanje.tekstPitanja
        adapter = ArrayAdapter(view.context, android.R.layout.simple_list_item_1, pitanje.opcije)
        odgovori.adapter = adapter
        odgovori.onItemClickListener = ItemIzabran(pitanje)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler().postDelayed({
            while(KvizRepository.pokusajKviza == null);
                KvizRepository.pokusajKviza?.id?.let {
                    pitanjeKvizViewModel.getOdgovorApp(
                        it,
                        pitanje.id,
                        onSuccess = ::onSuccess,
                        onError = ::onError
                    )
                }
        }, 1)

    }


    /*fun izbor(parent : AdapterView<*>, position:Int){
        var activity = activity as MainActivity
        if(position==pitanje.tacan){
            parent.getChildAt(position).setBackgroundColor(resources.getColor(R.color.tacan))
            activity.sharedViewModel.setBojaLive("zelena")
            var pom = activity.sharedViewModel.zavrsenKviz.toString()
        }
        else {
            parent.getChildAt(position).setBackgroundColor(resources.getColor(R.color.netacan))
            parent.getChildAt(pitanje.tacan).setBackgroundColor(resources.getColor(R.color.tacan))
            activity.sharedViewModel.setBojaLive("crvena")
        }

        listView.isEnabled=false
    }*/

    fun onSuccess(odgovor: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                if (odgovori.getChildAt(0) != null) {
                    if (odgovor != -1) {
                        val odgovorTacan = odgovori.getChildAt(pitanje.tacan) as TextView
                        lateinit var odgovorPogresan: TextView
                        if(odgovor != odgovori.size) odgovorPogresan = odgovori.getChildAt(odgovor) as TextView
                        odgovorTacan.setTextColor(Color.parseColor("#000000"))
                        odgovori.getChildAt(pitanje.tacan).setBackgroundColor(Color.parseColor("#3DDC84"))
                        if (pitanje.tacan != odgovor && odgovor != odgovori.size) {
                                odgovorPogresan.setTextColor(Color.parseColor("#000000"))
                            odgovori.getChildAt(odgovor).setBackgroundColor(Color.parseColor("#DB4F3D"))
                        }
                        for (odabir in odgovori.children) {
                            odabir.isEnabled = false
                            odabir.setOnClickListener(null)
                        }
                    }
                }
            }
        }
    }

    fun onError() {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val toast = Toast.makeText(context, "Ne valja", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}
