package uz.ghost.memorygame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.constraintlayout.widget.StateSet.TAG
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val images = mutableListOf(R.drawable.ic_heart, R.drawable.ic_light, R.drawable.ic_plane, R.drawable.ic_smiley)
        images.addAll(images)
        images.shuffle()

        buttons = listOf(imageButton1, imageButton2, imageButton3, imageButton4, imageButton5,
            imageButton6, imageButton7, imageButton8)

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                Log.i(TAG, "button clicked!!")
                updateModels(index)
                updateViews()
            }
        }
    }

    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.1f
            }
            button.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.ic_code)
        }
    }

    private fun updateModels(position: Int) {
        val card = cards[position]
        if (card.isFaceUp) {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show()
            return
        }
        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].identifier == cards[position2].identifier) {
            Toast.makeText(this, "Match found!!", Toast.LENGTH_SHORT).show()
            cards[position1].isMatched = true
            cards[position2].isMatched = true
        }
    }
}