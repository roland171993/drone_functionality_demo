package demoapp.mapping;

/**
	 The IPrimeMeridian interface defines the standard information stored with prime
	 meridian objects. Any prime meridian object must implement this interface as
	 well as the ISpatialReferenceInfo interface.
	*/
	public interface IPrimeMeridian extends IInfo
	{
		/**
		 Gets or sets the longitude of the prime meridian (relative to the Greenwich prime meridian).
		 */
		double getLongitude();
		void setLongitude(double value);

		/**
		 Gets or sets the AngularUnits.
		 */
		IAngularUnit getAngularUnit();
		void setAngularUnit(IAngularUnit value);
	}