package JumpWorship;

import javafx.animation.FadeTransition;
import javafx.beans.property.StringProperty;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.awt.*;

/**
 * Created by Martinus on 7/31/2017.
 */
class ProjectionScreen
{
	private static final double
		MINIMUM_FONT_SIZE = 24;
	private static final String
		PROJECTOR_WINDOW_TITLE = "JumpWorship Projector";
	private static double
		fldScreenWidth,
		fldScreenHeight,
		fldScreenX,
		fldScreenY;
	private static ApplicationSetting
		fldApplicationSetting = new ApplicationSetting();
	private static Font
		fldDefaultLyricFont,
		fldDefaultTitleFont;
	private static DropShadow
		fldDropShadow;
	private static Stage
		fldStage;
	private static Group
		fldBackgroundGroup,
		fldForegroundGroup,
		fldOldForeground;
	private static VideoBackground
		fldVideoBackground;
	private static ImageBackground
		fldImageBackground;
	private static FadeTransition
		fldFadeInTransition;

	VideoBackground getVideoBackground()
	{
		return fldVideoBackground;
	}

	ImageBackground getImageBackground()
	{
		return fldImageBackground;
	}

	Font getDefaultLyricFont()
	{
		return fldDefaultLyricFont;
	}

	void setDefaultLyricFont( Font defaultLyricFont )
	{
		fldDefaultLyricFont = defaultLyricFont;
	}

	double getScreenWidth()
	{
		return fldScreenWidth;
	}

	double getScreenHeight()
	{
		return fldScreenHeight;
	}

	void fromRectangle( Rectangle source )
	{
		fldScreenWidth = source.getWidth();
		fldScreenHeight = source.getHeight();

		if( fldApplicationSetting.isForceOutput() )
		{
			fldScreenWidth = fldApplicationSetting.getForceOutputWidth();
			fldScreenHeight = fldApplicationSetting.getForceOutputHeight();
		}

		fldScreenX = source.getX();
		fldScreenY = source.getY();
	}

	void createWindow( Stage primaryStage )
	{
		fldStage = new Stage( StageStyle.UNDECORATED );
		fldStage.setTitle( PROJECTOR_WINDOW_TITLE );
		fldStage.setX( fldScreenX );
		fldStage.setY( fldScreenY );
		fldStage.setWidth( fldScreenWidth );
		fldStage.setHeight( fldScreenHeight );

		fldBackgroundGroup = new Group();

		// TODO : Scene must be re-created only if width or height gets changed
		Scene
			locScene = new Scene( fldBackgroundGroup, fldScreenWidth,
			fldScreenHeight, Color.BLACK );

		initializeItems();

		fldStage.setScene( locScene );
		fldStage.show();

		primaryStage.setOnCloseRequest( event -> {
			fldStage.close();
			System.exit( 0 );
		} );

		fldStage.setOnCloseRequest( event -> {
			primaryStage.close();
			System.exit( 0 );
		} );

		// if main window is hidden/minimized, then set projection window
		// to background
		//		primaryStage.setOnHidden( t -> fldStage.toBack() );
		//		primaryStage.setOnHiding( t -> fldStage.toBack() );
	}

	void moveWindow()
	{
		fldStage.setX( fldScreenX );
		fldStage.setY( fldScreenY );
	}

	Text createLyricText( StringProperty text )
	{
		return createText( fldApplicationSetting.getLeftMargin(),
			getSongTextBaseY(), getSongTextWidth(), getSongTextHeight(),
			fldApplicationSetting.getLineSpacing(), fldDefaultLyricFont,
			text.get(), fldApplicationSetting.getDefaultTextAlignment(),
			fldApplicationSetting.getLyricVerticalAlignment() );
	}

	Text createTitleText( StringProperty text )
	{
		return createText( fldApplicationSetting.getLeftMargin(),
			getTitleTextBaseY(), getSongTextWidth(),
			fldApplicationSetting.getBottomMargin(),
			fldApplicationSetting.getLineSpacing(), fldDefaultTitleFont,
			text.get(), TextAlignment.LEFT, VPos.TOP );
	}

	void display( Group newForeground )
	{
		// TODO : set exit transition
		fldForegroundGroup.getChildren().remove( fldOldForeground );
		fldForegroundGroup.getChildren().add( newForeground );
		fldFadeInTransition.setNode( newForeground );
		fldFadeInTransition.play();
		fldOldForeground = newForeground;
	}

	private Text createText( double x, double y, double maxWidth,
		double maxHeight, double lineSpacing, Font font, String text,
		TextAlignment alignment, VPos origin )
	{
		Text
			locResult = new Text();

		locResult.setEffect( fldDropShadow );
		locResult.setFill( fldApplicationSetting.getTextColor() );
		locResult.setLayoutX( x );
		locResult.setLayoutY( y );
		locResult.setLineSpacing( lineSpacing );
		locResult.setText( text );
		locResult.setTextAlignment( alignment );
		locResult.setFont( font );
		locResult.setWrappingWidth( maxWidth );
		locResult.setTextOrigin( origin );

		//		locResult.setCache( true );
		//Include or not the stroke effect
		//        locResult.setStyle("-fx-stroke: black;-fx-stroke-width: 3;");

		if( fldApplicationSetting.isAutoResizeFont() )
		{
			adjustFont( locResult, maxHeight );
		}

		return locResult;
	}

