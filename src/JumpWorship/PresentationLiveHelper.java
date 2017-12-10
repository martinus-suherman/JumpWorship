package JumpWorship;

import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.dialog.FontSelectorDialog;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

class PresentationLiveHelper
{
	private static final String
		EMPTY_STRING = "",
		VIDEO_FILE_FILTER_DESCRIPTION = "Video File",
		IMAGE_FILE_FILTER_DESCRIPTION = "Image File";
	private static final int
		VERSE_ONE = 0,
		VERSE_TWO = 1,
		VERSE_THREE = 2,
		VERSE_FOUR = 3,
		CHORUS = 4,
		BRIDGE = 5,
		ENDING = 6,
		VERSE = 7,
		PRE = 8;
	private static final Pattern[]
		fldPattern = {
		Pattern.compile( "^v(.*)1" ),
		Pattern.compile( "^v(.*)2" ),
		Pattern.compile( "^v(.*)3" ),
		Pattern.compile( "^v(.*)4" ),
		Pattern.compile( "^(chorus|reff)" ),
		Pattern.compile( "^bridge" ),
		Pattern.compile( "^end" ),
		Pattern.compile( "^v" ),
		Pattern.compile( "^pre" )
	};
	private static final String[]
		fldStyle = {
		"-fx-text-fill: #aacc22;",
		"-fx-text-fill: #5599ff;",
		"-fx-text-fill: #ffcc66;",
		"-fx-text-fill: #6699bb;",
		"-fx-text-fill: #cc7733;",
		"-fx-text-fill: #669955;",
		"-fx-text-fill: #bbbb22;",
		"-fx-text-fill: #aabbcc;",
		"-fx-text-fill: #9977aa;"
	};
	private ApplicationSetting
		fldSetting = new ApplicationSetting();
	private ProjectionScreen
		fldProjectionScreen = new ProjectionScreen();
	private Utilities
		fldUtilities = new Utilities();
	private FileChooser
		fldFileChooser = new FileChooser();
	private List<String>
		fldVideoFileExtension,
		fldImageFileExtension;
	private TableView<LiveItem>
		fldTableView;
	private TableColumn<LiveItem, String>
		fldTableColumnTitle,
		fldTableColumnTag,
		fldTableColumnContent;
	private ToggleSwitch
		fldToggleSwitchVideoBackground,
		fldToggleSwitchImageBackground;
	private Button
		fldButtonChangeVideoBackground,
		fldButtonChangeImageBackground,
		fldButtonChangeProjectionMonitor,
		fldButtonChangeDefaultLyricFont;
	private LiveItem
		fldEmptyLiveItem;
	private VirtualFlow
		fldVirtualFlow;

	PresentationLiveHelper( TableView<LiveItem> tableView,
		TableColumn<LiveItem, String> tableColumnTitle,
		TableColumn<LiveItem, String> tableColumnTag,
		TableColumn<LiveItem, String> tableColumnContent,
		ToggleSwitch toggleSwitchVideoBackground,
		ToggleSwitch toggleSwitchImageBackground,
		Button buttonChangeVideoBackground, Button buttonChangeImageBackground,
		Button buttonChangeProjectionMonitor,
		Button buttonChangeDefaultLyricFont,
		LiveItem emptyLiveItem )
	{
		fldTableView = tableView;
		fldTableColumnTitle = tableColumnTitle;
		fldTableColumnTag = tableColumnTag;
		fldTableColumnContent = tableColumnContent;
		fldToggleSwitchVideoBackground = toggleSwitchVideoBackground;
		fldToggleSwitchImageBackground = toggleSwitchImageBackground;
		fldButtonChangeVideoBackground = buttonChangeVideoBackground;
		fldButtonChangeImageBackground = buttonChangeImageBackground;
		fldButtonChangeProjectionMonitor = buttonChangeProjectionMonitor;
		fldButtonChangeDefaultLyricFont = buttonChangeDefaultLyricFont;
		fldEmptyLiveItem = emptyLiveItem;
		fldFileChooser.setInitialDirectory( new File(
			fldSetting.getDefaultBackgroundFolder() ) );
		initializeVideoFileExtensionList();
		initializeImageFileExtensionList();
	}

