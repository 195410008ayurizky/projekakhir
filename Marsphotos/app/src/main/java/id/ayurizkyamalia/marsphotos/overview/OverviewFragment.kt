package id.ayurizkyamalia.marsphotos.overview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import id.ayurizkyamalia.marsphotos.R
import id.ayurizkyamalia.marsphotos.databinding.FragmentOverviewBinding
import id.ayurizkyamalia.marsphotos.network.MarsApiFilter


/**
 * Fragmen ini menunjukkan status transaksi layanan web real-estate Mars.
 */
class OverviewFragment : Fragment() {

    /**
     * Malas menginisialisasi [OverviewViewModel] kami.
     */
    private val viewModel: OverviewViewModel by lazy {
        ViewModelProvider(this).get(OverviewViewModel::class.java)
    }

    /**
     * Mengembang tata letak dengan Data Binding, menetapkan pemilik siklus hidupnya ke OverviewFragment
     * untuk mengaktifkan Pengikatan Data untuk mengamati LiveData, dan menyiapkan RecyclerView dengan adaptor.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Memungkinkan Pengikatan Data untuk Mengamati LiveData dengan siklus hidup Fragmen ini
        binding.lifecycleOwner = this

        // Memberikan akses pengikatan ke OverviewViewModel
        binding.viewModel = viewModel

        // Menyetel adaptor photosGrid RecyclerView dengan lambda clickHandler yang
        // memberi tahu viewModel saat properti kita diklik
        binding.photosGrid.adapter = PhotoGridAdapter(PhotoGridAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })

        // Amati LiveData NavigationToSelectedProperty dan Navigasi jika bukan null
        // Setelah menavigasi, panggil displayPropertyDetailsComplete() sehingga ViewModel siap
        // untuk acara navigasi lainnya.
        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if ( null != it ) {
                // Harus menemukan NavController dari Fragmen
                this.findNavController().navigate(OverviewFragmentDirections.actionShowDetail(it))
                // Beri tahu ViewModel bahwa kami telah melakukan panggilan navigasi untuk mencegah beberapa navigasi
                viewModel.displayPropertyDetailsComplete()
            }
        })

        setHasOptionsMenu(true)
        return binding.root
    }

    /**
     * Mengembang menu luapan yang berisi opsi pemfilteran.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * Memperbarui filter di [OverviewViewModel] saat item menu dipilih dari
     * menu melimpah.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.updateFilter(
            when (item.itemId) {
                R.id.show_rent_menu -> MarsApiFilter.SHOW_RENT
                R.id.show_buy_menu -> MarsApiFilter.SHOW_BUY
                else -> MarsApiFilter.SHOW_ALL
            }
        )
        return true
    }
}
