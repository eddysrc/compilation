import java.io.*;
import java.io.PrintWriter;

import java_cup.runtime.Symbol;
   
public class Main
{
	private static final String ERROR_MESSAGE = "ERROR";

	public static String getFormattedToken(Symbol symbol, Lexer lexer)
	{
		String formattedToken;
		switch (symbol.sym) {
			case TokenNames.ID:
				formattedToken = String.format("%s(%s)[%d,%d]\n", symbol.sym, symbol.value, lexer.getLine(), lexer.getTokenStartPosition());
				break;
			case TokenNames.INT:
				formattedToken = String.format("%s(%d)[%d,%d]\n", symbol.sym, symbol.value, lexer.getLine(), lexer.getTokenStartPosition());
				break;
			case TokenNames.STRING:
				formattedToken = String.format("%s(\"%s\")[%d,%d]\n", symbol.sym, symbol.value, lexer.getLine(), lexer.getTokenStartPosition());
				break;
			default:
				formattedToken = String.format("%s[%d,%d]\n", symbol.sym, lexer.getLine(), lexer.getTokenStartPosition());
		};

		return formattedToken;
	}

	public static void main(String argv[])
	{
		Lexer lexer;
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
			lexer = new Lexer(fileReader);

			/***********************/
			/* [4] Read next token */
			/***********************/
			symbol = lexer.next_token();

			/********************************/
			/* [5] Main reading tokens loop */
			/********************************/
			while (symbol.sym != TokenNames.EOF)
			{
				/************************/
				/* [6] Print to console */
				/************************/
				String formattedToken = getFormattedToken(symbol, lexer);
				System.out.print(formattedToken);
				
				/*********************/
				/* [7] Print to file */
				/*********************/
				fileContentBuilder.append(formattedToken);

				/***********************/
				/* [8] Read next token */
				/***********************/
				symbol = lexer.next_token();
			}
			
			/******************************/
			/* [9] Close lexer input file */
			/******************************/
			lexer.yyclose();

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


