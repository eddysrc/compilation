import java.io.*;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.HashMap;

import java_cup.runtime.Symbol;
   
public class Main
{
	private static final String ERROR_MESSAGE = "ERROR";

	private static HashMap<Integer, String> getTokenValueToNameMapper()
	{
		HashMap<Integer, String> valueToNameMapper = new HashMap<>();
		Class tokenNames = TokenNames.class;
		for (Field f: tokenNames.getDeclaredFields())
		{
			try
			{
				valueToNameMapper.put((Integer) f.get(null), f.getName());
			}
			catch (Exception ignored){}
		}

		return valueToNameMapper;
	}

	public static String getFormattedToken(Symbol symbol, Lexer lexer, HashMap<Integer, String> tokenValueToNameMapper)
	{
		String format;
		switch (symbol.sym) {
			case TokenNames.ID:
				format = "%s(%s)[%d,%d]\n";
				break;
			case TokenNames.INT:
				format = "%s(%d)[%d,%d]\n";
				break;
			case TokenNames.STRING:
				format = "%s(\"%s\")[%d,%d]\n";
				break;
			default:
				format = "%s[%d,%d]\n";
				return String.format(format, tokenValueToNameMapper.get((Integer) symbol.sym), lexer.getLine(), lexer.getTokenStartPosition());
		}

		return String.format(format, tokenValueToNameMapper.get((Integer) symbol.sym), symbol.value, lexer.getLine(), lexer.getTokenStartPosition());
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
		HashMap<Integer, String> valueToNameMapper = getTokenValueToNameMapper();

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
				String formattedToken = getFormattedToken(symbol, lexer, valueToNameMapper);
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


