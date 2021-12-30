package id.ayurizkyamalia.marsphotos.detail

import android.app.Application
import androidx.lifecycle.*
import id.ayurizkyamalia.marsphotos.R
import id.ayurizkyamalia.marsphotos.network.MarsProperty

/**
 * [ViewModel] yang terkait dengan [DetailFragment], berisi informasi tentang yang dipilih
 * [Properti Mars].
 */
class DetailViewModel(marsProperty: MarsProperty,
                      app: Application
) : AndroidViewModel(app) {

    // MutableLiveData internal untuk properti yang dipilih
    private val _selectedProperty = MutableLiveData<MarsProperty>()

    // LiveData eksternal untuk SelectedProperty
    val selectedProperty: LiveData<MarsProperty>
        get() = _selectedProperty

    // Initialize the _selectedProperty MutableLiveData
    init {
        _selectedProperty.value = marsProperty
    }

    // LiveData Peta Transformasi yang diformat displayPropertyPrice, yang menampilkan penjualan
    // atau harga sewa.
    val displayPropertyPrice = Transformations.map(selectedProperty) {
        app.applicationContext.getString(
            when (it.isRental) {
                true -> R.string.display_price_monthly_rental
                false -> R.string.display_price
            }, it.price)
    }

    // LiveData Peta Transformasi berformat DisplayPropertyType, yang menampilkan
    // String "Disewa/Dijual"
    val displayPropertyType = Transformations.map(selectedProperty) {
        app.applicationContext.getString(R.string.display_type,
            app.applicationContext.getString(
                when(it.isRental) {
                    true -> R.string.type_rent
                    false -> R.string.type_sale
                }))
    }
}

