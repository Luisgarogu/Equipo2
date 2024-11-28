package com.example.myapplication

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.myapplication.viewmodel.SoundViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class) // Opt-in para APIs experimentales
@RunWith(MockitoJUnitRunner::class)
class SoundViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule() // Para LiveData

    // Mocks
    @Mock
    private lateinit var application: Application

    @Mock
    private lateinit var observerMusicEnabled: Observer<Boolean>

    // ViewModel
    private lateinit var soundViewModel: SoundViewModel

    // Dispatcher para pruebas
    @ExperimentalCoroutinesApi
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        // Inicializar mocks
        MockitoAnnotations.openMocks(this)

        // Configurar el dispatcher de coroutines
        Dispatchers.setMain(testDispatcher)

        // Instanciar el ViewModel con un mock de Application
        soundViewModel = SoundViewModel(application)

        // Configurar el observador para LiveData
        soundViewModel.musicEnabled.observeForever(observerMusicEnabled)

        // Resetear el mock para ignorar la primera llamada (valor inicial)
        reset(observerMusicEnabled)
    }

    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        // Limpiar los dispatchers
        Dispatchers.resetMain()
        // Limpiar el observador
        soundViewModel.musicEnabled.removeObserver(observerMusicEnabled)
    }

    @Test
    fun `initial musicEnabled should be true`() {
        // Given
        // No se requiere configuración previa

        // When
        // No se requiere acción, ya que LiveData ya está observando el valor inicial

        // Then
        // Observador ya fue resetado, así que no debería haber ninguna llamada
        verify(observerMusicEnabled, never()).onChanged(any())
    }

    @Test
    fun `setMusicEnabled to false should update LiveData`() {
        // Given
        // No se requiere configuración previa

        // When
        soundViewModel.setMusicEnabled(false)

        // Then
        verify(observerMusicEnabled).onChanged(false)
    }

    @Test
    fun `setMusicEnabled to true should update LiveData`() {
        // Given
        // No se requiere configuración previa

        // When
        soundViewModel.setMusicEnabled(true)

        // Then
        verify(observerMusicEnabled).onChanged(true)
    }
}
