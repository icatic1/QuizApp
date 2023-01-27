package ba.etf.rma21.projekat.data.viewmodel

import ba.etf.rma21.projekat.data.models.Grupa
import ba.etf.rma21.projekat.data.models.Predmet
import ba.etf.rma21.projekat.data.repositories.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class GroupViewModel {

    fun getGroupsByPredmet(predmet: Predmet, onSuccess: (grupe: List<Grupa>) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            val grupe = GrupaRepository.getGroupsByPredmet(predmet)
            when(grupe){
                is List<Grupa> -> onSuccess?.invoke(grupe)
                else -> onError?.invoke()
            }
        }
    }

    fun upisUGrupu(grupaId: Int, onSuccess: (Boolean) -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            val upisan = PredmetIGrupaRepository.upisiUGrupu(grupaId)
            when(upisan){
                is Boolean  -> onSuccess?.invoke(upisan)
                else -> onError?.invoke()
            }
        }
    }

    fun promijeniHash(hash: String, onSuccess: () -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            val accUpisan = AccountRepository.noviHash(hash)
            when(accUpisan){
                is Boolean-> onSuccess?.invoke()
                else -> onError?.invoke()
            }
        }
    }

    fun updateNow(onSuccess: () -> Unit, onError: () -> Unit){
        GlobalScope.launch{
            val updateovano = DBRepository.updateNow()
            when(updateovano){
                is Boolean-> onSuccess?.invoke()
                else -> onError?.invoke()
            }
        }
    }

}