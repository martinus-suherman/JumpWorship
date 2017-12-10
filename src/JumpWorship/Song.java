package JumpWorship;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Martinus on 7/7/2016.
 */
public class Song implements PresentationItem
{
	private static final String
		INSERT_QUERY =
		"INSERT INTO Song ( SongId, Title, Lyrics ) " +
		"VALUES ( ?, ?, ? ) ",
		DELETE_QUERY =
		"DELETE FROM Song " +
		"WHERE SongId = ? ",
		NEW_ID_QUERY =
		"SELECT IFNULL( MAX( SongId ), 0 ) + 1 AS NewId " +
		"FROM Song",
		EMPTY_STRING = "",
		DUAL_NEW_LINES = "\n\n";
	private static final ItemType
		fldItemType = ItemType.Song;
	private static final Pattern
		fldPattern = Pattern.compile( "^\\[(.*)]\\s*" );
	private static DataAccess
		fldDataAccess = new DataAccess();
	private final StringProperty
		fldTitle = new SimpleStringProperty( this, "title" ),
		fldLyrics = new SimpleStringProperty( this, "lyrics" );
	private int
		fldId;
	private ArrayList<LiveItem>
		fldLyricsFragments = new ArrayList<>();

	public Song()
	{
		try
		{
			setId( retrieveNextId() );
			setTitle( EMPTY_STRING );
			setLyrics( EMPTY_STRING );
		}
		catch( SQLException e )
		{
			e.printStackTrace();
		}
	}

	public Song( int id, String title, String lyrics )
	{
		setId( id );
		setTitle( title );
		setLyrics( lyrics );
	}

	@Override
	public ItemType getItemType()
	{
		return fldItemType;
	}

	@Override
	public int getId()
	{
		return fldId;
	}

	public void setId( int id )
	{
		fldId = id;
	}

	@Override
	public String getTitle()
	{
		return fldTitle.get();
	}

	@Override
	public List<LiveItem> getLiveItems()
	{
		return fldLyricsFragments;
	}

	@Override
	public void parse()
	{
		String
			locTag = EMPTY_STRING;

		fldLyricsFragments.clear();

		for( String locFragment : getLyrics().split( DUAL_NEW_LINES ) )
		{
			//we don't quite care much for excessive blank lines
			if( locFragment.isEmpty() )
			{
				continue;
			}

			Matcher
				locMatcher = fldPattern.matcher( locFragment );

			if( locMatcher.find() )
			{
				locTag = locMatcher.group( 1 );
				locFragment = locMatcher.replaceAll( EMPTY_STRING );
			}

			fldLyricsFragments.add( new LyricsFragment( this, locTag,
				locFragment ) );
		}
	}

	void setTitle( String title )
	{
		fldTitle.set( title );
	}

	StringProperty titleProperty()
	{
		return fldTitle;
	}

	String getLyrics()
	{
		return fldLyrics.get();
	}

	void setLyrics( String lyrics )
	{
		fldLyrics.set( lyrics );
	}

	StringProperty lyricsProperty()
	{
		return fldLyrics;
	}

	@Override
	public int hashCode()
	{
		return getId();
	}

	@Override
	public boolean equals( Object obj )
	{
		if( this == obj )
		{
			return true;
		}

		if( obj == null || getClass() != obj.getClass() )
		{
			return false;
		}

		Song
			locSong = (Song) obj;

		return getId() == locSong.getId();
	}

	void save() throws SQLException
	{
		PreparedStatement
			locStatement = fldDataAccess.prepareStatement( INSERT_QUERY );

		locStatement.setInt( 1, getId() );
		locStatement.setString( 2, getTitle() );
		locStatement.setString( 3, getLyrics() );
		locStatement.execute();
	}

	void delete() throws SQLException
	{
		PreparedStatement
			locStatement = fldDataAccess.prepareStatement( DELETE_QUERY );

		locStatement.setInt( 1, getId() );
		locStatement.execute();
	}

	private int retrieveNextId() throws SQLException
	{
		ResultSet
			locResultSet = fldDataAccess.prepareStatement(
			NEW_ID_QUERY ).executeQuery();

		if( locResultSet.next() )
		{
			return locResultSet.getInt( 1 );
		}

		return 1;
	}
}
