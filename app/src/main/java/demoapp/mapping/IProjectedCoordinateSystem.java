package demoapp.mapping;

/**
	 The IProjectedCoordinateSystem interface defines the standard information stored with
	 projected coordinate system objects. A projected coordinate system is defined using a
	 geographic coordinate system object and a projection object that defines the
	 coordinate transformation from the geographic coordinate system to the projected
	 coordinate systems. The instances of a single ProjectedCoordinateSystem COM class can
	 be used to model different projected coordinate systems (e.g., UTM Zone 10, Albers)
	 by associating the ProjectedCoordinateSystem instances with Projection instances
	 belonging to different Projection COM classes (Transverse Mercator and Albers,
	 respectively).
	*/
	public interface IProjectedCoordinateSystem extends IHorizontalCoordinateSystem
	{
		/** 
		 Gets or sets the geographic coordinate system associated with the projected
		 coordinate system.
		/** 
		 Gets or sets the linear (projected) units of the projected coordinate system.
		*/

		IGeographicCoordinateSystem getGeographicCoordinateSystem();
		void setGeographicCoordinateSystem(IGeographicCoordinateSystem value);

		ILinearUnit getLinearUnit();
		void setLinearUnit(ILinearUnit value);

		/** 
		 Gets or sets the projection for the projected coordinate system.
		*/
		IProjection getProjection();
		void setProjection(IProjection value);
	}