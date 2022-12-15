package com.example.firstapp.dragging

import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.firstapp.R
import com.example.firstapp.databinding.ActivityDragAndDropBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlin.reflect.safeCast

class DragAndDropActivity : AppCompatActivity() {

    private val GAME_TIME: Long = 30_000L
    private lateinit var binding: ActivityDragAndDropBinding

    private val aliveGroup = arrayListOf<String>()
    private val deathGroup = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDragAndDropBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // Set each element in one random position
        Stars.getAllStars().forEach { star ->
            val chip = Chip(this)
            chip.text = star.name
            chip.chipIcon = star.getIcon(this)

            // Col·loquem el chip en un dels dos grups aleatoriament
            if (listOf(true, false).random()) {
                aliveGroup.add(star.name)
                binding.aliveChipsGroup.addView(chip)
            } else {
                deathGroup.add(star.name)
                binding.deathChipsGroup.addView(chip)
            }

            // Implementem el moviment del chip, que comenci el dragging
            chip.setOnLongClickListener {
                val shadow = View.DragShadowBuilder(it)
                it.startDragAndDrop(null, shadow, it, 0)
            }
        }

        binding.aliveChipsGroup.setOnDragListener(dragListenerMove)
        binding.deathChipsGroup.setOnDragListener(dragListenerMove)

        startBlackholeAnimations()
    }

    private val dragListenerMove = View.OnDragListener { destinationView, draggingView ->

        // Obtenim l'objecte que s'està arrossegant de manera segura
        val chip = draggingView.localState
        if (chip !is Chip)
            return@OnDragListener false

        when (draggingView.action) {
            // 1. Comença el drag: Hem de retornar true per continuar el drag. False per detenir-lo
            DragEvent.ACTION_DRAG_STARTED -> {
                chip.isVisible = false
            }

            // 2. Ens notifica que hem deixat anar l'element sobre la vista
            DragEvent.ACTION_DROP -> {

                // Remove form old view, insert to destination view
                ChipGroup::class.safeCast(chip.parent)?.removeView(chip)
                ChipGroup::class.safeCast(destinationView)?.addView(chip)

                // Update data lists
                val starName = chip.text.toString()
                if (destinationView.id == binding.aliveChipsGroup.id) {
                    aliveGroup.add(starName)
                    deathGroup.remove(starName)
                } else {
                    deathGroup.add(starName)
                    aliveGroup.remove(starName)
                }

                checkVictory()
            }

            // 3. Quan acabem el drag
            DragEvent.ACTION_DRAG_ENDED -> {
                chip.isVisible = true
            }
        }

        return@OnDragListener true
    }

    /**
     * GAME CONTROLLER
     */

    private fun finishGame() {
        // Tornem el BH a la seva mida original
        binding.blackhole.animate().scaleX(1f).scaleY(1f).apply {
            duration = 1_000
        }

        // Desactivem el drag
        binding.aliveChipsGroup.setOnDragListener(null)
        binding.deathChipsGroup.setOnDragListener(null)
    }

    private fun loseGame() {
        binding.loseMessage.isVisible = true
        finishGame()
    }

    private fun checkVictory() {
        if (aliveGroup.all { Stars.checkStatusOf(it) == Star.LifeStatus.ALIVE } &&
            deathGroup.all { Stars.checkStatusOf(it) == Star.LifeStatus.DEATH }) {
            finishGame()
            Toast.makeText(this, getString(R.string.win_message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun startBlackholeAnimations() {
        // Creix
        binding.blackhole.animate().scaleX(5f).scaleY(5f).apply {
            duration = GAME_TIME
            withEndAction {
                loseGame()
            }
        }

        // Gira
        binding.blackhole.startAnimation(
            RotateAnimation(
                0f,
                360f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            ).apply {
                repeatCount = Animation.INFINITE
                duration = 3_000L
            })
    }
}