package AST;
import TYPES.*;
import java.io.PrintWriter;

public class AST_STMT_ASSIGN_NEW extends AST_STMT
{
	/***************/
	/*  var := newExp; */
	/***************/
	public AST_VAR var;
	public AST_NEW_EXP ne;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN_NEW(AST_VAR var, AST_NEW_EXP ne, int lineNumber, PrintWriter fileWriter)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN newExp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.ne = ne;
		this.lineNumber = lineNumber;
		this.fileWriter = fileWriter;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (ne != null) ne.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,ne.SerialNumber);
	}
	public TYPE SemantMe()
	{
		TYPE type1 = null;
		TYPE type2 = null;
		TYPE_CLASS typeClass1 = null;
		TYPE_CLASS typeClass2 = null;
		TYPE_ARRAY typeArray1 = null;
		TYPE_ARRAY typeArray2 = null;

		if (var != null)
		{
			type1 = var.SemantMe();
		}
		if (ne != null)
		{
			type2 = ne.SemantMe();
		}

		if((type1.isClass() || type1.isArray()) && type2 == TYPE_NIL.getInstance())
		{
			return null;
		}

		if(type1.isClass() && type2.isClass())
		{
			typeClass1 = (TYPE_CLASS)type1;
			typeClass2 = (TYPE_CLASS)type2;

			if(typeClass1.name == typeClass2.name)
			{
				return null;
			}

			while(typeClass2.father!=null)
			{
				if(typeClass2.father.name == typeClass1.name)
				{
					return null;
				}

				typeClass2 = typeClass2.father;
			}
		}

		if(type1.isArray() && type2.isArray())
		{
			typeArray1 = (TYPE_ARRAY)type1;
			typeArray2 = (TYPE_ARRAY)type2;
			if(typeArray1.innerType == typeArray2.innerType && (typeArray1.name == typeArray2.name || typeArray2.name == null))
			{
				return null;
			}
		}

		if (type1 != type2)
		{
			System.out.format(">> ERROR type mismatch for var := exp\n");
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		return null;
	}
}
