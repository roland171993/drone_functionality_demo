package demoapp.mapping;


/**
 A set of quantities from which other quantities are calculated.
 
 
 For the OGC abstract model, it can be defined as a set of real points on the earth 
 that have coordinates. EG. A datum can be thought of as a set of parameters 
 defining completely the origin and orientation of a coordinate system with respect 
 to the earth. A textual description and/or a set of parameters describing the 
 relationship of a coordinate system to some predefined physical locations (such 
 as center of mass) and physical directions (such as axis of spin). The definition 
 of the datum may also include the temporal behavior (such as the rate of change of
 the orientation of the coordinate axes).
 
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public abstract class Datum extends Info implements IDatum
{
	/** 
	 Initializes a new instance of a Datum object
	 
	 @param type Datum type
	 @param name Name
	 @param authority Authority name
	 @param code Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	public Datum(DatumType type, String name, String authority, long code, String alias, String remarks, String abbreviation)
	{
		super(name, authority, code, alias, abbreviation, remarks);
		_DatumType = type;
	}
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IDatum Members

	private DatumType _DatumType;

	/** 
	 Gets or sets the type of the datum as an enumerated code.
	*/
	public final DatumType getDatumType()
	{
		return _DatumType;
	}
	public final void setDatumType(DatumType value)
	{
		_DatumType = value;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

	/** 
	 Checks whether the values of this instance is equal to the values of another instance.
	 Only parameters used for coordinate system are used for comparison.
	 Name, abbreviation, authority, alias and remarks are ignored in the comparison.
	 
	 @param obj
	 @return True if equal
	*/
	@Override
	public boolean EqualParams(Object obj)
	{
		if (!(obj instanceof Ellipsoid))
		{
			return false;
		}
		return (obj instanceof Datum ? (Datum)obj : null).getDatumType() == this.getDatumType();
	}
}