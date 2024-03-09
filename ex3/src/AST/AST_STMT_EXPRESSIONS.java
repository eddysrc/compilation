package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import java.io.PrintWriter;

public class AST_STMT_EXPRESSIONS extends AST_STMT
{
	/***************/
	/*  [var.]ID([exp[,exp]*]); */
	/***************/
	public AST_VAR var;
	public String id;
	public AST_EXPRESSIONS expList;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_EXPRESSIONS(AST_VAR var, String id, AST_EXPRESSIONS expList,int lineNumber, PrintWriter fileWriter)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> [var DOT]ID LPAREN [exp [exp]*] RPAREN SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.id = id;
		this.expList = expList;
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
	public TYPE SemantMe()
	{
		TYPE fieldType = null;

		if (var != null)
		{
			TYPE varType = var.SemantMe();

			if (varType == null)
			{
				System.out.format(">> ERROR non existing variable. Has no field %s\n",id);
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);
			}

			if (!varType.isClass())
			{
				System.out.format(">> ERROR variable isn't of classType and has no fields, including field %s\n",id);
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);
			}

			TYPE_CLASS varClass = (TYPE_CLASS)varType;
			fieldType = varClass.findMethodInClass(id);

			if (fieldType==null)
			{
				System.out.format(">> ERROR field %s isn't a method of the class\n",id);
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();

				System.exit(0);
			}
		}

		else
			{
				String className = SYMBOL_TABLE.getInstance().getClassScopeName();

				if (className!=null)
				{
					TYPE_CLASS scopeClass = (TYPE_CLASS)SYMBOL_TABLE.getInstance().find(className);
					fieldType = scopeClass.findMethodInClass(id);
				}

				if (fieldType == null)
				{
					fieldType = SYMBOL_TABLE.getInstance().find(id);
				}

				if (fieldType == null)
				{
					System.out.format(">> ERROR %s doesn't exist\n", id);
					fileWriter.write("ERROR(" + lineNumber + ")");
					fileWriter.close();
					System.exit(0);
				}

				if (!(fieldType instanceof TYPE_FUNCTION)){
					System.out.format(">> ERROR %s can't be called as a function\n", id);
					fileWriter.write("ERROR(" + lineNumber + ")");
					fileWriter.close();
					System.exit(0);
				}
		}

		TYPE_FUNCTION functionFieldType = (TYPE_FUNCTION)fieldType;
		TYPE_LIST expectedTypes = functionFieldType.params;
		TYPE_LIST argsTypes = null;

		if (expList != null)
		{
			argsTypes = expList.SemantMe();
		}

		while (argsTypes != null && expectedTypes != null)
		{
			TYPE argType = argsTypes.head;
			TYPE expectedType = expectedTypes.head;

			if (argType != expectedType)
			{
				if (!((expectedType.isArray() || expectedType.isClass()) && argType == TYPE_NIL.getInstance()))
				{
					if (expectedType.isClass() && argType.isClass())
					{
						TYPE_CLASS tcarg = (TYPE_CLASS)argType;
						TYPE_CLASS tcexp = (TYPE_CLASS)expectedType;

						if(tcarg.father != tcexp && tcarg.name != tcexp.name)
						{
							System.out.format(">> ERROR %s called with unmatching argType %s\n", id, expectedType.isArray());
							fileWriter.write("ERROR(" + lineNumber + ")");
							fileWriter.close();
							System.exit(0);
						}
					}
					else
						{
							System.out.format(">> ERROR %s called with unmatching argType %s\n", id, expectedType.isArray());
							fileWriter.write("ERROR(" + lineNumber + ")");
							fileWriter.close();
							System.exit(0);
						}
				}
			}
			argsTypes = argsTypes.next;
			expectedTypes = expectedTypes.next;

		}
		if (argsTypes != null || expectedTypes != null)
		{
			System.out.format(">> ERROR args number is not as expected in func %s\n", id);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		return functionFieldType.returnType;
	}
}
