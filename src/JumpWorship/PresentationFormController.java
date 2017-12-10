package JumpWorship;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.WindowEvent;
import org.controlsfx.control.ToggleSwitch;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by Martinus on 7/7/2016.
 */
public class PresentationFormController implements Initializable
{
	private static final String
		EMPTY_STRING = "";
	private static final LiveItem
		fldEmptyLiveItem = new EmptyLiveItem();
	@FXML
	private TabPane fldTabPanePresentation;
	@FXML
	private AnchorPane fldAnchorPaneSongManager;
	@FXML
	private AnchorPane fldAnchorPaneSongEditor;
	@FXML
	private AnchorPane fldAnchorPanePresentationEditor;
	@FXML
	private AnchorPane fldAnchorPaneLive;
	@FXML
	private TableView<Song> fldTableViewSong;
	@FXML
	private TableColumn<Song, String> fldTableColumnSongTitle;
	@FXML
	private TableColumn<Song, String> fldTableColumnSongLyrics;
	@FXML
	private TableView<LiveItem> fldTableViewLive;
	@FXML
	private TableColumn<LiveItem, String> fldTableColumnLiveTitle;
	@FXML
	private TableColumn<LiveItem, String> fldTableColumnLiveTag;
	@FXML
	private TableColumn<LiveItem, String> fldTableColumnLiveContent;
	@FXML
	private ListView<Presentation> fldListViewPresentation;
	@FXML
	private ListView<PresentationItem> fldListViewPresentationItem;
	@FXML
	private TextField fldTextFieldSongTitle;
	@FXML
	private TextField fldTextFieldSearchSong;
	@FXML
	private TextArea fldTextAreaSongLyrics;
	@FXML
	private Button fldButtonNewSong;
	@FXML
	private Button fldButtonSaveSong;
	@FXML
	private Button fldButtonDeleteSong;
	@FXML
	private Button fldButtonAddToPresentation;
	@FXML
	private Button fldButtonNewPresentation;
	@FXML
	private Button fldButtonDeletePresentation;
	@FXML
	private Button fldButtonDeletePresentationItem;
	@FXML
	private Button fldButtonMoveItemUp;
	@FXML
	private Button fldButtonMoveItemDown;
	@FXML
	private Button fldButtonSavePresentationItemList;
	@FXML
	private Button fldButtonChangeVideoBackground;
	@FXML
	private Button fldButtonChangeImageBackground;
	@FXML
	private Button fldButtonChangeProjectionMonitor;
	@FXML
	private Button fldButtonChangeDefaultLyricFont;
	@FXML
	private ToggleSwitch fldToggleSwitchVideoBackground;
	@FXML
	private ToggleSwitch fldToggleSwitchImageBackground;

	private PresentationFormDataHelper
		fldDataHelper = new PresentationFormDataHelper();
	private Utilities
		fldUtilities = new Utilities();
	private Song
		fldCurrentEditedSong;
	private Presentation
		fldCurrentPresentation;
	private ObservableList<Song>
		fldMasterList;
	private FilteredList<Song>
		fldFilteredList;
	private ObservableList<Presentation>
		fldPresentationList;
	private ObservableList<PresentationItem>
		fldPresentationItemList;
	private PresentationLiveHelper
		fldLiveHelper;
	private boolean
		fldOnSelectionChanged;

	@Override
	public void initialize( URL location, ResourceBundle resources )
	{
		fldLiveHelper = new PresentationLiveHelper( fldTableViewLive,
			fldTableColumnLiveTitle, fldTableColumnLiveTag,
			fldTableColumnLiveContent, fldToggleSwitchVideoBackground,
			fldToggleSwitchImageBackground, fldButtonChangeVideoBackground,
			fldButtonChangeImageBackground, fldButtonChangeProjectionMonitor,
			fldButtonChangeDefaultLyricFont, fldEmptyLiveItem );
		tableViewSongSetData();
		listViewPresentationSetData();
		tableViewLiveSetData();
		attachEventHandlers();
		songControlSetDisable();
		buttonDeletePresentationSetDisable();
		presentationItemControlSetDisable( true );
	}

	void onFormClosed( WindowEvent windowEvent )
	{
		fldDataHelper.closeConnection();
	}

