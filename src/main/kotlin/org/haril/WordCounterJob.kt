package org.haril

import org.apache.flink.api.common.typeinfo.TypeHint
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.apache.flink.api.java.tuple.Tuple2
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment

internal class WordCounterJob {
    private val words = """
            The quick brown fox jumps over the lazy dog.
            The quick blue fox jumps over the lazy dog.
            The quick brown cat jumps over the lazy dog.
            The quick blue cat jumps over the lazy dog.
        """.trimIndent()

    fun execute(args: Array<String>) {
        val env = StreamExecutionEnvironment.getExecutionEnvironment()
        val source = env.fromData(words).name("in-memory-source")
        val counts = source
            .flatMap { value, out ->
                val tokens = value.lowercase().split("\\W+".toRegex())
                for (token in tokens) {
                    if (token.isNotEmpty()) {
                        out.collect(Tuple2(token, 1))
                    }
                }
            }
            .returns(TypeInformation.of(object : TypeHint<Tuple2<String, Int>>() {}))
            .name("tokenizer")
            .keyBy { it.f0 }
            .sum(1)
            .name("counter")
        counts.print().name("print-sink")
        env.execute("HarilWordCount")
    }
}
