package AST;

public class AST_FUNC_DEC extends AST_DEC_ABSTRACT
{
	/***************/
	/*  type ID([type ID [COMMA type ID]*]){stmt [stmt]*} */
	/***************/
	public String name;
	public AST_TYPE type;
	public AST_ARGS_LIST args;
	public AST_STMT_LIST content;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_FUNC_DEC(String name, AST_TYPE type, AST_ARGS_LIST args, AST_STMT_LIST content)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== funcDec -> type ID([type ID [COMMA type ID]*]){stmt [stmt]*}\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.type = type;
		this.args = args;
		this.content = content;
	}

	/*********************************************************/
	/* The printing message for an dec array AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST FUNC DEC STMT\n");

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"FUNC DEC\ntype ID([type ID [COMMA type ID]*]){stmt [stmt]*}\n");
			
		type.PrintMe();
		if(args!=null) args.PrintMe();
		content.PrintMe();
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if(args!=null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,args.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,content.SerialNumber);
	}
}
