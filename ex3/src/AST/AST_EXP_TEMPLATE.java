package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import java.io.PrintWriter;
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
	public AST_EXP_TEMPLATE(AST_VAR var, String fieldName, AST_EXPRESSIONS expList, int lineNumber, PrintWriter fileWriter)
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

		if(var != null)
		{
			TYPE varType = var.SemantMe();

			if (varType == null)
			{
				System.out.format(">> ERROR non existing variable with field %s\n",fieldName);
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);
			}
			if (!varType.isClass())
			{
				System.out.format(">> ERROR variable isn't of classType and has no field %s\n",fieldName);
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);
			}

			TYPE_CLASS varClass = (TYPE_CLASS)varType;
			fieldType = varClass.findMethodInClass(fieldName);

			if (fieldType==null)
			{
				System.out.format(">> ERROR field %s isn't a method of the class\n",fieldName);
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
				fieldType = scopeClass.findMethodInClass(fieldName);
			}

			if (fieldType == null)
			{
				fieldType = SYMBOL_TABLE.getInstance().find(fieldName);
			}

			if (fieldType == null)
			{
				System.out.format(">> ERROR %s doesn't exist in scope\n", fieldName);
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);
			}

			if(!(fieldType instanceof TYPE_FUNCTION))
			{
				System.out.format(">> ERROR %s can't be called as a function\n", fieldName);
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

		TYPE argType = null;
		TYPE expectedType = null;
		TYPE_CLASS typeClassArg = null;
		TYPE_CLASS typeClassExp = null;

		while(argsTypes != null && expectedTypes != null)
		{
			argType = argsTypes.head;
			expectedType = expectedTypes.head;

			if (argType != expectedType)
			{
				if (!((expectedType.isArray() || expectedType.isClass()) && argType == TYPE_NIL.getInstance()))
				{
					if (expectedType.isClass() && argType.isClass())
					{
						typeClassArg = (TYPE_CLASS)argType;
						typeClassExp = (TYPE_CLASS)expectedType;

						if(typeClassArg.father != typeClassExp && typeClassArg.name != typeClassExp.name)
						{
							System.out.format(">> ERROR %s called with unmatching argType %s\n", fieldName, expectedType.name);
							fileWriter.write("ERROR(" + lineNumber + ")");
							fileWriter.close();
							System.exit(0);
						}
					}

					else
						{
							System.out.format(">> ERROR %s called with unmatching argType %s\n", fieldName,expectedType.name);
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
			System.out.format(">> ERROR args number is not as expected in func %s\n", fieldName);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		return functionFieldType.returnType;
	}

}
