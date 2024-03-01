package AST;

public class AST_ARGUMENTS extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_ARG head;
	public AST_ARGUMENTS next;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_ARGUMENTS(AST_ARG head, AST_ARGUMENTS next)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (next != null) System.out.print("====================== args -> arg args\n");
		if (next == null) System.out.print("====================== args -> arg \n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.head = head;
		this.next = next;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE ARGS LIST\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + next ... */
		/*************************************/
		if (head != null) head.PrintMe();
		if (next != null) next.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ARGS\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (next != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,next.SerialNumber);
	}

	public TYPE_LIST SemantMe()
	{
		TYPE type1 = null;
		TYPE_LIST type2 = null;

		if (head!=null)
		{
			type1 = head.SemantMe();
		}

		if (next!=null)
		{
			type2 = next.SemantMe();
		}
		return new TYPE_LIST(type1, type2);
	}

}
