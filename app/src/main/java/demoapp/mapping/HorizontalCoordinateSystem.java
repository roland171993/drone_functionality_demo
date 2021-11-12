package demoapp.mapping;


import java.util.concurrent.CopyOnWriteArrayList;

/**
 A 2D coordinate system suitable for positions on the Earth's surface.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public abstract class HorizontalCoordinateSystem extends CoordinateSystem implements IHorizontalCoordinateSystem
{
	/** 
	 Creates an instance of HorizontalCoordinateSystem
	 
	 @param datum Horizontal datum
	 @param axisInfo Axis information
	 @param name Name
	 @param authority Authority name
	 @param code Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	*/
	public HorizontalCoordinateSystem(IHorizontalDatum datum, CopyOnWriteArrayList<AxisInfo> axisInfo, String name, String authority, long code, String alias, String remarks, String abbreviation)
	{
		super(name, authority, code, alias, abbreviation, remarks);
		_HorizontalDatum = datum;
		if (axisInfo.size() != 2)
		{
			throw new IllegalArgumentException("Axis info should contain two axes for horizontal coordinate systems");
		}
		super.setAxisInfo(axisInfo);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IHorizontalCoordinateSystem Members

	private IHorizontalDatum _HorizontalDatum;

	/** 
	 Gets or sets the HorizontalDatum.
	*/
	public final IHorizontalDatum getHorizontalDatum()
	{
		return _HorizontalDatum;
	}
	public final void setHorizontalDatum(IHorizontalDatum value)
	{
		_HorizontalDatum = value;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}