	void attachEventHandlers()
	{
		fldTableView.widthProperty().addListener( this::tableViewOnWidthChanged );
		fldTableView.getSelectionModel().selectedItemProperty().addListener(
			this::tableViewOnSelectionChanged );
		fldTableView.setRowFactory( this::tableViewRowFactory );
		fldTableView.setOnKeyPressed( this::tableViewOnKeyPressed );
		fldToggleSwitchVideoBackground.selectedProperty().addListener(
			this::toggleSwitchVideoBackgroundOnChanged );
		fldToggleSwitchImageBackground.selectedProperty().addListener(
			this::toggleSwitchImageBackgroundOnChanged );
		fldButtonChangeVideoBackground.setOnAction(
			this::buttonChangeVideoBackgroundOnAction );
		fldButtonChangeImageBackground.setOnAction(
			this::buttonChangeImageBackgroundOnAction );
		fldButtonChangeProjectionMonitor.setOnAction(
			this::buttonChangeProjectionMonitorOnAction );
		fldButtonChangeDefaultLyricFont.setOnAction(
			this::buttonChangeDefaultLyricFontOnAction );

		fldTableColumnTitle.setCellValueFactory(
			cellData -> cellData.getValue().titleProperty() );
		fldTableColumnTag.setCellValueFactory(
			cellData -> cellData.getValue().tagProperty() );
		fldTableColumnContent.setCellValueFactory(
			cellData -> cellData.getValue().contentProperty() );
	}


	private void tableViewOnWidthChanged(
		ObservableValue<? extends Number> observable, Number oldValue,
		Number newValue )
	{
		fldTableColumnTitle.setPrefWidth( newValue.doubleValue() * 0.35 );
		fldTableColumnTag.setPrefWidth( newValue.doubleValue() * 0.10 );
		fldTableColumnContent.setPrefWidth( newValue.doubleValue() * 0.52 );
	}

	private void tableViewOnSelectionChanged(
		ObservableValue<? extends LiveItem> observable, LiveItem oldValue,
		LiveItem newValue )
	{
		if( newValue == null )
		{
			return;
		}

		newValue.displayInProjector();
	}

	private TableRow<LiveItem> tableViewRowFactory(
		TableView<LiveItem> liveItemTableView )
	{
		return new TableRow<LiveItem>()
		{
			protected void updateItem( LiveItem item, boolean empty )
			{
				super.updateItem( item, empty );

				if( item == null || item == fldEmptyLiveItem || empty )
				{
					return;
				}

				changeChildrenStyleOnMatch( item, getChildren() );
			}
		};
	}

	private void tableViewOnKeyPressed( KeyEvent keyEvent )
	{
		KeyCode
			locCode = keyEvent.getCode();
		int
			locSelectedIndex = fldTableView.getSelectionModel().getSelectedIndex();

		if( locCode == KeyCode.LEFT || locCode == KeyCode.KP_LEFT )
		{
			for( int locIndex = locSelectedIndex - 1; locIndex >= 0; locIndex-- )
			{
				if( selectIfItemIsEmpty( locIndex ) )
				{
					return;
				}
			}
		}

		if( locCode == KeyCode.RIGHT || locCode == KeyCode.KP_RIGHT )
		{
			for( int locIndex = locSelectedIndex + 1;
				locIndex < fldTableView.getItems().size(); locIndex++ )
			{
				if( selectIfItemIsEmpty( locIndex ) )
				{
					return;
				}
			}
		}

		findMatchedTag( locSelectedIndex, locCode );
	}

	private void toggleSwitchVideoBackgroundOnChanged( Observable observable )
	{
		if( fldToggleSwitchVideoBackground.isSelected() )
		{
			fldProjectionScreen.getVideoBackground().show();
			return;
		}

		fldProjectionScreen.getVideoBackground().hide();
	}

