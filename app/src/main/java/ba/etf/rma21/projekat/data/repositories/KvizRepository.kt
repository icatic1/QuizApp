package ba.etf.rma21.projekat.data.repositories

import android.annotation.SuppressLint
import ba.etf.rma21.projekat.data.AppDatabase
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class KvizRepository {


    /*companion object {
        private var myKvizes: ArrayList<Kviz> = ArrayList()
        private var sviKvizes: ArrayList<Kviz> = ArrayList()
        init {
           sviKvizes.addAll(allKvizes())
            dodajMojKviz(sviKvizes[8])
            dodajMojKviz(sviKvizes[0])
            dodajMojKviz(sviKvizes[5])
            dodajMojKviz(sviKvizes[6])

        }

        fun getMyKvizes(): List<Kviz> {
            return myKvizes
        }

        fun getAll(): List<Kviz> {
            return sviKvizes
        }

        fun getDone(): List<Kviz> {
            return myKvizes.stream().filter { t: Kviz? -> odrediStatus(t)==1  }.collect(Collectors.toList())
        }

        fun getFuture(): List<Kviz> {
            return myKvizes.stream().filter { t: Kviz? -> odrediStatus(t)==3  }.collect(Collectors.toList())
        }

        fun getNotTaken(): List<Kviz> {
            return myKvizes.stream().filter { t: Kviz? -> odrediStatus(t)==4  }.collect(Collectors.toList())
        }

        fun dodajMojKviz(kviz:Kviz){
            myKvizes.add(kviz)
            var trazeni = PredmetRepository.getAll().find { predmet: Predmet -> predmet.naziv.equals(kviz.nazivPredmeta) }
            if(trazeni!= null) PredmetRepository.upisiNovi(trazeni)
        }



    }*/

    companion object {
        lateinit var pokrenutiKviz: Kviz
        var pokusajKviza: KvizTaken? = null

        suspend fun getDone(): List<Kviz> {
            return withContext(Dispatchers.IO){
               val kvizovi = getUpisani()
                var rezultat = mutableListOf<Kviz>()
                for(kviz in kvizovi){
                    if(getZavrsenKviz(kviz))
                        rezultat.add(kviz)
                }
                return@withContext rezultat
            }
        }

        suspend fun getFuture(): List<Kviz> {
            return withContext(Dispatchers.IO){
                val kvizovi = getUpisani()
                var rezultat = mutableListOf<Kviz>()
                for(kviz in kvizovi){
                    if(!getZavrsenKviz(kviz) && dajStatus(kviz) == "zuta")
                        rezultat.add(kviz)
                }
                return@withContext rezultat
            }
         }

        suspend fun getNotTaken(): List<Kviz> {
            return withContext(Dispatchers.IO){
                val kvizovi = getUpisani()
                var rezultat = mutableListOf<Kviz>()
                for(kviz in kvizovi){
                    if(!getZavrsenKviz(kviz) && dajStatus(kviz) == "crvena")
                        rezultat.add(kviz)
                }
                return@withContext rezultat
            }
        }

        private fun dajStatus(kviz: Kviz): String {
            if(kviz.datumRada == null){
                var datumPocetka = uporediSaTrenutnimDatumom(kviz.datumPocetka)
                var datumKraja = kviz.datumKraj?.let { uporediSaTrenutnimDatumom(it) }
                if(datumPocetka == 1)return "zuta"
                else if(datumPocetka == 2 && datumKraja == 1)return "zelena"
                else if(datumPocetka == 2 && datumKraja == 2)return "crvena"
            }
            return "plava"
        }

        private fun uporediSaTrenutnimDatumom(datum1: Date): Int{
            var godina = Calendar.getInstance().get(Calendar.YEAR)
            var mjesec = Calendar.getInstance().get(Calendar.MONTH) + 1
            var dan = Calendar.getInstance().get(Calendar.DATE)
            if(datum1.getYear() > godina) return 1
            else if(godina > datum1.getYear()) return 2;
            else if(datum1.getMonth() > mjesec) return 1;
            else if(mjesec > datum1.getMonth()) return 2;
            else if(datum1.getDate() > dan) return 1;
            else if(dan > datum1.getDate()) return 2;
            return 0;
        }

        fun getStatus(kviz: Kviz): String{
            return dajStatus(kviz)
        }


        suspend fun getAll():List<Kviz>{
            return withContext(Dispatchers.IO){
                var rezultat = mutableListOf<Kviz>()
                for(kviz in ApiAdapter.retrofit.getAll()){
                    val ubaci = kviz
                    var listaNaziva = mutableListOf<String>()
                    for(kviz1 in ApiAdapter.retrofit.getGrupeZaKviz(kviz.id)){
                        val naziv = ApiAdapter.retrofit.getPredmetId(kviz1.predmetId).naziv
                        if(!listaNaziva.contains(naziv)) {
                            if (ubaci.nazivPredmeta == null) ubaci.nazivPredmeta = naziv
                            else ubaci.nazivPredmeta += ",$naziv"
                            listaNaziva.add(naziv)
                        }
                    }
                    rezultat.add(ubaci)
                    for(kv in rezultat){
                        if(getZavrsenKvizApi(kv)){
                            kv.osvojeniBodovi = PitanjeKvizRepository.getRezultatAPI(kv.id)
                            kv.predan = true
                        }
                        kv.datumPocetakDb = getDateFormat(kv.datumPocetka)
                        if(kv.datumKraj != null)
                            kv.datumKrajDb = getDateFormat(kv.datumKraj!!)
                        else kv.datumKrajDb = ""
                    }
                }
                return@withContext rezultat
            }
        }

        suspend fun getById(id:Int): Kviz? {
            return withContext(Dispatchers.IO){
                try {
                    val rezultat = ApiAdapter.retrofit.getById(id)
                    return@withContext rezultat
                }catch (e: Exception){
                    return@withContext null
                }
            }
        }

        @SuppressLint("SimpleDateFormat")
        private fun getDateFormat(date: Date): String {
            val format = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss")
            return format.format(date)
        }



        suspend fun zavrsiKviz(idKviza: KvizTaken){
            return withContext(Dispatchers.IO){
                val baza = AppDatabase.getInstance(AccountRepository.getContext())
                val odgovori = baza.odgovorDao().getOdgovori(pokrenutiKviz.id)
                for(pitanje in PitanjeKvizRepository.getPitanja(pokrenutiKviz.id)){
                    if(odgovori.stream().noneMatch{ x -> x.pitanjeId == pitanje.id }) {
                        OdgovorRepository.postaviOdgovorKviz(idKviza.id, pitanje.id, pitanje.opcije.size)
                        OdgovorRepository.dodajOdgovorAPI(idKviza.id, pitanje.id, pitanje.opcije.size)
                    }
                    else OdgovorRepository.dodajOdgovorAPI(idKviza.id, pitanje.id, odgovori.stream().filter{
                        x -> x.pitanjeId == pitanje.id}.findFirst().get().odgovoreno)
                }
                baza.kvizDao().zavrsiKviz(true, pokrenutiKviz.id)
                baza.kvizDao().upisiBodove(PitanjeKvizRepository.getRezultatZaKviz(pokrenutiKviz.id), pokrenutiKviz.id)
                return@withContext
            }
        }

        suspend fun getZavrsenKvizApi(kviz: Kviz): Boolean{
            return withContext(Dispatchers.IO){
                var postoji = false
                lateinit var pKvizi: KvizTaken
                for(pKviz in ApiAdapter.retrofit.getTakenKviz(AccountRepository.getHash())){
                    if(pKviz.KvizId == kviz.id){
                        pKvizi = pKviz
                        postoji = true
                        break
                    }
                }
                if(postoji){
                    return@withContext PitanjeKvizRepository.getZavrsenAPI(pKvizi)
                }
                return@withContext false
            }
        }

        suspend fun getZavrsenKviz(kviz: Kviz): Boolean{
            return withContext(Dispatchers.IO){
                var postoji = false
                lateinit var pKvizi: KvizTaken
                for(pKviz in ApiAdapter.retrofit.getTakenKviz(AccountRepository.getHash())){
                    if(pKviz.KvizId == kviz.id){
                        pKvizi = pKviz
                        postoji = true
                        break
                    }
                }
                if(postoji){
                    return@withContext PitanjeKvizRepository.getZavrsenKviz(pKvizi)
                }
                return@withContext false
            }
        }

        private fun stringToDate(value: String?): Date {
            val format = SimpleDateFormat("yyyy-MM-dd")
            return format.parse(value)
        }

        suspend fun getUpisaniDb(): List<Kviz>{
            return withContext(Dispatchers.IO){
                val baza = AppDatabase.getInstance(AccountRepository.getContext())
                val rezultat = baza.kvizDao().dajSveKvizoveDb()
                rezultat.forEach { x ->
                    x.datumPocetka = stringToDate(x.datumPocetakDb)
                    if(x.datumKrajDb == "")
                        x.datumKraj = null
                    else x.datumKraj = stringToDate(x.datumKrajDb)
                    if(x.datumRadaDb == "")
                        x.datumRada = null
                    else x.datumRada = stringToDate(x.datumRadaDb)
                }

                return@withContext rezultat
            }
        }


        suspend fun getUpisani():List<Kviz>{
            return withContext(Dispatchers.IO){
                var rezultat = mutableListOf<Kviz>()
                for(grupa in ApiAdapter.retrofit.getUpisaneGrupe(AccountRepository.getHash())){
                    var pom = ApiAdapter.retrofit.getUpisani(grupa.id)
                    val nazivPredmeta = ApiAdapter.retrofit.getPredmetId(grupa.predmetId).naziv
                    pom.forEach { x -> x.nazivGrupe = grupa.naziv;
                        if(x.nazivPredmeta == null)
                            x.nazivPredmeta = nazivPredmeta
                        else x.nazivPredmeta+= nazivPredmeta
                    }
                    rezultat.addAll(pom)
                }
                var izbacivanje = mutableListOf<Int>()
                var i = 0
                var j = 0
                while(i < rezultat.size){
                    j = i + 1
                    while(j < rezultat.size){
                        if(rezultat[i].id == rezultat[j].id){
                            rezultat[i].nazivPredmeta+= "," + rezultat[j].nazivPredmeta
                            rezultat.removeAt(j)
                            izbacivanje.add(j)
                        }
                        j++
                    }
                    i++
                }
                for(izbaci in izbacivanje)
                    rezultat.removeAt(izbaci)
                val kvizTakenZaDatum = TakeKvizRepository.getPocetiKvizovi()
                if (kvizTakenZaDatum != null) {
                    for(kviz in kvizTakenZaDatum){
                        rezultat.forEach { x ->
                            if(x.id == kviz.KvizId){
                                x.datumRada = kviz.datumRada
                                x.datumRadaDb = getDateFormat(kviz.datumRada)
                            }
                        }
                    }
                }
                for(kviz in rezultat){
                    if(getZavrsenKvizApi(kviz)){
                        kviz.osvojeniBodovi = PitanjeKvizRepository.getRezultatAPI(kviz.id)
                        kviz.predan = true
                    }
                    kviz.datumPocetakDb = getDateFormat(kviz.datumPocetka)
                    if(kviz.datumKraj != null)
                        kviz.datumKrajDb = getDateFormat(kviz.datumKraj!!)
                    else kviz.datumKrajDb = ""
                }
                return@withContext rezultat
            }
        }

    }
}