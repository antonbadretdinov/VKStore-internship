package ex.company.productlistvk.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ex.company.productlistvk.data.network.model.ProductListModel
import ex.company.productlistvk.data.network.retrofit.RetrofitInstance
import ex.company.productlistvk.data.repository.CatalogRepository
import ex.company.productlistvk.helpers.FIX_PRODUCTS_NUMBER
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogViewModel(private val catalogRepository: CatalogRepository) : ViewModel() {

    private val mutableProductsStateFlow = MutableStateFlow(ProductListModel(emptyList(), 0))
    val productsStateFlow: StateFlow<ProductListModel> = mutableProductsStateFlow

    private val mutableCategoriesStateFlow = MutableStateFlow(emptyList<String>())
    val categoriesStateFlow: StateFlow<List<String>> = mutableCategoriesStateFlow

    fun getAllProductsByPage(page: Int = 1, category: String = "") {

        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            println(throwable.message)
        }

        clearProducts()
        val skip = (page - 1) * FIX_PRODUCTS_NUMBER
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            if(category.isEmpty()){
                mutableProductsStateFlow.value = catalogRepository.getProductsWithSkipAndLimit(
                    skip = skip,
                    limit = FIX_PRODUCTS_NUMBER
                )
            }else{
                mutableProductsStateFlow.value = catalogRepository.getProductsByCategoryWithSkipAndLimit(
                    category = category,
                    skip = skip,
                    limit = FIX_PRODUCTS_NUMBER
                )
            }

        }
    }

    suspend fun getTotalPagesNumber(category: String = ""): Int {
        return try {
            if(category.isNotEmpty()){
                productsStateFlow.value.products.size / FIX_PRODUCTS_NUMBER
            }else{
                catalogRepository.getAllProducts().total / FIX_PRODUCTS_NUMBER
            }
        }catch (e: Exception){
            e.printStackTrace()
            return 0
        }
    }

    fun getAllCategories(){
        val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            println(throwable.message)
        }

        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            mutableCategoriesStateFlow.value = catalogRepository.getAllCategories()
        }
    }


    private fun clearProducts() {
        mutableProductsStateFlow.value = ProductListModel(emptyList(), 0)
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val catalogRepository = CatalogRepository(RetrofitInstance.service)
                CatalogViewModel(catalogRepository)
            }
        }
    }

}