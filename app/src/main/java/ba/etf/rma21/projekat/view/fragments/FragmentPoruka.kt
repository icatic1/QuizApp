package ba.etf.rma21.projekat.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import ba.etf.rma21.projekat.R


class FragmentPoruka : Fragment() {
    private lateinit var porukica: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_poruka, container, false)
        porukica = view.findViewById(R.id.tvPoruka)
        porukica.text = arguments?.getString("tekstZaIspis")
        return view
    }


    companion object {
        fun newInstance(tekstZaIspis: String): FragmentPoruka = FragmentPoruka().apply {
            arguments = Bundle().apply {
                putString("tekstZaIspis", tekstZaIspis)
            }
        }
    }
}