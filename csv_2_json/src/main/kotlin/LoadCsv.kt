import csv.CSVBaseListener
import csv.CSVLexer
import csv.CSVParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.FileInputStream

class LoadCsv {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = "csv.csv"
            val input = ANTLRInputStream(FileInputStream(file));
            val lexer = CSVLexer(input)
            val tokens = CommonTokenStream(lexer)
            val parse = CSVParser(tokens)
            val walker = ParseTreeWalker()
            val loader = Loader()
            walker.walk(loader,parse.file())
            println(loader.rows)
        }
    }
}

class Loader : CSVBaseListener() {
    companion object {
        const val EMPTY = ""
    }

    val rows = arrayListOf<MutableMap<String, String>>()
    lateinit var head: ArrayList<String>
    lateinit var currentRowFieldValues: ArrayList<String>

    override fun exitHead(ctx: CSVParser.HeadContext?) {
        head = ArrayList()
        head.addAll(currentRowFieldValues)
    }

    override fun enterRow(ctx: CSVParser.RowContext) {
        currentRowFieldValues = arrayListOf()
    }

    override fun exitRow(ctx: CSVParser.RowContext) {
        if(ctx.parent is CSVParser.HeadContext) return
        val map = mutableMapOf<String,String>()
        currentRowFieldValues.forEachIndexed { index, v ->
            map[head[index]] = v
        }
        rows.add(map)
    }

    override fun exitText(ctx: CSVParser.TextContext) {
        currentRowFieldValues.add(ctx.TEXT().text)
    }

    override fun exitString(ctx: CSVParser.StringContext) {
        currentRowFieldValues.add(ctx.STRING().text)
    }

    override fun exitEmpty(ctx: CSVParser.EmptyContext) {
        currentRowFieldValues.add(EMPTY)
    }
}