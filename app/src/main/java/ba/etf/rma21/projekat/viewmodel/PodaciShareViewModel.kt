package ba.etf.rma21.projekat.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PodaciShareViewModel : ViewModel(){

        var godina:Int=0
        var predmet:Int=0
        var grupa:Int=0
        var kvizNaziv:String = ""

        val bojaLive = MutableLiveData<String>()
        fun setBojaLive(item: String) {
                bojaLive.value = item
        }

        val zavrsenKviz = MutableLiveData<String>()
        fun setZavrsenKviz(item: String) {
                zavrsenKviz.value = item
        }



}