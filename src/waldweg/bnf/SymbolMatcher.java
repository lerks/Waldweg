/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

package waldweg.bnf;

class SymbolMatcher extends Matcher
{
	private String id;
	
	SymbolMatcher (Parser parser, String id)
	{
		this.parser = parser;
		this.id = new String (id);
	}
	
	protected Matcher clone ()
	{
		return new SymbolMatcher (parser, id);
	}
	
	protected boolean match (Symbol base, String target, int offset, Matcher ending)
	{
		if (offset <= parser.dfs_helper.get (id))
			return false;
		
		int previous = parser.dfs_helper.get (id);
		parser.dfs_helper.put (id, offset);
		
//		System.out.println (offset+"\tReached the start of Symbol: "+id);
//		System.out.println ("\t* -> "+zeroOrMore+"\t\t? -> "+zeroOrOne);
		Symbol result = new Symbol (id, target);
		result.setBegin (offset);
		base.addSymbol (result);
		
		SymbolEndMatcher endmatcher = new SymbolEndMatcher (this, base, ending);
		
		if (parser.getSymbol (id).match (result, target, offset, endmatcher))
		{
			parser.dfs_helper.put (id, previous);
			return true;
		}
		else
		{
			parser.dfs_helper.put (id, previous);
			base.removeSymbol (result);
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
		System.out.print (id+" ");
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
