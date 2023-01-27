package ba.etf.rma21.projekat.viewmodel


import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.models.Pitanje
import ba.etf.rma21.projekat.data.repositories.OdgovorRepository

import ba.etf.rma21.projekat.data.repositories.PitanjeKvizRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PitanjeKvizViewModel {

    fun getPitanja(idKviza: Int, onSuccess: (pitanja: List<Pitanje>) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val pitanja = PitanjeKvizRepository.getPitanjaDb(idKviza)){
                is List<Pitanje> -> onSuccess?.invoke(pitanja)
                else -> onError?.invoke()
            }
        }
    }

    fun postaviOdgovorKviz(idKvizTaken:Int, idPitanje:Int, odgovor:Int){
        GlobalScope.launch{
            OdgovorRepository.postaviOdgovorKviz(idKvizTaken, idPitanje, odgovor)
        }
    }

    fun getOdgovorApp(idKvizTaken:Int, idPitanje:Int, onSuccess: (odgovor: Int) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val odgovor = OdgovorRepository.getOdgovorKviz(idKvizTaken, idPitanje)){
                is Int -> onSuccess?.invoke(odgovor)
                else -> onError?.invoke()
            }
        }
    }

    fun getOdgovorPokusaj(idKvizTaken:Int, idPitanje:Int, index: Int, onSuccess: (odgovor: Int, i: Int) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val odgovor = OdgovorRepository.getOdgovorKviz(idKvizTaken, idPitanje)){
                is Int -> onSuccess?.invoke(odgovor, index)
                else -> onError?.invoke()
            }
        }
    }

    fun getRezultat(kvizTaken: Int, onSuccess: (rezultat: Int) -> Unit, onError: () -> Unit){
        GlobalScope.launch {
            when(val rez = PitanjeKvizRepository.getRezBaza(kvizTaken)){
                is Int -> onSuccess?.invoke(rez)
                else -> onError?.invoke()
            }
        }
    }

    fun getZavrsenKviz(kvizTaken: KvizTaken, onSuccess: (rezultat: Boolean) -> Unit, onError: () -> Unit){
        GlobalScope.launch {
            when(val rez = PitanjeKvizRepository.getZavrsenKviz(kvizTaken)){
                is Boolean -> onSuccess?.invoke(rez)
                else -> onError?.invoke()
            }
        }
    }


    fun setOdabranaGodina(odabranaGodina: Int){
        PitanjeKvizRepository.odabranaGodina = odabranaGodina
    }
    fun getOdabranaGodina(): Int{
        return PitanjeKvizRepository.odabranaGodina
    }

    fun setOdabraniPredmet(odabraniPredmet: Int){
        PitanjeKvizRepository.odabraniPredmet = odabraniPredmet
    }
    fun getOdabraniPredmet(): Int{
        return PitanjeKvizRepository.odabraniPredmet
    }

    fun setOdabranaGrupa(odabanaGrupa: Int){
        PitanjeKvizRepository.odabranaGrupa = odabanaGrupa
    }
    fun getOdabranaGrupa(): Int{
        return PitanjeKvizRepository.odabranaGrupa
    }

    fun setIndexPitanja(indexPitanja: String){
        PitanjeKvizRepository.pitanjeIndeks = indexPitanja
    }
    fun getIndexPitanja(): String{
        return PitanjeKvizRepository.pitanjeIndeks
    }

    /*fun getPitanja(nazivKviza: String, nazivPredmeta: String): List<Pitanje>{
        return PitanjeKvizRepository.getPitanja(nazivKviza,nazivPredmeta)
    }


    fun dodajOdgovor(pitanje: Pitanje, odgovor: Int){
        PitanjeKvizRepository.dodajOdgovor(pitanje,odgovor)
    }

    fun dajOdgovor(pitanje: Pitanje): Int?{
        return PitanjeKvizRepository.dajOdgovor(pitanje)
    }*/
}