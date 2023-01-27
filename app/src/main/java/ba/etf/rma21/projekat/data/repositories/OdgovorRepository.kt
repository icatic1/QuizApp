package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.OdgPitBod
import ba.etf.rma21.projekat.data.models.Odgovor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class OdgovorRepository {
    companion object {
        //Dodano za contexte---------------------------------------

        private lateinit var context: Context
        fun getContext(): Context {
            return context
        }
        fun setContext(_context: Context) {
            context = _context
        }

        //---------------------------------------------------------

        suspend fun postaviOdgovorKviz(idKvizTaken:Int, idPitanje:Int, odgovor:Int): Int{
            return withContext(Dispatchers.IO) {
                val baza = AppDatabase.getInstance(AccountRepository.getContext())
                var rijeseno = false
                for (odg in baza.odgovorDao().postojiOdgovor(idKvizTaken)) {
                    if (odg.pitanjeId == idPitanje) {
                        rijeseno = true
                        break
                    }
                }
                val kvz = TakeKvizRepository.getPocetiKvizovi()
                var kid = -1
                if (kvz != null) {
                    for (kviz in kvz) {
                        if (kviz.id == idKvizTaken)
                            kid = kviz.KvizId
                    }
                }



                if (!rijeseno) {
                    val bodovi = PitanjeKvizRepository.getRezultatSaKvizaZaOdgovor(kid, idPitanje, odgovor)
                    try {
                        val o = Odgovor(baza.odgovorDao().getAllOdgovori().size, odgovor, idPitanje, kid, idKvizTaken)
                        baza.odgovorDao().dodajOdgovor(o)
                        return@withContext bodovi
                    } catch (e: Exception) {
                        return@withContext -1
                    }
                }
                return@withContext PitanjeKvizRepository.getRezultatZaKviz(kid)
            }
        }

        suspend fun getOdgovorKviz(idKvizTaken:Int, idPitanje:Int): Int{
            return withContext(Dispatchers.IO) {
                val baza = AppDatabase.getInstance(AccountRepository.getContext())
                for(odgovor in baza.odgovorDao().postojiOdgovor(idKvizTaken)){
                    if(odgovor.pitanjeId == idPitanje)
                        return@withContext odgovor.odgovoreno
                }
                return@withContext -1
            }
        }







        suspend fun getOdgovoriKviz(idKviza: Int): List<Odgovor> {
            return withContext(Dispatchers.IO) {
                val baza = AppDatabase.getInstance(AccountRepository.getContext())
                val odgovori = baza.odgovorDao().getOdgovori(idKviza)
                if(odgovori.isEmpty())
                    return@withContext emptyList<Odgovor>()
                else return@withContext odgovori
            }
        }

        suspend fun odgovoriAPI(idKviza: Int): List<Odgovor> {
            return withContext(Dispatchers.IO) {
                var pom = -1
                for(pKviz in ApiAdapter.retrofit.getTakenKviz(AccountRepository.getHash())){
                    if(pKviz.KvizId == idKviza){
                        pom = pKviz.id
                        break
                    }
                }
                if(pom == -1)
                    return@withContext emptyList<Odgovor>()
                try {
                    val rezultat = ApiAdapter.retrofit.getOdgovoriKviz(pom, AccountRepository.getHash())
                    return@withContext rezultat
                }
                catch (e: Exception){
                    return@withContext emptyList<Odgovor>()
                }
            }
        }

        suspend fun dodajOdgovorAPI(idKvizTaken:Int, idPitanje:Int, odgovor:Int):Int{
            return withContext(Dispatchers.IO){
                val bodovi = PitanjeKvizRepository.getRezultatSaKvizaZaOdgovor(idKvizTaken, idPitanje, odgovor)
                val odg = OdgPitBod(odgovor = odgovor, pitanje = idPitanje, bodovi = bodovi)
                try {
                    ApiAdapter.retrofit.postaviOdgovorKviz(AccountRepository.getHash(), idKvizTaken, odg)
                    return@withContext bodovi
                }catch(e: Exception){
                    return@withContext -1
                }
            }
        }

        /*suspend fun postaviOdgovorKviz(idKvizTaken:Int, idPitanje:Int, odgovor:Int):Int{
            return withContext(Dispatchers.IO){
                val bodovi = PitanjeKvizRepository.getRezultatSaKvizaZaOdgovor(idKvizTaken, idPitanje, odgovor)
                val odgovor = OdgovorPitanjeBodovi(odgovor = odgovor, pitanje = idPitanje, bodovi = bodovi)
                val acc = AccountRepository()
                try {
                    ApiAdapter.retrofit.postaviOdgovoriZaTaken(acc.getHash(), idKvizTaken, odgovor)
                    return@withContext bodovi
                }catch(e: Exception){
                    return@withContext -1
                }
            }
        }*/


    }
}