package dev.salih.ampligram.ui.photo_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import dev.salih.ampligram.model.Comment
import dev.salih.ampligram.ui.components.common.AmpligramTopAppBar
import dev.salih.ampligram.ui.components.common.CenteredCircularProgressIndicator

@OptIn(ExperimentalMaterial3Api::class)
@ExperimentalComposeUiApi
@Composable
fun PhotoDetailScreen(
    isBackButtonEnabled: Boolean,
    onBackButtonClick: () -> Unit,
    photoUrl: String,
    description: String,
    username: String,
    shouldOpenCommentEntry: Boolean = false,
    comments: List<Comment>,
    onCommentSubmitted: (String) -> Unit,
    isLoading: Boolean,
) {
    val value = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(focusRequester) {
        if (shouldOpenCommentEntry && !isLoading) {
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }

    Scaffold(topBar = {
        AmpligramTopAppBar(
            isBackButtonEnabled = isBackButtonEnabled,
            onBackButtonClick = onBackButtonClick,
            title = "Photo Detail"
        )
    }) {
        if (isLoading) {
            CenteredCircularProgressIndicator()
        } else {
            Column(modifier = Modifier.padding(it)) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = description,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "Taken by $username",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Divider()
                if (comments.isNotEmpty())
                    Text(
                        text = "Comments",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(comments) { comment ->
                        Text(
                            text = comment.comment,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = comment.username,
                            modifier = Modifier.padding(8.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Divider()
                    }
                    item {
                        OutlinedTextField(
                            value = value.value,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(
                                onDone = {
                                    if (value.value.isNotEmpty()) {
                                        onCommentSubmitted(value.value)
                                    }
                                    value.value = ""
                                    keyboard?.hide()
                                    focusManager.clearFocus()
                                },
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .focusRequester(focusRequester),
                            onValueChange = { text ->
                                value.value = text
                            },
                            label = { Text("Write a new comment") }
                        )
                    }
                }
            }
        }
    }
}