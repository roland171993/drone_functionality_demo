package demoapp.mapping;

/**
	 Procedure used to measure positions on the surface of the Earth.
	*/
	public interface IHorizontalDatum extends IDatum
	{
		/** 
		 Gets or sets the ellipsoid of the datum.
		*/
		IEllipsoid getEllipsoid();
		void setEllipsoid(IEllipsoid value);
		/**
		 Gets preferred parameters for a Bursa Wolf transformation into WGS84. The 7 returned values 
		 correspond to (dx,dy,dz) in meters, (ex,ey,ez) in arc-seconds, and scaling in parts-per-million.
		*/


		/// <summary>
		/// Gets preferred parameters for a Bursa Wolf transformation into WGS84. The 7 returned values
		/// correspond to (dx,dy,dz) in meters, (ex,ey,ez) in arc-seconds, and scaling in parts-per-million.
		/// </summary>
		Wgs84ConversionInfo getWgs84Parameters();
		void setWgs84Parameters(Wgs84ConversionInfo value);
	}