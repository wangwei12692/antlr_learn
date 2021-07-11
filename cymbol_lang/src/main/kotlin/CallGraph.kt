import gen.CymbolBaseListener
import gen.CymbolLexer
import gen.CymbolParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.misc.MultiMap
import org.antlr.v4.runtime.misc.OrderedHashSet
import org.antlr.v4.runtime.tree.ParseTreeProperty
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.FileInputStream

class CallGraph {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = "t.json"
            val input = ANTLRInputStream(FileInputStream(file));
            val lexer = CymbolLexer(input)
            val tokens = CommonTokenStream(lexer)
            val parse = CymbolParser(tokens)
            val tree = parse.file()
            val collector = FunctionListener()
            ParseTreeWalker().walk(collector, tree)
            println(collector.graph.toDot())
        }
    }
}

class Graph {
    val nodes = OrderedHashSet<String>()
    val edges = MultiMap<String, String>()

    fun edge(source: String, target: String) {
        edges.map(source, target)
    }

    fun toDot(): String {
        return buildString {
            append("digraph G {\n")
            append("  ranksep=.25;\n")
            append("  edge [arrowsize=.5]\n")
            append("  node [shape=circle,fontname=\"ArialNarrow\",\n")
            append("  fontsize=12, fixedsize=true, height=.45];\n")
            append("   ")
            for (node in nodes) {
                append(node)
                append("; ")
            }
            append("\n")
            for (src in edges.keys) {
                for (trg in edges[src]!!) {
                    append(" ")
                    append(src)
                    append(" -> ")
                    append(trg)
                    append(";\n")
                }
            }
            append("}\n")
        }
    }
}

class FunctionListener : CymbolBaseListener() {
    val graph = Graph()
    var currentFunctionName: String? = null

    override fun enterFunctionDecl(ctx: CymbolParser.FunctionDeclContext) {
        currentFunctionName = ctx.ID().text
        graph.nodes.add(currentFunctionName)
    }

    override fun exitCall(ctx: CymbolParser.CallContext) {
        val funcName = ctx.ID().text
        graph.edge(currentFunctionName!!, funcName)
    }
}
















