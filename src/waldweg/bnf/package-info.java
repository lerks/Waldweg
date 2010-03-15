/**
 * This package includes a parser for the Backus-Naur Form (BNF for short).
 * It supports basic validation of BNF syntax, and full validation of strings using the given syntax rules.
 * This works using a {@link waldweg.bnf.Parser}, which collects the syntax rules and which starts the parsing of a string.
 * Every syntax rule is represented by a {@link waldweg.bnf.Symbol} object, so parsing a string return a {@link waldweg.bnf.Symbol} object representing the given string, of the type of the given syntax rule. From this object it's possible to obtain other {@link waldweg.bnf.Symbol}s, representing the rules which compose the rule represented by the object.
 * For a detailed description and some examples see the documentation of {@link waldweg.bnf.Parser}.
 *
 * @author Luca Wehrstedt
 */
package waldweg.bnf;
