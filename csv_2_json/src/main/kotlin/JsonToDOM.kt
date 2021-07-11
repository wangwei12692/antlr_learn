import Utils.stripQuotes
import json.JSONBaseListener
import json.JSONLexer
import json.JSONParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeProperty
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.FileInputStream
import java.lang.StringBuilder
import javax.json.stream.JsonParser

class JsonToDOM {

    companion object{
        @JvmStatic
        fun main(args: Array<String>) {
            val file = "t.json"
            val input = ANTLRInputStream(FileInputStream(file));
            val lexer = JSONLexer(input)
            val tokens = CommonTokenStream(lexer)
            val parse = JSONParser(tokens)

            val tree = parse.json()
            val conveter = XMLEmitter()
            ParseTreeWalker().walk(conveter, tree)
            println(conveter.nodes[tree])

        }

    }

    sealed class Node {
        class Element(
            val name: String?,
            val children: MutableList<Node> = mutableListOf()
        ) : Node()

        class TextNode(val content: String) : Node()
    }

    fun Node.Element.toString(): String {
        val content = this.children.joinToString("")
        if (name == null) return content
        return XMLEmitter.tag(name, content)
    }


    class XMLEmitter : JSONBaseListener() {
        companion object {
            fun tag(name: String, content: String): String {
                return buildString {
                    append("<$name>")
                    append(content)
                    append("</$name>")
                }
            }
        }

        val nodes = ParseTreeProperty<Node>()

        override fun exitJson(ctx: JSONParser.JsonContext) {
            nodes.put(ctx, nodes[ctx.children[0]])
        }

        override fun exitAnObject(ctx: JSONParser.AnObjectContext) {
            val ele = Node.Element(null)
            for (pctx in ctx.pair()) {
                ele.children.add(nodes[pctx])
            }
            nodes.put(ctx, ele)
        }

        override fun exitArrayOfValues(ctx: JSONParser.ArrayOfValuesContext) {
            val ele = Node.Element(null)
            for (vctx in ctx.value()) {
                ele.children.add(nodes[vctx])
            }
            nodes.put(ctx,ele)
        }

        override fun exitPair(ctx: JSONParser.PairContext) {
            val name = stripQuotes(ctx.STRING().text)
            val ele = Node.Element(name)
            val value = nodes[ctx.value()]
            if (value != null) {
                if (value is Node.Element && value.name == null) {
                    // if null content, must be object or array, copy in elements
                    ele.children.addAll(value.children)
                } else {
                    ele.children.add(value)
                }
            }
            nodes.put(ctx,ele)

        }

        override fun exitObjectValue(ctx: JSONParser.ObjectValueContext) {
            nodes.put(ctx,nodes[ctx.`object`()])
        }

        override fun exitArrayValue(ctx: JSONParser.ArrayValueContext) {
            nodes.put(ctx,nodes[ctx.array()])
        }

        override fun exitString(ctx: JSONParser.StringContext) {
            val text = Node.TextNode(stripQuotes(ctx.start.text))
            if (ctx.parent is JSONParser.ArrayContext) {
                val implicitTag = Node.Element("element")
                implicitTag.children.add(Node.TextNode(ctx.start.text))
                nodes.put(ctx, implicitTag)
            } else {
                nodes.put(ctx,text)
            }
        }

        override fun exitAtom(ctx: JSONParser.AtomContext) {
            val text = Node.TextNode(ctx.start.text)
            if (ctx.parent is JSONParser.ArrayContext) {
                val implicitTag = Node.Element("element")
                implicitTag.children.add(Node.TextNode(ctx.start.text))
                nodes.put(ctx,implicitTag)
            } else {
                nodes.put(ctx,text)
            }
        }


    }

}