package ba.etf.rma21.projekat.view.fragments

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.viewmodel.GroupViewModel
import ba.etf.rma21.projekat.viewmodel.PitanjeKvizViewModel
import ba.etf.rma21.projekat.viewmodel.PredmetViewModel
import ba.etf.rma21.projekat.viewmodel.PodaciShareViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentPredmeti: Fragment() {
    private lateinit var godinespin: Spinner
    private lateinit var predmetspin: Spinner
    private lateinit var grupaspin: Spinner
    private lateinit var btnUpis: Button


    private var predmetViewModel = PredmetViewModel()
    private var grupaViewModel = GroupViewModel()
    private var pitanjeKvizListViewModel = PitanjeKvizViewModel()

    private var prviPutPredmet = true
    private var prviPutGrupa = true
    private lateinit var view1: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view1 = inflater.inflate(R.layout.fragment_predmeti, container, false)
        godinespin = view1.findViewById(R.id.odabirGodina)
        predmetspin = view1.findViewById(R.id.odabirPredmet)
        grupaspin = view1.findViewById(R.id.odabirGrupa)

        ArrayAdapter.createFromResource(
            view1.context,
            R.array.godine,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            godinespin.adapter = adapter
        }

        godinespin.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                if(godinespin.selectedItem.toString() != "") {
                    predmetspin.isEnabled = true
                    updatePredmete()

                    if(!prviPutGrupa) {
                        updateGrupe()
                        pitanjeKvizListViewModel.setOdabranaGrupa(-1)
                    }

                    if(!prviPutPredmet) pitanjeKvizListViewModel.setOdabraniPredmet(-1)

                    pitanjeKvizListViewModel.setOdabranaGodina(godinespin.selectedItemPosition)
                }
                else {
                    predmetspin.isEnabled = false
                    grupaspin.isEnabled = false
                }
                prepraviZaUpis()
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                //nista
            }
        }

        if(pitanjeKvizListViewModel.getOdabranaGodina() != -1)
            godinespin.setSelection(pitanjeKvizListViewModel.getOdabranaGodina())

        predmetspin.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                if(predmetspin.selectedItem.toString() != "") {
                    grupaspin.isEnabled = true
                    updateGrupe()
                    if(!prviPutGrupa)
                        pitanjeKvizListViewModel.setOdabranaGrupa(-1)

                    pitanjeKvizListViewModel.setOdabraniPredmet(predmetspin.selectedItemPosition)
                }
                else {
                    updateGrupe()
                    grupaspin.isEnabled = false
                }
                prepraviZaUpis()
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                //nista
            }
        }

        grupaspin.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long){
                prepraviZaUpis()
                pitanjeKvizListViewModel.setOdabranaGrupa(grupaspin.selectedItemPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                //nista
            }
        }

        btnUpis = view1.findViewById(R.id.dodajPredmetDugme)
        btnUpis.setOnClickListener {

            grupaViewModel.upisUGrupu((grupaspin.selectedItem as Grupa).id, onSuccess = ::onSuccessPoruka, onError = ::onError)

        }

        return view1
    }
    private var sharedViewModel = PodaciShareViewModel()

    private fun updateScreen() {
        var predmetSpin = view?.findViewById<Spinner>(R.id.odabirPredmet)
        var grupaSpin = view?.findViewById<Spinner>(R.id.odabirGrupa)
        val fragment2 = FragmentPoruka.newInstance("Uspješno ste upisani u grupu ${grupaSpin!!.selectedItem} predmeta ${predmetSpin!!.selectedItem}!")
        sharedViewModel.godina= 0
        sharedViewModel.predmet= 0
        sharedViewModel.grupa= 0
        fragmentManager?.beginTransaction()?.replace(R.id.container, fragment2)?.commit()
    }

    companion object {
        fun newInstance(): FragmentPredmeti = FragmentPredmeti()
    }



    private fun prepraviZaUpis(){
        if(godinespin.selectedItem != null && predmetspin.selectedItem != null && grupaspin.selectedItem != null
            && (predmetspin.selectedItem as Predmet).naziv != "" && (grupaspin.selectedItem as Grupa).naziv != ""){
            btnUpis.isClickable = godinespin.selectedItem.toString() != ""
                    && predmetspin.selectedItem.toString() != ""
                    && grupaspin.selectedItem.toString() != ""
        }
        else btnUpis.isClickable = false
    }

    private fun openFragment(fragment: Fragment) {
        val fr = getFragmentManager()?.beginTransaction()
        fr?.replace(R.id.container, fragment)
        fr?.commit()
    }

    fun onSuccessGlavna(predmeti1:List<Predmet>){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val adapter = ArrayAdapter(
                    view1.context,
                    android.R.layout.simple_spinner_item,
                    predmeti1
                )
                adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)

                predmetspin.adapter = adapter
                if(prviPutPredmet && pitanjeKvizListViewModel.getOdabraniPredmet() != -1){
                    predmetspin.setSelection(pitanjeKvizListViewModel.getOdabraniPredmet())
                    prviPutPredmet = false
                }
            }
        }
    }

    fun updatePredmetSpinner(predmeti: ArrayList<Predmet>){
        var predmetSpin = view?.findViewById<Spinner>(R.id.odabirPredmet)
        var opcijeP = predmeti//predmetViewModel.getZaGodinu(1) as ArrayList<Predmet>
        var imena: ArrayList<String> = ArrayList()
        for (x in opcijeP) imena.add(x.naziv!!)
        var adp1: ArrayAdapter<String> = ArrayAdapter<String>(
            view?.context!!,
            android.R.layout.simple_list_item_1,
            imena
        )
        if (predmetSpin != null) {
            predmetSpin.adapter = adp1
        }

        /*grupaViewModel.getGroupsByPredmet(
            onSuccess = ::onSuccessGrupa,
            onError = ::onError,
            nazivPredmeta = imena[0]
        )*/

        if(opcijeP.size.equals(0))btnUpis.setEnabled(false)
        else btnUpis.setEnabled(true)
    }

    fun updateGrupeSpinner(grupe: ArrayList<Grupa>){
        var grupaSpin = view?.findViewById<Spinner>(R.id.odabirGrupa)
        var opcijeG = grupe //groupViewModel.getGroupsByPredmet(imena[0]) as ArrayList<Grupa>
        var imenaG: ArrayList<String> = ArrayList()
        for (x in opcijeG) imenaG.add(x.naziv!!)
        val adp2: ArrayAdapter<String> = ArrayAdapter<String>(
            view?.context!!,
            android.R.layout.simple_list_item_1,
            imenaG
        )
        grupaSpin?.adapter = adp2
    }





    fun onSuccessGrupe(grupe: List<Grupa>) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                val adapter1 = ArrayAdapter(
                    view1.context,
                    android.R.layout.simple_spinner_item,
                    grupe
                )
                adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
                grupaspin.adapter = adapter1

                if (prviPutGrupa && pitanjeKvizListViewModel.getOdabranaGrupa() != -1) {
                    grupaspin.setSelection(pitanjeKvizListViewModel.getOdabranaGrupa())
                    prviPutGrupa = false
                }
            }

        }
    }

    fun onSuccessPoruka(upisan: Boolean){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                if(upisan){
                    pitanjeKvizListViewModel.setOdabranaGodina(-1)
                    pitanjeKvizListViewModel.setOdabraniPredmet(-1)
                    pitanjeKvizListViewModel.setOdabranaGrupa(-1)
                    val nazivGrupe1 = grupaspin.selectedItem.toString()
                    val nazivPredmeta1 = predmetspin.selectedItem.toString()
                    val porukicaFragment = FragmentPoruka.newInstance("Uspješno ste upisani u grupu $nazivGrupe1 predmeta $nazivPredmeta1!")
                    openFragment(porukicaFragment)
                }else onError()
            }
        }
    }


    private fun updatePredmete(){
        predmetViewModel.getPredmetsByGodina(godinespin.selectedItem.toString().toInt(), onSuccess = ::onSuccessGlavna, onError = ::onError)
    }

    private fun updateGrupe(){
        if ((predmetspin.selectedItem == null) || (predmetspin.selectedItem as Predmet).naziv == "") {
            val grupe = emptyArray<String>()
            val adapter1 = ArrayAdapter(
                view1.context,
                android.R.layout.simple_spinner_item,
                grupe
            )
            adapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
            grupaspin.adapter = adapter1
        } else{
            grupaViewModel.getGroupsByPredmet(predmetspin.selectedItem as Predmet, onSuccess = ::onSuccessGrupe, onError = ::onError)
        }
    }

    fun onError() {
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val toast = Toast.makeText(context, "Ne radi", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }
}
