package ex.company.productlistvk.data.repository

import ex.company.productlistvk.data.network.retrofit.RetrofitApi
import javax.inject.Inject

class CatalogRepository @Inject constructor(private val catalogService: RetrofitApi) {

    suspend fun getAllProducts() = catalogService.getAllProducts()
    suspend fun getAllCategories() = catalogService.getAllCategories()

    suspend fun getProductsByCategoryWithSkipAndLimit(
        category: String,
        skip: Int,
        limit: Int
    ) = catalogService.getProductsByCategoryWithSkipAndLimit(category, skip, limit)

    suspend fun getProductsWithSkipAndLimit(
        skip: Int,
        limit: Int
    ) = catalogService.getProductWithSkipAndLimit(skip, limit)
}