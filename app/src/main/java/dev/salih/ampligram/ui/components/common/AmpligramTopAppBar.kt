package dev.salih.ampligram.ui.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import dev.salih.ampligram.R

@Composable
fun AmpligramTopAppBar(
    actions: @Composable RowScope.() -> Unit = {},
    isBackButtonEnabled: Boolean = false,
    onBackButtonClick: () -> Unit = {},
    title: String = "Ampligram"
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!isBackButtonEnabled) {
                    Image(
                        modifier = Modifier.size(32.dp),
                        painter = painterResource(R.drawable.amplify_logo),
                        contentDescription = "Amplify Logo",
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = title)
            }
        },
        navigationIcon = {
            if (isBackButtonEnabled) {
                IconButton(onClick = onBackButtonClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = actions
    )
}