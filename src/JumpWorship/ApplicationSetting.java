package JumpWorship;

import javafx.geometry.VPos;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import org.ini4j.Wini;

import java.io.File;
import java.io.IOException;

/**
 * Created by Martinus on 8/12/2017.
 */
class ApplicationSetting
{
	private static final String
		INI_FILE_PATH = "JumpWorship.ini",
		PATH_SECTION = "Path",
		DATABASE_URL = "DatabaseUrl",
		DEFAULT_BACKGROUND_FOLDER = "DefaultBackgroundFolder",
		DEFAULT_VIDEO_BACKGROUND = "DefaultVideoBackground",
		DEFAULT_IMAGE_BACKGROUND = "DefaultImageBackground",
		OUTPUT_SECTION = "Output",
		FORCE_OUTPUT = "ForceOutput",
		FORCE_OUTPUT_HEIGHT = "ForceOutputHeight",
		FORCE_OUTPUT_WIDTH = "ForceOutputWidth",
		DEFAULT_FONT_NAME = "DefaultFontName",
		DEFAULT_LYRIC_FONT_SIZE = "DefaultLyricFontSize",
		DEFAULT_TITLE_FONT_SIZE = "DefaultTitleFontSize",
		DEFAULT_LYRIC_ALIGNMENT = "DefaultLyricAlignment",
		LYRIC_VERTICAL_ALIGNMENT = "LyricVerticalAlignment",
		TEXT_SHADOW_SPREAD = "TextShadowSpread",
		TEXT_SHADOW_RADIUS = "TextShadowRadius",
		AUTO_RESIZE_FONT = "AutoResizeFont",
		TOP_MARGIN = "TopMargin",
		BOTTOM_MARGIN = "BottomMargin",
		LEFT_MARGIN = "LeftMargin",
		RIGHT_MARGIN = "RightMargin",
		LINE_SPACING = "LineSpacing",
		FADE_IN_DURATION = "FadeInDuration",
		FADE_IN_START_OPACITY = "FadeInStartOpacity";
	private static Color
		fldTextColor = Color.WHITE,
		fldTextShadowColor = Color.BLACK;
	private static TextAlignment
		fldDefaultTextAlignment;
	private static VPos
		fldLyricVerticalAlignment;
	private static String
		fldDatabaseUrl,
		fldDefaultBackgroundFolder,
		fldDefaultVideoBackground,
		fldDefaultImageBackground,
		fldDefaultFontName;
	private static double
		fldTextShadowRadius,
		fldTextShadowSpread,
		fldDefaultLyricFontSize,
		fldDefaultTitleFontSize,
		fldForceOutputWidth,
		fldForceOutputHeight,
		fldTopMargin,
		fldBottomMargin,
		fldLeftMargin,
		fldRightMargin,
		fldLineSpacing,
		fldFadeInDuration,
		fldFadeInStartOpacity;
	private static boolean
		fldForceOutput,
		fldAutoResizeFont;


	boolean isForceOutput()
	{
		return fldForceOutput;
	}

	double getForceOutputWidth()
	{
		return fldForceOutputWidth;
	}

	double getForceOutputHeight()
	{
		return fldForceOutputHeight;
	}

	boolean isAutoResizeFont()
	{
		return fldAutoResizeFont;
	}

	double getDefaultLyricFontSize()
	{
		return fldDefaultLyricFontSize;
	}

	double getDefaultTitleFontSize()
	{
		return fldDefaultTitleFontSize;
	}

	double getTopMargin()
	{
		return fldTopMargin;
	}

	double getBottomMargin()
	{
		return fldBottomMargin;
	}

	double getLeftMargin()
	{
		return fldLeftMargin;
	}

	double getRightMargin()
	{
		return fldRightMargin;
	}

	double getLineSpacing()
	{
		return fldLineSpacing;
	}

	double getFadeInDuration()
	{
		return fldFadeInDuration;
	}

	double getFadeInStartOpacity()
	{
		return fldFadeInStartOpacity;
	}

	String getDatabaseUrl()
	{
		return fldDatabaseUrl;
	}

	String getDefaultBackgroundFolder()
	{
		return fldDefaultBackgroundFolder;
	}

	String getDefaultVideoBackground()
	{
		return fldDefaultVideoBackground;
	}

	String getDefaultImageBackground()
	{
		return fldDefaultImageBackground;
	}

	TextAlignment getDefaultTextAlignment()
	{
		return fldDefaultTextAlignment;
	}

	VPos getLyricVerticalAlignment()
	{
		return fldLyricVerticalAlignment;
	}

	String getDefaultFontName()
	{
		return fldDefaultFontName;
	}

