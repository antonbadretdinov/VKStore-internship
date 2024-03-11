package ex.company.productlistvk.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ex.company.productlistvk.ui.screens.catalog.CatalogScreen
import ex.company.productlistvk.ui.theme.ProductListVKTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProductListVKTheme {
                CatalogScreen()
            }
        }
    }
}
