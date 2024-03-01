package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import java.io.PrintWriter;
public class AST_STMT_RETURN_EXP extends AST_STMT
{
	/***************/
	/* return [exp]; */
	/***************/
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN_EXP(AST_EXP exp, int lineNumber, PrintWriter fileWriter)
	{
		SerialNumber = AST_Node_Serial_Number.getFresh();
		this.exp = exp;
		this.lineNumber = lineNumber;
		this.fileWriter = fileWriter;
	}
	public void PrintMe()
	{
		
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST RETURN STMT\n");

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"return exp\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if(exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}
	public TYPE SemantMe()
	{
		TYPE type = null;

		if(exp != null)
		{
			type = exp.SemantMe();
		}

		TYPE expectedReturnType = SYMBOL_TABLE.getInstance().find("return");

		if (expectedReturnType == null)
		{
			System.out.format(">> ERROR no returnType in function scope");
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		if (expectedReturnType==TYPE_VOID.getInstance())
		{
			if( type != null)
			{
				System.out.format(">> ERROR function is of type void and return stmt isn't null");
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);
			}
			return null;
		}

		if(expectedReturnType != type)
		{
			System.out.format(">> ERROR expectedReturnType is %s and return stmt's type is %s", expectedReturnType, type);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		return null;
	}
}