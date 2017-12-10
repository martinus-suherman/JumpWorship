package JumpWorship;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Martinus on 10/4/2016.
 */
public class Presentation
{
	private static final String
		fldHeaderInsertQuery =
		"INSERT INTO PresentationHeader " +
			"( PresentationId, Title ) " +
			"VALUES ( ?, ? ) ",
		fldDetailInsertQuery =
			"INSERT INTO PresentationDetail " +
				"( PresentationId, SequenceNumber, ItemId, ItemType ) " +
				"VALUES ( ?, ?, ?, ? ) ",
		fldHeaderDeleteQuery =
			"DELETE FROM PresentationHeader " +
				"WHERE PresentationId = ? ",
		fldDetailDeleteAllQuery =
			"DELETE FROM PresentationDetail " +
				"WHERE PresentationId = ? ",
		fldNewIdQuery =
			"SELECT IFNULL( MAX( PresentationId ), 0 ) + 1 AS NewId " +
				"FROM PresentationHeader";
	private static DataAccess
		fldDataAccess = new DataAccess();
	private final StringProperty
		fldTitle = new SimpleStringProperty( this, "title" );
	private int
		fldId;
	private ObservableList<PresentationItem>
		fldPresentationItemList = FXCollections.observableArrayList();

	public Presentation()
	{
		try
		{
			setId( retrieveNextId() );
			setTitle( "" );
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}

	public Presentation( int id, String title )
	{
		setId( id );
		setTitle( title );
	}

	int getId()
	{
		return fldId;
	}

	private void setId( int id )
	{
		fldId = id;
	}

	public String getTitle()
	{
		return fldTitle.get();
	}

	public void setTitle( String title )
	{
		fldTitle.set( title );
	}

	public StringProperty titleProperty()
	{
		return fldTitle;
	}

	ObservableList<PresentationItem> getPresentationItemList()
	{
		return fldPresentationItemList;
	}

	void saveHeader() throws SQLException
	{
		PreparedStatement
			locStatement = fldDataAccess.prepareStatement( fldHeaderInsertQuery );

		locStatement.setInt( 1, getId() );
		locStatement.setString( 2, getTitle() );
		locStatement.execute();
	}

	void saveDetail() throws SQLException
	{
		for( PresentationItem locItem : fldPresentationItemList )
		{
			saveDetail( locItem );
		}
	}

	void delete() throws SQLException
	{
		PreparedStatement
			locHeaderStatement = fldDataAccess.prepareStatement(
			fldHeaderDeleteQuery ),
			locDetailStatement = fldDataAccess.prepareStatement(
				fldDetailDeleteAllQuery );

		locHeaderStatement.setInt( 1, getId() );
		locHeaderStatement.execute();
		locDetailStatement.setInt( 1, getId() );
		locDetailStatement.execute();
	}

	void deleteAllDetail() throws SQLException
	{
		PreparedStatement
			locStatement = fldDataAccess.prepareStatement(
			fldDetailDeleteAllQuery );

		locStatement.setInt( 1, getId() );
		locStatement.execute();
	}

	private void saveDetail( PresentationItem presentationItem )
		throws SQLException
	{
		PreparedStatement
			locStatement = fldDataAccess.prepareStatement( fldDetailInsertQuery );

		locStatement.setInt( 1, getId() );
		locStatement.setInt( 2, fldPresentationItemList.indexOf(
			presentationItem ) );
		locStatement.setInt( 3, presentationItem.getId() );
		locStatement.setInt( 4, presentationItem.getItemType().ordinal() );
		locStatement.execute();
	}

	private int retrieveNextId() throws SQLException
	{
		ResultSet
			locResultSet = fldDataAccess.prepareStatement(
			fldNewIdQuery ).executeQuery();

		if( locResultSet.next() )
		{
			return locResultSet.getInt( 1 );
		}

		return 1;
	}
}
