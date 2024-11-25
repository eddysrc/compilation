/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/*************/
/* USER CODE */
/*************/
import java_cup.runtime.*;

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
    private static final int INTEGER_UPPER_LIMIT = (int) Math.pow(2,15) - 1;

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
WhiteSpace	    = [ ]|\t
Letter          = [a-zA-Z]
Digit           = [0-9]
Parentheses     = \(|\)|\{|\}|\[|\]
Operators       = \?|\!|\+|\-|\*|\/|\.|\;
OpComment2      = \?|\!|\+|\-|\.|\;
CommonContent   = {Letter}|{Digit}|{WhiteSpace}|{Parentheses}
Comment1Content = {CommonContent}|{Operators}
Comment2Content = {CommonContent}|{OpComment2}
INT	            = 0|[1-9]{Digit}*
ID              = {Letter}+[{Digit}|{Letter}]*
STRING          = \"{Letter}*\"
COMMENT_1       = \/\/{Comment1Content}*{LineTerminator}
COMMENT_2       = \/\*(({Comment2Content}|\/)*|\**{Comment2Content}+|{LineTerminator})*\**\/
SKIP            = {WhiteSpace}|{LineTerminator}|{COMMENT_1}|{COMMENT_2}
ZERO_LEAD_INT   = 0[0-9]+


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

{SKIP}              { /* just skip what was found, do nothing */ }
"("	                { return symbol(TokenNames.LPAREN);}
")"	                { return symbol(TokenNames.RPAREN);}
"["                 { return symbol(TokenNames.LBRACK);}
"]"                 { return symbol(TokenNames.RBRACK);}
"{"                 { return symbol(TokenNames.LBRACE);}
"}"                 { return symbol(TokenNames.RBRACE);}
"nil"               { return symbol(TokenNames.NIL);}
"+"	                { return symbol(TokenNames.PLUS);}
"-"	                { return symbol(TokenNames.MINUS);}
"*"                 { return symbol(TokenNames.TIMES);}
"/"	                { return symbol(TokenNames.DIVIDE);}
","                 { return symbol(TokenNames.COMMA);}
"."                 { return symbol(TokenNames.DOT);}
";"                 { return symbol(TokenNames.SEMICOLON);}
"int"               { return symbol(TokenNames.TYPE_INT);}
"void"              { return symbol(TokenNames.TYPE_VOID);}
":="                { return symbol(TokenNames.ASSIGN);}
"="                 { return symbol(TokenNames.EQ);}
"<"                 { return symbol(TokenNames.LT);}
">"                 { return symbol(TokenNames.GT);}
"array"             { return symbol(TokenNames.ARRAY);}
"class"             { return symbol(TokenNames.CLASS);}
"extends"           { return symbol(TokenNames.EXTENDS);}
"return"            { return symbol(TokenNames.RETURN);}
"while"             { return symbol(TokenNames.WHILE);}
"if"                { return symbol(TokenNames.IF);}
"new"               { return symbol(TokenNames.NEW);}
"string"            { return symbol(TokenNames.TYPE_STRING);}
{INT}		    	{
                        Integer parsedInteger = Integer.parseInt(yytext());
                        if (parsedInteger > INTEGER_UPPER_LIMIT)
                        {
                            throw new NumberFormatException();
                        }
                        return symbol(TokenNames.INT, parsedInteger);
                    }
{STRING}            { return symbol(TokenNames.STRING, new String( yytext()));}
{ZERO_LEAD_INT}     { throw new NumberFormatException();}
{ID}                { return symbol(TokenNames.ID, new String( yytext()));}
<<EOF>>	            { return symbol(TokenNames.EOF);}
}
