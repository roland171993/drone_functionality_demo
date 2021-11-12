package demoapp.mapping;


/**
	 The IProjection interface defines the standard information stored with projection
	 objects. A projection object implements a coordinate transformation from a geographic
	 coordinate system to a projected coordinate system, given the ellipsoid for the
	 geographic coordinate system. It is expected that each coordinate transformation of
	 interest, e.g., Transverse Mercator, Lambert, will be implemented as a COM class of
	 coType Projection, supporting the IProjection interface.
	*/
	public interface IProjection extends IInfo
	{
		/** 
		 Gets number of parameters of the projection.
		*/

		int getNumParameters();
		/** 
		 Gets the projection classification name (e.g. 'Transverse_Mercator').
		*/
		String getClassName();

		/** 
		 Gets an indexed parameter of the projection.
		 
		 @param index Index of parameter
		 @return n'th parameter
		*/
		ProjectionParameter GetParameter(int index);

		/** 
		 Gets an named parameter of the projection.
		 
		 The parameter name is case insensitive
		 @param name Name of parameter
		 @return parameter or null if not found
		*/
		ProjectionParameter GetParameter(String name);
	}