package ba.etf.rma21.projekat.viewmodel

import ba.etf.rma21.projekat.data.models.Kviz
import ba.etf.rma21.projekat.data.models.KvizTaken
import ba.etf.rma21.projekat.data.repositories.KvizRepository
import ba.etf.rma21.projekat.data.repositories.TakeKvizRepository
import ba.etf.rma21.projekat.view.KvizListAdapter
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class KvizListViewModel {
    fun getQuizzes(onSuccess: (quizzes: List<Kviz>) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val kvizovi = KvizRepository.getAll()){
                is List<Kviz> -> onSuccess?.invoke(kvizovi)
                else -> onError?.invoke()
            }
        }
    }

    fun getMyQuizzes(onSuccess: (quizzes: List<Kviz>) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val kvizovi = KvizRepository.getUpisaniDb()){
                is List<Kviz> -> onSuccess?.invoke(kvizovi)
                else -> onError?.invoke()
            }
        }
    }

    fun getDoneQuizzes(onSuccess: (quizzes: List<Kviz>) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val kvizovi = KvizRepository.getDone()){
                is List<Kviz> -> onSuccess?.invoke(kvizovi)
                else -> onError?.invoke()
            }
        }
    }

    fun getFutureQuizzes(onSuccess: (quizzes: List<Kviz>) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val kvizovi = KvizRepository.getFuture()){
                is List<Kviz> -> onSuccess?.invoke(kvizovi)
                else -> onError?.invoke()
            }
        }
    }

    fun getPastQuizzes(onSuccess: (quizzes: List<Kviz>) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val kvizovi = KvizRepository.getNotTaken()){
                is List<Kviz> -> onSuccess?.invoke(kvizovi)
                else -> onError?.invoke()
            }
        }
    }

    fun getStatus(kviz: Kviz): String{
        return KvizRepository.getStatus(kviz)
    }


    fun getPocetiKvizovi(onSuccess: (quizzes: List<KvizTaken>) -> Unit,
                         onError: () -> Unit){
        GlobalScope.launch{
            when(val kvizovi = TakeKvizRepository.getPocetiKvizovi()){
                is List<KvizTaken> -> onSuccess?.invoke(kvizovi)
                else -> onError?.invoke()
            }
        }
    }

    fun zapocniKviz(
        pKvizId: Int,
        onSuccess: (pKviz: KvizTaken) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val pKviz = TakeKvizRepository.zapocniKviz(pKvizId)){
                is KvizTaken-> onSuccess?.invoke(pKviz)
                else -> onError?.invoke()
            }
        }
    }

    fun getPocetiKvizoviApp(kviz: Kviz, onSuccess: (rezultat: Boolean, kviz: Kviz) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val rezultat = TakeKvizRepository.getPokusajiAPI()){
                is Boolean-> onSuccess?.invoke(rezultat, kviz)
                else -> onError?.invoke()
            }
            }
        }

    fun zavrsiKviz(idKvizTaken: KvizTaken, rezultat: Int, onSuccess: (rezultat: Int) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            KvizRepository.zavrsiKviz(idKvizTaken)
            onSuccess?.invoke(rezultat)
        }
    }

    fun getZavrsenKviz(idKviz: Kviz, holder: KvizListAdapter.KvizzViewHolder, position: Int, onSuccess: (kviz: Kviz, rezultat: Boolean, holder: KvizListAdapter.KvizzViewHolder, position: Int) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            when(val rezultat = KvizRepository.getZavrsenKviz(idKviz)){
                is  Boolean-> onSuccess?.invoke(idKviz, rezultat, holder, position)
            }
        }
    }

    /*fun dodajMojKviz(kviz:Kviz){
        KvizRepository.dodajMojKviz(kviz)
    }*/
}