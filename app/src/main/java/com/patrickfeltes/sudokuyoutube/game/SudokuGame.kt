package com.patrickfeltes.sudokuyoutube.game

import androidx.lifecycle.MutableLiveData

class SudokuGame {

    var selectedCellLiveData = MutableLiveData<Pair<Int, Int>>()
    var cellsLiveData = MutableLiveData<List<Cell>>()
    val isTakingNotesLiveData = MutableLiveData<Boolean>()
    val highlightedKeysLiveData = MutableLiveData<Set<Int>>()

    private var selectedRow = -1
    private var selectedCol = -1
    private var isTakingNotes = false
    var isSolved = false

    private val gridGenerator = GridGenerator()
    private var board: Board

    init {
        board = generateBoard()

        selectedCellLiveData.postValue(Pair(selectedRow, selectedCol))
        cellsLiveData.postValue(board.cells)
        isTakingNotesLiveData.postValue(isTakingNotes)
    }

    fun generateBoard(): Board {
        val cells = List(9 * 9) { i -> Cell(i / 9, i % 9, 0) }
        board = Board(9, cells)
        println("### generate grid ###")
        var valid: Boolean
        lateinit var grid: IntArray
        do {
            grid = gridGenerator.generateGrid()
            println("### print grid ###")
            gridGenerator.printGrid(grid)
            println("### done grid ###")
            valid = gridGenerator.isPerfect(grid)
            println("is valid: $valid")
        } while (!valid)

        val test = IntArray(79)
        for (i in test.indices) {
            test[i] = (0..80).random()
        }
        for (i in test.indices) {
            cells[test[i]].value = grid[test[i]]
            cells[test[i]].isStartingCell = true
        }
        return board
    }

    fun handleInput(number: Int) {
        if (selectedRow == -1 || selectedCol == -1) return
        val cell = board.getCell(selectedRow, selectedCol)
        if (cell.isStartingCell) return

        if (isTakingNotes) {
            if (cell.notes.contains(number)) {
                cell.notes.remove(number)
            } else {
                cell.notes.add(number)
            }
            highlightedKeysLiveData.postValue(cell.notes)
        } else {
            cell.value = number
        }
        cellsLiveData.postValue(board.cells)
        isSolved = gridGenerator.isPerfect(board.getValues())
    }

    fun updateSelectedCell(row: Int, col: Int) {
        val cell = board.getCell(row, col)
        if (!cell.isStartingCell) {
            selectedRow = row
            selectedCol = col
            selectedCellLiveData.postValue(Pair(row, col))

            if (isTakingNotes) {
                highlightedKeysLiveData.postValue(cell.notes)
            }
        }
    }

    fun changeNoteTakingState() {
        isTakingNotes = !isTakingNotes
        isTakingNotesLiveData.postValue(isTakingNotes)

        val curNotes = if (isTakingNotes) {
            board.getCell(selectedRow, selectedCol).notes
        } else {
            setOf<Int>()
        }
        highlightedKeysLiveData.postValue(curNotes)
    }

    fun delete() {
        val cell = board.getCell(selectedRow, selectedCol)
        if (isTakingNotes) {
            cell.notes.clear()
            highlightedKeysLiveData.postValue(setOf())
        } else {
            cell.value = 0
        }
        cellsLiveData.postValue(board.cells)
    }
}
