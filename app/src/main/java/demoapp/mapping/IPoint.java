package demoapp.mapping;


public interface IPoint extends IGeometry, IPuntal
{

	double getY();
	void setY(double value);

	double getZ();
	void setZ(double value);

	double getM();
	void setM(double value);

	ICoordinateSequence getCoordinateSequence();
}