package com.example.myapplication.view.fragment
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import com.example.myapplication.databinding.FrHomeBinding
import com.example.myapplication.R
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myapplication.viewmodel.SoundViewModel
import kotlin.random.Random
import androidx.navigation.fragment.findNavController
import com.example.myapplication.view.LoginRegisterActivity
import com.example.myapplication.view.dialog.RandomRetoDialog
import com.example.myapplication.viewmodel.RetosViewModel
import com.google.firebase.auth.FirebaseAuth


class HomeFr : Fragment() {
    private lateinit var binding: FrHomeBinding



    private lateinit var interfaceTimer: CountDownTimer

    private lateinit var interfaceText: TextView

    private lateinit var soundButton: ImageView
    private lateinit var plusButton: ImageView
    private lateinit var bottle: ImageView
    private lateinit var shareButton: ImageView
    private lateinit var starButton: ImageView
    private lateinit var controllButton: ImageView
    private lateinit var logout_button: ImageView


    private var countStart = false

    private lateinit var music: MediaPlayer
    private val soundViewModel: SoundViewModel by viewModels()
    private val retosViewModel: RetosViewModel by viewModels()




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FrHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //BOTELLA INI
        bottle = binding.bottle

        //REPRODUCCION DE MUSICA
        music = MediaPlayer.create(requireContext(),R.raw.home_sound_background)
        music.isLooping = true
        if(soundViewModel.musicEnabled.value == true){
            music.start()
        }
        //BOTON PRESIONAME
        val lotButton = binding.pushButton
        val layoutParams = lotButton.layoutParams