	private void attachEventHandlers()
	{
		fldLiveHelper.attachEventHandlers();
		fldTabPanePresentation.widthProperty().addListener(
			this::tabPanePresentationOnWidthChanged );
		fldTableViewSong.getSelectionModel().selectedItemProperty().addListener(
			this::tableViewSongOnSelectionChanged );
		fldTableViewSong.widthProperty().addListener(
			this::tableViewSongOnWidthChanged );
		fldListViewPresentation.getSelectionModel().selectedItemProperty().
			addListener( this::listViewPresentationOnSelectionChanged );
		fldListViewPresentationItem.getSelectionModel().selectedItemProperty().
			addListener( this::listViewPresentationItemOnSelectionChanged );
		fldTextAreaSongLyrics.textProperty().addListener(
			this::onSongDataChanged );
		fldTextFieldSongTitle.textProperty().addListener(
			this::onSongDataChanged );
		fldButtonNewSong.setOnAction( this::buttonNewSongOnAction );
		fldButtonSaveSong.setOnAction( this::buttonSaveSongOnAction );
		fldButtonDeleteSong.setOnAction( this::buttonDeleteSongOnAction );
		fldButtonAddToPresentation.setOnAction(
			this::buttonAddToPresentationOnAction );
		fldButtonNewPresentation.setOnAction(
			this::buttonNewPresentationOnAction );
		fldButtonDeletePresentation.setOnAction(
			this::buttonDeletePresentationOnAction );
		fldButtonDeletePresentationItem.setOnAction(
			this::buttonDeletePresentationItemOnAction );
		fldButtonSavePresentationItemList.setOnAction(
			this::buttonSavePresentationItemListOnAction );
		fldButtonMoveItemUp.setOnAction( this::buttonMoveItemUpOnAction );
		fldButtonMoveItemDown.setOnAction( this::buttonMoveItemDownOnAction );

		fldTableColumnSongTitle.setCellValueFactory(
			cellData -> cellData.getValue().titleProperty() );
		fldTableColumnSongLyrics.setCellValueFactory(
			cellData -> cellData.getValue().lyricsProperty() );

		fldListViewPresentation.setCellFactory(
			this::presentationSetCellFactory );
		fldListViewPresentationItem.setCellFactory(
			this::presentationItemSetCellFactory );
	}

	private ListCell<Presentation> presentationSetCellFactory(
		ListView<Presentation> listView )
	{
		return new ListCell<Presentation>()
		{
			protected void updateItem( Presentation item, boolean empty )
			{
				super.updateItem( item, empty );

				if( item != null )
				{
					setText( item.getTitle() );
					return;
				}

				setText( EMPTY_STRING );
			}
		};
	}

	private ListCell<PresentationItem> presentationItemSetCellFactory(
		ListView<PresentationItem> listView )
	{
		return new ListCell<PresentationItem>()
		{
			protected void updateItem( PresentationItem item, boolean empty )
			{
				super.updateItem( item, empty );

				if( item != null )
				{
					setText( item.getTitle() );
					return;
				}

				setText( EMPTY_STRING );
			}
		};
	}

	private void tableViewSongSetData()
	{
		fldDataHelper.loadSongList();
		fldMasterList = fldDataHelper.getSongList();
		fldFilteredList = new FilteredList<>( fldMasterList, p -> true );
		fldTextFieldSearchSong.textProperty().addListener(
			this::textFieldSearchSongOnTextChanged );

		SortedList<Song>
			locSortedList = new SortedList<>( fldFilteredList );

		locSortedList.comparatorProperty().bind(
			fldTableViewSong.comparatorProperty() );
		fldTableViewSong.setItems( locSortedList );
	}

	private void tableViewLiveSetData()
	{
		ObservableList<LiveItem>
			locLiveItemList = FXCollections.observableArrayList();
		fldTableViewLive.setItems( locLiveItemList );
		addEmptyLiveItem();
	}

	private void onSongDataChanged( ObservableValue<? extends String> observable,
		String oldValue, String newValue )
	{
		if( fldOnSelectionChanged )
		{
			return;
		}

		fldButtonSaveSong.setDisable( false );
	}

	private void textFieldSearchSongOnTextChanged(
		ObservableValue<? extends String> observable, String oldValue,
		String newValue )
	{
		fldFilteredList.setPredicate( song ->
		{
			if( newValue == null || newValue.isEmpty() )
			{
				return true;
			}

			String
				locSearched = newValue.toLowerCase();

			return song.getTitle().toLowerCase().contains( locSearched ) ||
				song.getLyrics().toLowerCase().contains( locSearched );
		} );
	}

	private void tabPanePresentationOnWidthChanged(
		ObservableValue<? extends Number> observable, Number oldValue,
		Number newValue )
	{
		fldAnchorPaneSongManager.setPrefWidth( newValue.doubleValue() * 0.44 );
		fldAnchorPaneSongEditor.setPrefWidth( newValue.doubleValue() * 0.34 );
		fldAnchorPanePresentationEditor.setPrefWidth(
			newValue.doubleValue() * 0.22 );
		fldAnchorPaneLive.setPrefWidth( newValue.doubleValue() - 200 );
	}

	private void tableViewSongOnWidthChanged(
		ObservableValue<? extends Number> observable, Number oldValue,
		Number newValue )
	{
		fldTableColumnSongTitle.setPrefWidth( newValue.doubleValue() * 0.36 );
		fldTableColumnSongLyrics.setPrefWidth( newValue.doubleValue() * 0.60 );
	}

