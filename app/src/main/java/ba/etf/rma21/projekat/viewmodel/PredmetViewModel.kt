package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.PredmetRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PredmetViewModel {

    fun getPredmetsByGodina(godina: Int, onSuccess: (predmeti: List<Predmet>) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val pred = PredmetRepository.getPredmetiGodina(godina)){
                is List<Predmet> -> onSuccess?.invoke(pred)
                else -> onError?.invoke()
            }
        }
    }

}