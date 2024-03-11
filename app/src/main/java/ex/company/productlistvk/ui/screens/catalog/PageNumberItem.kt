package ex.company.productlistvk.ui.screens.catalog

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PageNumberItem(
    pageNumber: Int,
    pressed: Boolean,
    onClick: (number: Int) -> Unit
) {
    FilledIconButton(
        modifier = Modifier.padding(4.dp),
        onClick = {
            onClick(pageNumber)
        },
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = if (pressed)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.background,
            contentColor = if (pressed)
                MaterialTheme.colorScheme.onPrimaryContainer
            else
                MaterialTheme.colorScheme.onBackground
        )
    ) {
        Text(text = pageNumber.toString())
    }
}