	void setDefaultFontName( String defaultFontName )
	{
		fldDefaultFontName = defaultFontName;
		saveConfigurationToIniFile( OUTPUT_SECTION, DEFAULT_FONT_NAME,
			fldDefaultFontName );
	}

	Color getTextColor()
	{
		return fldTextColor;
	}

	void setTextColor( Color textColor )
	{
		fldTextColor = textColor;
	}

	Color getTextShadowColor()
	{
		return fldTextShadowColor;
	}

	public void setTextShadowColor( Color textShadowColor )
	{
		fldTextShadowColor = textShadowColor;
	}

	double getTextShadowRadius()
	{
		return fldTextShadowRadius;
	}

	double getTextShadowSpread()
	{
		return fldTextShadowSpread;
	}

	void readConfigurationFromIniFile() throws IOException
	{
		Wini
			locIni = new Wini( new File( INI_FILE_PATH ) );

		fldDatabaseUrl = locIni.get( PATH_SECTION, DATABASE_URL, String.class );
		fldDefaultBackgroundFolder = locIni.get( PATH_SECTION,
			DEFAULT_BACKGROUND_FOLDER, String.class );
		fldDefaultVideoBackground = locIni.get( PATH_SECTION,
			DEFAULT_VIDEO_BACKGROUND, String.class );
		fldDefaultImageBackground = locIni.get( PATH_SECTION,
			DEFAULT_IMAGE_BACKGROUND, String.class );
		fldForceOutput = locIni.get( OUTPUT_SECTION, FORCE_OUTPUT,
			boolean.class );
		fldAutoResizeFont = locIni.get( OUTPUT_SECTION, AUTO_RESIZE_FONT,
			boolean.class );
		fldForceOutputHeight = locIni.get( OUTPUT_SECTION, FORCE_OUTPUT_HEIGHT,
			double.class );
		fldForceOutputWidth = locIni.get( OUTPUT_SECTION, FORCE_OUTPUT_WIDTH,
			double.class );
		fldDefaultFontName = locIni.get( OUTPUT_SECTION, DEFAULT_FONT_NAME,
			String.class );
		fldDefaultLyricFontSize = locIni.get( OUTPUT_SECTION,
			DEFAULT_LYRIC_FONT_SIZE, double.class );
		fldDefaultTitleFontSize = locIni.get( OUTPUT_SECTION,
			DEFAULT_TITLE_FONT_SIZE, double.class );
		fldTextShadowSpread = locIni.get( OUTPUT_SECTION, TEXT_SHADOW_SPREAD,
			double.class );
		fldTextShadowRadius = locIni.get( OUTPUT_SECTION, TEXT_SHADOW_RADIUS,
			double.class );
		fldTopMargin = locIni.get( OUTPUT_SECTION, TOP_MARGIN, double.class );
		fldBottomMargin = locIni.get( OUTPUT_SECTION, BOTTOM_MARGIN,
			double.class );
		fldLeftMargin = locIni.get( OUTPUT_SECTION, LEFT_MARGIN, double.class );
		fldRightMargin = locIni.get( OUTPUT_SECTION, RIGHT_MARGIN, double.class );
		fldLineSpacing = locIni.get( OUTPUT_SECTION, LINE_SPACING, double.class );
		fldFadeInDuration = locIni.get( OUTPUT_SECTION, FADE_IN_DURATION,
			double.class );
		fldFadeInStartOpacity = locIni.get( OUTPUT_SECTION, FADE_IN_START_OPACITY,
			double.class );
		fldDefaultTextAlignment = textAlignmentFromString( locIni.get(
			OUTPUT_SECTION, DEFAULT_LYRIC_ALIGNMENT, String.class ) );
		fldLyricVerticalAlignment = verticalAlignmentFromString( locIni.get(
			OUTPUT_SECTION, LYRIC_VERTICAL_ALIGNMENT, String.class ) );
	}

	void saveConfigurationToIniFile( String sectionName, String optionName,
		Object value )
	{
		try
		{
			Wini
				locIni = new Wini( new File( INI_FILE_PATH ) );

			locIni.put( sectionName, optionName, value );
			locIni.store();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}

	private TextAlignment textAlignmentFromString( String source )
	{
		try
		{
			return TextAlignment.valueOf( source.toUpperCase() );
		}
		catch( IllegalArgumentException e )
		{
			return TextAlignment.LEFT;
		}
	}

	private VPos verticalAlignmentFromString( String source )
	{
		try
		{
			return VPos.valueOf( source.toUpperCase() );
		}
		catch( IllegalArgumentException e )
		{
			return VPos.CENTER;
		}
	}
}
