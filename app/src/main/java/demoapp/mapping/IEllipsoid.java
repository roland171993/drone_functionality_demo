package demoapp.mapping;

/**
	 The IEllipsoid interface defines the standard information stored with ellipsoid objects.
	*/
	public interface IEllipsoid extends IInfo
	{
		/** 
		 Gets or sets the value of the semi-major axis.
		*/
		double getSemiMajorAxis();
		void setSemiMajorAxis(double value);
		/** 
		 Gets or sets the value of the semi-minor axis.
		*/
		double getSemiMinorAxis();
		void setSemiMinorAxis(double value);

		/** 
		 Gets or sets the value of the inverse of the flattening constant of the ellipsoid.
		*/
		double getInverseFlattening();
		void setInverseFlattening(double value);

		/** 
		 Gets or sets the value of the axis unit.
		*/
		ILinearUnit getAxisUnit();
		void setAxisUnit(ILinearUnit value);

		/** 
		 Is the Inverse Flattening definitive for this ellipsoid? Some ellipsoids use the
		 IVF as the defining value, and calculate the polar radius whenever asked. Other
		 ellipsoids use the polar radius to calculate the IVF whenever asked. This
		 distinction can be important to avoid floating-point rounding errors.
		*/
		boolean getIsIvfDefinitive();
		void setIsIvfDefinitive(boolean value);
	}