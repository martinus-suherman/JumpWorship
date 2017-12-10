package JumpWorship;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Martinus on 7/8/2016.
 */
class DataAccess
{
	private static Connection
		fldConnection;

	public Connection getConnection()
	{
		return fldConnection;
	}

	public void setConnection( Connection connection )
	{
		fldConnection = connection;
	}

	PreparedStatement prepareStatement( String sql ) throws SQLException
	{
		return fldConnection.prepareStatement( sql );
	}
}
