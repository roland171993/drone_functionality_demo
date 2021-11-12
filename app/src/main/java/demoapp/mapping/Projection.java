package demoapp.mapping;


import java.util.concurrent.CopyOnWriteArrayList;

/**
 The Projection class defines the standard information stored with a projection
 objects. A projection object implements a coordinate transformation from a geographic
 coordinate system to a projected coordinate system, given the ellipsoid for the
 geographic coordinate system. It is expected that each coordinate transformation of
 interest, e.g., Transverse Mercator, Lambert, will be implemented as a class of
 type Projection, supporting the IProjection interface.
*/
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
//#if HAS_SYSTEM_SERIALIZABLEATTRIBUTE

//#endif
public class Projection extends Info implements IProjection
{
	public Projection(String className, CopyOnWriteArrayList<ProjectionParameter> parameters, String name, String authority, long code, String alias, String remarks, String abbreviation)
	{
		super(name, authority, code, alias, abbreviation, remarks);
		_parameters = parameters;
		_ClassName = className;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region Predefined projections
//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#region IProjection Members

	/** 
	 Gets the number of parameters of the projection.
	*/
	public final int getNumParameters()
	{
		return _parameters.size();
	}

	private CopyOnWriteArrayList<ProjectionParameter> _parameters;

	/** 
	 Gets or sets the parameters of the projection
	*/
	public final CopyOnWriteArrayList<ProjectionParameter> getParameters()
	{
		return _parameters;
	}
	public final void setParameters(CopyOnWriteArrayList<ProjectionParameter> value)
	{
		_parameters = value;
	}

	/** 
	 Gets an indexed parameter of the projection.
	 
	 @param index Index of parameter
	 @return n'th parameter
	*/
	public final ProjectionParameter GetParameter(int index)
	{
		return _parameters.get(index);
	}

	/** 
	 Gets an named parameter of the projection.
	 
	 The parameter name is case insensitive
	 @param name Name of parameter
	 @return parameter or null if not found
	*/
	public final ProjectionParameter GetParameter(String name)
	{
		for (ProjectionParameter par : _parameters)
		{
			if (par.getName().equalsIgnoreCase(name))
			{
				return par;
			}
		}
		return null;
	}

	private String _ClassName;

	/** 
	 Gets the projection classification name (e.g. "Transverse_Mercator").
	*/
	public final String getClassName()
	{
		return _ClassName;
	}

	/** 
	 Returns the Well-known text for this object
	 as defined in the simple features specification.
	*/
	@Override
	public String getWKT()
	{
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("PROJECTION[\"%1$s\"", getName()));
		if (!StringHelper.isNullOrEmpty(getAuthority()) && getAuthorityCode() > 0)
		{
			sb.append(String.format(", AUTHORITY[\"%1$s\", \"%2$s\"]", getAuthority(), getAuthorityCode()));
		}
		sb.append("]");
		return sb.toString();
	}

	/** 
	 Gets an XML representation of this object
	*/
	@Override
	public String getXML()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("<CS_Projection Classname=\"{0}\">{1}");
		sb.append(getClassName());
		sb.append(getInfoXml());
		for (ProjectionParameter param : getParameters())
		{
			sb.append(param.getXML());
		}
		sb.append("</CS_Projection>");
		return sb.toString();
	}

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
		if (!(obj instanceof Projection))
		{
			return false;
		}
		Projection proj = obj instanceof Projection ? (Projection)obj : null;
		if (proj.getNumParameters() != this.getNumParameters())
		{
			return false;
		}
		for (int i = 0; i < _parameters.size(); i++)
		{
			ProjectionParameter param = GetParameter(proj.GetParameter(i).getName());
			if (param == null)
			{
				return false;
			}
			if (param.getValue() != proj.GetParameter(i).getValue())
			{
				return false;
			}
		}
		return true;
	}

//C# TO JAVA CONVERTER TODO TASK: There is no preprocessor in Java:
		///#endregion
}