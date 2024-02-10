package AST;

public class AST_DEC extends AST_Node
{
	/***************/
	/*  type ID [= exp]; */
	/***************/
	public AST_DEC_VAR varDec;
	public AST_DEC_FUNC funcDec;
	public AST_DEC_CLASS classDec;
	public AST_DEC_ARRAY arrDec;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_DEC(AST_DEC_VAR varDec)
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
	public AST_DEC(AST_DEC_ARRAY arrDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== arrDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.arrDec = arrDec;
	}
	public AST_DEC(AST_DEC_CLASS classDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== classDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.classDec = classDec;
	}
	public AST_DEC(AST_DEC_FUNC funcDec)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== funcDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.funcDec = funcDec;
	}

	/*********************************************************/
	/* The printing message for an dec array AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST DEC STMT\n");

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (this.varDec != null){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\n varDec\n");
		
			this.varDec.PrintMe();
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,varDec.SerialNumber);
		}
		else if (this.funcDec != null){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\n funcDec\n");
			this.funcDec.PrintMe();
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,funcDec.SerialNumber);
		}
		else if (this.classDec != null){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\n classDec\n");
			this.classDec.PrintMe();
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,classDec.SerialNumber);
		}
		else if (this.arrDec != null){
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"DEC\n arrDec\n");
			this.arrDec.PrintMe();
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,arrDec.SerialNumber);
		}
		
	}
}
