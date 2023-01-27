package ba.etf.rma21.projekat.data.repositories

import android.content.Context
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PredmetIGrupaRepository {
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


        suspend fun getGrupe(): List<Grupa> {
            return withContext(Dispatchers.IO) {
                return@withContext ApiAdapter.retrofit.getGrupe()
            }
        }


        suspend fun getPredmeti(): List<Predmet> {
            return withContext(Dispatchers.IO) {
                return@withContext ApiAdapter.retrofit.getPredmeti()
            }
        }


        suspend fun upisiUGrupu(idGrupa:Int):Boolean{
            return withContext(Dispatchers.IO) {
                val mes = ApiAdapter.retrofit.upisiUGrupu(idGrupa,AccountRepository.acHash)
                if(mes.message.contains("je dodan")) return@withContext true
                return@withContext false
            }
        }




        suspend fun getGrupeZaPredmet(idPredmeta: Int): List<Grupa> {
            return withContext(Dispatchers.IO) {
                return@withContext ApiAdapter.retrofit.getGrupeZaPredmet(idPredmeta)
            }
        }










        suspend fun getUpisaneGrupe(): List<Grupa> {
            return withContext(Dispatchers.IO) {
                return@withContext ApiAdapter.retrofit.getUpisaneGrupe(AccountRepository.getHash())
            }
        }
    }
}