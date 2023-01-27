package ba.etf.rma21.projekat.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.MainActivity
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.viewmodel.GroupViewModel
import ba.etf.rma21.projekat.viewmodel.KvizListViewModel
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel
import ba.etf.rma21.projekat.view.KvizListAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentKvizovi : Fragment() {
    private lateinit var kvizoviNosac: RecyclerView
    private lateinit var kvizoviAdapt: KvizListAdapter
    private var kvizListViewModel = KvizListViewModel()
    private var pitanjaKvizViewModel = PitanjeKvizViewModel()
    private var groupViewModel = GroupViewModel()
    private lateinit var filterKvizova: Spinner

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_kvizovi, container, false)

        filterKvizova = view.findViewById(R.id.filterKvizova)

        ArrayAdapter.createFromResource(
            view.context,
            R.array.filtriranje,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            filterKvizova.adapter = adapter
        }
        kvizoviNosac = view.findViewById(R.id.listaKvizova)
        kvizoviNosac.layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)
        kvizoviAdapt = KvizListAdapter(arrayListOf()) { kviz ->
            prikaziKviz(kviz)
        }
        kvizoviNosac.adapter = kvizoviAdapt
        filterKvizova.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                updateKvizovi()
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateKvizovi()
            }
        }
        napraviBottomNav()
        return view
    }

    companion object {
        fun newInstance(): FragmentKvizovi = FragmentKvizovi()
    }



    private fun updateKvizovi(){
        if(filterKvizova.selectedItem.toString() == "Svi kvizovi") kvizListViewModel.getQuizzes(onSuccess = ::onSuccess, onError = ::onError)
        else groupViewModel.updateNow(onSuccess = ::onUpdate, onError = ::onError)

    }

    override fun onResume() {
        super.onResume()
        updateKvizovi()
    }

    private fun prikaziKviz(kviz: Kviz) {
        if(filterKvizova.selectedItem.toString() != "Svi kvizovi" && kvizListViewModel.getStatus(kviz) != "zuta") {
            KvizRepository.pokrenutiKviz = kviz
            kvizListViewModel.getPocetiKvizoviApp(kviz, onSuccess = ::onSuccess, onError = ::onError)
        }
    }



    fun onSuccess(rezultat: Boolean, kviz: Kviz){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(rezultat)
                    pitanjaKvizViewModel.getPitanja(kviz.id, onSuccess = ::onSuccessDruga, onError = ::onError)
            }
        }
    }

    fun onSuccess(quizzes:List<Kviz>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                kvizoviAdapt.updateKvizove(quizzes)
            }
        }
    }

    fun onSuccessDruga(pitanja:List<Pitanje>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val fragment = FragmentPokusaj(pitanja)
                var fr = getFragmentManager()?.beginTransaction()
                fr?.replace(R.id.container, fragment)
                fr?.commit()
            }
        }
    }


    fun onUpdate(){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(filterKvizova.selectedItem.toString() == "Svi moji kvizovi")
                    kvizListViewModel.getMyQuizzes(
                        onSuccess = ::onSuccess,
                        onError = ::onError)
                else if(filterKvizova.selectedItem.toString() == "Urađeni kvizovi")
                    kvizListViewModel.getDoneQuizzes(
                        onSuccess = ::onSuccess,
                        onError = ::onError)
                else if(filterKvizova.selectedItem.toString() == "Budući kvizovi")
                    kvizListViewModel.getFutureQuizzes(
                        onSuccess = ::onSuccess,
                        onError = ::onError)
                else  kvizListViewModel.getPastQuizzes(
                    onSuccess = ::onSuccess,
                    onError = ::onError)
            }
        }
    }






    fun onError() {
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val toast = Toast.makeText(context, "Greska", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }


    private fun napraviBottomNav() {
        MainActivity.bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = true
        MainActivity.bottomNavigation.menu.findItem(R.id.predmeti).isVisible = true
        MainActivity.bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = false
        MainActivity.bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = false
    }

}