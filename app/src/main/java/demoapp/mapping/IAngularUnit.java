package demoapp.mapping;

/**
	 The IAngularUnit interface defines methods on angular units.
	*/
	public interface IAngularUnit extends IUnit
	{
		/** 
		 Gets or sets the number of radians per angular unit.
		*/
		double getRadiansPerUnit();
		void setRadiansPerUnit(double value);
	}