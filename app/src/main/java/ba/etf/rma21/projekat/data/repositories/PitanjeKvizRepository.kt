package ba.etf.rma21.projekat.data.repositories

import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Pitanje


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

class PitanjeKvizRepository {



    /*companion object{
        private var uradenaPitanja: HashMap<Pitanje,Int> = HashMap()
        private var lista : List<PitanjeKviz> = svaPitanjaKviz()
        private var svaPitanja : List<Pitanje> = svaPitanja()
        fun getPitanja(nazivKviza:String, nazivPredmeta: String) : List<Pitanje>{

            var pov:ArrayList<Pitanje> = ArrayList()
            for(p in lista){
                if(p.kviz.equals(nazivKviza) && p.nazivPredmeta.equals(nazivPredmeta)){
                    for(p2 in svaPitanja){
                        if(p2.naziv.equals(p.naziv)) pov.add(p2)
                    }
                }
            }
            return pov

        }




        fun dodajOdgovor(pitanje: Pitanje, odgovor: Int){
            uradenaPitanja.put(pitanje,odgovor)
        }

        fun dajOdgovor(pitanje: Pitanje): Int?{
            return uradenaPitanja.get(pitanje)
        }
    }*/

    companion object{
        var odabranaGodina: Int = -1
        var odabraniPredmet: Int = -1
        var odabranaGrupa: Int = -1

        var pitanjeIndeks = ""
        var odgovor = -1



        suspend fun getPitanja(idKviza:Int):List<Pitanje> {
            return withContext(Dispatchers.IO){
                val rezultat = ApiAdapter.retrofit.getPitanja(idKviza)
                rezultat.forEach { x ->
                    x.opcijeDb = x.opcije.joinToString(",")
                    x.kvizId = idKviza
                }
                return@withContext rezultat
            }
        }

        suspend fun getPitanjaDb(idKviza:Int):List<Pitanje> {
            return withContext(Dispatchers.IO){
                val baza = AppDatabase.getInstance(AccountRepository.getContext())
                val rezultat = baza.pitanjeDao().getPitanja(idKviza)
                rezultat.forEach { x ->
                    x.opcije = x.opcijeDb.split(",")
                }
                return@withContext rezultat
            }
        }




        suspend fun getZavrsenKviz(idKviza: KvizTaken): Boolean{
            return withContext(Dispatchers.IO){
                val db = AppDatabase.getInstance(AccountRepository.getContext())
                val odgovori = db.odgovorDao().postojiOdgovor(idKviza.id)
                if(odgovori.isEmpty())
                    return@withContext false
                val pitanja = db.pitanjeDao().getPitanja(odgovori[0].kvizId)
                return@withContext pitanja.size == odgovori.size
            }
        }

        suspend fun getRezultatAPI(idKviza: Int): Int{
            return withContext(Dispatchers.IO){
                val pitanja = getPitanja(idKviza)
                //
                var rezultat = 0.0
                for(pitanje in pitanja){
                    for(odgovor in OdgovorRepository.odgovoriAPI(idKviza)){
                        if(odgovor.pitanjeId == pitanje.id && odgovor.odgovoreno == pitanje.tacan){
                            rezultat += (1/pitanja.size.toDouble())*100
                        }
                    }
                }
                return@withContext rezultat.roundToInt()
            }
        }

        suspend fun getRezultatZaKviz(idKviza: Int): Int{
            return withContext(Dispatchers.IO){
                val baza = AppDatabase.getInstance(AccountRepository.getContext())
                val pitanja = baza.pitanjeDao().getPitanja(idKviza)


                var rezultat = 0.0
                for(pitanje in pitanja){
                    for(odgovor in baza.odgovorDao().getOdgovori(idKviza)){
                        if(odgovor.pitanjeId == pitanje.id && odgovor.odgovoreno == pitanje.tacan){
                            rezultat += (1/pitanja.size.toDouble())*100
                        }
                    }
                }
                return@withContext rezultat.roundToInt()
            }
        }

        suspend fun getZavrsenAPI(idKviza: KvizTaken): Boolean{
            return withContext(Dispatchers.IO){
                val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi()
                var kid = -1
                if (pocetiKvizovi != null) {
                    for(kviz in pocetiKvizovi){
                        if(kviz.id == idKviza.id)
                            kid = kviz.KvizId
                    }
                }
                val pitanja = ApiAdapter.retrofit.getPitanja(kid)
                val odgovori = OdgovorRepository.odgovoriAPI(kid)
                return@withContext pitanja.size == odgovori.size
            }
        }

        /*suspend fun getRezovi(idKviza: Int, idPitanje: Int, odgovorInt: Int): Int{
            return withContext(Dispatchers.IO){
                val pocetiKvizovi = TakeKvizRepository.getPocetiKvizovi()
                var kvizId = -1
                if (pocetiKvizovi != null) {
                    for(kviz in pocetiKvizovi){
                        if(kviz.id == idKviza)
                            kvizId = kviz.KvizId
                    }
                }
                val pitanja = getPitanja(kvizId)
                val odgovori = OdgovorRepository.getOdgovoriKviz(kvizId)
                var rezultat = 0.0
                for(pitanje in pitanja){
                    for(odgovor in odgovori){
                        if(odgovor.pitanjeId == pitanje.id && odgovor.odgovoreno == pitanje.tacan){
                            rezultat += (1/pitanja.size.toDouble())*100
                        }
                    }
                    if(pitanje.id == idPitanje && pitanje.tacan == odgovorInt){
                        rezultat += (1/pitanja.size.toDouble())*100
                    }
                }
                return@withContext rezultat.roundToInt()
            }
        }*/

        suspend fun getRezultatSaKvizaZaOdgovor(idKviza: Int, idPitanje: Int, odgovorInt: Int): Int{
            return withContext(Dispatchers.IO){
                val baza = AppDatabase.getInstance(AccountRepository.getContext())
                val pitanja = baza.pitanjeDao().getPitanja(idKviza)


                var rezultat = 0.0
                for(pitanje in pitanja){
                    for(odgovor in baza.odgovorDao().getOdgovori(idKviza)){
                        if(odgovor.pitanjeId == pitanje.id && odgovor.odgovoreno == pitanje.tacan){
                            rezultat += (1/pitanja.size.toDouble())*100
                        }
                    }
                    if(pitanje.id == idPitanje && pitanje.tacan == odgovorInt){
                        rezultat += (1/pitanja.size.toDouble())*100
                    }
                }
                return@withContext rezultat.roundToInt()
            }
        }




        suspend fun getRezBaza(idKviza: Int): Int{
            return withContext(Dispatchers.IO){
                val kvz = TakeKvizRepository.getPocetiKvizovi()
                var kid = -1
                if (kvz != null) {
                    for(kviz in kvz){
                        if(kviz.id == idKviza)
                            kid = kviz.KvizId
                    }
                }
                return@withContext getRezultatZaKviz(kid)
            }
        }


    }

}