package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import java.io.PrintWriter;

public class AST_FUNC_DEC extends AST_DEC_ABSTRACT
{
	/***************/
	/*  type ID([type ID [COMMA type ID]*]){stmt [stmt]*} */
	/***************/
	public String name;
	public AST_TYPE type;
	public AST_ARGUMENTS args;
	public AST_STMT_LIST content;
	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_FUNC_DEC(String name, AST_TYPE type, AST_ARGUMENTS args, AST_STMT_LIST content, int lineNumber, PrintWriter fileWriter)
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
		this.lineNumber = lineNumber;
		this.fileWriter = fileWriter;
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

	public TYPE SemantMe(TYPE_CLASS fatherClass)
	{
		TYPE type = null;
		TYPE_LIST typeList = null;
		SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
		type = symbolTable.find(this.type.type);

		if(this.type.type == "void"){
			type = TYPE_VOID.getInstance();
		}

		if (type == null)
		{
			System.out.format(">> ERROR non existing return type %s\n",this.type.type);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		if (symbolTable.findInCurrentScope(name) != null)
		{
			System.out.format(">> ERROR function %s already exists in scope\n",name);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		if (fatherClass != null)
		{
			TYPE fatherMember = fatherClass.findFieldInClass(name);

			if (fatherMember!=null)
			{
				System.out.format(">> ERROR can't override field %s with method\n",name);
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);
			}

			fatherMember = fatherClass.findMethodInClass(name);

			if (fatherMember != null)
			{
				TYPE_FUNCTION fatherMethod = (TYPE_FUNCTION)fatherMember;

				if(fatherMethod.returnType != type)
				{
					System.out.format(">> ERROR can't override function %s with different return type\n",name);
					fileWriter.write("ERROR(" + lineNumber + ")");
					fileWriter.close();
					System.exit(0);
				}

				TYPE_LIST expectedTypes = fatherMethod.params;
				TYPE_LIST argsTypes = null;

				if (args!=null)
				{
					argsTypes = args.SemantMe();
				}

				TYPE argType = null;
				TYPE expectedType = null;
				TYPE_CLASS tcarg = null;
				TYPE_CLASS tcexp = null;

				while (argsTypes != null && expectedTypes != null)
				{
					argType = argsTypes.head;
					expectedType = expectedTypes.head;

					if (argType != expectedType)
					{
						System.out.format(">> ERROR can't overload method %s with different arg types\n",name);
						fileWriter.write("ERROR(" + lineNumber + ")");
						fileWriter.close();
						System.exit(0);
					}

					argsTypes = argsTypes.next;
					expectedTypes = expectedTypes.next;
				}

				if (argsTypes != null || expectedTypes != null)
				{
					System.out.format(">> ERROR can't overload method %s with different amount of args %s\n", name);
					fileWriter.write("ERROR(" + lineNumber + ")");
					fileWriter.close();
					System.exit(0);
				}
			}
		}

		symbolTable.beginScope("NONE");
		symbolTable.enter("return", type);

		if (args != null)
		{
			typeList = args.SemantMe();
		}

		symbolTable.enter(name,new TYPE_FUNCTION(type,name,typeList));
		content.SemantMe();
		symbolTable.endScope();
		symbolTable.enter(name,new TYPE_FUNCTION(type,name,typeList));

		return null;
	}
}
