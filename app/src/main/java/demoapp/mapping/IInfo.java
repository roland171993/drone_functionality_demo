package demoapp.mapping;

/**
	 The ISpatialReferenceInfo interface defines the standard 
	 information stored with spatial reference objects. This
	 interface is reused for many of the spatial reference
	 objects in the system.
	*/
	public interface IInfo
	{
		/** 
		 Gets or sets the name of the object.
		*/
		String getName();
		void setName(String value);

		/** 
		 Gets or sets the authority name for this object, e.g., �POSC�,
		 is this is a standard object with an authority specific
		 identity code. Returns �CUSTOM� if this is a custom object.
		*/
		String getAuthority();

		/** 
		 Gets or sets the authority specific identification code of the object
		*/
		long getAuthorityCode();

		/** 
		 Gets or sets the alias of the object.
		*/
		String getAlias();

		/** 
		 Gets or sets the abbreviation of the object.
		*/
		String getAbbreviation();

		/** 
		 Gets or sets the provider-supplied remarks for the object.
		*/
		String getRemarks();

		/** 
		 Returns the Well-known text for this spatial reference object
		 as defined in the simple features specification.
		*/
		String getWKT();

		/** 
		 Gets an XML representation of this object.
		*/
		String getXML();

		/** 
		 Checks whether the values of this instance is equal to the values of another instance.
		 Only parameters used for coordinate system are used for comparison.
		 Name, abbreviation, authority, alias and remarks are ignored in the comparison.
		 
		 @param obj
		 @return True if equal
		*/
		boolean EqualParams(Object obj);
	}