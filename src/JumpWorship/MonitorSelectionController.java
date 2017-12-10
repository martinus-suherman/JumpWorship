package JumpWorship;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Martinus on 7/26/2017.
 */
public class MonitorSelectionController implements Initializable
{
	@FXML
	private Pane fldCanvas;
	@FXML
	private Button fldButtonExit;
	private Stage
		fldStage;
	private Utilities
		fldUtilities = new Utilities();

	public Stage getStage()
	{
		return fldStage;
	}

	void setStage( Stage stage )
	{
		fldStage = stage;
	}

	@Override
	public void initialize( URL location, ResourceBundle resources )
	{
		attachEventHandlers();
		displayMonitors();
	}

	private void attachEventHandlers()
	{
		fldButtonExit.setOnAction( this::buttonExitOnAction );
	}

	private void displayMonitors()
	{
		ObservableList<Screen>
			locScreens = Screen.getScreens();
		java.awt.Rectangle
			locMinimumBounds = getMinimumBounds( locScreens );
		Font
			locFont = Font.font( "System", 18 );

		//Check how many monitors we have and put representative buttons on screen.
		for( Screen locScreen : locScreens )
		{
			final Rectangle2D
				locScreenBounds = locScreen.getBounds();
			StackPane
				locPane = new StackPane();
			java.awt.Rectangle
				locRectangle = scale( locScreenBounds, locMinimumBounds, 400, 300 );
			Rectangle
				locBackground = initializeBackground( locRectangle );

			locPane.setCursor( javafx.scene.Cursor.OPEN_HAND );
			locPane.setLayoutX( locRectangle.getMinX() );
			locPane.setLayoutY( locRectangle.getMinY() );

			ProjectionScreen
				locProjectionScreen = new ProjectionScreen();

			locPane.setOnMouseClicked( mouseEvent -> {
				locProjectionScreen.fromRectangle( fldUtilities.convertToRectangle(
					locScreenBounds ) );
				fldStage.hide();
			} );

			locPane.setOnMouseEntered( mouseEvent -> locBackground.setFill(
				Color.BLUEVIOLET ) );
			locPane.setOnMouseExited( mouseEvent -> locBackground.setFill(
				Color.web( "66a3ff" ) ) );

			locPane.getChildren().add( locBackground );
			locPane.getChildren().add( initializeLabel( locScreen, locFont ) );
			fldCanvas.getChildren().add( locPane );
		}
	}

	private Rectangle initializeBackground( java.awt.Rectangle rectangle )
	{
		Rectangle
			locResult = new Rectangle();

		locResult.setWidth( rectangle.getWidth() );
		locResult.setHeight( rectangle.getHeight() );
		locResult.setFill( Color.web( "66a3ff" ) );
		locResult.setStroke( Color.WHITE );
		locResult.setStrokeWidth( 6 );
		locResult.setStrokeType( StrokeType.INSIDE );
		locResult.setEffect( new DropShadow() );
		return locResult;
	}

	private Label initializeLabel( Screen screen, Font font )
	{
		Label
			locResult = new Label();

		locResult.setTextFill( Color.WHITE );
		locResult.setFont( font );

		if( screen.equals( Screen.getPrimary() ) )
		{
			locResult.setText( "Main Screen" );
			return locResult;
		}

		locResult.setText( "Screen" );
		return locResult;
	}

	private java.awt.Rectangle getMinimumBounds( List<Screen> devices )
	{
		java.awt.Rectangle
			locResult = new java.awt.Rectangle();

		for( Screen locDevice : devices )
		{
			locResult = locResult.union( fldUtilities.convertToRectangle(
				locDevice.getBounds() ) );
		}

		return locResult;
	}

	private java.awt.Rectangle scale( Rectangle2D deviceBounds,
		java.awt.Rectangle totalBounds, double maxWidth, double maxHeight )
	{
		java.awt.Rectangle
			locResult = new java.awt.Rectangle();

		locResult.setRect(
			scaleNumber( deviceBounds.getMinX(), totalBounds.x,
				totalBounds.x + totalBounds.width, 0, maxWidth ),
			scaleNumber( deviceBounds.getMinY(), totalBounds.y,
				totalBounds.y + totalBounds.height, 0, maxHeight ),
			deviceBounds.getWidth() * maxWidth / totalBounds.getWidth(),
			deviceBounds.getHeight() * maxHeight / totalBounds.getHeight() );
		return locResult;
	}

	private double scaleNumber( double number, double sourceMinimum,
		double sourceMaximum, double destinationMinimum,
		double destinationMaximum )
	{
		return Math.abs( ( sourceMinimum - number ) /
			( sourceMaximum - sourceMinimum ) ) *
			( destinationMaximum - destinationMinimum );
	}

	private void buttonExitOnAction( ActionEvent actionEvent )
	{
		System.exit( 0 );
	}
}
