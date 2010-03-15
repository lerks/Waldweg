/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

package waldweg.bnf;

class Tokenizer
{
	private String target;
	private int index = 0;
	private int last_index = 0;
	private boolean quote = false;
	private boolean metachar = false;
	
	Tokenizer (String target)
	{
		// TODO Manage null pointer
		this.target = new String (target);
		findNext ();
	}
	
	int getOffset ()
	{
		return last_index;
	}
	
	private boolean isWhitespace (int index)
	{
		if (target.charAt (index) == ' '
		    || target.charAt (index) == '\t'
		    || target.charAt (index) == '\r'
		    || target.charAt (index) == '\n')
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isQuote (int index)
	{
		if (target.charAt (index) == '\''
		    || target.charAt (index) == '\"')
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isMetacharacter (int index)
	{
		if (target.charAt (index) == '|'
		    || target.charAt (index) == '('
		    || target.charAt (index) == ')'
		    || target.charAt (index) == '['
		    || target.charAt (index) == ']'
		    || target.charAt (index) == '{'
		    || target.charAt (index) == '}'
		    || target.charAt (index) == '*'
		    || target.charAt (index) == '?'
		    || target.charAt (index) == '+')
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private void findNext ()
	{
		while (hasMoreTokens () && isWhitespace (index))
		{
			index ++;
		}
	}
	
	boolean hasMoreTokens ()
	{
		return index < target.length ();
	}
	
	boolean isQuote ()
	{
		return quote;
	}
	
	boolean isMetacharacter ()
	{
		return metachar;
	}
	
	String nextToken ()
	{
		// TODO Manage index out of bounds
		quote = false;
		metachar = false;
		if (isMetacharacter (index))
		{
			last_index = index++;
			String result = target.substring (last_index, index);
			findNext ();
			metachar = true;
			return result;
		}
		else if (isQuote (index))
		{
			last_index = ++index;
			while (hasMoreTokens () && !isQuote (index))
			{
				index++;
			}
			String result = target.substring (last_index, index++);
			findNext ();
			quote = true;
			return result;
		}
		else
		{
			last_index = index++;
			while (hasMoreTokens () && !isWhitespace (index) && !isQuote(index) && !isMetacharacter (index))
			{
				index++;
			}
			String result = target.substring (last_index, index);
			findNext ();
			return result;
		}
	}
}

