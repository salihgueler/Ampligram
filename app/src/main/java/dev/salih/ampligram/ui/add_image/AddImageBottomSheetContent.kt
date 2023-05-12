package dev.salih.ampligram.ui.add_image

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Create
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AddImageBottomSheetContent(
    onPhotoAdded: (String, String) -> Unit,
    shouldShowKeyboard: Boolean = false
) {
    var value by remember { mutableStateOf("") }
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(focusRequester) {
        if (shouldShowKeyboard) {
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }


    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Add a New Photo",
            modifier = Modifier.padding(bottom = 8.dp, top = 8.dp),
            style = MaterialTheme.typography.headlineLarge
        )
        Divider(
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(5.dp)
                )
                .height(200.dp)
                .fillMaxWidth()
                .clickable(onClick = {
                    singlePhotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                })
        ) {
            if (selectedImageUri != null) {
                AsyncImage(
                    model = selectedImageUri,
                    contentDescription = "Selected image from gallery",
                    modifier = Modifier.fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            } else
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Create,
                        contentDescription = "Image Icon",
                        modifier = Modifier
                            .padding(bottom = 8.dp, top = 8.dp)
                            .size(32.dp)
                    )
                    Text(
                        text = "Image Preview",
                    )
                }
        }
        OutlinedTextField(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = value,
            onValueChange = { value = it },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboard?.hide()
                    focusManager.clearFocus()
                },
            ),
            label = { Text(text = "Image description for accessibility tools") }
        )
        Button(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            onClick = {
                if (selectedImageUri != null && value.isNotBlank()) {
                    value = ""
                    onPhotoAdded(selectedImageUri.toString(), value)
                    selectedImageUri = null
                }
            }
        ) {
            Text(text = "Add a New Post")
        }
    }
}
