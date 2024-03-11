package ex.company.productlistvk.data.network.retrofit

import ex.company.productlistvk.data.network.model.ProductListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {


    @GET("products")
    suspend fun getAllProducts(): ProductListModel

    @GET("products")
    suspend fun getProductWithSkipAndLimit(
        @Query("skip") skip: Int,
        @Query("limit") limit: Int
    ): ProductListModel

}