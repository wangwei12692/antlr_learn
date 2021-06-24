import gen.RowsLexer
import gen.RowsParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import java.io.FileInputStream

class Rows {
    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            val file = "Rows.txt"
            val input = ANTLRInputStream(FileInputStream(file));
            val lexer = RowsLexer(input)
            val tokens = CommonTokenStream(lexer)
            val parse = RowsParser(tokens,2)
            parse.buildParseTree = false
            parse.file()
        }
    }
}