package ru.skillbranch.devintensive.extensions

fun <E> MutableList<E>.insertIf(e: E, index: Int, predicate: MutableList<E>.() -> Boolean): MutableList<E> {
    if (this.predicate()) this.add(index, e)
    return this
}