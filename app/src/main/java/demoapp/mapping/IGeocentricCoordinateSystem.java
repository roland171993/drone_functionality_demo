package demoapp.mapping;


/**
	 A 3D coordinate system, with its origin at the center of the Earth.
	*/
	public interface IGeocentricCoordinateSystem extends ICoordinateSystem
	{
		/** 
		 Returns the HorizontalDatum. The horizontal datum is used to determine where
		 the centre of the Earth is considered to be. All coordinate points will be 
		 measured from the centre of the Earth, and not the surface.
		*/

		/** 
		 Gets the units used along all the axes.
		*/
		ILinearUnit getLinearUnit();
		void setLinearUnit(ILinearUnit value);

		/** 
		 Returns the PrimeMeridian.
		*/
		IPrimeMeridian getPrimeMeridian();
		void setPrimeMeridian(IPrimeMeridian value);

		IHorizontalDatum getHorizontalDatum();
		void setHorizontalDatum(IHorizontalDatum value);
	}