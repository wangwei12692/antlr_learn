import Utils.stripQuotes
import csv.CSVLexer
import csv.CSVParser
import json.JSONBaseListener
import json.JSONLexer
import json.JSONParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeProperty
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.FileInputStream
import java.lang.StringBuilder

class JsonToXMl {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val file = "t.json"
            val input = ANTLRInputStream(FileInputStream(file));
            val lexer = JSONLexer(input)
            val tokens = CommonTokenStream(lexer)
            val parse = JSONParser(tokens)
            val emitter = XMLEmitter()
            val tree = parse.json()
            ParseTreeWalker().walk(emitter, tree)
            println(emitter.xml.get(tree))
        }
    }
}

class XMLEmitter : JSONBaseListener() {
    val xml = ParseTreeProperty<String>()
    fun getXML(ctx: ParseTree) = xml.get(ctx)
    fun setXML(ctx: ParseTree, s: String) {
        xml.put(ctx, s)
    }

    override fun exitAtom(ctx: JSONParser.AtomContext) {
        setXML(ctx, ctx.text)
    }

    override fun exitString(ctx: JSONParser.StringContext) {
        setXML(ctx, stripQuotes(ctx.text))
    }

    override fun exitObjectValue(ctx: JSONParser.ObjectValueContext) {
        setXML(ctx, getXML(ctx.`object`()))
    }

    override fun exitArrayValue(ctx: JSONParser.ArrayValueContext) {
        setXML(ctx,getXML(ctx.array()))
    }

    override fun exitPair(ctx: JSONParser.PairContext) {
        val tag = stripQuotes(ctx.STRING().text)
        val vctx = ctx.value()
        val x = String.format("<%s>%s</%s>\n", tag, getXML(vctx), tag)
        setXML(ctx,x)
    }

    override fun exitAnObject(ctx: JSONParser.AnObjectContext) {
        val sb = StringBuilder()
        sb.append("\n")
        ctx.pair().forEach {
            sb.append(getXML(it))
        }
        setXML(ctx,sb.toString())
    }

    override fun exitEmptyObject(ctx: JSONParser.EmptyObjectContext) {
        setXML(ctx,"")
    }

    override fun exitArrayOfValues(ctx: JSONParser.ArrayOfValuesContext) {
        val buf = StringBuilder()
        buf.append("\n")
        for (vctx in ctx.value()) {
            buf.append("<element>")
            buf.append(getXML(vctx))
            buf.append("</element>")
            buf.append("\n")
        }
        setXML(ctx,buf.toString())
    }

    override fun exitEmptyArray(ctx: JSONParser.EmptyArrayContext) {
        setXML(ctx,"")
    }

    override fun exitJson(ctx: JSONParser.JsonContext) {
        setXML(ctx, getXML(ctx.children[0]))
    }

}

