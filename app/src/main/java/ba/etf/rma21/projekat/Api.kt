package ba.etf.rma21.projekat

import ba.etf.rma21.projekat.data.models.*
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.repositories.ApiConfig
import retrofit2.http.*

interface Api {

    @GET("kviz/{id}/pitanja")
    suspend fun getPitanja(@Path("id") idKviza: Int): List<Pitanje>

    @GET("kviz")
    suspend fun getAll(): List<Kviz>

    @POST("student/{id}/kviz/{kid}")
    suspend fun zapocniKviz(@Path("kid") idKviza: Int, @Path("id") id: String): KvizTaken

    @GET("student/{id}/kviztaken")
    suspend fun getTakenKviz(@Path("id") id: String): List<KvizTaken>

    @GET("student/{id}/kviztaken/{ktid}/odgovori")
    suspend fun getOdgovoriKviz(@Path("ktid") idKviza:Int, @Path("id") id: String): List<Odgovor>

    @GET("kviz/{id}")
    suspend fun getById(@Path("id") id: Int): Kviz

    @GET("predmet")
    suspend fun getPredmeti(): List<Predmet>

    @GET("grupa")
    suspend fun getGrupe(): List<Grupa>

    @GET("predmet/{id}/grupa")
    suspend fun getGrupeZaPredmet(@Path("id") idPredmeta: Int): List<Grupa>

    @GET("student/{id}/grupa")
    suspend fun getUpisaneGrupe(@Path("id") id: String): List<Grupa>

    @GET("kviz/{id}/grupa")
    suspend fun getGrupeZaKviz(@Path("id") kvizId: Int): List<GrupaIPredmetId>

    @GET("predmet/{id}")
    suspend fun getPredmetId(@Path("id") predmetId: Int): Predmet

    @GET("grupa/{id}/kvizovi")
    suspend fun getUpisani(@Path("id") id: Int): List<Kviz>

    @GET("predmet")
    suspend fun getPredmetiGodina(): List<Predmet>

    @POST("grupa/{gid}/student/{id}")
    suspend fun upisiUGrupu(@Path("gid") gid: Int, @Path("id") id: String): Message

    @POST("student/{id}/kviztaken/{ktid}/odgovor")
    suspend fun postaviOdgovorKviz(@Path("id") idKviza: String, @Path("ktid") idKvizTaken: Int, @Body odgovor: OdgPitBod) : PovratniOdgovor

    @GET("https://rma21-etf.herokuapp.com/account/{id}/lastUpdate")
    suspend fun validniPodaci(@Path("id") id: String, @Query("date") datum: String): Changed

    @GET("student/{id}")
    suspend fun getAcc(@Path("id") id: String): Account
}