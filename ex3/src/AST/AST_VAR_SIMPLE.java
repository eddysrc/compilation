package AST;
import SYMBOL_TABLE.*;
import TYPES.TYPE;
import TYPES.TYPE_CLASS;

import java.io.PrintWriter;

public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	public int lineNumber;
	public PrintWriter fileWriter;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name, PrintWriter fileWriter, int lineNumber)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA MEMBERS ... */
		/*******************************/
		this.name = name;
		this.fileWriter = fileWriter;
		this.lineNumber = lineNumber;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}
	public TYPE SemantMe()
	{
		SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
		TYPE type = symbolTable.findInClassScope(name);

		if (type != null)
		{
			return type;
		}

		String className = symbolTable.getClassScopeName();

		if (className != null)
		{
			TYPE_CLASS scopeClass = (TYPE_CLASS) symbolTable.find(className);
			type = scopeClass.findFieldInClass(name);
		}

		if (type == null)
		{
			type = symbolTable.find(name);
		}

		if (type == null)
		{
			System.out.format(">> ERROR %s doesn't exist\n", name);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();

			System.exit(0);
		}

		return type;
	}
}
