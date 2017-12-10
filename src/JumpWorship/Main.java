package JumpWorship;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Main extends Application
{
	public static void main( String[] args )
	{
		launch( args );
	}

	@Override
	public void start( Stage primaryStage ) throws Exception
	{
		ApplicationSetting
			locSetting = new ApplicationSetting();

		locSetting.readConfigurationFromIniFile();

		Utilities
			locUtilities = new Utilities();

		locUtilities.displayMonitorSelection();

		PresentationFormController
			locController = locUtilities.displayFxmlForm( primaryStage,
			Modality.NONE, null, "JumpWorship", "PresentationForm.fxml", false,
			800, 600 ).getController();
//		new Image( "file://../../JumpWorship.ico" )
		
		ProjectionScreen
			locProjectionScreen = new ProjectionScreen();

		locProjectionScreen.createWindow( primaryStage );
		primaryStage.setOnHidden( locController::onFormClosed );
		primaryStage.show();
	}
}