	private void adjustFont( Text text, double maxHeight )
	{
		while( text.boundsInLocalProperty().getValue().getHeight() > maxHeight &&
			text.getFont().getSize() > MINIMUM_FONT_SIZE )
		{
			text.setFont( new Font( fldApplicationSetting.getDefaultFontName(),
				text.getFont().getSize() - 4 ) );
		}
	}

	private void initializeItems()
	{
		fldForegroundGroup = new Group();
		fldVideoBackground = new VideoBackground();
		fldImageBackground = new ImageBackground();

		initializeDropShadow();
		initializeFadeInTransition();
		initializeLyricFont();
		initializeTitleFont();

		fldBackgroundGroup.getChildren().add( fldImageBackground.getImageView() );
		fldBackgroundGroup.getChildren().add( fldVideoBackground.getMediaView() );

		// Video player (not video background)
		//		fldBackgroundGroup.getChildren().add(
		//			MediaProjector.getInstance().getMediaView() );

		//		AddAlert();
		fldBackgroundGroup.getChildren().add( fldForegroundGroup );
	}

	private void initializeLyricFont()
	{
		if( fldDefaultLyricFont != null )
		{
			return;
		}

		fldDefaultLyricFont = new Font(
			fldApplicationSetting.getDefaultFontName(),
			fldApplicationSetting.getDefaultLyricFontSize() );
	}

	private void initializeTitleFont()
	{
		if( fldDefaultTitleFont != null )
		{
			return;
		}

		fldDefaultTitleFont = new Font(
			fldApplicationSetting.getDefaultFontName(),
			fldApplicationSetting.getDefaultTitleFontSize() );
	}

	private void initializeDropShadow()
	{
		if( fldDropShadow != null )
		{
			return;
		}

		fldDropShadow = new DropShadow();
		fldDropShadow.setColor( fldApplicationSetting.getTextShadowColor() );
		fldDropShadow.setRadius( fldApplicationSetting.getTextShadowRadius() );
		fldDropShadow.setSpread( fldApplicationSetting.getTextShadowSpread() );
	}

	private void initializeFadeInTransition()
	{
		if( fldFadeInTransition != null )
		{
			return;
		}

		fldFadeInTransition = new FadeTransition( Duration.millis(
			fldApplicationSetting.getFadeInDuration() ) );
		fldFadeInTransition.setFromValue(
			fldApplicationSetting.getFadeInStartOpacity() );
		fldFadeInTransition.setToValue( 1 );
		fldFadeInTransition.setCycleCount( 1 );
	}

	private double getSongTextWidth()
	{
		return fldScreenWidth - fldApplicationSetting.getLeftMargin() -
			fldApplicationSetting.getRightMargin();
	}

	private double getSongTextHeight()
	{
		return fldScreenHeight - fldApplicationSetting.getTopMargin() -
			fldApplicationSetting.getBottomMargin();
	}

	private double getSongTextBaseY()
	{
		switch( fldApplicationSetting.getLyricVerticalAlignment() )
		{
			case TOP:
				return fldApplicationSetting.getTopMargin();

			case CENTER:
				return fldApplicationSetting.getTopMargin() +
					getSongTextHeight() / 2;

			case BOTTOM:
				return fldApplicationSetting.getTopMargin() + getSongTextHeight();
		}

		return fldApplicationSetting.getTopMargin();
	}

	private double getTitleTextBaseY()
	{
		if( fldApplicationSetting.getLyricVerticalAlignment() == VPos.BOTTOM )
		{
			return fldApplicationSetting.getTopMargin();
		}

		return fldScreenHeight - fldApplicationSetting.getBottomMargin();
	}

	// TODO : might not be needed (?)
	private void AddAlert()
	{
		// Alert (currently unused)
		Text
			textAlert;

		textAlert = new Text();
		textAlert.setX( 0 );
		textAlert.setY( 3 * ( fldScreenHeight / 10 ) / 4 );
		textAlert.setFont( Font.font( "Arial", 46 ) );
		textAlert.setFill( Color.WHITE );
		textAlert.setVisible( false );

		javafx.scene.shape.Rectangle
			bgAlert;

		bgAlert = new javafx.scene.shape.Rectangle();
		bgAlert.setX( 0 );
		bgAlert.setY( 0 );
		bgAlert.setWidth( fldScreenWidth );
		bgAlert.setHeight( fldScreenHeight / 10 );
		bgAlert.setFill( Color.BLACK );
		bgAlert.setVisible( false );
		fldBackgroundGroup.getChildren().add( bgAlert );
		fldBackgroundGroup.getChildren().add( textAlert );
	}
}
