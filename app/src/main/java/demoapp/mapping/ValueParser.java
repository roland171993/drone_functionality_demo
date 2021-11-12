package demoapp.mapping;


// Ref: http://www.yortondotnet.com/2009/11/tryparse-for-compact-framework.html

/**
 Provides methods to parse simple value types without throwing format exception.
*/
public final class ValueParser
{

//C# TO JAVA CONVERTER TODO TASK: Java annotations will not correspond to .NET attributes:
//ORIGINAL LINE: [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Naming", "CA1704:IdentifiersShouldBeSpelledCorrectly", MessageId = "s")] public static bool TryParse(string s, System.Globalization.NumberStyles style, IFormatProvider provider, out double result)
	public static boolean TryParse(String s, OutObject<Double> result)
	{
		boolean retVal = false;
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		try
		{
			result.argValue = Double.parseDouble(s);
			retVal = true;
		}
		catch (NumberFormatException e)
		{
			result.argValue = 0d;
		}
		catch (ClassCastException e2)
		{
			result.argValue = 0d;
		}
//#endif
		return retVal;
	}
}