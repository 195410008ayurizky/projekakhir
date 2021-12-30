package id.ayurizkyamalia.marsphotos.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.ayurizkyamalia.marsphotos.network.MarsApi
import id.ayurizkyamalia.marsphotos.network.MarsApiFilter
import id.ayurizkyamalia.marsphotos.network.MarsProperty
import kotlinx.coroutines.launch

enum class MarsApiStatus { LOADING, ERROR, DONE }

/**
 * [ViewModel] yang dilampirkan ke [OverviewFragment].
 */
class OverviewViewModel : ViewModel() {

    // MutableLiveData internal yang menyimpan status permintaan terbaru
    private val _status = MutableLiveData<MarsApiStatus>()

    // LiveData eksternal yang tidak dapat diubah untuk status permintaan
    val status: LiveData<MarsApiStatus>
        get() = _status

    // Secara internal, kami menggunakan MutableLiveData, karena kami akan memperbarui Daftar Properti Mars
    // dengan nilai baru
    private val _properties = MutableLiveData<List<MarsProperty>>()

    // Antarmuka LiveData eksternal ke properti tidak dapat diubah, jadi hanya kelas ini yang dapat memodifikasi
    val properties: LiveData<List<MarsProperty>>
        get() = _properties

    // LiveData untuk menangani navigasi ke properti yang dipilih
    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty: LiveData<MarsProperty>
        get() = _navigateToSelectedProperty


    /**
     * Panggil getMarsRealEstateProperties() pada init sehingga kami dapat segera menampilkan status.
     */
    init {
        getMarsRealEstateProperties(MarsApiFilter.SHOW_ALL)
    }

    /**
     * Mendapat informasi properti real estat Mars yang disaring dari layanan Mars API Retrofit dan
     * memperbarui [MarsProperty] [Daftar] dan [MarsApiStatus] [LiveData]. Layanan Retrofit
     * mengembalikan coroutine Deferred, yang kita tunggu untuk mendapatkan hasil transaksi.
     * @param menyaring [MarsApiFilter] yang dikirim sebagai bagian dari permintaan server web
     */
    private fun getMarsRealEstateProperties(filter: MarsApiFilter) {
        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING
            try {
                _properties.value = MarsApi.retrofitService.getProperties(filter.value)
                _status.value = MarsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _properties.value = ArrayList()
            }
        }
    }

    /**
     * Memperbarui filter kumpulan data untuk layanan web dengan menanyakan data dengan filter baru
     * dengan memanggil [getMarsRealEstateProperties]
     * @param menyaring [MarsApiFilter] yang dikirim sebagai bagian dari permintaan server web
     */
    fun updateFilter(filter: MarsApiFilter) {
        getMarsRealEstateProperties(filter)
    }

    /**
     * Saat properti diklik, atur [_navigateToSelectedProperty] [MutableLiveData]
     * @param marsProperty [MarsProperty] yang diklik.
     */
    fun displayPropertyDetails(marsProperty: MarsProperty) {
        _navigateToSelectedProperty.value = marsProperty
    }

    /**
     * Setelah navigasi dilakukan, pastikan navigasiToSelectedProperty disetel ke null
     */
    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }
}
