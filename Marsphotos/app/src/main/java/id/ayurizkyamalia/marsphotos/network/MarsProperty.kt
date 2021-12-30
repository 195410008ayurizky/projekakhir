package id.ayurizkyamalia.marsphotos.network

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

/**
 * Kelas data ini mendefinisikan properti Mars yang mencakup ID, URL gambar, jenis (penjualan
 * atau sewa) dan harga (bulanan jika sewa).
 * Nama properti dari kelas data ini digunakan oleh Moshi untuk mencocokkan nama nilai di JSON.
 */
@Parcelize
data class MarsProperty (
    val id: String,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "img_src") val imgSrcUrl: String,
    val type: String,
    val price: Double) : Parcelable {
    val isRental
        get() = type == "rent"
}