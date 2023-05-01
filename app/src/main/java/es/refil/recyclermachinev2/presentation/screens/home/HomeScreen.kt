package es.refil.recyclermachinev2.presentation.screens.home

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import es.refil.recyclermachinev2.R
import es.refil.recyclermachinev2.common.ProgressBar
import es.refil.recyclermachinev2.destinations.LoginScreenDestination
import es.refil.recyclermachinev2.domain.model.UserDetails
import es.refil.recyclermachinev2.presentation.screens.home.components.CameraPreview

@Destination
@Composable
fun HomeScreen(
    userDetails: UserDetails,
    viewModel: HomeViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {
    val state = viewModel.state.collectAsState()
    val error = viewModel.error.collectAsState()

    LaunchedEffect(userDetails.uuid) {
        viewModel.setUserPoints(userDetails.points)
    }

    // Set painter
    val painter: AsyncImagePainter = if (state.value.imageUri != Uri.EMPTY) {
        rememberAsyncImagePainter(state.value.imageUri)
    } else {
        rememberAsyncImagePainter(R.drawable.recicler_machine_placeholder)
    }

    // Show error toast
    error.value?.let { errorMessage ->
        ShowToast(message = errorMessage)
    }

    // Show image prediction toast
    if (state.value.imagePrediction.isNotEmpty()) {
        ShowToast(message = state.value.imagePrediction)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Screen content
        when {
            state.value.isLoading -> {
                ProgressBar()
            }

            state.value.isCameraVisible -> {
                CameraPreview()
            }

            else -> {
                Greeting(name = userDetails.name)
                BarcodeSection(
                    barcode = state.value.barcode,
                    onClick = { viewModel.startScanning() }
                )
                ImageSection(
                    painter = painter,
                    onClick = { viewModel.openCamera() }
                )
                PointsSection(points = state.value.userPoints, onClick = {
                    viewModel.processBottle(userDetails.uuid)
                })
                CloseSession {
                    navigator.navigate(LoginScreenDestination)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "Welcome $name",
        style = MaterialTheme.typography.headlineSmall,
    )
}

@Composable
fun BarcodeSection(barcode: String, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = barcode)
        Button(onClick = onClick) {
            Text(text = "Scan barcode of bottle")
        }
    }
}

@Composable
fun ImageSection(painter: AsyncImagePainter, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painter,
            contentDescription = "Image detected",
            modifier = Modifier
                .size(300.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Button(onClick = onClick) {
            Text(text = "Take a picture")
        }
    }
}

@Composable
fun PointsSection(points: Int, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Points: $points")
        Button(onClick = onClick) {
            Text(text = "Process...")
        }
    }
}

@Composable
fun CloseSession(onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = "Close session")
    }
}

@Composable
fun ShowToast(message: String) {
    val context = LocalContext.current

    LaunchedEffect(message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
