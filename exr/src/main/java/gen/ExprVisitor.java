// Generated from C:/Users/mx/IdeaProjects/antlr_learn/exr/src\Expr.g4 by ANTLR 4.9.1
package gen;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link ExprParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface ExprVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link ExprParser#s}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitS(ExprParser.SContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Add}
	 * labeled alternative in {@link ExprParser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAdd(ExprParser.AddContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Mult}
	 * labeled alternative in {@link ExprParser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMult(ExprParser.MultContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Int}
	 * labeled alternative in {@link ExprParser#e}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInt(ExprParser.IntContext ctx);
}