package ba.etf.rma21.projekat.data.models

import kotlin.math.roundToInt

// - naziv: String - jedinstveni naziv pitanja u okviru kvizova u kojima se nalazi
// - kviz: String - jedinstveni naziv kviza

class PitanjeKviz(val naziv: String, val kviz: String){

    private lateinit var nazivPredmeta: String
    private var odgovorNaPitanje: Int = -1
    private var zavrsenKviz = false

    private var rezultat: Double = (-1).toDouble()

    fun setNazivPredmeta(naziv: String){
        nazivPredmeta = naziv
    }

    fun setOdgovorNaPitanje(odgovor: Int){
        odgovorNaPitanje = odgovor
    }

    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false

        other as PitanjeKviz

        if (naziv != other.naziv) return false
        if (kviz != other.kviz) return false
        if (nazivPredmeta != other.nazivPredmeta) return false

        return true
    }

    override fun hashCode(): Int {
        var result = naziv.hashCode()
        result = 31 * result + kviz.hashCode()
        result = 31 * result + nazivPredmeta.hashCode()
        return result
    }

    fun getOdgovorNaPitanje(): Int{
        return odgovorNaPitanje
    }

    fun getNazivPredmeta(): String{
        return nazivPredmeta
    }

    fun setZavrsenKviz(){
        zavrsenKviz = true
        //this.rezultat = rezultat
    }

    fun getZavrsenKviz(): Boolean{
        return zavrsenKviz
    }

    fun getRezultat(): Int{
        return rezultat.roundToInt()
    }

    fun dodajRezultat(rezultat: Double) {
        if(this.rezultat == (-1).toDouble())
            this.rezultat = 0.toDouble()
        this.rezultat+= rezultat
    }


}