package demoapp.mapping;


/** 
 A named projection parameter value.
 
 
 The linear units of parameters' values match the linear units of the containing 
 projected coordinate system. The angular units of parameter values match the 
 angular units of the geographic coordinate system that the projected coordinate 
 system is based on. (Notice that this is different from <see cref="Parameter"/>,
 where the units are always meters and degrees.)
 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class ProjectionParameter
{
	/** 
	 Initializes an instance of a ProjectionParameter
	 
	 @param name Name of parameter
	 @param value Parameter value
	*/
	public ProjectionParameter(String name, double value)
	{
		_Name = name;
		_Value = value;
	}


	private String _Name;

	/** 
	 Parameter name.
	*/
	public final String getName()
	{
		return _Name;
	}
	public final void setName(String value)
	{
		_Name = value;
	}

	private double _Value;

	/** 
	 Parameter value.
	 The linear units of a parameters' values match the linear units of the containing 
	 projected coordinate system. The angular units of parameter values match the 
	 angular units of the geographic coordinate system that the projected coordinate 
	 system is based on.
	*/
	public final double getValue()
	{
		return _Value;
	}
	public final void setValue(double value)
	{
		_Value = value;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	public final String getWKT()
	{
		return String.format( "PARAMETER[\"%1$s\", %2$s]", getName(), getValue());
	}

	/** 
	 Gets an XML representation of this object
	*/
	public final String getXML()
	{
		return String.format("<CS_ProjectionParameter Name=\"%1$s\" Value=\"%2$s\"/>", getName(), getValue());
	}

	/** 
	 Function to get a textual representation of this envelope
	 
	 @return A textual representation of this envelope
	*/
	@Override
	public String toString()
	{
		return String.format("ProjectionParameter '%1$s': %2$s", getName(), getValue());
	}
}