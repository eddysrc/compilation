package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import java.io.PrintWriter;

public class AST_CLASS_DEC extends AST_DEC_ABSTRACT
{
	/***************/
	/*  class ID [extends id] {cField [cField]*} */
	/***************/
	public String name;
	public String fatherName;
	public AST_C_FIELD_LIST content;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_CLASS_DEC(String name, String fatherName, AST_C_FIELD_LIST content, int lineNumber, PrintWriter fileWriter)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== classDec -> class ID [extends id] {cField [cField]*}\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.fatherName = fatherName;
		this.content = content;
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
		System.out.print("AST CLASS DEC STMT\n");

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"CLASS DEC\nclass ID [extends id] {cField [cField]*}\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,content.SerialNumber);
	}
	public TYPE SemantMe()
	{
		SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();

		if (!symbolTable.isInGlobalScope())
		{
			System.out.format(">> ERROR class %s can only be defined in global scope\n",name);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		TYPE_CLASS fatherClass = null;

		if (fatherName != null)
		{
			fatherClass = (TYPE_CLASS)symbolTable.find(fatherName);
			if (fatherClass == null)
			{
				System.out.format(">> ERROR class %s doesn't exist\n",fatherName);
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);
			}
		}

		symbolTable.beginScope(name);
		symbolTable.enter(name,new TYPE_CLASS(fatherClass,name, null));
		TYPE_CLASS type = new TYPE_CLASS(fatherClass,name,(TYPE_LIST)content.SemantMe(fatherClass));
		symbolTable.endScope();

		if (symbolTable.find(name) != null)
		{
			System.out.println(">> ERROR class name "+ name +" already exists");
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		symbolTable.enter(name,type);

		return null;		
	}
}
