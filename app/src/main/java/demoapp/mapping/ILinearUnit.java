package demoapp.mapping;

/**
	 The ILinearUnit interface defines methods on linear units.
	*/
	public interface ILinearUnit extends IUnit
	{
		/** 
		 Gets or sets the number of meters per <see cref="ILinearUnit"/>.
		*/
		double getMetersPerUnit();
		void setMetersPerUnit(double value);
	}