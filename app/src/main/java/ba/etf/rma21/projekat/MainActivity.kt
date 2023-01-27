package ba.etf.rma21.projekat



import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.view.fragments.FragmentKvizovi
import ba.etf.rma21.projekat.view.fragments.FragmentPredmeti
import ba.etf.rma21.projekat.data.repositories.AccountRepository
import ba.etf.rma21.projekat.data.viewmodel.GroupViewModel
import ba.etf.rma21.projekat.view.fragments.FragmentPokusaj
import ba.etf.rma21.projekat.view.fragments.FragmentPoruka
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var bottomNavigation: BottomNavigationView
    }
    private val mOnNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.kvizovi -> {
                        val kvizoviFragments = FragmentKvizovi.newInstance()
                        openFragment(kvizoviFragments)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.predmeti -> {
                        val predmetiFragments = FragmentPredmeti.newInstance()
                        openFragment(predmetiFragments)
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            }

    private val groupViewModel = GroupViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        delegate.applyDayNight()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation= findViewById(R.id.bottomNav)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = false
        bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = false
        bottomNavigation.selectedItemId= R.id.kvizovi
        val kvizoviFragment = FragmentKvizovi.newInstance()
        openFragment(kvizoviFragment)

        AccountRepository.setContext(applicationContext)
        val payload = intent?.getStringExtra("payload")
        if (payload != null) {
             groupViewModel.promijeniHash(payload, onSuccess = ::onSuccess, onError = ::onError)
        }
        else groupViewModel.promijeniHash("26c754ff-22e5-48fe-a0e2-8bcd54191c22", onSuccess = ::onSuccess, onError = ::onError)

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    /*override fun onBackPressed() {
        super.onBackPressed()
        Handler().postDelayed({
            napraviBottomNav()
            val favoritesFragment = FragmentKvizovi.newInstance()
            openFragment(favoritesFragment)
        }, 2)

    }*/
    override fun onBackPressed() {
        if(supportFragmentManager.fragments[0] is FragmentPoruka || supportFragmentManager.fragments[0] is FragmentPredmeti){
            openFragment(FragmentKvizovi.newInstance())
            bottomNavigation.selectedItemId= R.id.kvizovi
        }
        else if(supportFragmentManager.fragments[0] is FragmentKvizovi) finish()
        else if(supportFragmentManager.fragments[0] is FragmentPokusaj){
            napraviBottomNav()
            bottomNavigation.selectedItemId= R.id.kvizovi
            openFragment(FragmentKvizovi.newInstance())
        }
    }



    fun onSuccess(){
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val toast = Toast.makeText(applicationContext, "Dobar", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    fun onError() {
        GlobalScope.launch(Dispatchers.IO){
            withContext(Dispatchers.Main){
                val toast = Toast.makeText(applicationContext, "Ne radi", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }


    private fun napraviBottomNav() {
        bottomNavigation.menu.findItem(R.id.kvizovi).isVisible = true
        bottomNavigation.menu.findItem(R.id.predmeti).isVisible = true
        bottomNavigation.menu.findItem(R.id.predajKviz).isVisible = false
        bottomNavigation.menu.findItem(R.id.zaustaviKviz).isVisible = false
    }
}

