package com.patrickfeltes.sudokuyoutube.viewmodel

import androidx.lifecycle.ViewModel
import com.patrickfeltes.sudokuyoutube.game.SudokuGame

class PlaySudokuViewModel : ViewModel() {
    val sudokuGame = SudokuGame()
}