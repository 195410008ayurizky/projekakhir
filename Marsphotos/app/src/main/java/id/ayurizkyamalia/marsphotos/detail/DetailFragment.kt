package id.ayurizkyamalia.marsphotos.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import id.ayurizkyamalia.marsphotos.databinding.FragmentDetailBinding


/**
 * [Fragmen] ini menunjukkan informasi rinci tentang bagian terpilih dari real estate Mars.
 * Ini menetapkan informasi ini di [DetailViewModel], yang didapatnya sebagai properti Parcelable
 * melalui Navigasi Jetpack SafeArgs.
 */
class DetailFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val application = requireNotNull(activity).application
        val binding = FragmentDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        val marsProperty = DetailFragmentArgs.fromBundle(requireArguments()).selectedProperty
        val viewModelFactory = DetailViewModelFactory(marsProperty, application)
        binding.viewModel = ViewModelProvider(
            this, viewModelFactory).get(DetailViewModel::class.java)

        return binding.root
    }
}