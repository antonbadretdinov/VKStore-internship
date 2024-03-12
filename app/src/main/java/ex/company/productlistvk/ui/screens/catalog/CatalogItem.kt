package ex.company.productlistvk.ui.screens.catalog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import ex.company.productlistvk.R
import ex.company.productlistvk.ui.theme.rating_star_color

@ExperimentalMaterial3Api
@Composable
fun CatalogItem(
    title: String,
    description: String,
    thumbNail: String,
    price: Int,
    rating: Double,
    onItemClicked: () -> Unit
) {

    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            onItemClicked()
        }
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .onSizeChanged { size = it },
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {

            val imageModel = ImageRequest
                .Builder(LocalContext.current)
                .data(thumbNail)
                .crossfade(true)
                .build()

            AsyncImage(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(16.dp)),
                model = imageModel,
                contentDescription = stringResource(id = R.string.catalog_item_description),
                contentScale = ContentScale.Crop
            )

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$price${stringResource(id = R.string.currency_sign)}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    Icon(
                        modifier = Modifier
                            .defaultMinSize(20.dp)
                            .padding(horizontal = 4.dp),
                        painter = painterResource(id = R.drawable.ic_star),
                        contentDescription = stringResource(id = R.string.rating_star_description),
                        tint = rating_star_color
                    )

                    Text(
                        text = rating.toString(),
                        fontSize = 16.sp
                    )
                }

            }

            val containerWidthDp = size.width.dp

            val titleWidthDp = rememberTextMeasurer().measure(
                text = title,
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
            ).size.width.dp

            val descriptionWidthDp = rememberTextMeasurer().measure(
                text = description,
                style = TextStyle(fontSize = 12.sp)
            ).size.width.dp

            Text(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .drawWithContent {
                        drawContent()
                        if ((containerWidthDp - 36.dp) < titleWidthDp)
                            drawFadedEdgeEnd(16.dp)
                    },
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                softWrap = false
            )

            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .drawWithContent {
                        drawContent()
                        if ((containerWidthDp - 36.dp) < descriptionWidthDp)
                            drawFadedEdgeEnd(16.dp)
                    },
                text = description,
                maxLines = 1,
                fontSize = 12.sp,
                softWrap = false
            )
        }
    }
}

fun ContentDrawScope.drawFadedEdgeEnd(edgeWidth: Dp) {
    val edgeWidthPx = edgeWidth.toPx()

    drawRect(
        topLeft = Offset(size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startX = size.width,
            endX = size.width - edgeWidthPx
        ),
        blendMode = BlendMode.DstIn
    )
}