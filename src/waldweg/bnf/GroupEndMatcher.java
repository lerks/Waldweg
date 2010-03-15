/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

package waldweg.bnf;

class GroupEndMatcher extends Matcher
{
	private GroupMatcher begin;
	
	private Matcher ending;
	
	GroupEndMatcher (GroupMatcher begin, Matcher ending)
	{
		this.begin = begin;
		this.ending = ending;
	}
	
	protected Matcher clone ()
	{
		return new GroupEndMatcher (begin, ending);
	}
	
	protected boolean match (Symbol base, String target, int offset, Matcher ending)
	{
//		System.out.println (offset+"\tReached the end of a group");
		if (begin.zeroOrMore && begin.match (base, target, offset, this.ending))
			return true;
		if (begin.next != null)
		{
			if (begin.next.match (base, target, offset, this.ending))
				return true;
		}
		else
		{
			if (this.ending.match (base, target, offset, this.ending))
				return true;
		}
		if (begin.alternative != null && begin.alternative.match (base, target, offset, this.ending))
			return true;
		return false;
	}
	
	protected void print ()
	{
	}
}
