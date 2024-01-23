import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
   
public class Main
{
	private static final String ERROR_MESSAGE = "ERROR";

	public static void appendToBuilder(StringBuilder fileContentBuilder, Symbol symbol) {

		switch (symbol.type)
		{
			case TokenNames.ID:
				fileContentBuilder.append("%s(%s)[%d,%d]\n", symbol.type, symbol.value, symbol.getLine(), symbol.getTokenStartPosition());
				break;
			case TokenNames.INT:
				fileContentBuilder.append("%s(%d)[%d,%d]\n", symbol.type, symbol.value, symbol.getLine(), symbol.getTokenStartPosition());
				break;
			case TokenNames.STRING:
				fileContentBuilder.append("%s(\"%s\")[%d,%d]\n", symbol.type, symbol.value, symbol.getLine(), symbol.getTokenStartPosition());
				break;
			default:
				fileContentBuilder.append("%s[%d,%d]\n", symbol.type, symbol.getLine(), symbol.getTokenStartPosition());
		}
	}

	public static void main(String argv[])
	{
		Lexer l;
		Symbol symbol;
		FileReader fileReader;
		PrintWriter fileWriter;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		StringBuilder fileContentBuilder = new StringBuilder();

		try {
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			fileReader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			fileWriter = new PrintWriter(outputFilename);
		}
		catch (FileNotFoundException e){
			return;
		}

		try
		{
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(fileReader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			symbol = l.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (symbol.sym != TokenNames.EOF)
			{
				/************************/
				/* [6] Print to console */
				/************************/
				System.out.print("[");
				System.out.print(l.getLine());
				System.out.print(",");
				System.out.print(l.getTokenStartPosition());
				System.out.print("]:");
				System.out.print(symbol.value);
				System.out.print("\n");
				
				/*********************/
				/* [7] Print to file */
				/*********************/
				appendToBuilder(fileContentBuilder, symbol);
				/***********************/
				/* [8] Read next token */
				/***********************/
				symbol = l.next_token();
			}
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			l.yyclose();

			/**************************/
			/* [10] Close output file */
			/**************************/
			fileWriter.print(fileContentBuilder);
    	}
		catch (Exception e)
		{
			fileWriter.print(ERROR_MESSAGE);
			e.printStackTrace();
		}
		finally
		{
			fileWriter.close();
		}
	}
}


