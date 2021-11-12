package demoapp.mapping;


/**
	 The IGeographicCoordinateSystem interface is a subclass of IGeodeticSpatialReference and
	 defines the standard information stored with geographic coordinate system objects.
	*/
	public interface IGeographicCoordinateSystem extends IHorizontalCoordinateSystem
	{
		/** 
		 Gets or sets the angular units of the geographic coordinate system.
		*/
		IAngularUnit getAngularUnit();

		/** 
		 Gets or sets the prime meridian of the geographic coordinate system.
		*/
		IPrimeMeridian getPrimeMeridian();
		void setPrimeMeridian(IPrimeMeridian value);

		/** 
		 Gets the number of available conversions to WGS84 coordinates.
		*/
		int getNumConversionToWGS84();

		/** 
		 Gets details on a conversion to WGS84.
		*/
		Wgs84ConversionInfo GetWgs84ConversionInfo(int index);
	}