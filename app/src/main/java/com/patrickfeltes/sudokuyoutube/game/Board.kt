package com.patrickfeltes.sudokuyoutube.game

class Board(val size: Int, val cells: List<Cell>) {
    fun getCell(row: Int, col: Int) = cells[row * size + col]
    fun getValues(): IntArray {
        val arr = IntArray(cells.size)
        cells.forEachIndexed { index, cell ->
            arr[index] = cell.value
        }
        return arr
    }
}