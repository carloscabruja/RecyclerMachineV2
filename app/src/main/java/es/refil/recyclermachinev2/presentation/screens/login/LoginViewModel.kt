package es.refil.recyclermachinev2.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import es.refil.recyclermachinev2.common.Resource
import es.refil.recyclermachinev2.domain.repo.MainRepository
import es.refil.recyclermachinev2.domain.repo.NetworkRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repo: MainRepository,
    private val network: NetworkRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginScreenState())
    val state = _state.asStateFlow()

    fun startScanning() = viewModelScope.launch {
        repo.startScanning().collect { data ->
            if (!data.isNullOrEmpty()) {
                _state.value = state.value.copy(barcodeScanned = data)
                getUserDetails(data)
            }
        }
    }

    fun setLoadedState() = viewModelScope.launch {
        _state.value = state.value.copy(isLoading = false)
    }

    fun resetSreenState() = viewModelScope.launch {
        _state.value = LoginScreenState()
    }

    private fun getUserDetails(uuid: String) = viewModelScope.launch {
        network.getUserDetails(uuid).collect { response ->
            when (response) {
                is Resource.Success -> {
                    _state.value = state.value.copy(userDetails = response.data)
                }

                is Resource.Error -> {
                    _state.value =
                        state.value.copy(error = response.message ?: "An unexpected error occurred")
                }

                is Resource.Loading -> {
                    _state.value = state.value.copy(isLoading = true)
                }
            }
        }
    }
}