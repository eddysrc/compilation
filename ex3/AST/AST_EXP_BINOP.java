package AST;
import TYPES.*;
import java.io.PrintWriter;
public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left, AST_EXP right, int OP, int lineNumber, PrintWriter fileWriter)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
		this.lineNumber = lineNumber;
		this.fileWriter = fileWriter;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP="";
		
		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		if (OP == 0) {sOP = "+";}
		if (OP == 1) {sOP = "-";}
		if (OP == 2) {sOP = "*";}
		if (OP == 3) {sOP = "/";}
		if (OP == 4) {sOP = "<";}
		if (OP == 5) {sOP = ">";}
		if (OP == 6) {sOP = "=";}
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)",sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}

	public TYPE SemantMe()
	{
		TYPE type1 = null;
		TYPE type2 = null;
		TYPE_CLASS typeClass1 = null;
		TYPE_CLASS typeClass2 = null;

		if (left  != null)
		{
			type1 = left.SemantMe();
		}

		if (right != null)
		{
			type2 = right.SemantMe();
		}

		if(OP==6)
		{
			if(type1 == type2)
			{
				return TYPE_INT.getInstance();
			}

			if(type1.isClass() && type2.isClass())
			{
				typeClass1 = (TYPE_CLASS)type1;
				typeClass2 = (TYPE_CLASS)type2;

				if(typeClass1.father == typeClass2 || typeClass2.father == typeClass1)
				{
					return TYPE_INT.getInstance();
				}

				System.out.format(">> ERROR cannot make binop between %s and %s\n",type1,type2);
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);

				return null;
			}

			if((type1.isClass() || type1.isArray()) && type2 == TYPE_NIL.getInstance())
			{
				return TYPE_INT.getInstance();
			}

			System.out.format(">> ERROR cannot make binop between %s and %s\n",type1,type2);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		if(OP==3)
		{
			if ((right instanceof AST_EXP_INT)){
				AST_EXP_INT intexp = (AST_EXP_INT)right;
				if(intexp.value==0){
					System.out.format(">> ERROR can't divide by const zero\n");
					fileWriter.write("ERROR(" + lineNumber + ")");
					fileWriter.close();
					System.exit(0);
				}
			}
		}

		if ((type1 == TYPE_INT.getInstance()) && (type2 == TYPE_INT.getInstance()))
		{
			return TYPE_INT.getInstance();
		}

		if((OP==0) && (type1 == TYPE_STRING.getInstance()) && (type2 == TYPE_STRING.getInstance()))
		{
			return TYPE_STRING.getInstance();
		}

		System.out.format(">> ERROR cannot make binop between %s and %s\n",type1,type2);
		fileWriter.write("ERROR(" + lineNumber + ")");
		fileWriter.close();
		System.exit(0);

		return null;
	}
}
