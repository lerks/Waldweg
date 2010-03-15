import java.io.StreamTokenizer;
import java.io.StringReader;
import java.io.IOException;

import java.util.StringTokenizer;

import waldweg.bnf.*;

public class Test
{
	public static void main (String[] args)
	{
		// Do something nice
		Parser p = new Parser ();
		
		/*
		p.addSymbol ("digit", "'0' | '1' | '2'| '3' | '4' | '5' | '6' | '7' | '8' | '9'");
		p.addSymbol ("sign", "'+' | '-'");
		p.addSymbol ("number", "sign? digit+");
		Symbol s = p.parse ("number", "59797656596");
		//*/
		
		/*
		p.addSymbol ("foo", "\'ciao\'?");
		Symbol s = p.parse ("foo", "");
		//*/
		
		//*
		p.addSymbol ("transform-list", "wsp* transforms? wsp*");
		p.addSymbol ("transforms", "transform | transform comma-wsp+ transforms");
		p.addSymbol ("transform", "matrix | translate | scale | rotate | skewX | skewY");
		p.addSymbol ("matrix", "\"matrix\" wsp* \"(\" wsp* number comma-wsp number comma-wsp number comma-wsp number comma-wsp number comma-wsp number wsp* \")\"");
		p.addSymbol ("translate", "\"translate\" wsp* \"(\" wsp* number ( comma-wsp number )? wsp* \")\"");
		p.addSymbol ("scale", "\"scale\" wsp* \"(\" wsp* number ( comma-wsp number )? wsp* \")\"");
		p.addSymbol ("rotate", "\"rotate\" wsp* \"(\" wsp* number ( comma-wsp number comma-wsp number )? wsp* \")\"");
		p.addSymbol ("skewX", "\"skewX\" wsp* \"(\" wsp* number wsp* \")\"");
		p.addSymbol ("skewY", "\"skewY\" wsp* \"(\" wsp* number wsp* \")\"");
		p.addSymbol ("number", "sign? integer-constant | sign? floating-point-constant");
		p.addSymbol ("comma-wsp", "(wsp+ comma? wsp*) | (comma wsp*)");
		p.addSymbol ("comma", "\",\"");
		p.addSymbol ("integer-constant", "digit-sequence");
		p.addSymbol ("floating-point-constant", "fractional-constant exponent? | digit-sequence exponent");
		p.addSymbol ("fractional-constant", "digit-sequence? \".\" digit-sequence | digit-sequence \".\"");
		p.addSymbol ("exponent", "( \"e\" | \"E\" ) sign? digit-sequence");
		p.addSymbol ("sign", "\"+\" | \"-\"");
		p.addSymbol ("digit-sequence", "digit | digit digit-sequence");
		p.addSymbol ("digit", "\"0\" | \"1\" | \"2\" | \"3\" | \"4\" | \"5\" | \"6\" | \"7\" | \"8\" | \"9\"");
		p.addSymbol ("wsp", "(\" \" | \"\t\" | \"\r\" | \"\n\")");
		Symbol s = p.parse ("transform-list", "translate(-10,-20) scale(2) rotate(45) translate(5,10)");
		//*/
		
		/*
		System.out.println (s);
		if (s != null)
		{
			System.out.println (s.getString());
//			System.out.println (s.getSymbol ("sign", 0).getString());
			System.out.println (s.getSymbolCount ("digit"));
			System.out.println (s.getSymbol ("digit", 0).getString());
			System.out.println (s.getSymbol ("digit", 1).getString());
		}
		//*/
		
		while (s.getSymbolCount ("transforms") > 0)
		{
			s = s.getSymbol ("transforms", 0);
			Symbol transform = s.getSymbol ("transform", 0);
			if (transform.getSymbolCount ("matrix") > 0)
			{
				System.out.println ("Matrix");
				transform = transform.getSymbol ("matrix", 0);
				System.out.println (transform.getSymbol ("number", 0).getString ()+" "+
				                    transform.getSymbol ("number", 1).getString ()+" "+
				                    transform.getSymbol ("number", 2).getString ()+" "+
				                    transform.getSymbol ("number", 3).getString ()+" "+
				                    transform.getSymbol ("number", 4).getString ()+" "+
				                    transform.getSymbol ("number", 5).getString ());
			}
			else if (transform.getSymbolCount ("translate") > 0)
			{
				System.out.println ("Translation");
				transform = transform.getSymbol ("translate", 0);
				if (transform.getSymbolCount ("number") == 1)
					System.out.println (transform.getSymbol ("number", 0).getString ());
				else
					System.out.println (transform.getSymbol ("number", 0).getString ()+" "+
				                            transform.getSymbol ("number", 1).getString ());
			}
			else if (transform.getSymbolCount ("scale") > 0)
			{
				System.out.println ("Scale");
				transform = transform.getSymbol ("scale", 0);
				if (transform.getSymbolCount ("number") == 1)
					System.out.println (transform.getSymbol ("number", 0).getString ());
				else
					System.out.println (transform.getSymbol ("number", 0).getString ()+" "+
				                            transform.getSymbol ("number", 1).getString ());
			}
			else if (transform.getSymbolCount ("rotate") > 0)
			{
				System.out.println ("Rotation");
				transform = transform.getSymbol ("rotate", 0);
				if (transform.getSymbolCount ("number") == 1)
					System.out.println (transform.getSymbol ("number", 0).getString ());
				else
					System.out.println (transform.getSymbol ("number", 0).getString ()+" "+
					                    transform.getSymbol ("number", 1).getString ()+" "+
				                            transform.getSymbol ("number", 2).getString ());
			}
			else if (transform.getSymbolCount ("skewX") > 0)
			{
				System.out.println ("Skew X");
				transform = transform.getSymbol ("skewX", 0);
				System.out.println (transform.getSymbol ("number", 0).getString ());
			}
			else if (transform.getSymbolCount ("skewY") > 0)
			{
				System.out.println ("Skew Y");
				transform = transform.getSymbol ("skewY", 0);
				System.out.println (transform.getSymbol ("number", 0).getString ());
			}
		}
		
		System.out.println ();
	}
}
