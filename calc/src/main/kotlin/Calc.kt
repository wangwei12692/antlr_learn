import gen.LabledExprBaseVisitor
import gen.LabledExprLexer
import gen.LabledExprParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream

class Calc {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val input = ANTLRInputStream(System.`in`)
            val lexer = LabledExprLexer(input)
            val tokens = CommonTokenStream(lexer)
            val parser = LabledExprParser(tokens)
            val tree = parser.prog()
            EvalVisitor().visit(tree)
        }
    }
}

class EvalVisitor : LabledExprBaseVisitor<Int>() {
    private val memory = hashMapOf<String, Int>()

    override fun visitClear(ctx: LabledExprParser.ClearContext?): Int {
        memory.clear()
        return 0
    }

    override fun visitPrintExpr(ctx: LabledExprParser.PrintExprContext): Int {
        val value = visit(ctx.expr())
        println(value)
        return 0
    }

    override fun visitAssign(ctx: LabledExprParser.AssignContext): Int {
        val id = ctx.ID().text
        val value = visit(ctx.expr())
        memory[id] = value
        return value
    }

    override fun visitParens(ctx: LabledExprParser.ParensContext): Int {
        //返回子表达式的值
        return visit(ctx.expr())
    }

    override fun visitMulDiv(ctx: LabledExprParser.MulDivContext): Int {
        val left = visit(ctx.expr(0))
        val right = visit(ctx.expr(1))
        if (ctx.op.type == LabledExprParser.MUL) {
            return left * right
        }
        return left / right
    }

    override fun visitAddSub(ctx: LabledExprParser.AddSubContext): Int {
        val left = visit(ctx.expr(0))
        val right = visit(ctx.expr(1))
        if (ctx.op.type == LabledExprParser.ADD) {
            return left + right
        }
        return left - right
    }

    override fun visitId(ctx: LabledExprParser.IdContext): Int {
        val id = ctx.ID().text
        if (memory[id] != null) {
            return memory[id]!!
        }
        return 0
    }

    override fun visitInt(ctx: LabledExprParser.IntContext): Int {
        return ctx.INT().text.toInt()
    }
}