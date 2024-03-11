package ex.company.productlistvk.ui.screens.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ex.company.productlistvk.R
import ex.company.productlistvk.helpers.utils.hasInternetConnection
import ex.company.productlistvk.ui.viewmodel.CatalogViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(
    catalogViewModel: CatalogViewModel = viewModel(
        factory = CatalogViewModel.Factory
    )
) {

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var currentPageState by rememberSaveable {
        mutableIntStateOf(1)
    }

    var totalPageNumber by remember {
        mutableIntStateOf(0)
    }

    val catalogUIState = catalogViewModel.productsStateFlow.collectAsStateWithLifecycle()

    var reloadPage by remember {
        mutableStateOf(false)
    }


    LaunchedEffect(key1 = Unit, key2 = reloadPage) {
        if (catalogUIState.value.products.isEmpty()) {
            catalogViewModel.getProductsByPage(currentPageState)
        }

        if (totalPageNumber == 0) {
            totalPageNumber = catalogViewModel.getTotalPagesNumber()
        }
        reloadPage = false
    }


    val lazyGridState = rememberLazyGridState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(text = stringResource(id = R.string.catalog))
                },
                actions = {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = stringResource(id = R.string.filter_icon_description)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { topAppBarPadding ->
        if (context.hasInternetConnection()) {
            if (catalogUIState.value.products.isNotEmpty()) {
                LazyVerticalGrid(
                    state = lazyGridState,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .padding(topAppBarPadding)
                        .fillMaxSize(),
                    columns = GridCells.Adaptive(minSize = 200.dp),
                ) {
                    items(catalogUIState.value.products.size) { index ->

                        val catalogItem = catalogUIState.value.products[index]

                        CatalogItem(
                            title = catalogItem.title,
                            description = catalogItem.description,
                            thumbNail = catalogItem.thumbnail,
                            price = catalogItem.price,
                            rating = catalogItem.rating
                        )
                    }

                    item(span = {
                        GridItemSpan(maxLineSpan)
                    }) {
                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            items(totalPageNumber) { index ->
                                val page = index + 1
                                PageNumberItem(
                                    pageNumber = page,
                                    pressed = currentPageState == page,
                                    onClick = { newPageClicked ->

                                        catalogViewModel.getProductsByPage(newPageClicked)
                                        currentPageState = newPageClicked

                                        coroutineScope.launch {
                                            lazyGridState.scrollToItem(0)
                                            scrollBehavior.state.heightOffset = 0f
                                        }

                                    })
                            }
                        }
                    }
                }
            } else {
                Box(Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(topAppBarPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_no_internet),
                        contentDescription = stringResource(id = R.string.no_internet_icon_description)
                    )

                    Text(
                        modifier = Modifier.padding(16.dp),
                        text = stringResource(id = R.string.no_internet),
                        fontSize = 18.sp
                    )
                }

                Button(
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        reloadPage = true
                    }) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}