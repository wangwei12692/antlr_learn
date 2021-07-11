import gen.CymbolBaseListener
import gen.CymbolParser
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.Token
import org.antlr.v4.runtime.tree.ParseTreeProperty

class CheckSymbols {
    companion object{
        fun getType(tokenType: Int):Symbol.Type{
            return when(tokenType){
                CymbolParser.K_VOID -> Symbol.Type.tVOID
                CymbolParser.K_INT -> Symbol.Type.tINT
                CymbolParser.K_FLOAT -> Symbol.Type.tFLOAT
                else -> Symbol.Type.tINVALID
            }
        }

        fun error(t: Token, msg: String) {
            println("line ${t.line}:${t.charPositionInLine} $msg")
        }


    }
}

interface Scope {
    val scopeName: String?

    /** Where to look next for symbols  */
    val enclosingScope: Scope?

    /** Define a symbol in the current scope  */
    fun define(sym: Symbol)

    /** Look up name in this scope or in enclosing scope if not here  */
    fun resolve(name: String): Symbol?
}

open class Symbol(  // All symbols at least have a name
    val name: String,
    val type: Type? = null
) {
    // A generic programming language symbol
    enum class Type {
        tINVALID, tVOID, tINT, tFLOAT
    }

    var scope // All symbols know what scope contains them.
            : Scope? = null

    override fun toString(): String {
        return if (type != Type.tINVALID) "<$name:$type>" else name
    }
}

abstract class BaseScope(  // null if global (outermost) scope
    override val enclosingScope: Scope?
) : Scope {
    val symbols = LinkedHashMap<String, Symbol>()

    override fun resolve(name: String): Symbol? {
        val s = symbols[name]
        if (s != null) return s
        // if not here, check any enclosing scope
        return if (enclosingScope != null) enclosingScope!!.resolve(name) else null
        // not found
    }

    override fun define(sym: Symbol) {
        symbols[sym.name] = sym
        sym.scope = this // track the scope in each symbol
    }

    override fun toString(): String {
        return scopeName + ":" + symbols.keys.toString()
    }
}

class LocalScope(val parent: Scope) : BaseScope(parent) {
    override val scopeName: String
        get() = "locals"
}

class GlobalScope(enclosingScope: Scope?) : BaseScope(enclosingScope) {
    override val scopeName: String
        get() = "globals"
}

class FunctionSymbol(name: String, retType: Type?, override val enclosingScope: Scope?) : Symbol(name, retType), Scope {

    val arguments = LinkedHashMap<String, Symbol>()

    override fun resolve(name: String): Symbol? {
        val s = arguments[name]
        if (s != null) return s
        // if not here, check any enclosing scope
        return enclosingScope?.resolve(name)
        // not found
    }

    override fun define(sym: Symbol) {
        arguments[sym.name] = sym
        sym.scope = this // track the scope in each symbol
    }

    override val scopeName: String
        get() = name

    override fun toString(): String {
        return "function" + super.toString() + ":" + arguments.values
    }
}

class VariableSymbol(name: String, type: Type?) : Symbol(name, type)

class DefPhase : CymbolBaseListener() {
    val scopes = ParseTreeProperty<Scope>()
    lateinit var globals: GlobalScope
    lateinit var currentScope: Scope

    override fun enterFile(ctx: CymbolParser.FileContext) {
        globals = GlobalScope(null)
        currentScope = globals
    }

    override fun exitFile(ctx: CymbolParser.FileContext) {
        println(globals)
    }

    override fun enterFunctionDecl(ctx: CymbolParser.FunctionDeclContext) {
        val name = ctx.ID().text
        val typeTokenType = ctx.type().start.type
        val type = CheckSymbols.getType(typeTokenType)

        //新建一个指向外围作用域的作用域，这样子就完成了入栈操作
        val function = FunctionSymbol(name, type, currentScope)
        currentScope.define(function)
        saveScope(ctx, function)
        currentScope = function
    }

    fun saveScope(ctx: ParserRuleContext, s: Scope) {
        scopes.put(ctx, s)
    }

    override fun exitFunctionDecl(ctx: CymbolParser.FunctionDeclContext) {
        println(currentScope)
        currentScope = currentScope.enclosingScope!!
    }

    override fun exitFormalParameter(ctx: CymbolParser.FormalParameterContext) {
        defineVar(ctx.type(), ctx.ID().symbol)
    }

    private fun defineVar(typeCtx: CymbolParser.TypeContext, nameToken: Token) {
        val typeTokenType = typeCtx.start.type
        val type = CheckSymbols.getType(typeTokenType)
        val `var` = VariableSymbol(nameToken.text,type)
        currentScope.define(`var`)
    }

    override fun exitVarDecl(ctx: CymbolParser.VarDeclContext) {
        defineVar(ctx.type(), ctx.ID().symbol)
    }

    override fun enterBlock(ctx: CymbolParser.BlockContext) {
        currentScope = LocalScope(currentScope)
        saveScope(ctx,currentScope)
    }

    override fun exitBlock(ctx: CymbolParser.BlockContext) {
        println(currentScope)
        currentScope = currentScope.enclosingScope!!


    }
}

class RefPhase(
    val globals:GlobalScope,
    val scopes:ParseTreeProperty<Scope>
): CymbolBaseListener(){
    lateinit var currentScope: Scope

    override fun enterFunctionDecl(ctx: CymbolParser.FunctionDeclContext) {
        currentScope = scopes.get(ctx)
    }

    override fun exitFunctionDecl(ctx: CymbolParser.FunctionDeclContext) {
        currentScope = currentScope.enclosingScope!!
    }

    override fun enterBlock(ctx: CymbolParser.BlockContext) {
        currentScope = scopes.get(ctx)
    }

    override fun exitBlock(ctx: CymbolParser.BlockContext) {
        currentScope = currentScope.enclosingScope!!
    }

    override fun exitVar(ctx: CymbolParser.VarContext) {
        val name = ctx.ID().symbol.text
        val `var` = currentScope.resolve(name)
        if (`var` == null){
            CheckSymbols.error(ctx.ID().symbol,"no such variable：$name")
        }
        if (`var` is FunctionSymbol){
            CheckSymbols.error(ctx.ID().symbol,"$name is not a variable.")
        }
    }
}