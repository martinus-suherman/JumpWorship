package JumpWorship;

import javafx.animation.ScaleTransition;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;

class VideoBackground
{
	private File
		fldVideoFile;
	private MediaView
		fldMediaView;
	private MediaPlayer
		fldMediaPlayer;
	private static ScaleTransition
		fldZoomInTransition;
	private static ApplicationSetting
		fldApplicationSetting = new ApplicationSetting();
	private static ProjectionScreen
		fldProjectionScreen = new ProjectionScreen();

	VideoBackground()
	{
		fldVideoFile = new File(
			fldApplicationSetting.getDefaultVideoBackground() );
		initializeZoomInTransition();
		initialize();
		show();
	}

	MediaView getMediaView()
	{
		return fldMediaView;
	}

	void hide()
	{
		fldMediaPlayer.stop();
		fldMediaView.setVisible( false );
	}

	void show()
	{
		fldMediaView.setVisible( true );
		fldZoomInTransition.setNode( fldMediaView );
		fldZoomInTransition.play();
		fldMediaPlayer.play();
	}

	void changeTo( File newVideo )
	{
		boolean
			locIsVisible = fldMediaView.isVisible();

		fldVideoFile = newVideo;

		if( locIsVisible )
		{
			hide();
		}

		initialize();

		if( locIsVisible )
		{
			show();
		}
	}

	private void initialize()
	{
		if( fldMediaPlayer != null )
		{
			fldMediaPlayer.stop();
		}

		fldMediaPlayer = new MediaPlayer( new Media(
			fldVideoFile.toURI().toString() ) );
		fldMediaPlayer.setAutoPlay( false );
		fldMediaPlayer.setVolume( 0 );
		fldMediaPlayer.setCycleCount( MediaPlayer.INDEFINITE );

		// Create MediaView from MediaPlayer.
		// View must not be recreated as it is attached externally.
		if( fldMediaView != null )
		{
			fldMediaView.setMediaPlayer( fldMediaPlayer );
			return;
		}

		fldMediaView = new MediaView( fldMediaPlayer );
		fldMediaView.setX( 0 );
		fldMediaView.setY( 0 );
		fldMediaView.setFitWidth( fldProjectionScreen.getScreenWidth() );
		fldMediaView.setFitHeight( fldProjectionScreen.getScreenHeight() );
		fldMediaView.setPreserveRatio( false );
	}

	private void initializeZoomInTransition()
	{
		if( fldZoomInTransition != null )
		{
			return;
		}

		fldZoomInTransition = new ScaleTransition( Duration.millis( 600 ) );
		fldZoomInTransition.setFromX( 0.1 );
		fldZoomInTransition.setFromY( 0.1 );
		fldZoomInTransition.setToX( 1 );
		fldZoomInTransition.setToY( 1 );
		fldZoomInTransition.setCycleCount( 1 );
	}
}
