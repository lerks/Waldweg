/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

package waldweg.bnf;

class GroupMatcher extends Matcher
{
	private Matcher head = null;
	private Matcher tail = null;
	
	GroupMatcher (Parser parser)
	{
		this.parser = parser;
	}
	
	void add (Matcher element)
	{
		if (head == null)
		{
			head = tail = element;
		}
		else
		{
			tail.next = element;
			tail = element;
		}
	}
	
	Matcher getHead ()
	{
		return head;
	}
	
	Matcher getTail ()
	{
		return tail;
	}
	
	protected Matcher clone ()
	{
		GroupMatcher result = new GroupMatcher (parser);
		if (head != null)
		{
			result.add (head.clone ());
		}
		return result;
	}
	
	protected void clean ()
	{
		super.clean ();
		head.clean ();
	}
	
	protected boolean match (Symbol base, String target, int offset, Matcher ending)
	{
//		System.out.println (offset+"\tReached the start of a group");
		GroupEndMatcher endmatcher = new GroupEndMatcher (this, ending);
				
		if (head.match (base, target, offset, endmatcher))
		{
			return true;
		}
		else
		{
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
		System.out.print ("( ");
		head.print ();
		System.out.print (") ");
		if (zeroOrOne)
			System.out.print ("? ");
		if (zeroOrMore)
			System.out.print ("* ");
		if (oneOrMore)
			System.out.print ("+ ");
		if (next != null)
			next.print ();
	}
}
