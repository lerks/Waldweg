/*  
 *  Copyright (C) 2010  Luca Wehrstedt
 *
 *  This file is released under the GPLv2
 *  Read the file 'COPYING' for more information
 */

package waldweg.bnf;

import java.util.HashMap;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * A Backus-Naur Form syntax rule mapped to a string.
 * Symbol objects can be obtained only using {@link waldweg.bnf.Parser#parse(String,String)}. The one returned maps the entire string and is of the type given as parameter ("type" means the name of the syntax rule).
 * If the syntax rule contains one or more other non-terminal symbols these ones can be accessed using methods of this class.
 * Terminal symbols cannot be obtained, since they doesn't depend on the string given but are always present in the symbol.
 * SInce the number of child symbols of a particular type may vary because of quantifiers, it's also possible to obtain this number.
 */
public class Symbol
{
	private String name;
	
	private String target;
	
	private int begin = 0;
	
	private int end = 0;
	
	private Map<String,List<Symbol>> children = new HashMap<String,List<Symbol>> ();
	
	Symbol (String name, String target)
	{
		this.name = name;
		this.target = target;
	}
	
	void setBegin (int offset)
	{
		begin = offset;
	}
	
	void setEnd (int offset)
	{
		end = offset;
	}
	
	/**
	 * Get the name of the sytax rule represented by the Symbol.
	 * @return The name of the syntax rule.
	 */
	public String getType ()
	{
		return name;
	}
	
	/**
	 * Get the String mapped by the Symbol.
	 * @return The mapped String.
	 */
	public String getString ()
	{
//		System.out.println (begin+" -> "+end+" : "+target);
		if (begin > end)
			return target.substring (begin);
		else
			return target.substring (begin, end);
	}
	
	void addSymbol (Symbol value)
	{
		if (!children.containsKey (value.getType()))
		{
			children.put (value.getType(), new ArrayList<Symbol> ());
		}
		children.get (value.getType()).add (value);
	}
	
	void removeSymbol (Symbol value)
	{
		if (children.containsKey (value.getType()))
		{
			children.get (value.getType()).remove (value);
		}
	}

	/**
	 * Get the index-th child Symbol of type id.
	 * @param id The type of the Symbol.
	 * @param index The index of the Symbol.
	 * @return The specified Symbol.
	 */
	public Symbol getSymbol (String id, int index)
	{
		if (children.containsKey (id))
		{
			try
			{
				return children.get (id).get (index);
			}
			catch (IndexOutOfBoundsException e)
			{
				return null;
			}
		}
		else
			return null;
	}
	
	/**
	 * Get the number of child Symbols of the specified type.
	 * @param id The type of the Symbol.
	 * @return The number of child Symbols of that type .
	 */
	public int getSymbolCount (String id)
	{
		if (children.containsKey (id))
			return children.get (id).size ();
		else
			return 0;
	}
}
