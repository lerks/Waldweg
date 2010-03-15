/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

package waldweg.bnf;

import java.util.Deque;
import java.util.LinkedList;
import java.text.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * A parser for the Backus-Naur Form
 * <p>
 * A syntax expressed using Backus-Naur Form (BNF for short) is based on symbols, which are themselves sequences of other symbols.
 * The symbols used in these sequences may be terminals or non-terminals. The first ones are strings, the second ones are other symbol, which have to defined in the same way descibed above.
 * Some metacharachters are allowed in the sequence. 
 * <ul>
 *   <li> Choice character "<code>|</code>". It means that the symbol can be either the sequence of symbol preceding this character or the sequence following it.
 *   <li> Grouping characters "<code>(</code>" and "<code>)</code>". A group does nothing but extending the action of the other metahcaracters to more than one symbol. A choice inside a group doesn't include symbols outside that group.
 *   <li> Quantifiers "<code>?</code>", "<code>*</code>" and "<code>+</code>". They respectively mean that the precedent symbol (or group) can be optional, present zero or more times or one or more times.
 *   <li> Shortcuts "<code>[</code>", "<code>]</code>", "<code>{</code>" and "<code>}</code>". They can be translated respectively with "<code>( ) ?</code>" and "<code>( ) *</code>".
 * </ul>
 * Metacharacters can be composed as whished. Strings have to be surrounded by single or double quotes (<code>'...'</code> or <code>"..."</code>);
 * Whitespace can be any of SPACE, CHARACTER TABULATION, CARRIAGE RETURN or LINE FEED (referred with their Unicode names).
 * </p><p>
 * Here are some examples:
 * <ul>
 *   <li><p><code>"a" ? "very" * "beautiful" ("voyage" | "cruise")</code></p>
         <p>It matches "beautiful voyage", "beautiful cruise", "a beautiful voyage", "a beautiful cruise", "very beautiful voyage", "very beautiful cruise", "a very beautiful voyage", "a very beautiful cruise", and so on.</p>
 *   <li><p><code><b>sentence</b> = article ? adjective noun</code></p>
         <p><code><b>article</b> = "a"</code></p>
         <p><code><b>adjective</b> = "very" * "beautiful"</code></p>
         <p><code><b>noun</b> = "voyage" | "cruise"</code></p>
         <p>It matches exactly the same strings as the example above.</p>
 * </ul>
 */
public class Parser
{
	private Map<String,Matcher> symbols = new HashMap<String,Matcher> ();
	
	Map<String,Integer> dfs_helper = new HashMap<String,Integer> ();
	
	private Matcher parseGroup (Tokenizer t)
	{
		GroupMatcher result = new GroupMatcher (this);
		
		while (t.hasMoreTokens())
		{
			String token = t.nextToken ();
//			System.out.println ("Parsing: "+token);
			if (t.isMetacharacter ())
			{
				if (token.charAt (0) == '(')
				{
					result.add(parseGroup (t));
				}
				else if (token.charAt (0) == ')')
				{
					break;
				}
				else if (token.charAt (0) == '[')
				{
					result.add(parseGroup (t));
					result.getTail ().zeroOrOne = true;
				}
				else if (token.charAt (0) == ']')
				{
					break;
				}
				else if (token.charAt (0) == '{')
				{
					result.add(parseGroup (t));
					result.getTail ().zeroOrMore = true;
				}
				else if (token.charAt (0) == '}')
				{
					break;
				}
				else if (token.charAt (0) == '?')
				{
					result.getTail ().zeroOrOne = true;
				}
				else if (token.charAt (0) == '*')
				{
					result.getTail ().zeroOrMore = true;
				}
				else if (token.charAt (0) == '+')
				{
					result.getTail ().oneOrMore = true;
				}
				else if (token.charAt (0) == '|')
				{
					result.getHead ().alternative = parseGroup (t);
					break;
				}
			}
			else if (t.isQuote ())
			{
				result.add(new StringMatcher (this, token));
			}
			else
			{
				result.add(new SymbolMatcher (this, token));
			}
		}
		return result;
	}
	
	/**
	 * Creates an empty Parser
	 */
	public Parser ()
	{
	}
	
	/**
	 * Add a new symbol to the collection of syntax rules.
	 * @param id The name with which the symbol will be referred to.
	 * @param symbol The sequence of symbols and metacharacters, formatted using the rules described above.
	 */
	public void addSymbol (String id, String symbol)
	{
		Matcher result = parseGroup (new Tokenizer (symbol.concat (")")));
		result.clean ();
		symbols.put (id, result);
		dfs_helper.put (id, -1);
	}
	
	Matcher getSymbol (String id)
	{
		return symbols.get (id);
	}
	
	/**
	 * Parses a string using the collection of syntax rules given before.
	 * @param id The name of the top-level symbol used to match the string.
	 * @param target The string which will be parsed.
	 * @return A {@link waldweg.bnf.Symbol} representing the given string.
	 */
	public Symbol parse (String id, String target)
	{
		Symbol result = new Symbol (id, target);
		result.setBegin (0);
		result.setEnd (target.length());
		if (getSymbol (id).match (result, target, 0, new TargetEndMatcher ()))
			return result;
		else
			return new Symbol ("fail", "fail");
	}
}

