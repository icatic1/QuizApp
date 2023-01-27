package ba.etf.rma21.projekat.data.repositories

import android.annotation.SuppressLint
import android.content.Context
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Account
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AccountRepository {
    companion object {
        var acHash: String = "26c754ff-22e5-48fe-a0e2-8bcd54191c22"

        fun getHash(): String {
            return acHash
        }

        //Dodano za contexte---------------------------------------

        private lateinit var context: Context
        fun getContext(): Context {
            return context
        }
        fun setContext(_context: Context) {
            context = _context
        }

        //---------------------------------------------------------

        suspend fun noviHash(accHash: String): Boolean {
            return withContext(Dispatchers.IO) {
                acHash = accHash
                try {
                    val baza = AppDatabase.getInstance(getContext())
                    baza.accountDao().obrisiAcc()
                    try {
                        baza.accountDao().obrisiAcc()
                        val acc = ApiAdapter.retrofit.getAcc(accHash)
                        acc.lastUpdate = getDateFormat(Date())
                        baza.accountDao().insertAcc(acc)
                    }catch (e: Exception){
                        baza.accountDao().obrisiAcc()
                        val dat = getDateFormat(Date())
                        baza.accountDao().insertAcc(Account(0, "", accHash, dat))
                    }
                    baza.kvizDao().obrisiDb()
                    baza.pitanjeDao().obrisiDb()
                    baza.odgovorDao().obrisiDb()

                    baza.grupaDao().obrisiDb()
                    baza.predmetDao().obrisiDb()
                    return@withContext true
                } catch (e: Exception) {
                    return@withContext false
                }
            }
        }



        suspend fun getAccount(): Account? {
            return withContext(Dispatchers.IO) {
                try {
                    val db = AppDatabase.getInstance(getContext())
                    return@withContext db.accountDao().getAcc()
                } catch (e: Exception) {
                    return@withContext null
                }
            }
        }


        @SuppressLint("SimpleDateFormat")
        private fun getDateFormat(date: Date): String {
            val format = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            return format.format(date)
        }


    }

}