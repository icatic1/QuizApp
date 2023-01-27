package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.models.KvizTaken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class TakeKvizRepository {
    companion object {


        suspend fun getPokusajiAPI(): Boolean{
            return withContext(Dispatchers.IO) {

                var postoji = false
                for(pKviz in ApiAdapter.retrofit.getTakenKviz(AccountRepository.getHash())){
                    if(pKviz.KvizId == KvizRepository.pokrenutiKviz.id){
                        KvizRepository.pokusajKviza = pKviz
                        postoji = true
                        break
                    }
                }


                if(!postoji){
                    val kviz = ApiAdapter.retrofit.zapocniKviz(KvizRepository.pokrenutiKviz.id, AccountRepository.getHash())
                    KvizRepository.pokusajKviza = kviz
                }
                return@withContext true
            }
        }

        suspend fun zapocniKviz(idKviza: Int): KvizTaken? {
            return withContext(Dispatchers.IO) {
                try {
                    val rezultat = ApiAdapter.retrofit.zapocniKviz(idKviza, AccountRepository.getHash())
                    return@withContext rezultat
                }catch (e: Exception){
                    return@withContext null
                }
            }
        }

        /*
        * suspend fun getPokusajiAPI(): Boolean{
            return withContext(Dispatchers.IO) {

                for(kz in ApiAdapter.retrofit.getPocetiKvizovi(AccountRepository.getHash())){
                    if(kz.KvizId == KvizRepository.pokrenutiKviz.id){
                        KvizRepository.radjeniKviz = kz
                        break
                    }
                }

                return@withContext true
            }
        }
        * */




        suspend fun getPocetiKvizovi(): List<KvizTaken>? {
            return withContext(Dispatchers.IO) {
                try {
                    val rezultat = ApiAdapter.retrofit.getTakenKviz(AccountRepository.getHash())
                    if(rezultat.isEmpty())
                        return@withContext null
                    return@withContext rezultat
                }catch(e: Exception){
                    return@withContext null
                }

            }
        }


    }
}