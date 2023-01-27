package ba.etf.rma21.projekat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ba.etf.rma21.projekat.data.dao.*
import ba.etf.rma21.projekat.data.models.*
import kotlin.coroutines.CoroutineContext

@Database(entities = arrayOf(Account::class, Grupa::class, Predmet::class, Kviz::class, Pitanje::class, Odgovor::class), version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun accountDao(): AccountDao


    abstract fun kvizDao(): KvizDao
    abstract fun pitanjeDao(): PitanjeDao
    abstract fun odgovorDao(): OdgovorDao
    abstract fun grupaDao(): GrupaDao
    abstract fun predmetDao(): PredmetDao


    companion object{
        private var INSTANCE: AppDatabase? = null

        fun setInstance(appdb:AppDatabase):Unit{
            INSTANCE=appdb
        }

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = buildRoomDB(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildRoomDB(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "RMA21DB"
            ).build()
    }
}