package JumpWorship;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;

public class EmptyLiveItem implements LiveItem
{
	private static final ProjectionScreen
		fldProjectionScreen = new ProjectionScreen();
	private static final Group
		fldGroup = new Group();
	private static final StringProperty
		fldStringProperty = new SimpleStringProperty();

	@Override
	public String getTitle()
	{
		return fldStringProperty.get();
	}

	@Override
	public StringProperty titleProperty()
	{
		return fldStringProperty;
	}

	@Override
	public String getTag()
	{
		return fldStringProperty.get();
	}

	@Override
	public StringProperty tagProperty()
	{
		return fldStringProperty;
	}

	@Override
	public String getContent()
	{
		return fldStringProperty.get();
	}

	@Override
	public StringProperty contentProperty()
	{
		return fldStringProperty;
	}

	@Override
	public void displayInProjector()
	{
		fldProjectionScreen.display( fldGroup );
	}

	@Override
	public void applyNewSetting()
	{

	}
}
