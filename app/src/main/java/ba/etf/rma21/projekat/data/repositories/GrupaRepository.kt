package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GrupaRepository {
    companion object {
        suspend fun getGroupsByPredmet(predmet: Predmet): List<Grupa> {
            return withContext(Dispatchers.IO){
                var rez: MutableList<Grupa> = ApiAdapter.retrofit.getGrupeZaPredmet(predmet.id) as MutableList<Grupa>
                rez.add(0, Grupa(-1, "", -1))
                return@withContext rez
            }
        }
    }
}