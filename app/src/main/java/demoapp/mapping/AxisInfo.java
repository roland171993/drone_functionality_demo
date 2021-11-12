package demoapp.mapping;

import java.util.Locale;


/** 
 Details of axis. This is used to label axes, and indicate the orientation.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class AxisInfo
{
	/** 
	 Initializes a new instance of an AxisInfo.
	 
	 @param name Name of axis
	 @param orientation Axis orientation
	*/
	public AxisInfo(String name, AxisOrientationEnum orientation)
	{
		_Name = name;
		_Orientation = orientation;
	}

	private String _Name;

	/**
	 Human readable name for axis. Possible values are X, Y, Long, Lat or any other short string.
	*/
	public final String getName()
	{
		return _Name;
	}
	public final void setName(String value)
	{
		_Name = value;
	}

	private AxisOrientationEnum _Orientation;

	/** 
	 Gets enumerated value for orientation.
	*/
	public final AxisOrientationEnum getOrientation()
	{
		return _Orientation;
	}
	public final void setOrientation(AxisOrientationEnum value)
	{
		_Orientation = value;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	public final String getWKT()
	{
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_STRING_TOUPPERINVARIANT
		return String.format("AXIS[\"%1$s\", %2$s]", getName(), getOrientation().toString().toUpperCase(Locale.ROOT));
////#elif HAS_SYSTEM_STRING_TOUPPER_CULTUREINFO
//		return String.format("AXIS[\"%1$s\", %2$s]", getName(), getOrientation().toString().toUpperCase(Locale.ROOT));
////#else
//		return "Must have at least one or the other.";
////#endif
	}

	/** 
	 Gets an XML representation of this object
	*/
	public final String getXML()
	{
		return String.format("<CS_AxisInfo Name=\"%1$s\" Orientation=\"%2$s\"/>", getName(), getOrientation().toString());
////C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
////#if HAS_SYSTEM_STRING_TOUPPERINVARIANT
//			.ToUpperInvariant());
////#elif HAS_SYSTEM_STRING_TOUPPER_CULTUREINFO
//			.ToUpper(CultureInfo.InvariantCulture));
////#else
//		return "Must have at least one or the other";
////#endif
	}
}