package AST;
import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_C_FIELD_VAR extends AST_C_FIELD
{
	/***************/
	/*  varDec */
	/***************/
	public AST_VAR_DEC varDec;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_C_FIELD_VAR(AST_VAR_DEC varDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== varDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.varDec = varDec;
	}

	/*********************************************************/
	/* The printing message for an dec array AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST CFIELD VAR STMT\n");

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR DEC\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber, varDec.SerialNumber);
	}

	public TYPE SemantMe(TYPE_CLASS fatherClass)
	{
		TYPE type = null;

		if (varDec != null)
		{
			varDec.SemantMe(fatherClass);
			type = SYMBOL_TABLE.getInstance().find(varDec.name);
		}

		return new TYPE_CLASS_VAR_DEC(type, varDec.name);
	}
}
