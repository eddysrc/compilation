package AST;

public class AST_CLASS_DEC extends AST_DEC_ABSTRACT
{
	/***************/
	/*  class ID [extends id] {cField [cField]*} */
	/***************/
	public String name;
	public AST_CFIELD_LIST content;
	public String predecessorName;
	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_CLASS_DEC(String name, String predecessorName, AST_CFIELD_LIST content)
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
		this.predecessorName = predecessorName;
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
}
