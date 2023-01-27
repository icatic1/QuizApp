package ba.etf.rma21.projekat.view

import android.graphics.Color
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.view.children
import ba.etf.rma21.projekat.view.fragments.FragmentPokusaj
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel


class ItemIzabran(val pitanje: Pitanje): AdapterView.OnItemClickListener {

    private var pkw = PitanjeKvizViewModel()
    private val menu: Menu = FragmentPokusaj.navigationView.menu



    private val indeks = pkw.getIndexPitanja()

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        val odgovorTacan = parent?.getChildAt(position) as TextView
        odgovorTacan.setTextColor(Color.parseColor("#000000"))
        parent.getChildAt(pitanje.tacan).setBackgroundColor(Color.parseColor("#3DDC84"))
        var boja: String
        if(position == pitanje.tacan){
            boja="#3DDC84"
        }else{
            val odgovorPogresan = parent?.getChildAt(position) as TextView
            odgovorPogresan.setTextColor(Color.parseColor("#000000"))
            parent.getChildAt(position).setBackgroundColor(Color.parseColor("#DB4F3D"))
            boja="#DB4F3D"

        }

        if(indeks != "") {
            for (i in 0 until menu.size()) {
                val menuItem: MenuItem = menu.getItem(i)
                if (menuItem.title.equals(indeks)) {
                    val spanString =
                        SpannableString(menuItem.title.toString())
                    spanString.setSpan(
                        ForegroundColorSpan(Color.parseColor(boja)),
                        0,
                        spanString.length,
                        0
                    )
                    menuItem.title = spanString
                }
            }
        }


        for(odabir in parent.children){
            odabir.isEnabled = false
            odabir.setOnClickListener(null)
        }




        KvizRepository.pokusajKviza?.id?.let { pkw.postaviOdgovorKviz(it, pitanje.id, position) }
    }

}