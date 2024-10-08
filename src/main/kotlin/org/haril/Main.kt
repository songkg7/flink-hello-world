package org.haril

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val wordCounter = WordCounterJob()
            wordCounter.execute(args)
        }
    }
}
