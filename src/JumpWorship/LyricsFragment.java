package JumpWorship;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.text.Text;

/**
 * Created by Martinus on 7/8/2016.
 */
public class LyricsFragment implements LiveItem
{
	private static ProjectionScreen
		fldProjectionScreen = new ProjectionScreen();
	private Song
		fldSong;
	private Group
		fldGroup;
	private Text
		fldLyricText;
	private StringProperty
		fldTag = new SimpleStringProperty( this, "tag" ),
		fldContent = new SimpleStringProperty( this, "content" );

	public LyricsFragment( Song song, String tag, String content )
	{
		setSong( song );
		setTag( tag );
		setContent( content );
	}

	public Song getSong()
	{
		return fldSong;
	}

	public void setSong( Song song )
	{
		fldSong = song;
	}

	@Override
	public String getTitle()
	{
		return fldSong.getTitle();
	}

	@Override
	public StringProperty titleProperty()
	{
		return fldSong.titleProperty();
	}

	@Override
	public String getTag()
	{
		return fldTag.get();
	}

	public void setTag( String tag )
	{
		fldTag.set( tag );
	}

	public StringProperty tagProperty()
	{
		return fldTag;
	}

	@Override
	public String getContent()
	{
		return fldContent.get();
	}

	public void setContent( String content )
	{
		fldContent.set( content );
	}

	@Override
	public StringProperty contentProperty()
	{
		return fldContent;
	}

	@Override
	public void displayInProjector()
	{
		if( fldGroup == null )
		{
			fldGroup = new Group();
			fldLyricText = fldProjectionScreen.createLyricText(
				contentProperty() );
			fldGroup.getChildren().add( fldLyricText );
			fldGroup.getChildren().add( fldProjectionScreen.createTitleText(
				titleProperty() ) );
		}

		fldProjectionScreen.display( fldGroup );
	}

	@Override
	public void applyNewSetting()
	{
		if( fldGroup == null )
		{
			return;
		}

		fldGroup.getChildren().remove( fldLyricText );
		fldLyricText = fldProjectionScreen.createLyricText(
			contentProperty() );
		fldGroup.getChildren().add( fldLyricText );
	}
}
