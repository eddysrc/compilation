package AST;
import TYPES.*;

public class AST_C_FIELD_LIST extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_C_FIELD head;
	public AST_C_FIELD_LIST next;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_C_FIELD_LIST(AST_C_FIELD head, AST_C_FIELD_LIST next)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (next != null) System.out.print("====================== cFields -> cField cFields\n");
		if (next == null) System.out.print("====================== cFields -> cField \n");

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
		System.out.print("AST NODE CFIELDS LIST\n");

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
			"CFIELDS\nLIST\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (head != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,head.SerialNumber);
		if (next != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,next.SerialNumber);
	}

	public TYPE_LIST SemantMe(TYPE_CLASS fatherClass)
	{
		TYPE type1 = null;
		TYPE_LIST type2 = null;

		if (head!=null)
		{
			type1 = head.SemantMe(fatherClass);
		}

		if (next!=null)
		{
			type2 = next.SemantMe(fatherClass);
		}

		return new TYPE_LIST(type1, type2);
	}

}
