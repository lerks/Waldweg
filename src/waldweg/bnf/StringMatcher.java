/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

package waldweg.bnf;

class StringMatcher extends Matcher
{
	private String value;
	
	StringMatcher (Parser parser, String value)
	{
		this.parser = parser;
		this.value = new String (value);
	}
	
	protected Matcher clone ()
	{
		return new StringMatcher (parser, value);
	}
	
	protected boolean match (Symbol base, String target, int offset, Matcher ending)
	{
//		System.out.println (offset+"\tReached string: "+value);
		if (target.regionMatches (offset, value, 0, value.length()))
		{
//			System.out.println ("   ++\tMatch succeded");
			if (zeroOrMore && this.match (base, target, offset + value.length(), ending))
				return true;
			if (next != null)
			{
				if (next.match (base, target, offset + value.length(), ending))
					return true;
			}
			else
			{
				if (ending.match (base, target, offset + value.length(), ending))
					return true;
			}
			if (alternative != null && alternative.match (base, target, offset + value.length(), ending))
				return true;
			return false;
		}
		else
		{
//			System.out.println ("   --\tMatch failed");
			if (zeroOrOne || zeroOrMore)
			{
				if (next != null)
				{
					if (next.match (base, target, offset, ending))
						return true;
				}
				else
				{
					if (ending.match (base, target, offset, ending))
						return true;
				}
			}
			if (alternative != null && alternative.match (base, target, offset, ending))
				return true;
			return false;
		}
	}
	
	protected void print ()
	{
		System.out.print ("\""+value+"\" ");
		if (zeroOrOne)
			System.out.print ("? ");
		if (zeroOrMore)
			System.out.print ("* ");
		if (oneOrMore)
			System.out.print ("+ ");
		if (next != null)
			next.print ();
		if (alternative != null) 
		{
			System.out.print ("| ");
			alternative.print ();
		}
	}
}
