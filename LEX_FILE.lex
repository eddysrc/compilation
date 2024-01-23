/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

public class Utils
{
    private static final int INTEGER_UPPER_LIMIT = 2**15 - 1;

    public static Symbol validateConsumeInteger(string yytext) throws Exception
    {
        // Test with long number
        // Test with -
        int parsedInteger = new Integer(yytext);

        if (parsedInteger > Utils.INTEGER_UPPER_LIMIT)
        {
            throw new Exception();
        }

        return symbol(TokenNames.NUMBER, parsedInteger));
    }
}

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column

/*******************************************************************************/
/* Note that this has to be the EXACT same name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup

/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied verbatim (letter to letter) into the Lexer class code.     */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine() { return yyline + 1; } 

	/**********************************************/
	/* Enable token position extraction from main */
	/**********************************************/
	public int getTokenStartPosition() { return yycolumn + 1; } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator	= \r|\n|\r\n
WhiteSpace		= [ ] | [\t]
Letter          = [a-zA-Z]
Digit           = [0-9]
Parentheses     = \( | \) | \{ | \} \[ | \]
Operators       = \? | ! | \+ | \- | \* | /
Comment1Content = {Letter} | {Digit} | {WhiteSpace} | {Parentheses} | {Operators} | \. | ;
Comment2Content = {Comment1Content} | LineTerminator
KEYWORDS        = class
INT			    = 0 | [1-9]{Digit}*
ID				= {Letter}+[{Digit} | {Letter}]*
STRING          = "{Letter}*"
COMMENT_1       = //[{Comment1Content}]*{LineTerminator}
COMMENT_2       = /*[{Comment2Content}]*/
SKIP            = {WhiteSpace} | {LineTerminator} | {COMMENT_1} | {COMMENT_2}


/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/

/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

"PPP"				{ return symbol(TokenNames.TIMES);}
"("					{ return symbol(TokenNames.LPAREN);}
")"					{ return symbol(TokenNames.RPAREN);}
"["                 { return symbol(TokenNames.RPAREN);}
"]"                 { return symbol(TokenNames.RPAREN);}
"{"                 { return symbol(TokenNames.RPAREN);}
"}"                 { return symbol(TokenNames.RPAREN);}
"nil"               { return symbol(TokenNames.RPAREN);}
"+"					{ return symbol(TokenNames.PLUS);}
"-"					{ return symbol(TokenNames.MINUS);}
"*"                 { return symbol(TokenNames.RPAREN);}
"/"					{ return symbol(TokenNames.DIVIDE);}
","                 { return symbol(TokenNames.RPAREN);}
"."                 { return symbol(TokenNames.RPAREN);}
";"                 { return symbol(TokenNames.RPAREN);}
"int"               { return symbol(TokenNames.RPAREN);}
"void"              { return symbol(TokenNames.RPAREN);}
":="                { return symbol(TokenNames.RPAREN);}
"="                 { return symbol(TokenNames.RPAREN);}
"<"                 { return symbol(TokenNames.RPAREN);}
">"                 { return symbol(TokenNames.RPAREN);}
"array"             { return symbol(TokenNames.RPAREN);}
"class"             { return symbol(TokenNames.RPAREN);}
"extends"           { return symbol(TokenNames.RPAREN);}
"return"            { return symbol(TokenNames.RPAREN);}
"while"             { return symbol(TokenNames.RPAREN);}
"if"                { return symbol(TokenNames.RPAREN);}
"new"               { return symbol(TokenNames.RPAREN);}
{INT}		    	{ Utils.validateConsumeInteger(yytext());}
{STRING}            { return symbol(TokenNames.STRING, new String( yytext()));}
{ID}				{ return symbol(TokenNames.ID, new String( yytext()));}
{SKIP}		        { /* just skip what was found, do nothing */ }
<<EOF>>				{ return symbol(TokenNames.EOF);}
}
