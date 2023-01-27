package ba.etf.rma21.projekat.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ba.etf.rma21.projekat.R
import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.viewmodel.KvizListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import java.util.stream.Collectors


class KvizListAdapter(
    private var kvizovi: List<Kviz>,
    private val onItemClicked: (kviz: Kviz) -> Unit
): RecyclerView.Adapter<KvizListAdapter.KvizzViewHolder>(){

    private val kvizViewModel = KvizListViewModel()
    private var status: String? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): KvizzViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.prikaz_kviza, parent, false)
        return KvizzViewHolder(view)
    }

    override fun getItemCount(): Int = kvizovi.size

    override fun onBindViewHolder(holder: KvizzViewHolder, position: Int) {

        holder.itemView.setOnClickListener{onItemClicked(kvizovi[position])}

        holder.quizName.text = kvizovi[position].naziv
        if(kvizovi[position].datumRada == null){
            if(uporediSaTrenutnimDatumom(kvizovi[position].datumPocetka) == 1 || kvizovi[position].datumKraj == null)
                holder.quizDate.text = dajDatum(kvizovi[position].datumPocetka)
            else holder.quizDate.text = kvizovi[position].datumKraj?.let { dajDatum(it) }
        }
        else holder.quizDate.text = kvizovi[position].datumRada?.let { dajDatum(it) }
        holder.quizSubjectName.text = kvizovi[position].nazivPredmeta
        holder.quizTime.text = kvizovi[position].trajanje.toString() + " min"

        postaviStatus(kvizovi[position], holder, position)


    }


    inner class KvizzViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val quizStatus: ImageView = itemView.findViewById(R.id.kviz_status)
        val quizName: TextView = itemView.findViewById(R.id.kviz_naziv)
        val quizSubjectName: TextView = itemView.findViewById(R.id.kviz_predmet)
        val quizPoints: TextView = itemView.findViewById(R.id.kviz_bodovi)

        val quizDate: TextView = itemView.findViewById(R.id.kviz_datum)
        val quizTime: TextView = itemView.findViewById(R.id.kviz_trajanje)

    }

    private fun postaviStatus(kviz: Kviz, holder: KvizzViewHolder, position: Int) {
        kvizViewModel.getZavrsenKviz(kviz, holder, position, onSuccess = ::onSuccess, onError = ::onError)
    }

    private fun dajDatum(datumRada: Date): String{
        val dan: Int = datumRada.getDate()
        var mjesec: Int = datumRada.getMonth() +1
        val danString: String?
        val mjesecString: String?
        if(dan < 10)danString = "0$dan."
        else danString = "$dan."
        if(mjesec < 10) mjesecString = "0$mjesec."
        else mjesecString = "$mjesec."
        return danString + mjesecString + datumRada.getYear().plus(1900).toString()
    }

    private fun uporediSaTrenutnimDatumom(datum1: Date): Int{
        var godina = Calendar.getInstance().get(Calendar.YEAR)
        var mjesec = Calendar.getInstance().get(Calendar.MONTH) + 1
        var dan = Calendar.getInstance().get(Calendar.DATE)
        if(datum1.getYear().plus(1900) > godina) return 1
        else if(godina > datum1.getYear().plus(1900)) return 2;
        else if(datum1.getMonth().plus(1) > mjesec) return 1;
        else if(mjesec > datum1.getMonth().plus(1)) return 2;
        else if(datum1.getDate() > dan) return 1;
        else if(dan > datum1.getDate()) return 2;
        return 0;
    }





    fun updateKvizove(quizzes: List<Kviz>) {
        this.kvizovi = quizzes
        this.kvizovi = this.kvizovi.stream().sorted { o1, o2 -> uporediDatume(o1, o2)}.collect(Collectors.toList())
        notifyDataSetChanged()
    }



    private fun uporediDatume(o1: Kviz, o2: Kviz): Int {
        if(o1.datumPocetka.getYear() > o2.datumPocetka.getYear()) return 1
        else if(o2.datumPocetka.getYear() > o1.datumPocetka.getYear()) return -1;
        else if(o1.datumPocetka.getMonth() > o2.datumPocetka.getMonth()) return 1;
        else if(o2.datumPocetka.getMonth() > o1.datumPocetka.getMonth()) return -1;
        else if(o1.datumPocetka.getDate() > o2.datumPocetka.getDate()) return 1;
        else if(o2.datumPocetka.getDate() > o1.datumPocetka.getDate()) return -1;
        return 0;
    }



    fun onSuccess(kviz: Kviz, rezultat: Boolean, holder: KvizzViewHolder, position: Int){
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                if(rezultat){
                    status = "plava"
                }
                else{
                    var datumPocetka = uporediSaTrenutnimDatumom(kviz.datumPocetka)
                    var datumKraja = kviz.datumKraj?.let { uporediSaTrenutnimDatumom(it) }

                    if(datumPocetka == 1)status = "zuta"
                    else if(datumPocetka == 2 && (datumKraja == 1 || datumKraja == null))status = "zelena"
                    else if(datumPocetka == 2 && datumKraja == 2) status = "crvena"
                }
                if(status == "plava"){
                    val context: Context = holder.quizStatus.context
                    if(kviz.osvojeniBodovi != -1) holder.quizPoints.text = kviz.osvojeniBodovi.toString()
                    else holder.quizPoints.text = ""

                    var id: Int = context.resources.getIdentifier(status, "drawable", context.packageName)
                    holder.quizStatus.setImageResource(id)
                    return@withContext
                }
                else{
                    val context: Context = holder.quizStatus.context
                    holder.quizPoints.text = ""
                    val id: Int = context.resources.getIdentifier(status, "drawable", context.packageName)
                    holder.quizStatus.setImageResource(id)
                    return@withContext
                }
            }
        }
    }

    fun onError() {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                // Nije dobro
            }
        }
    }
}