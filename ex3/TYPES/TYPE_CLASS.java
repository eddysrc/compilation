package TYPES;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_LIST data_members;
	
	/****************/
	/* CTOR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
	}

	public TYPE findFieldInClass(String name)
	{
		TYPE member = null;
		TYPE_CLASS_VAR_DEC field = null;
		TYPE_LIST dataMembers = data_members;

		while (dataMembers != null) {
				member = dataMembers.head;

				if (member instanceof TYPE_CLASS_VAR_DEC)
				{
					field = (TYPE_CLASS_VAR_DEC) member;

					if (field.name.equals(name))
					{
						return field.t;
					}
				}

				dataMembers = dataMembers.next;
		}

		if (father == null)
		{
			return null;
		}

		return father.findFieldInClass(name);
	}
}
