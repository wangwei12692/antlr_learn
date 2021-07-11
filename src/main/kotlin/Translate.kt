import gen.ArrayInitBaseListener
import gen.ArrayInitLexer
import gen.ArrayInitParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker

class Translate {
    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            val input = ANTLRInputStream(System.`in`)
            val lexer = ArrayInitLexer(input)
            val tokens = CommonTokenStream(lexer)
            val parser = ArrayInitParser(tokens)
            val tree = parser.init()

            val walker = ParseTreeWalker()
            walker.walk(ShortToUnicodeString(), tree)
            println()
        }
    }
}

class ShortToUnicodeString : ArrayInitBaseListener() {
    override fun enterInit(ctx: ArrayInitParser.InitContext) {
        print('"')
    }

    override fun exitInit(ctx: ArrayInitParser.InitContext) {
        print('"')
    }

    override fun enterValue(ctx: ArrayInitParser.ValueContext) {
        val value = ctx.INT().text.toInt()
        print(String.format("\\u%04x", value))
    }
}