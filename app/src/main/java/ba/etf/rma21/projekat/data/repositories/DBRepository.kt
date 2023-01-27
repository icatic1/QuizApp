package ba.etf.rma21.projekat.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.models.Predmet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class DBRepository {

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



        suspend fun updateNow(): Boolean? {
            return withContext(Dispatchers.IO) {
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val acc1 = AccountRepository.getAccount()
                if (acc1 != null) {
                        val rez = acc1.lastUpdate.let { ApiAdapter.retrofit.validniPodaci(AccountRepository.getHash(), it) }
                            if (rez.changed) {
                                brisanjeBaza()
                                generisiBaze()
                                val dat = getDateFormat(Date())
                                db.accountDao().updateLastUpdate(dat, AccountRepository.getHash())
                                return@withContext true
                            } else return@withContext false
                    return@withContext false
                }
                return@withContext false
            }
        }

        suspend fun generisiBaze() {
            return withContext(Dispatchers.IO) {
                val baza = AppDatabase.getInstance(AccountRepository.getContext())
                val grupe = ApiAdapter.retrofit.getUpisaneGrupe(AccountRepository.getHash())
                baza.grupaDao().napraviDb(grupe)
                val predmeti = mutableListOf<Predmet>()
                grupe.forEach { t: Grupa? ->
                    if (t != null) {
                        predmeti.add(ApiAdapter.retrofit.getPredmetId(t.predmetId))
                    }
                }

                baza.predmetDao().napraviDb(predmeti)

                val kvizovi = KvizRepository.getUpisani()
                baza.kvizDao().napraviDb(kvizovi)

                val pitanja = mutableListOf<Pitanje>()
                kvizovi.forEach { t: Kviz? ->
                    if (t != null) {
                        pitanja.addAll(PitanjeKvizRepository.getPitanja(t.id))
                    }
                }

                var indeks = 0
                pitanja.forEach { x -> x.idDb = indeks++}
                baza.pitanjeDao().napraviDb(pitanja)
            }
        }

        suspend fun brisanjeBaza() {
            return withContext(Dispatchers.IO) {
                val baza = AppDatabase.getInstance(AccountRepository.getContext())
                baza.kvizDao().obrisiDb()
                baza.pitanjeDao().obrisiDb()

                baza.grupaDao().obrisiDb()
                baza.predmetDao().obrisiDb()
            }
        }


        @SuppressLint("SimpleDateFormat")
        private fun getDateFormat(date: Date): String {
            val format = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            return format.format(date)
        }
    }
}