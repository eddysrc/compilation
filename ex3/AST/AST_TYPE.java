package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import java.io.PrintWriter;
public class AST_TYPE extends AST_Node
{
	/***************/
	/*  type */
	/***************/
	public String type;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_TYPE(String type,int lineNumber, PrintWriter fileWriter)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== type\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.lineNumber = lineNumber;
		this.fileWriter = fileWriter;
	}

	/*********************************************************/
	/* The printing message for an dec array AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.format("AST TYPE STMT\n");

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("type\n%s", type));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
	}
	public TYPE SemantMe()
	{
		TYPE type;
		type = SYMBOL_TABLE.getInstance().find(this.type);

		if (type == null)
		{
			System.out.format(">> ERROR non existing type %s\n", this.type);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		return type;
	}
}
