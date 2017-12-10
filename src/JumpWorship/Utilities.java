package JumpWorship;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Martinus on 7/30/2017.
 */
class Utilities
{
	FXMLLoader displayFxmlForm( Stage stage, Modality modality,
		Image icon, String title, String path, boolean isCenterOnScreen,
		double minWidth, double minHeight ) throws IOException
	{
		FXMLLoader
			locLoader = new FXMLLoader( getClass().getResource( path ) );

		if( title != null )
		{
			stage.setTitle( title );
		}

		if( icon != null )
		{
			stage.getIcons().add( icon );
		}

		try
		{
			stage.initModality( modality );
		}
		catch( IllegalStateException ignored )
		{

		}

		stage.setScene( new Scene( locLoader.load() ) );
		stage.setWidth( minWidth );
		stage.setHeight( minHeight );
		stage.setMinWidth( minWidth );
		stage.setMinHeight( minHeight );

		if( isCenterOnScreen )
		{
			stage.centerOnScreen();
		}

		return locLoader;
	}

	void displayMonitorSelection() throws IOException
	{
		ObservableList<Screen>
			locScreens = Screen.getScreens();
		ProjectionScreen
			locProjectionScreen = new ProjectionScreen();

		if( locScreens.size() == 1 )
		{
			locProjectionScreen.fromRectangle( convertToRectangle(
				locScreens.get( 0 ).getBounds() ) );
			return;
		}

		Stage
			locStage = new Stage( StageStyle.UNDECORATED );
		MonitorSelectionController
			locController = displayFxmlForm( locStage, Modality.APPLICATION_MODAL,
			null, "Please select projection monitor", "MonitorSelection.fxml",
			true, 800, 500 ).getController();

		locController.setStage( locStage );
		locStage.showAndWait();
	}

	Rectangle convertToRectangle( Rectangle2D source )
	{
		Rectangle
			locResult = new Rectangle();

		locResult.setRect( source.getMinX(), source.getMinY(), source.getWidth(),
			source.getHeight() );
		return locResult;
	}

	boolean matchPattern( LiveItem item, Pattern pattern )
	{
		Matcher
			locMatcher = pattern.matcher( item.getTag().toLowerCase() );

		return locMatcher.find();
	}

	void changeChildrenStyle( ObservableList<Node> children, String style )
	{
		for( Node locNode : children )
		{
			locNode.setStyle( style );
		}
	}

	void initializeDialogStyle( Dialog dialog )
	{
		dialog.getDialogPane().getStylesheets().add( getClass().getResource(
			"dark-modena.css" ).toExternalForm() );
	}
}
