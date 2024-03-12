package ex.company.productlistvk.data.network.retrofit

import ex.company.productlistvk.data.network.model.ProductListModel
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitApi {


    @GET("products")
    suspend fun getAllProducts(): ProductListModel

    @GET("products/categories")
    suspend fun getAllCategories(): List<String>

    @GET("products")
    suspend fun getProductWithSkipAndLimit(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ProductListModel

    @GET("products/category/{category}")
    suspend fun getProductsByCategoryWithSkipAndLimit(
        @Path("category") category: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ProductListModel
}