	private void tableViewSongOnSelectionChanged(
		ObservableValue<? extends Song> observable, Song oldValue, Song newValue )
	{
		if( newValue == null )
		{
			return;
		}

		fldOnSelectionChanged = true;
		displaySelectedSong( newValue );
		fldOnSelectionChanged = false;
	}

	private void buttonSaveSongOnAction( ActionEvent actionEvent )
	{
		fldCurrentEditedSong.setTitle( fldTextFieldSongTitle.getText() );
		fldCurrentEditedSong.setLyrics( fldTextAreaSongLyrics.getText() );

		if( !fldMasterList.contains( fldCurrentEditedSong ) )
		{
			fldMasterList.add( fldCurrentEditedSong );
		}

		fldDataHelper.saveSong( fldCurrentEditedSong );
		fldTableViewSong.refresh();
		fldTableViewSong.getSelectionModel().select( fldCurrentEditedSong );
		fldTableViewSong.scrollTo( fldCurrentEditedSong );
		fldButtonSaveSong.setDisable( true );
		fldButtonNewSong.setDisable( false );
		fldTextAreaSongLyrics.requestFocus();
		songControlSetDisable();

		if( !fldCurrentEditedSong.getLiveItems().isEmpty() )
		{
			fldCurrentEditedSong.parse();
			rebuildLiveItemList();
		}
	}

	private void buttonDeleteSongOnAction( ActionEvent actionEvent )
	{
		Song
			locDeleted = fldTableViewSong.getSelectionModel().getSelectedItem();

		fldDataHelper.deleteSong( locDeleted );
		fldMasterList.remove( locDeleted );
		songControlSetDisable();
	}

	private void buttonNewSongOnAction( ActionEvent actionEvent )
	{
		createNewSong();
	}

	private void listViewPresentationSetData()
	{
		fldPresentationList = fldDataHelper.loadPresentationList();
		fldListViewPresentation.setItems( fldPresentationList );
	}

	private void listViewPresentationOnSelectionChanged(
		ObservableValue<? extends Presentation> observable,
		Presentation oldValue, Presentation newValue )
	{
		if( newValue == null )
		{
			return;
		}

		fldCurrentPresentation = newValue;
		fldPresentationItemList =
			fldCurrentPresentation.getPresentationItemList();
		fldListViewPresentationItem.setItems( fldPresentationItemList );
		rebuildLiveItemList();
		presentationItemControlSetDisable( true );
		buttonDeletePresentationSetDisable();
	}

	private void listViewPresentationItemOnSelectionChanged(
		ObservableValue<? extends PresentationItem> observable,
		PresentationItem oldValue, PresentationItem newValue )
	{
		if( newValue == null )
		{
			return;
		}

		fldButtonMoveItemUp.setDisable( fldPresentationItemList.size() == 1 ||
			fldPresentationItemList.indexOf( newValue ) <= 0 );
		fldButtonMoveItemDown.setDisable( fldPresentationItemList.size() == 1 ||
			fldPresentationItemList.indexOf( newValue ) >=
				fldPresentationItemList.size() - 1 );
		fldButtonDeletePresentationItem.setDisable(
			fldPresentationItemList.isEmpty() ||
				fldListViewPresentationItem.getSelectionModel().isEmpty() );

		if( newValue.getItemType() == ItemType.Song )
		{
			displaySelectedSong( (Song) newValue );
		}
	}

	private void buttonAddToPresentationOnAction( ActionEvent actionEvent )
	{
		if( fldPresentationList.isEmpty() ||
			fldListViewPresentation.getSelectionModel().isEmpty() )
		{
			return;
		}

		Song
			locSelected = fldTableViewSong.getSelectionModel().getSelectedItem();

		if( locSelected.getLiveItems().isEmpty() )
		{
			locSelected.parse();
		}

		fldPresentationItemList.add( locSelected );
		fldTableViewLive.getItems().addAll( locSelected.getLiveItems() );
		fldTableViewLive.getItems().add( fldEmptyLiveItem );
		presentationControlSetDisable( false );
	}

	private void buttonNewPresentationOnAction( ActionEvent actionEvent )
	{
		TextInputDialog
			locDialog = new TextInputDialog( EMPTY_STRING );

		locDialog.setTitle( "New Presentation" );
		locDialog.setContentText( "Presentation title : " );
		locDialog.setHeaderText( EMPTY_STRING );
		fldUtilities.initializeDialogStyle( locDialog );

		Optional<String>
			locResult = locDialog.showAndWait();

		if( locResult.isPresent() )
		{
			fldCurrentPresentation = new Presentation();
			fldCurrentPresentation.setTitle( locResult.get() );
			fldDataHelper.savePresentationHeader( fldCurrentPresentation );
			fldPresentationList.add( fldCurrentPresentation );
		}

		buttonDeletePresentationSetDisable();
	}

