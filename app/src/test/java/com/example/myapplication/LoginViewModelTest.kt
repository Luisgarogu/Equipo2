package com.example.myapplication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.model.UserRequest
import com.example.myapplication.model.UserResponse
import com.example.myapplication.repository.LoginRepository
import com.example.myapplication.viewmodel.LoginViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.eq
import org.mockito.kotlin.doAnswer
import org.mockito.MockedStatic
import java.lang.reflect.Field

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: LoginRepository

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    @Mock
    private lateinit var task: com.google.android.gms.tasks.Task<AuthResult>

    @Mock
    private lateinit var authResult: AuthResult

    @Mock
    private lateinit var userResponse: UserResponse

    @Mock
    private lateinit var observer: Observer<UserResponse>

    private lateinit var loginViewModel: LoginViewModel

    private lateinit var mockedStaticFirebaseAuth: MockedStatic<FirebaseAuth>

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        // Mockear FirebaseAuth.getInstance() para que retorne el mock de firebaseAuth
        mockedStaticFirebaseAuth = mockStatic(FirebaseAuth::class.java)
        mockedStaticFirebaseAuth.`when`<FirebaseAuth> { FirebaseAuth.getInstance() }.thenReturn(firebaseAuth)

        // Instanciar LoginViewModel
        loginViewModel = LoginViewModel()

        // Reemplazar el LoginRepository interno con el mock usando reflection
        val repositoryField: Field = LoginViewModel::class.java.getDeclaredField("repository")
        repositoryField.isAccessible = true
        repositoryField.set(loginViewModel, repository)

        // Configurar el observador para LiveData
        loginViewModel.isRegister.observeForever(observer)
    }

    @After
    fun tearDown() {
        // Cerrar los mocks estáticos
        mockedStaticFirebaseAuth.close()
    }

    @Test
    fun `sesion with non-null email should enable view`() {
        var isEnabled = false
        loginViewModel.sesion("test@example.com") { enabled ->
            isEnabled = enabled
        }
        Assert.assertTrue(isEnabled)
    }

    @Test
    fun `sesion with null email should disable view`() {
        var isEnabled = true
        loginViewModel.sesion(null) { enabled ->
            isEnabled = enabled
        }
        Assert.assertFalse(isEnabled)
    }

    @Test
    fun `loginUser with empty email should return false`() {
        var loginResult = true
        loginViewModel.loginUser("", "password") { isLogin ->
            loginResult = isLogin
        }
        Assert.assertFalse(loginResult)
    }

    @Test
    fun `loginUser with empty password should return false`() {
        var loginResult = true
        loginViewModel.loginUser("test@example.com", "") { isLogin ->
            loginResult = isLogin
        }
        Assert.assertFalse(loginResult)
    }

    @Test
    fun `loginUser with valid credentials should return true`() {
        // Configurar el comportamiento del mock de FirebaseAuth
        whenever(firebaseAuth.signInWithEmailAndPassword("luisgrgrg@gmail.com", "univalle123**"))
            .thenReturn(task)
        whenever(task.isSuccessful).thenReturn(true)

        // Capturar el OnCompleteListener
        val captor = ArgumentCaptor.forClass(com.google.android.gms.tasks.OnCompleteListener::class.java) as ArgumentCaptor<com.google.android.gms.tasks.OnCompleteListener<AuthResult>>

        // Ejecutar el método a probar
        var loginResult = false
        loginViewModel.loginUser("test@example.com", "password") { isLogin ->
            loginResult = isLogin
        }

        // Verificar que se llamó signInWithEmailAndPassword
        verify(firebaseAuth).signInWithEmailAndPassword("test@example.com", "password")

        // Verificar que se agregó un OnCompleteListener
        verify(task).addOnCompleteListener(captor.capture())

        // Simular la llamada del listener
        captor.value.onComplete(task)

        // Verificar el resultado
        Assert.assertTrue(loginResult)
    }

    @Test
    fun `loginUser with invalid credentials should return false`() {
        // Configurar el comportamiento del mock de FirebaseAuth
        whenever(firebaseAuth.signInWithEmailAndPassword("test@example.com", "wrongpassword"))
            .thenReturn(task)
        whenever(task.isSuccessful).thenReturn(false)

        // Capturar el OnCompleteListener
        val captor = ArgumentCaptor.forClass(com.google.android.gms.tasks.OnCompleteListener::class.java) as ArgumentCaptor<com.google.android.gms.tasks.OnCompleteListener<AuthResult>>

        // Ejecutar el método a probar
        var loginResult = true
        loginViewModel.loginUser("test@example.com", "wrongpassword") { isLogin ->
            loginResult = isLogin
        }

        // Verificar que se llamó signInWithEmailAndPassword
        verify(firebaseAuth).signInWithEmailAndPassword("test@example.com", "wrongpassword")

        // Verificar que se agregó un OnCompleteListener
        verify(task).addOnCompleteListener(captor.capture())

        // Simular la llamada del listener
        captor.value.onComplete(task)

        // Verificar el resultado
        Assert.assertFalse(loginResult)
    }

    @Test
    fun `registerUser should call repository registerUser and update LiveData`() {
        // Inicializar con datos de prueba
        val userRequest = UserRequest(email = "newuser@example.com", password = "securepassword")
        val userResponse = UserResponse(
            message = "User registered successfully",
            isRegister = true
            // Asegúrate de incluir todos los parámetros requeridos por el constructor
        )

        // Configurar el comportamiento del mock de repository
        doAnswer {
            val callback = it.getArgument<(UserResponse) -> Unit>(1)
            callback(userResponse)
            null
        }.`when`(repository).registerUser(eq(userRequest), any())

        // Ejecutar el método a probar
        loginViewModel.registerUser(userRequest)

        // Verificar que repository.registerUser haya sido llamado correctamente
        verify(repository).registerUser(eq(userRequest), any())

        // Verificar que el LiveData se haya actualizado con userResponse
        verify(observer).onChanged(userResponse)
    }
}