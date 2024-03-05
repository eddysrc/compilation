package AST;

import TYPES.*;
import SYMBOL_TABLE.*;
import java.io.PrintWriter;

public class AST_VAR_DEC extends AST_DEC_ABSTRACT
{
	/***************/
	/*  type ID [= exp]; */
	/***************/
	public String name;
	public AST_TYPE type;
	public AST_EXP exp;
	public AST_NEW_EXP newExp;
	public PrintWriter fileWriter;
	public int lineNumber;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_VAR_DEC(String name, AST_TYPE type, AST_EXP exp, AST_NEW_EXP newExp, int lineNumber, PrintWriter fileWriter)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== type ID [= exp];\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.type = type;
		this.exp = exp;
		this.newExp = newExp;
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
		System.out.print("AST VAR DEC STMT\n");

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"VAR DEC\ntype ID [= exp/newExp];\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if(exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
		if(newExp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,newExp.SerialNumber);
	}
	public TYPE SemantMe(TYPE_CLASS fatherClass)
	{
		SYMBOL_TABLE symbolTable = SYMBOL_TABLE.getInstance();
		TYPE type = symbolTable.find(this.type.type);

		if (type == null)
		{
			System.out.format(">> ERROR %d non existing type %s\n",lineNumber,this.type.type);
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		if (symbolTable.findInCurrentScope(name) != null)
		{
			System.out.format(">> ERROR variable %s already exists in scope\n",name);	
			fileWriter.write("ERROR(" + lineNumber + ")");
			fileWriter.close();
			System.exit(0);
		}

		if (fatherClass != null)
		{
			TYPE fatherMember = fatherClass.findMethodInClass(name);

			if(fatherMember != null)
			{
				System.out.format(">> ERROR can't override method %s with a field\n", name);
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);
				
			}

			fatherMember = fatherClass.findFieldInClass(name);

			if (fatherMember!=null)
			{
				if (fatherMember != type)
				{
					System.out.format(">> ERROR can't override field %s with a different type %s\n", name, fatherMember.name);
					fileWriter.write("ERROR(" + lineNumber + ")");
					fileWriter.close();
					System.exit(0);
				}
			}
		}
		
		if(exp!=null || newExp!=null)
		{
			TYPE expType = null;

			if (exp!=null)
			{
				expType = exp.SemantMe();
			}

			else
			{
				expType = newExp.SemantMe();
			}

			TYPE_CLASS typeClass1 = null;
			TYPE_CLASS typeClass2 = null;
			TYPE_ARRAY typeArray1 = null;
			TYPE_ARRAY typeArray2 = null;
			
			if((type.isClass() || type.isArray()) && expType == TYPE_NIL.getInstance())
			{
				symbolTable.enter(name,type);
				return null;
			}

			if(type.isClass() && expType.isClass())
			{
				typeClass1 = (TYPE_CLASS)type;
				typeClass2 = (TYPE_CLASS)expType;

				if(typeClass1.name == typeClass2.name)
				{
					symbolTable.enter(name,type);
					return null;
				}

				while(typeClass2.father!=null)
				{
					if(typeClass2.father.name == typeClass1.name)
					{
						symbolTable.enter(name,type);
						return null;
					}
					typeClass2 = typeClass2.father;
				}
			}

			if(type.isArray() && expType.isArray())
			{
				typeArray1 = (TYPE_ARRAY)type;
				typeArray2 = (TYPE_ARRAY)expType;

				if(typeArray1.innerType == typeArray2.innerType && (typeArray1.name == typeArray2.name || typeArray2.name == null))
				{
					symbolTable.enter(name,type);
					return null;
				} 
			}

			if (type != expType)
			{
				System.out.format(">> ERROR type mismatch for var := exp\n");	
				fileWriter.write("ERROR(" + lineNumber + ")");
				fileWriter.close();
				System.exit(0);
			}
			
			if(symbolTable.isClassScope())
			{
				if(exp == null)
				{
					System.out.format(">> ERROR can't initialize field with newExp\n");	
					fileWriter.write("ERROR(" + lineNumber + ")");
					fileWriter.close();
					System.exit(0);
				}

				if(!(exp instanceof AST_EXP_INT) && !(exp instanceof AST_EXP_STRING) && !(exp instanceof AST_EXP_NIL))
				{
					System.out.format(">> ERROR can't initialize field with complex exp\n");
					fileWriter.write("ERROR(" + lineNumber + ")");
					fileWriter.close();
					System.exit(0);
				}
			}
		}

		symbolTable.enter(name,type);

		return null;		
	}
}