	private void buttonDeletePresentationOnAction( ActionEvent actionEvent )
	{
		Presentation
			locDeleted =
			fldListViewPresentation.getSelectionModel().getSelectedItem();

		fldDataHelper.deletePresentation( locDeleted );
		fldPresentationList.remove( locDeleted );
		buttonDeletePresentationSetDisable();
	}

	private void buttonDeletePresentationItemOnAction( ActionEvent actionEvent )
	{
		if( fldPresentationList.isEmpty() ||
			fldListViewPresentation.getSelectionModel().isEmpty() )
		{
			return;
		}

		fldPresentationItemList.remove(
			fldListViewPresentationItem.getSelectionModel().getSelectedItem() );
		rebuildLiveItemList();
		presentationControlSetDisable( false );
		presentationItemControlSetDisable( fldPresentationItemList.isEmpty() );
	}

	private void buttonSavePresentationItemListOnAction(
		ActionEvent actionEvent )
	{
		fldDataHelper.deletePresentationDetail( fldCurrentPresentation );
		fldDataHelper.savePresentationDetail( fldCurrentPresentation );
		presentationControlSetDisable( true );
	}

	private void buttonMoveItemUpOnAction( ActionEvent actionEvent )
	{
		if( !movePresentationItem( -1 ) )
		{
			return;
		}

		rebuildLiveItemList();
		presentationControlSetDisable( false );
	}

	private void buttonMoveItemDownOnAction( ActionEvent actionEvent )
	{
		if( !movePresentationItem( 1 ) )
		{
			return;
		}

		rebuildLiveItemList();
		presentationControlSetDisable( false );
	}

	private void createNewSong()
	{
		songInputControlSetDisable( false );
		fldCurrentEditedSong = new Song();
		displaySong();
		fldButtonNewSong.setDisable( true );
		fldTextFieldSongTitle.requestFocus();
	}

	private void displaySelectedSong( Song song )
	{
		fldCurrentEditedSong = song;
		displaySong();
		fldButtonNewSong.setDisable( false );
		songInputControlSetDisable( false );
		songControlSetDisable();
	}

	private void displaySong()
	{
		fldTextFieldSongTitle.textProperty().set(
			fldCurrentEditedSong.getTitle() );
		fldTextAreaSongLyrics.textProperty().set(
			fldCurrentEditedSong.getLyrics() );
		fldButtonSaveSong.setDisable( true );
	}

	private void songControlSetDisable()
	{
		boolean
			locDisable = fldMasterList.isEmpty() ||
			fldTableViewSong.getSelectionModel().isEmpty();

		fldButtonDeleteSong.setDisable( locDisable );
		fldButtonAddToPresentation.setDisable( locDisable );
	}

	private void buttonDeletePresentationSetDisable()
	{
		fldButtonDeletePresentation.setDisable( fldPresentationList.isEmpty() ||
			fldListViewPresentation.getSelectionModel().isEmpty() );
	}

	private void songInputControlSetDisable( boolean value )
	{
		fldTextFieldSongTitle.setDisable( value );
		fldTextAreaSongLyrics.setDisable( value );
	}

	private void presentationControlSetDisable( boolean value )
	{
		fldButtonSavePresentationItemList.setDisable( value );
		fldListViewPresentation.setDisable( !value );
		fldButtonNewPresentation.setDisable( !value );
		fldButtonDeletePresentation.setDisable( !value );
	}

	private void presentationItemControlSetDisable( boolean value )
	{
		fldButtonDeletePresentationItem.setDisable( value );
		fldButtonMoveItemUp.setDisable( value );
		fldButtonMoveItemDown.setDisable( value );
	}

	private boolean movePresentationItem( int delta )
	{
		int
			locIndex =
			fldListViewPresentationItem.getSelectionModel().getSelectedIndex();

		if( locIndex < 0 || locIndex > fldPresentationItemList.size() )
		{
			return false;
		}

		PresentationItem
			locItem = fldPresentationItemList.remove( locIndex );

		fldPresentationItemList.add( locIndex + delta, locItem );
		fldListViewPresentationItem.getSelectionModel().select(
			locIndex + delta );
		return true;
	}

	private void addEmptyLiveItem()
	{
		fldTableViewLive.getItems().add( fldEmptyLiveItem );
	}

	private void rebuildLiveItemList()
	{
		fldTableViewLive.getItems().clear();
		addEmptyLiveItem();

		for( PresentationItem locItem : fldPresentationItemList )
		{
			if( locItem.getLiveItems().isEmpty() )
			{
				locItem.parse();
			}

			fldTableViewLive.getItems().addAll( locItem.getLiveItems() );
			addEmptyLiveItem();
		}
	}
}
