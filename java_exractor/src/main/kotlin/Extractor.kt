import gen.JavaBaseListener
import gen.JavaLexer
import gen.JavaParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.File
import java.io.FileInputStream

class Extractor {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = "Hello.java"
            val input = ANTLRInputStream(FileInputStream(file));
            val lexer = JavaLexer(input)
            val tokens = CommonTokenStream(lexer)
            val parse = JavaParser(tokens)
            val tree = parse.compilationUnit()

            val walker = ParseTreeWalker()
            walker.walk(ExtractInterfaceListener(parse),tree)
        }
    }
}

class ExtractInterfaceListener(private val parse: JavaParser) : JavaBaseListener() {
    override fun enterImportDeclaration(ctx: JavaParser.ImportDeclarationContext) {
        val import = parse.tokenStream.getText(ctx)
        println(import)
    }

    override fun enterClassDeclaration(ctx: JavaParser.ClassDeclarationContext) {
        println("interface I${ctx.Identifier()}{")
    }

    override fun exitClassBodyDeclaration(ctx: JavaParser.ClassBodyDeclarationContext) {
        println("}")
    }

    override fun enterMethodDeclaration(ctx: JavaParser.MethodDeclarationContext) {
        val tokens = parse.tokenStream
        var type = "void"
         if (ctx.type() != null) {
            type = tokens.getText(ctx.type())
        }
        val args = tokens.getText(ctx.formalParameters())
        println("\t$type ${ctx.Identifier()}${args};")
    }

}