        //EFECTO AL PRESIONAR EL BOTON DEL INICIO
        lotButton.setOnTouchListener { _, motionEvent ->

            //ANIMACION BOTELLA
            turnBottle()

            // EFECTO BOTON INICIO
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    layoutParams.width = layoutParams.width - 40
                    layoutParams.height = layoutParams.width - 40
                    lotButton.layoutParams = layoutParams
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {

                    layoutParams.width = layoutParams.width + 40
                    layoutParams.height = layoutParams.width + 40
                    lotButton.layoutParams = layoutParams
                    true
                }
                else -> false
            }
        }
        //BOTON DE NOTAS
        plusButton = binding.toolbarContainer.findViewById(R.id.plus_button)
        applyPressAnimation(plusButton)
        plusButton.setOnClickListener {
            // ESPERAR LA ANIMACIÓN
            Handler(Looper.getMainLooper()).postDelayed({
                findNavController().navigate(R.id.action_homeFragment_to_retosListFragment)
                music.pause()
            }, 200)
        }

        //BOTON DE VOLUMEN
        soundButton = binding.toolbarContainer.findViewById(R.id.sound_button)
        applyPressAnimation(soundButton)
        soundButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                soundHandler(music)
            }, 200)
        }

        //BOTON DE COMPARTIR
        shareButton = binding.toolbarContainer.findViewById(R.id.share_button)
        applyPressAnimation(shareButton)
        shareButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                sharApp()
            }, 200)
        }

        //BOTON DE CALIFICAR
        starButton = binding.toolbarContainer.findViewById(R.id.star_button)
        applyPressAnimation(starButton)
        starButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                qualApp()
            }, 200)
        }

        //BOTON REGLAS
        controllButton = binding.toolbarContainer.findViewById(R.id.controll_button)
        applyPressAnimation(controllButton)
        controllButton.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                navRulesFragment(music)
            }, 200)
        }

        //BOTON DE CERRAR SESION
        logout_button = binding.toolbarContainer.findViewById(R.id.logout_button)
        applyPressAnimation(logout_button)
        logout_button.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                navLogin(music)
            }, 200)
        }


        //CUENTA REGRESIVA

        interfaceText = binding.contText
        interfaceText.visibility = View.GONE

        // Extrae un reto aleatorio de la BD
        retosViewModel.getRandomReto()

        // Extrae los pokemons de la API
        retosViewModel.getPokemonlist()
    }

    //NAVEGAR A LAS REGLAS

    private fun navRulesFragment (music: MediaPlayer){
        music.pause()
        findNavController().navigate(R.id.action_homeFr_to_rulesFr)
    }

    //CERRAR SESION
    private fun navLogin (music: MediaPlayer){
        music.pause()
        val intent = Intent(requireActivity(), LoginRegisterActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
        FirebaseAuth.getInstance().signOut()
        //findNavController().navigate.setContentView(R.layout.activity_login_register)
    }

    //CALIFICAR APLICACIÓN

    private fun qualApp() {
        //INICIALIZACIÓN DE VARIABLE PARA TOMAR EL URI DE LA PLAY STORE PARA CALIFICAR NEQUI
        val playStoreURI = Uri
            .parse("https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es&pli=1")

        val playStoreInt = Intent(Intent.ACTION_VIEW, playStoreURI)
        try {
            //INTENTAR IR A LA PLAYSTORE
            startActivity(playStoreInt)
        } catch (e: ActivityNotFoundException) {
            // SI NO ESTÁ LA APPSTORE USAR EL NAVEGADOR
            playStoreInt.data = Uri
                .parse("https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es&pli=1")
            startActivity(playStoreInt)
        }
    }

    //COMPARTIR APLICACION

    private fun sharApp() {
        val sentTry = Intent().apply {

            action = Intent.ACTION_SEND
            type = "text/plain"

            putExtra(Intent
                .EXTRA_TEXT,
                "App pico botella " +
                        "\nSolo los valientes lo juegan !!:" +
                        "\n https://play.google.com/store/apps/details?id=com.nequi.MobileApp&hl=es_419&gl=es")
        }

        val shareIntent = Intent.createChooser(sentTry, null)
        startActivity(shareIntent)
    }

    //INACTIVAR MUSICA
    override fun onStop() {
        super.onStop()
        if (music.isPlaying){
            music.pause()
        }
    }

    //HANDLER PARA EL MANEJOR DEL SONIDO
    private fun soundHandler(music: MediaPlayer){

        if(music.isPlaying){
            soundButton.setImageResource(R.drawable.icon_no_sound)
            soundViewModel.setMusicEnabled(false)
            music.pause()
        }else{
            soundButton.setImageResource(R.drawable.icon_sound)
            soundViewModel.setMusicEnabled(true)
            music.start()
        }

    }
    //PAUSAR MUSICA
    override fun onResume() {
        super.onResume()
        if(soundViewModel.musicEnabled.value == true){
            music.start()
        } else {
            soundButton.setImageResource(R.drawable.icon_no_sound)
        }
    }

    //INICIAR CONTADOR
    private fun iniCount(initialNumber: Long, firstTime: Boolean = true){
        interfaceText.visibility = View.VISIBLE

        // REINICIAR BOTON
        binding.pushButton.visibility = View.GONE

        interfaceTimer = object : CountDownTimer(initialNumber * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //ACTUALIZAR TEXTO
                val restSeconds = millisUntilFinished / 1000
                interfaceText.text = restSeconds.toString()
            }

            override fun onFinish() {
                // FINALIZAR AL TERMINAR LA CUENTA REGRESIVA
                interfaceText.visibility = View.GONE
                binding.pushButton.visibility = View.VISIBLE

                if (!firstTime){

                    // CARGAR CHALLENGE
                    retosViewModel.getRandomReto()

                    val dialogBuilder = RandomRetoDialog(retosViewModel)
                    val dialog = dialogBuilder.showDialog(binding.root.context)

                    dialog.setOnDismissListener {
                        onResume()
                    }
                    countStart = false
                }
            }
        }

        // INICIA CUENTA REGRESIVA
        interfaceTimer.start()
    }
    private fun turnBottle() {


        // DESHABILITAR BOTON
        binding.pushButton.visibility = View.GONE

        // SONIDO BOTELLA
        val spin_sound_bottle: MediaPlayer = MediaPlayer.create(requireContext(),R.raw.bottle_spin)
        spin_sound_bottle.start()

        // PARAR MUSICA
        onStop()

        // POSICIÓN ACTUAL BOTELLA
        val currPosRotation = bottle.rotation

        // ANGULO DE GIRO ALEATORIO
        val randomAngle = currPosRotation + 180f + Random.nextFloat() * 720f

        // OBJETO DE ROTACION CON ANGULO BOTELLA Y EL ANGULO ALEATORIO
        val rotation = ObjectAnimator
            .ofFloat(bottle, "rotation", currPosRotation, randomAngle)

        // GIRAR 4 SEGUNDOS
        rotation.duration = 4000

        // INTERPOLADOR PARA EL MOVIMIENTO FLUIDO
        rotation.interpolator = LinearInterpolator()

        // INICIAR ANIMACIÓN
        rotation.start()

        val handler = Handler(Looper.getMainLooper())

        handler.postDelayed({
            if(countStart == false){
                countStart = true
                interfaceText.visibility = View.VISIBLE


                // CONTADOR DESDE 3

                iniCount(4, firstTime = false)
            }
        }, 5000)
    }

    //ANIMACION DE CADA BOTON

    @SuppressLint("ClickableViewAccessibility")
    private fun applyPressAnimation(view: View) {
        view.setOnTouchListener { _, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    // SIMULAR PRESIONAR BOTON AL ALTERAR ESCALAS X y Y
                    ObjectAnimator.ofFloat(view, "scaleX", 0.9f).apply {
                        duration = 100
                        start()
                    }
                    ObjectAnimator.ofFloat(view, "scaleY", 0.9f).apply {
                        duration = 100
                        start()
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    //VOLVER A LA ESCALA NORMAL
                    ObjectAnimator.ofFloat(view, "scaleX", 1f).apply {
                        duration = 100
                        start()
                    }
                    ObjectAnimator.ofFloat(view, "scaleY", 1f).apply {
                        duration = 100
                        start()
                    }
                }
            }
            false
        }
    }
}