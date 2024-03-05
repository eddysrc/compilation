package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import java.io.PrintWriter;

public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond, AST_STMT_LIST body, int lineNumber, PrintWriter fileWriter)
	{
		this.cond = cond;
		this.body = body;
		SerialNumber = AST_Node_Serial_Number.getFresh();
		this.lineNumber = lineNumber;
		this.fileWriter = fileWriter;
	}
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE STMT IF\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (cond != null) cond.PrintMe();
		if (body != null) body.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
				SerialNumber,
				"IF (left)\nTHEN right");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (cond != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		if (body != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}
	public TYPE SemantMe()
	{
		if (cond.SemantMe() != TYPE_INT.getInstance())
		{
			System.out.format(">> ERROR condition inside IF is not integral\n");
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		SYMBOL_TABLE.getInstance().beginScope("NONE");
		body.SemantMe();
		SYMBOL_TABLE.getInstance().endScope();

		return null;
	}
}