package demoapp.mapping;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 Base interface for all coordinate systems.


 <p>A coordinate system is a mathematical space, where the elements of the space
 are called positions. Each position is described by a list of numbers. The length 
 of the list corresponds to the dimension of the coordinate system. So in a 2D 
 coordinate system each position is described by a list containing 2 numbers.</p>
 <p>However, in a coordinate system, not all lists of numbers correspond to a 
 position - some lists may be outside the domain of the coordinate system. For 
 example, in a 2D Lat/Lon coordinate system, the list (91,91) does not correspond
 to a position.</p>
 <p>Some coordinate systems also have a mapping from the mathematical space into 
 locations in the real world. So in a Lat/Lon coordinate system, the mathematical 
 position (lat, long) corresponds to a location on the surface of the Earth. This 
 mapping from the mathematical space into real-world locations is called a Datum.</p>

 */
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public abstract class CoordinateSystem extends Info implements ICoordinateSystem
{
	/**
	 Initializes a new instance of a coordinate system.

	 @param name Name
	 @param authority Authority name
	 @param authorityCode Authority-specific identification code.
	 @param alias Alias
	 @param abbreviation Abbreviation
	 @param remarks Provider-supplied remarks
	 */
	public CoordinateSystem(String name, String authority, long authorityCode, String alias, String abbreviation, String remarks)
	{
		super(name, authority, authorityCode, alias, abbreviation, remarks);
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#region ICoordinateSystem Members

	/**
	 Dimension of the coordinate system.
	 */
	public final int getDimension()
	{
		return _AxisInfo.size();
	}

	/**
	 Gets the units for the dimension within coordinate system. 
	 Each dimension in the coordinate system has corresponding units.
	 */
	public abstract IUnit GetUnits(int dimension);

	private CopyOnWriteArrayList<AxisInfo> _AxisInfo;
	public final CopyOnWriteArrayList<AxisInfo> getAxisInfo()
	{
		return _AxisInfo;
	}
	public final void setAxisInfo(CopyOnWriteArrayList<AxisInfo> value)
	{
		_AxisInfo = value;
	}


	/**
	 Gets axis details for dimension within coordinate system.

	 @param dimension Dimension
	 @return Axis info
	 */
	public final AxisInfo GetAxis(int dimension)
	{
		if (dimension >= _AxisInfo.size() || dimension < 0)
		{
			throw new IllegalArgumentException("AxisInfo not available for dimension ");
		}
		return _AxisInfo.get(dimension);
	}


	private double[] _DefaultEnvelope;

	/**
	 Gets default envelope of coordinate system.


	 Coordinate systems which are bounded should return the minimum bounding box of their domain. 
	 Unbounded coordinate systems should return a box which is as large as is likely to be used. 
	 For example, a (lon,lat) geographic coordinate system in degrees should return a box from 
	 (-180,-90) to (180,90), and a geocentric coordinate system could return a box from (-r,-r,-r)
	 to (+r,+r,+r) where r is the approximate radius of the Earth.

	 */
	public final double[] getDefaultEnvelope()
	{
		return _DefaultEnvelope;
	}
	public final void setDefaultEnvelope(double[] value)
	{
		_DefaultEnvelope = value;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
	///#endregion
}