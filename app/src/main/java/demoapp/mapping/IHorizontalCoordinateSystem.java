package demoapp.mapping;

/**
	 A 2D coordinate system suitable for positions on the Earth's surface.
	*/
	public interface IHorizontalCoordinateSystem extends ICoordinateSystem
	{
		/** 
		 Returns the HorizontalDatum.
		*/
		IHorizontalDatum getHorizontalDatum();
		void setHorizontalDatum(IHorizontalDatum value);
	}