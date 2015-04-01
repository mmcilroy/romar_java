package tools;

import romar.Romar;
import romar.RomarUser;

public class DataDump
{
	public static void main( String[] args ) throws Exception
	{
		Romar.load();
		for( String user : Romar.availableUsers )
		{
			RomarUser ruser = Romar.loadUser( user );
			System.out.println( ruser.id + " - " + ruser.pass );
		}
	}
}
