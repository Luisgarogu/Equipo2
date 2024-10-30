package com.example.myapplication.view.fragment
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.media.MediaPlayer
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


class HomeFr : Fragment() {
    private lateinit var binding: FrHomeBinding
    private lateinit var music: MediaPlayer

    private lateinit var interfaceTimer: CountDownTimer

    private lateinit var interfaceText: TextView

    private lateinit var soundButton: ImageView
    private lateinit var plusButton: ImageView
    private lateinit var bottle: ImageView
    private var countStart = false



    private val soundViewModel: SoundViewModel by viewModels()


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

        plusButton = binding.toolbarContainer.findViewById(R.id.plus_button)
        plusButton.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_retosListFragment)
            music.pause()
        }

        //BOTON DE VOLUMEN
        soundButton = binding.toolbarContainer.findViewById(R.id.sound_button)
        soundButton.setOnClickListener{
            soundHandler(music)
        }

        //CUENTA REGRESIVA

        interfaceText = binding.contText
        interfaceText.visibility = View.GONE
    }

    override fun onStop() {
        super.onStop()
        if (music.isPlaying){
            music.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if(soundViewModel.musicEnabled.value == true){
            music.start()
        } else {
            soundButton.setImageResource(R.drawable.icon_no_sound)
        }
    }
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

    private fun iniCount(initialNumber: Long, firstTime: Boolean = true){
        interfaceText.visibility = View.VISIBLE

        // Resetear botón
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
                   // challengesModel.getResults... I GUESS
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




}