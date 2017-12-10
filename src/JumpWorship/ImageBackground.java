package JumpWorship;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

class ImageBackground
{
	private File
		fldImageFile;
	private ImageView
		fldImageView;
	private static ApplicationSetting
		fldApplicationSetting = new ApplicationSetting();
	private ProjectionScreen
		fldProjectionScreen = new ProjectionScreen();

	ImageBackground()
	{
		fldImageFile = new File(
			fldApplicationSetting.getDefaultImageBackground() );

		initialize();
		hide();
	}

	ImageView getImageView()
	{
		return fldImageView;
	}

	void hide()
	{
		fldImageView.setVisible( false );
	}

	void show()
	{
		fldImageView.setVisible( true );
	}

	void changeTo( File newImage )
	{
		boolean
			locIsVisible = fldImageView.isVisible();

		fldImageFile = newImage;

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

	void initialize()
	{
		Image
			locImage = new Image( fldImageFile.toURI().toString() );

		if( fldImageView == null )
		{
			fldImageView = new ImageView();
			fldImageView.setFitWidth( fldProjectionScreen.getScreenWidth() );
			fldImageView.setFitHeight( fldProjectionScreen.getScreenHeight() );
			fldImageView.setSmooth( true );
		}

		fldImageView.setImage( locImage );
	}
}
