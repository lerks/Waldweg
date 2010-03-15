/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

package waldweg.bnf;

class SymbolEndMatcher extends Matcher
{
	private SymbolMatcher begin;
	
	private Symbol parent;
	
	private Matcher ending;
	
	SymbolEndMatcher (SymbolMatcher begin, Symbol parent, Matcher ending)
	{
		this.parent = parent;
		this.begin = begin;
		this.ending = ending;
	}
	
	protected Matcher clone ()
	{
		return new SymbolEndMatcher (begin, parent, ending);
	}
	
	protected boolean match (Symbol base, String target, int offset, Matcher ending)
	{
//		System.out.println (offset+"\tReached the end of Symbol: "+begin.id);
		base.setEnd (offset);
		
		if (begin.zeroOrMore && begin.match (parent, target, offset, this.ending))
			return true;
		if (begin.next != null)
		{
			if (begin.next.match (parent, target, offset, this.ending))
				return true;
		}
		else
		{
			if (this.ending.match (parent, target, offset, this.ending))
				return true;
		}
		if (begin.alternative != null && begin.alternative.match (parent, target, offset, this.ending))
			return true;
		return false;
	}
	
	protected void print ()
	{
	}
}