	private void toggleSwitchImageBackgroundOnChanged( Observable observable )
	{
		if( fldToggleSwitchImageBackground.isSelected() )
		{
			fldProjectionScreen.getImageBackground().show();
			return;
		}

		fldProjectionScreen.getImageBackground().hide();
	}

	private void buttonChangeVideoBackgroundOnAction( ActionEvent actionEvent )
	{
		fldFileChooser.getExtensionFilters().clear();
		fldFileChooser.getExtensionFilters().setAll(
			new FileChooser.ExtensionFilter( VIDEO_FILE_FILTER_DESCRIPTION,
				fldVideoFileExtension ) );
		File
			locFile = fldFileChooser.showOpenDialog( null );

		if( locFile == null )
		{
			return;
		}

		fldProjectionScreen.getVideoBackground().changeTo( locFile );
	}

	private void buttonChangeImageBackgroundOnAction( ActionEvent actionEvent )
	{
		fldFileChooser.getExtensionFilters().clear();
		fldFileChooser.getExtensionFilters().setAll(
			new FileChooser.ExtensionFilter( IMAGE_FILE_FILTER_DESCRIPTION,
				fldImageFileExtension ) );
		File
			locFile = fldFileChooser.showOpenDialog( null );

		if( locFile == null )
		{
			return;
		}

		fldProjectionScreen.getImageBackground().changeTo( locFile );
	}

	private void buttonChangeProjectionMonitorOnAction( ActionEvent actionEvent )
	{
		try
		{
			fldUtilities.displayMonitorSelection();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}

		fldProjectionScreen.moveWindow();
	}

	private void buttonChangeDefaultLyricFontOnAction( ActionEvent actionEvent )
	{
		FontSelectorDialog
			locDialog = new FontSelectorDialog(
			fldProjectionScreen.getDefaultLyricFont() );

		fldUtilities.initializeDialogStyle( locDialog );
		locDialog.showAndWait();

		if( locDialog.getResult() == null || locDialog.getResult().equals(
			fldProjectionScreen.getDefaultLyricFont() ) )
		{
			return;
		}

		fldProjectionScreen.setDefaultLyricFont( new Font(
			locDialog.getResult().getName(),
			fldSetting.getDefaultLyricFontSize() ) );
		fldSetting.setDefaultFontName( locDialog.getResult().getName() );

		for( LiveItem locItem : fldTableView.getItems() )
		{
			locItem.applyNewSetting();
		}
	}


	private void initializeVideoFileExtensionList()
	{
		fldVideoFileExtension = new ArrayList<>();
		fldVideoFileExtension.add( "*.mp4" );
		fldVideoFileExtension.add( "*.flv" );
	}

	private void initializeImageFileExtensionList()
	{
		fldImageFileExtension = new ArrayList<>();
		fldImageFileExtension.add( "*.jpg" );
		fldImageFileExtension.add( "*.jpeg" );
		fldImageFileExtension.add( "*.gif" );
		fldImageFileExtension.add( "*.bmp" );
	}

	private void changeChildrenStyleOnMatch( LiveItem item,
		ObservableList<Node> children )
	{
		for( int locIndex = 0; locIndex < fldPattern.length; locIndex++ )
		{
			if( fldUtilities.matchPattern( item, fldPattern[ locIndex ] ) )
			{
				fldUtilities.changeChildrenStyle( children, fldStyle[ locIndex ] );
				return;
			}
		}

		fldUtilities.changeChildrenStyle( children, EMPTY_STRING );
	}

	private boolean selectIfItemIsEmpty( int index )
	{
		if( fldTableView.getItems().get( index ) == fldEmptyLiveItem )
		{
			tableViewSelectAndScrollTo( index );
			return true;
		}

		return false;
	}

