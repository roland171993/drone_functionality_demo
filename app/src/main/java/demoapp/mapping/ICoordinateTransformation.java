package demoapp.mapping;

/**
	 Describes a coordinate transformation. This interface only describes a 
	 coordinate transformation, it does not actually perform the transform 
	 operation on points. To transform points you must use a math transform.
	*/
	public interface ICoordinateTransformation
	{
		/** 
		 Human readable description of domain in source coordinate system.
		*/

		/** 
		 Authority which defined transformation and parameter values.
		 
		 
		 An Authority is an organization that maintains definitions of Authority Codes. For example the European Petroleum Survey Group (EPSG) maintains a database of coordinate systems, and other spatial referencing objects, where each object has a code number ID. For example, the EPSG code for a WGS84 Lat/Lon coordinate system is �4326�
		 
		*/
		String getAuthority();

		/** 
		 Code used by authority to identify transformation. An empty string is used for no code.
		 
		 The AuthorityCode is a compact string defined by an Authority to reference a particular spatial reference object. For example, the European Survey Group (EPSG) authority uses 32 bit integers to reference coordinate systems, so all their code strings will consist of a few digits. The EPSG code for WGS84 Lat/Lon is �4326�.
		*/
		long getAuthorityCode();

		/** 
		 Gets math transform.
		*/
		IMathTransform getMathTransform();

		/** 
		 Name of transformation.
		*/
		String getName();

		/** 
		 Gets the provider-supplied remarks.
		*/
		String getRemarks();

		/** 
		 Source coordinate system.
		*/
		ICoordinateSystem getSourceCS();

		/** 
		 Target coordinate system.
		*/
		ICoordinateSystem getTargetCS();

		/** 
		 Semantic type of transform. For example, a datum transformation or a coordinate conversion.
		*/
		TransformType getTransformType();
	}