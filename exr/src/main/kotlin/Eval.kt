import gen.ExprBaseListener
import gen.ExprBaseVisitor
import gen.ExprLexer
import gen.ExprParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeProperty
import java.io.FileInputStream
import java.util.*

class Eval {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            testEvalVisitor()
        }

        private fun testEvalVisitor() {
            val file = "eval.txt"
            val input = ANTLRInputStream(FileInputStream(file));
            val lexer = ExprLexer(input)
            val tokens = CommonTokenStream(lexer)
            val parse = ExprParser(tokens)
            val result = EvalVisitor().visit(parse.s())
            println("result: $result")
        }
    }
}

class EvaluatorWithProps : ExprBaseListener() {
    val values = ParseTreeProperty<Int>()

    override fun exitAdd(ctx: ExprParser.AddContext) {
       val left = getValue(ctx.e(0))
       val right = getValue(ctx.e(1))
        setValue(ctx, left + right)
    }

    override fun exitMult(ctx: ExprParser.MultContext) {
        val left = getValue(ctx.e(0))
        val right = getValue(ctx.e(1))
        setValue(ctx, left * right)
    }

    override fun exitS(ctx: ExprParser.SContext) {
        setValue(ctx, getValue(ctx.e()))
    }

    override fun exitInt(ctx: ExprParser.IntContext) {
        setValue(ctx, ctx.INT().text.toInt())
    }

    fun setValue(node: ParseTree, value: Int) {
        values.put(node,value)
    }

    fun getValue(node: ParseTree): Int = values.get(node)
}

class TestEvaluator : ExprBaseListener() {
    val stack = Stack<Int>()

    override fun exitAdd(ctx: ExprParser.AddContext) {
        val right = stack.pop()
        val left = stack.pop()
        stack.push(left + right)
    }

    override fun exitMult(ctx: ExprParser.MultContext) {
        val right = stack.pop()
        val left = stack.pop()
        stack.push(left * right)
    }

    override fun exitInt(ctx: ExprParser.IntContext) {
        stack.push(ctx.INT().text.toInt())
    }
}

class EvalVisitor : ExprBaseVisitor<Int>() {
    override fun visitAdd(ctx: ExprParser.AddContext): Int {
        return visit(ctx.e(0)) + visit(ctx.e(1))
    }

    override fun visitMult(ctx: ExprParser.MultContext): Int {
        return visit(ctx.e(0)) * visit(ctx.e(1))
    }

    override fun visitInt(ctx: ExprParser.IntContext): Int {
        return ctx.INT().text.toInt()
    }
}