	private void tableViewSelectAndScrollTo( int index )
	{
		loadVirtualFlow();
		fldTableView.getSelectionModel().select( index );

		if( index >= fldVirtualFlow.getFirstVisibleCell().getIndex() &&
			index <= fldVirtualFlow.getLastVisibleCell().getIndex() )
		{
			return;
		}

		fldTableView.scrollTo( index );
	}

	private void loadVirtualFlow()
	{
		if( fldVirtualFlow == null )
		{
			fldVirtualFlow = (VirtualFlow) ( (TableViewSkin)
				fldTableView.getSkin() ).getChildren().get( 1 );
		}
	}

	private void findMatchedTag( int selectedIndex, KeyCode keyCode )
	{
		for( int locIndex = selectedIndex + 1;
			locIndex < fldTableView.getItems().size() &&
				fldTableView.getItems().get( locIndex ) != fldEmptyLiveItem;
			locIndex++ )
		{
			if( findMatchedTag( keyCode, locIndex ) )
			{
				return;
			}
		}

		int
			locIndex = selectedIndex - 1;

		// TODO : goto start of song, then find first match
		while( locIndex >= 0 &&
			fldTableView.getItems().get( locIndex ) != fldEmptyLiveItem )
		{
			locIndex--;
		}

		for( locIndex++; locIndex < fldTableView.getItems().size() &&
			fldTableView.getItems().get( locIndex ) != fldEmptyLiveItem;
			locIndex++ )
		{
			if( findMatchedTag( keyCode, locIndex ) )
			{
				return;
			}
		}
	}

	private boolean findMatchedTag( KeyCode keyCode, int index )
	{
		LiveItem
			locItem = fldTableView.getItems().get( index );

		if( ( keyCode == KeyCode.DIGIT1 || keyCode == KeyCode.NUMPAD1 ) &&
			fldUtilities.matchPattern( locItem, fldPattern[ VERSE_ONE ] ) )
		{
			tableViewSelectAndScrollTo( index );
			return true;
		}

		if( ( keyCode == KeyCode.DIGIT2 || keyCode == KeyCode.NUMPAD2 ) &&
			fldUtilities.matchPattern( locItem, fldPattern[ VERSE_TWO ] ) )
		{
			tableViewSelectAndScrollTo( index );
			return true;
		}

		if( ( keyCode == KeyCode.DIGIT3 || keyCode == KeyCode.NUMPAD3 ) &&
			fldUtilities.matchPattern( locItem, fldPattern[ VERSE_THREE ] ) )
		{
			tableViewSelectAndScrollTo( index );
			return true;
		}

		if( ( keyCode == KeyCode.DIGIT4 || keyCode == KeyCode.NUMPAD4 ) &&
			fldUtilities.matchPattern( locItem, fldPattern[ VERSE_FOUR ] ) )
		{
			tableViewSelectAndScrollTo( index );
			return true;
		}

		if( keyCode == KeyCode.V &&
			fldUtilities.matchPattern( locItem, fldPattern[ VERSE ] ) )
		{
			tableViewSelectAndScrollTo( index );
			return true;
		}

		if( keyCode == KeyCode.B &&
			fldUtilities.matchPattern( locItem, fldPattern[ BRIDGE ] ) )
		{
			tableViewSelectAndScrollTo( index );
			return true;
		}

		if( keyCode == KeyCode.E &&
			fldUtilities.matchPattern( locItem, fldPattern[ ENDING ] ) )
		{
			tableViewSelectAndScrollTo( index );
			return true;
		}

		if( keyCode == KeyCode.P &&
			fldUtilities.matchPattern( locItem, fldPattern[ PRE ] ) )
		{
			tableViewSelectAndScrollTo( index );
			return true;
		}

		if( ( keyCode == KeyCode.C || keyCode == KeyCode.R ) &&
			fldUtilities.matchPattern( locItem, fldPattern[ CHORUS ] ) )
		{
			tableViewSelectAndScrollTo( index );
			return true;
		}

		return false;
	}
}
