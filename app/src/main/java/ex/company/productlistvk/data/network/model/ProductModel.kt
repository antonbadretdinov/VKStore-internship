package ex.company.productlistvk.data.network.model

import androidx.compose.runtime.Stable

@Stable
data class ProductListModel(
    val products: List<ProductModel>,
    val total: Int
)

@Stable
data class ProductModel(
    val title: String,
    val description: String,
    val price: Int,
    val rating: Double,
    val thumbnail: String
)
