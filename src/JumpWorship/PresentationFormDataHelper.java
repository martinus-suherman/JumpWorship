package JumpWorship;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.h2.Driver;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Martinus on 7/8/2016.
 */
class PresentationFormDataHelper
{
	private static final String
		fldPresentationLoadAllQuery =
		"SELECT PresentationId, Title " +
		"FROM PresentationHeader ",
		fldPresentationItemLoadQuery =
		"SELECT ItemId, ItemType, SequenceNumber " +
		"FROM PresentationDetail " +
		"WHERE PresentationId = ? " +
		"ORDER BY SequenceNumber ",
		fldSongLoadAllQuery =
		"SELECT SongId, Title, Lyrics " +
		"FROM Song " +
		"ORDER BY SongId",
		fldUser = "sa",
		fldPassword = "";
	private static ObservableList<Song>
		fldSongList;
	private DataAccess
		fldDataAccess = new DataAccess();

	ObservableList<Song> getSongList()
	{
		return fldSongList;
	}

	void closeConnection()
	{
		try
		{
			fldDataAccess.getConnection().close();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}

	void loadSongList()
	{
		ApplicationSetting
			locSetting = new ApplicationSetting();

		try
		{
			DriverManager.registerDriver( new Driver() );
			fldDataAccess.setConnection( DriverManager.getConnection(
				locSetting.getDatabaseUrl(), fldUser, fldPassword ) );

			fldSongList = loadAllSong();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}

	void saveSong( Song song )
	{
		try
		{
			song.delete();
			song.save();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}

	void deleteSong( Song song )
	{
		try
		{
			song.delete();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}

	ObservableList<Presentation> loadPresentationList()
	{
		try
		{
			return loadAllPresentation();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}

		return null;
	}

	void savePresentationHeader( Presentation presentation )
	{
		try
		{
			presentation.saveHeader();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}

	void savePresentationDetail( Presentation presentation )
	{
		try
		{
			presentation.saveDetail();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}

	void deletePresentation( Presentation presentation )
	{
		try
		{
			presentation.delete();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}

	void deletePresentationDetail( Presentation presentation )
	{
		try
		{
			presentation.deleteAllDetail();
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}

	private ObservableList<Song> loadAllSong() throws SQLException
	{
		ResultSet
			locResultSet = fldDataAccess.prepareStatement(
			fldSongLoadAllQuery ).executeQuery();
		ObservableList<Song>
			locResult = FXCollections.observableArrayList();

		while( locResultSet.next() )
		{
			locResult.add( new Song( locResultSet.getInt( 1 ),
				locResultSet.getString( 2 ), locResultSet.getString( 3 ) ) );
		}

		return locResult;
	}

	private ObservableList<Presentation> loadAllPresentation()
		throws SQLException
	{
		ResultSet
			locResultSet = fldDataAccess.prepareStatement(
			fldPresentationLoadAllQuery ).executeQuery();
		ObservableList<Presentation>
			locResult = FXCollections.observableArrayList();

		while( locResultSet.next() )
		{
			Presentation
				locPresentation = new Presentation( locResultSet.getInt( 1 ),
				locResultSet.getString( 2 ) );

			loadPresentationItem( locPresentation );
			locResult.add( locPresentation );
		}

		return locResult;
	}

	private void loadPresentationItem( Presentation presentation )
		throws SQLException
	{
		PreparedStatement
			locStatement = fldDataAccess.prepareStatement(
			fldPresentationItemLoadQuery );

		locStatement.setInt( 1, presentation.getId() );

		ResultSet
			locResultSet = locStatement.executeQuery();

		while( locResultSet.next() )
		{
			ItemType
				locItemType = ItemType.fromInteger( locResultSet.getInt( 2 ) );

			if( locItemType == ItemType.Song )
			{
				presentation.getPresentationItemList().add( findSongWithIndex(
					locResultSet.getInt( 1 ) ) );
			}
			else
			{
				presentation.getPresentationItemList().add( new Song() );
			}
		}
	}

	private Song findSongWithIndex( int id )
	{
		for( Song locSong : fldSongList )
		{
			if( locSong.getId() == id )
			{
				return locSong;
			}
		}

		return new Song();
	}
}
