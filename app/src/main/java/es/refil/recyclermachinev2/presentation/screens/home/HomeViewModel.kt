package es.refil.recyclermachinev2.presentation.screens.home

import android.content.Context
import android.net.Uri
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.refil.recyclermachinev2.common.Resource
import es.refil.recyclermachinev2.domain.repo.MainRepository
import es.refil.recyclermachinev2.domain.repo.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MainRepository,
    private val network: NetworkRepository
) : ViewModel() {
    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun processBottle(uuid: String) = viewModelScope.launch {
        _state.value = state.value.copy(isLoading = true)

        // Validate input
        if (_state.value.imagePrediction.isEmpty()) {
            _error.emit("No se ha detectado ningún envase")
            _state.value = state.value.copy(isLoading = false)
            return@launch
        }

        if (_state.value.barcode.isEmpty()) {
            _error.emit("No se ha detectado ningún código de barras")
            _state.value = state.value.copy(isLoading = false)
            return@launch
        }

        network.processBottle(uuid, state.value.barcode, state.value.imagePrediction)
            .collect { data ->
                when (data) {
                    is Resource.Loading -> {

                    }

                    is Resource.Success -> {
                        // If everything is OK -> Clean the screen
                        _state.value = state.value.copy(
                            isLoading = false,
                            imagePrediction = "",
                            barcode = "",
                            imageUri = Uri.EMPTY
                        )
                        // Refresh user points
                        refreshUserPoints(uuid)

                        _state.value = state.value.copy(isLoading = false)
                    }

                    is Resource.Error -> {
                        _error.emit(data.message ?: "An error occurred")
                        _state.value = state.value.copy(isLoading = false)
                    }
                }
            }
    }

    fun startScanning() = viewModelScope.launch {
        repo.startScanning().collect { data ->
            if (!data.isNullOrEmpty()) {
                _state.value = state.value.copy(barcode = data)
            }
        }
    }

    fun showCameraPreview(
        PreviewView: PreviewView,
        lifecycleOwner: LifecycleOwner
    ) = viewModelScope.launch {
        repo.showCameraPreview(PreviewView, lifecycleOwner)
    }

    fun captureAndSaveImage(context: Context) = viewModelScope.launch {
        repo.captureAndSaveImage(context).collect { uriPath ->
            if (uriPath != null) {
                _state.value = state.value.copy(
                    imageUri = uriPath
                )
            } else {
                _error.emit("No se ha podido guardar la imagen")
            }.also {
                if (uriPath != null) {
                    uploadImage(uriPath, context)
                }

                closeCamera()
            }
        }
    }

    fun setUserPoints(points: Int) {
        _state.value = state.value.copy(userPoints = points)
    }

    fun openCamera() {
        _state.value = state.value.copy(isCameraVisible = true)
    }

    private fun closeCamera() {
        _state.value = state.value.copy(isCameraVisible = false)
    }

    private fun uploadImage(imageUri: Uri, context: Context) {
        val contentResolver = context.contentResolver
        val inputStream = contentResolver.openInputStream(imageUri)

        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir).apply {
            inputStream?.use { input ->
                outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        viewModelScope.launch {
            network.uploadImage(tempFile).collect { response ->
                when (response) {
                    is Resource.Success -> {
                        _state.value =
                            state.value.copy(imagePrediction = response.data!!.prediction)
                    }

                    is Resource.Error -> {
                        _error.emit(response.message ?: "An unexpected error occurred")
                    }

                    is Resource.Loading -> {}
                }
            }
        }
    }

    private fun refreshUserPoints(uuid: String) = viewModelScope.launch {
        network.getUserDetails(uuid).collect { response ->
            when (response) {
                is Resource.Success -> {
                    _state.value = state.value.copy(userPoints = response.data!!.points)
                }

                is Resource.Error -> {
                    _error.emit(response.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {

                }
            }
        }
    }
}