package ba.etf.rma21.projekat.view.fragments

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class FragmentPokusaj(private var listaPitanja: List<Pitanje>) : Fragment() {

    companion object{
        lateinit var navigationView: NavigationView
    }
    private var pkw = PitanjeKvizViewModel()
    private var kvizViewModel = KvizListViewModel()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_pokusaj, container, false)
        napraviBottomNav()

        navigationView = view.findViewById(R.id.navigacijaPitanja)
        val brojPitanja: Int = listaPitanja.size
        for(i in 1 .. brojPitanja)
            navigationView.menu.add(123456, i-1, i-1, (i).toString())

        for (i in 0 until listaPitanja.size) {
            KvizRepository.pokusajKviza?.id?.let {
                pkw.getOdgovorPokusaj(
                    it, listaPitanja[i].id, i , onSuccess = ::onSuccessMain, onError = ::onError)
            }
        }
        KvizRepository.pokusajKviza?.let { pkw.getZavrsenKviz(it, onSuccess = ::onSuccessPredan, onError = ::onError) }

        navigationView.setNavigationItemSelectedListener { item ->
            if(item.toString() == "Rezultat"){
                KvizRepository.pokusajKviza?.id?.let { pkw.getRezultat(it, onSuccess = ::onSuccessRezultati, onError = ::onError) }
            }
            else {
                when (item.toString().toInt()) {
                    in 1..listaPitanja.size -> {


                        val fragment = FragmentPitanje(listaPitanja[item.toString().toInt() - 1])
                        pkw.setIndexPitanja((item.toString().toInt()).toString())
                        openPitanje(fragment)
                    }

                }
            }
            false
        }
        navigationView.setCheckedItem(0)
        pkw.setIndexPitanja("1")
        val fragment = FragmentPitanje(listaPitanja[0])
        openPitanje(fragment)
        return view
    }




    private fun napraviBottomNav() {
        MainActivity.bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = false
        MainActivity.bottomNavigation.menu.findItem(R.id.predmeti).isVisible = false
        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = true
        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isEnabled = true
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = true

        val krajKviza = MenuItem.OnMenuItemClickListener {
            val kvizoviFragments = FragmentKvizovi.newInstance()
            openOstalo(kvizoviFragments)
            return@OnMenuItemClickListener true
        }

        val predanKviz = MenuItem.OnMenuItemClickListener {
            if(kvizViewModel.getStatus(KvizRepository.pokrenutiKviz) != "crvena") {
                KvizRepository.pokusajKviza?.id?.let { it1 -> pkw.getRezultat(it1, onSuccess = ::onSuccessKraj, onError = ::onError) }
                MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isEnabled = false
            }
            return@OnMenuItemClickListener true
        }

        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).setOnMenuItemClickListener(predanKviz)
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).setOnMenuItemClickListener(krajKviza)
    }

    fun onSuccessMain(odgovor: Int, i :Int) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                if (odgovor == listaPitanja.get(i).tacan) {
                    val menuItem: MenuItem = navigationView.menu.getItem(i)
                    val spanString =
                        SpannableString(menuItem.getTitle().toString())
                    spanString.setSpan(
                        ForegroundColorSpan(Color.parseColor("#3DDC84")),
                        0,
                        spanString.length,
                        0
                    )
                    menuItem.title = spanString
                }
                if ((odgovor != -1 && odgovor != listaPitanja.get(i).tacan)) {
                    val menuItem: MenuItem = navigationView.menu.getItem(i)
                    val spanString =
                        SpannableString(menuItem.title.toString())
                    spanString.setSpan(
                        ForegroundColorSpan(Color.parseColor("#DB4F3D")),
                        0,
                        spanString.length,
                        0
                    )
                    menuItem.title = spanString
                }
            }
        }
    }

    fun onSuccessKraj(rezultat: Int){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                KvizRepository.pokusajKviza?.let { kvizViewModel.zavrsiKviz(it, rezultat , onSuccess = ::onSuccessRezultatStari, onError = ::onError) }
            }
        }
    }




    fun onSuccessRezultatStari(rezultat: Int){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val kvizoviFragments = FragmentPoruka.newInstance("Završili ste kviz " + KvizRepository.pokrenutiKviz.naziv + " sa tačnosti " + rezultat)
                navigationView.menu.clear()

                for(i in 1 .. listaPitanja.size)
                    navigationView.menu.add(123456, i-1, i-1, (i).toString())

                for (i in listaPitanja.indices) {
                    KvizRepository.pokusajKviza?.id?.let {
                        pkw.getOdgovorPokusaj(
                            it, listaPitanja[i].id, i , onSuccess = ::onSuccessMain, onError = ::onError)
                    }
                }
                KvizRepository.pokusajKviza?.let { pkw.getZavrsenKviz(it, onSuccess = ::onSuccessPredan, onError = ::onError) }
                openPitanje(kvizoviFragments)
            }
        }
    }

    fun pokaziPitanje(pitanje: MenuItem): Boolean {
        if(pitanje.title.toString().equals("Rezultat")){
            KvizRepository.pokusajKviza?.id?.let { pkw.getRezultat(it, onSuccess = ::onSuccessRezultatStari, onError = ::onError) }
        }
        else{
            var trenutni = pitanje.title.toString().toInt()-1
            var frag = FragmentPitanje(listaPitanja[pitanje.title.toString().toInt() - 1])
            val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.framePitanje, frag).commit()
        }
        return true
    }


    fun onSuccessRezultati(rezultat: Int){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val kvizoviFragments = FragmentPoruka.newInstance(
                    "Završili ste kviz " + KvizRepository.pokrenutiKviz.naziv + " sa tačnosti " +
                            rezultat
                )
                openPitanje(kvizoviFragments)
            }
        }
    }

    fun onSuccessPredan(rezultat: Boolean){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                if(rezultat){
                    navigationView.menu.add(123456, listaPitanja.size, listaPitanja.size, "Rezultat")
                    MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isEnabled = false
                }
            }
        }
    }

    private fun openPitanje(fragment: Fragment) {
        var fr = getFragmentManager()?.beginTransaction()
        fr?.replace(R.id.framePitanje, fragment)
        fr?.commit()
    }

    private fun openOstalo(fragment: Fragment) {
        var fr = getFragmentManager()?.beginTransaction()
        fr?.replace(R.id.container, fragment)
        fr?.commit()
    }

    private fun otvoriRez(rezultat: Int) {
        var activity = activity as MainActivity
        var frag = FragmentPoruka.newInstance("Završili ste kviz ${KvizRepository.pokrenutiKviz.naziv} sa tačnosti ${rezultat}%")
        val transaction: FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.framePitanje, frag).commit()
        //activity.sharedViewModel.setZavrsenKviz("poruka")
        var godina = Calendar.getInstance().get(Calendar.YEAR)
        var mjesec = Calendar.getInstance().get(Calendar.MONTH) + 1
        var dan = Calendar.getInstance().get(Calendar.DATE)
        KvizRepository.pokrenutiKviz.datumRada = Date(godina,mjesec,dan)
    }

    fun onError() {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val toast = Toast.makeText(context, "Ne radi", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}
