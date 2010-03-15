/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

package waldweg.bnf;

abstract class Matcher
{
	Parser parser = null;
	
	Matcher next = null;
	
	Matcher alternative = null;
	
	boolean zeroOrOne = false;
	
	boolean zeroOrMore = false;
	
	boolean oneOrMore = false;
	
	protected abstract Matcher clone ();
	
	protected void clean ()
	{
		if (zeroOrMore || (oneOrMore && zeroOrOne))
		{
			zeroOrMore = true;
			oneOrMore = false;
			zeroOrOne = false;
		}
		if (oneOrMore)
		{
			Matcher second = clone ();
			second.next = next;
			next = second;
			next.zeroOrMore = true;
			oneOrMore = false;
		}
		if (next != null)
		{
			next.clean ();
		}
		if (alternative != null)
		{
			alternative.clean ();
		}
	}
	
	protected abstract boolean match (Symbol base, String target, int offset, Matcher ending);
	
	protected abstract void print ();
}
