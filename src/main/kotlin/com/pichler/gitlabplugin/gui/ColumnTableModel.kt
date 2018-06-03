package com.pichler.gitlabplugin.gui

import javax.swing.table.AbstractTableModel

class ColumnTableModel<T>(
        val columns: List<Column<T, *>>,
        val rows: List<T>
) : AbstractTableModel() {

    override fun getColumnName(column: Int): String = columns[column].title

    override fun getRowCount(): Int = rows.size

    override fun getColumnCount(): Int = columns.size

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? = columns[columnIndex](rows[rowIndex])

    override fun getColumnClass(columnIndex: Int): Class<*> = columns[columnIndex].clz
}

class Column<I, O>(
        val title: String,
        val mapper: (I) -> O,
        val clz: Class<I>
) {
    operator fun invoke(obj: I) = mapper(obj)
}

inline fun <reified I, O> column(title: String,
                                 noinline mapper: (I) -> O) = Column(title, mapper, I::class.java)
