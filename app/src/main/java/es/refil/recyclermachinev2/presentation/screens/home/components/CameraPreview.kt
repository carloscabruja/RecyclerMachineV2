package es.refil.recyclermachinev2.presentation.screens.home.components

import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import es.refil.recyclermachinev2.R
import es.refil.recyclermachinev2.presentation.screens.home.HomeViewModel


@Composable
fun CameraPreview(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()

    if (state.value.isCameraVisible) {
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val configuration = LocalConfiguration.current
        val screenHeight = configuration.screenHeightDp

        var previewView: PreviewView

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight.dp * 0.9f),
            ) {
                AndroidView(
                    factory = {
                        previewView = PreviewView(it)
                        viewModel.showCameraPreview(previewView, lifecycleOwner)
                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight.dp * 0.1f),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                    viewModel.captureAndSaveImage(context)
                }) {
                    Icon(
                        painter = painterResource(
                            id = R.drawable.ic_baseline_camera_24
                        ),
                        contentDescription = "Take a picture"
                    )
                }
            }
        }
    }
}