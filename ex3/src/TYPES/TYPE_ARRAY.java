package TYPES;

public class TYPE_ARRAY extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE innerType;

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_ARRAY(String name, TYPE innerType)
	{
		this.name = name;
		this.innerType = innerType;		
	}
	public boolean isArray(){ return true;}
	
}
