import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.assignment.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: FirebaseUser) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val user = repository.signIn(email, password)
                user?.let {
                    _authState.value = AuthState.Success(it)
                } ?: throw Exception("Authentication failed")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign in failed")
            }
        }
    }

    fun signUp(email: String, password: String, confirmPassword: String) {
        if (!validatePasswords(password, confirmPassword)) {
            _authState.value = AuthState.Error("Passwords do not match")
            return
        }

        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading
                val user = repository.signUp(email, password)
                user?.let {
                    _authState.value = AuthState.Success(it)
                } ?: throw Exception("Registration failed")
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Sign up failed")
            }
        }
    }

    fun validateInput(email: String, password: String): String? {
        return when {
            email.isEmpty() || password.isEmpty() ->
                "Please fill in all fields"
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Please enter a valid email address"
            password.length < 6 ->
                "Password must be at least 6 characters"
            else -> null
        }
    }

    private fun validatePasswords(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }
}
