/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

package waldweg.bnf;

class TargetEndMatcher extends Matcher
{
	protected Matcher clone ()
	{
		return new TargetEndMatcher ();
	}
	
	protected boolean match (Symbol base, String target, int offset, Matcher ending)
	{
//		System.out.println (offset+"\tReached the end of pattern");
		return offset == target.length ();
	}
	
	protected void print ()
	{
	}
}
