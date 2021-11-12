package demoapp.mapping;


/**
 The Info object defines the standard information
 stored with spatial reference objects
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public abstract class Info implements IInfo
{
	/** 
	 A base interface for metadata applicable to coordinate system objects.
	 
	 
	 <p>The metadata items 'Abbreviation', 'Alias', 'Authority', 'AuthorityCode', 'Name' and 'Remarks' 
	 were specified in the Simple Features interfaces, so they have been kept here.</p>
	 <p>This specification does not dictate what the contents of these items 
	 should be. However, the following guidelines are suggested:</p>
	 <p>When <see cref="ICoordinateSystemAuthorityFactory"/> is used to create an object, the 'Authority'
	 and 'AuthorityCode' values should be set to the authority name of the factory object, and the authority 
	 code supplied by the client, respectively. The other values may or may not be set. (If the authority is 
	 EPSG, the implementer may consider using the corresponding metadata values in the EPSG tables.)</p>
	 <p>When <see cref="CoordinateSystemFactory"/> creates an object, the 'Name' should be set to the value
	 supplied by the client. All of the other metadata items should be left empty</p>
	 
	 @param name Name
	 @param authority Authority name
	 @param code Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	public Info(String name, String authority, long code, String alias, String abbreviation, String remarks)
	{
		_Name = name;
		_Authority = authority;
		_Code = code;
		_Alias = alias;
		_Abbreviation = abbreviation;
		_Remarks = remarks;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region ISpatialReferenceInfo Members

	private String _Name;

	/** 
	 Gets or sets the name of the object.
	*/
	public final String getName()
	{
		return _Name;
	}
	public final void setName(String value)
	{
		_Name = value;
	}

	private String _Authority;

	/** 
	 Gets or sets the authority name for this object, e.g., "EPSG",
	 is this is a standard object with an authority specific
	 identity code. Returns "CUSTOM" if this is a custom object.
	*/
	public final String getAuthority()
	{
		return _Authority;
	}
	public final void setAuthority(String value)
	{
		_Authority = value;
	}

	private long _Code;

	/** 
	 Gets or sets the authority specific identification code of the object
	*/
	public final long getAuthorityCode()
	{
		return _Code;
	}
	public final void setAuthorityCode(long value)
	{
		_Code = value;
	}

	private String _Alias;

	/** 
	 Gets or sets the alias of the object.
	*/
	public final String getAlias()
	{
		return _Alias;
	}
	public final void setAlias(String value)
	{
		_Alias = value;
	}

	private String _Abbreviation;

	/** 
	 Gets or sets the abbreviation of the object.
	*/
	public final String getAbbreviation()
	{
		return _Abbreviation;
	}
	public final void setAbbreviation(String value)
	{
		_Abbreviation = value;
	}

	private String _Remarks;

	/** 
	 Gets or sets the provider-supplied remarks for the object.
	*/
	public final String getRemarks()
	{
		return _Remarks;
	}
	public final void setRemarks(String value)
	{
		_Remarks = value;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String toString()
	{
		return getWKT();
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	public abstract String getWKT();

	/** 
	 Gets an XML representation of this object.
	*/
	public abstract String getXML();

	/** 
	 Returns an XML string of the info object
	*/
	public final String getInfoXml()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<CS_Info");
		if (getAuthorityCode() > 0)
		{
			sb.append(String.format(" AuthorityCode=\"%1$s\"",getAuthorityCode()));
		}
		if (!StringHelper.isNullOrEmpty(getAbbreviation()))
		{
			sb.append(String.format(" Abbreviation=\"%1$s\"", getAbbreviation()));
		}
		if (!StringHelper.isNullOrEmpty(getAuthority()))
		{
			sb.append(String.format(" Authority=\"%1$s\"", getAuthority()));
		}
		if (!StringHelper.isNullOrEmpty(getName()))
		{
			sb.append(String.format(" Name=\"%1$s\"", getName()));
		}
		sb.append("/>");
		return sb.toString();
	}

	/** 
	 Checks whether the values of this instance is equal to the values of another instance.
	 Only parameters used for coordinate system are used for comparison.
	 Name, abbreviation, authority, alias and remarks are ignored in the comparison.
	 
	 @param obj
	 @return True if equal
	*/
	public abstract boolean EqualParams(Object obj);

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}