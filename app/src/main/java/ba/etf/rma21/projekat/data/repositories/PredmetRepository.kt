package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PredmetRepository {

    /* companion object {
        private var sviPredmeti:ArrayList<Predmet> = ArrayList()
        private var upisaniPredmeti:ArrayList<Predmet> = ArrayList()

        init {
            sviPredmeti.addAll(sviPredmeti())
        }

        fun getUpisani(): List<Predmet> {
            return upisaniPredmeti;
        }

        fun getAll(): List<Predmet> {
            return sviPredmeti
        }

        fun getZaGodinu(godina:Int):List<Predmet>{
            return sviPredmeti.stream().filter { t: Predmet -> t.godina.equals(godina) }.collect(Collectors.toList())
        }

        fun upisiNovi(predmet:Predmet){
            if(!upisaniPredmeti.contains(predmet))upisaniPredmeti.add(predmet)
        }
    }

*/


    companion object {

        suspend fun getPredmetiGodina(godina: Int): List<Predmet>{

            return withContext(Dispatchers.IO){
                var rezultat = mutableListOf<Predmet>()
                rezultat.add(Predmet(-1, "", -1))
                for(predmet in ApiAdapter.retrofit.getPredmetiGodina()) {
                    if(predmet.godina == godina)
                        rezultat.add(predmet)
                }

                for(grupa in ApiAdapter.retrofit.getUpisaneGrupe(AccountRepository.getHash())){
                    val naziv = ApiAdapter.retrofit.getPredmetId(grupa.predmetId).naziv
                    var brojac = 0;
                    if(rezultat.stream().anyMatch{x -> x.naziv == naziv}){
                        rezultat.removeAt(brojac+1)
                        brojac++
                    }
                }
                return@withContext rezultat
            }
        }

    }

}