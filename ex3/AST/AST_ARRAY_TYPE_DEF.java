package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import java.io.PrintWriter;

public class AST_ARRAY_TYPE_DEF extends AST_DEC_ABSTRACT
{
	/***************/
	/*  array ID = type[]; */
	/***************/
	public String name;
	public AST_TYPE type;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_ARRAY_TYPE_DEF(String name, AST_TYPE type, int lineNumber, PrintWriter fileWriter)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== arrayTypedef -> array ID = type[];\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
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
		System.out.print("AST ARRAY TYPE DEF\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/		

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ARRAY DEC\narray ID = type[];\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,this.type.SerialNumber);
	}
	public TYPE SemantMe()
	{
		SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
		if (!symbolTable.isInGlobalScope())
		{
			System.out.format(">> ERROR array %s can only be defined in global scope\n",name);
			System.exit(0);
		}

		TYPE type;
		type = symbolTable.find(this.type.type);

		if (type == null)
		{
			System.out.format(">> ERROR array def with non existing type %s\n",this.type.type);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		if (!type.isClass() && !type.isArray() && this.type.type != "int" && this.type.type != "string")
		{
			System.out.format(">> ERROR array def with non existing type %s\n",this.type.type);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		if (symbolTable.find(name) != null)
		{
			System.out.format(">> ERROR name %s already exists\n",name);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
		}

		symbolTable.enter(name, new TYPE_ARRAY(name, type));

		return null;		
	}
}
