package AST;

public class AST_EXP_TEMPLATE extends AST_EXP
{
	/***************/
	/*  [var.]ID([exp[,exp]*]) */
	/***************/
	public AST_VAR var;
	public String fieldName;
	public AST_EXPRESSIONS expList;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_EXP_TEMPLATE(AST_VAR var, String fieldName, AST_EXPRESSIONS expList)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> [var DOT]ID LPAREN [exp [exp]*] RPAREN\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
		this.expList = expList;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE SUBSCRIPT STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (expList != null) expList.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"STMT\n[var.]ID([exp[,exp]*]);\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if(expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
	}
}
