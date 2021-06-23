// Generated from C:/Users/mx/IdeaProjects/antlr_learn/calc/src\LabledExpr.g4 by ANTLR 4.9.1
package gen;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LabledExprParser}.
 */
public interface LabledExprListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LabledExprParser#prog}.
	 * @param ctx the parse tree
	 */
	void enterProg(LabledExprParser.ProgContext ctx);
	/**
	 * Exit a parse tree produced by {@link LabledExprParser#prog}.
	 * @param ctx the parse tree
	 */
	void exitProg(LabledExprParser.ProgContext ctx);
	/**
	 * Enter a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link LabledExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterPrintExpr(LabledExprParser.PrintExprContext ctx);
	/**
	 * Exit a parse tree produced by the {@code printExpr}
	 * labeled alternative in {@link LabledExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitPrintExpr(LabledExprParser.PrintExprContext ctx);
	/**
	 * Enter a parse tree produced by the {@code assign}
	 * labeled alternative in {@link LabledExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterAssign(LabledExprParser.AssignContext ctx);
	/**
	 * Exit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link LabledExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitAssign(LabledExprParser.AssignContext ctx);
	/**
	 * Enter a parse tree produced by the {@code blank}
	 * labeled alternative in {@link LabledExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterBlank(LabledExprParser.BlankContext ctx);
	/**
	 * Exit a parse tree produced by the {@code blank}
	 * labeled alternative in {@link LabledExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitBlank(LabledExprParser.BlankContext ctx);
	/**
	 * Enter a parse tree produced by the {@code clear}
	 * labeled alternative in {@link LabledExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void enterClear(LabledExprParser.ClearContext ctx);
	/**
	 * Exit a parse tree produced by the {@code clear}
	 * labeled alternative in {@link LabledExprParser#stat}.
	 * @param ctx the parse tree
	 */
	void exitClear(LabledExprParser.ClearContext ctx);
	/**
	 * Enter a parse tree produced by the {@code parens}
	 * labeled alternative in {@link LabledExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterParens(LabledExprParser.ParensContext ctx);
	/**
	 * Exit a parse tree produced by the {@code parens}
	 * labeled alternative in {@link LabledExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitParens(LabledExprParser.ParensContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link LabledExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterMulDiv(LabledExprParser.MulDivContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MulDiv}
	 * labeled alternative in {@link LabledExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitMulDiv(LabledExprParser.MulDivContext ctx);
	/**
	 * Enter a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link LabledExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterAddSub(LabledExprParser.AddSubContext ctx);
	/**
	 * Exit a parse tree produced by the {@code AddSub}
	 * labeled alternative in {@link LabledExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitAddSub(LabledExprParser.AddSubContext ctx);
	/**
	 * Enter a parse tree produced by the {@code id}
	 * labeled alternative in {@link LabledExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterId(LabledExprParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by the {@code id}
	 * labeled alternative in {@link LabledExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitId(LabledExprParser.IdContext ctx);
	/**
	 * Enter a parse tree produced by the {@code int}
	 * labeled alternative in {@link LabledExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void enterInt(LabledExprParser.IntContext ctx);
	/**
	 * Exit a parse tree produced by the {@code int}
	 * labeled alternative in {@link LabledExprParser#expr}.
	 * @param ctx the parse tree
	 */
	void exitInt(LabledExprParser.IntContext ctx);
}