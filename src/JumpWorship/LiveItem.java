package JumpWorship;

import javafx.beans.property.StringProperty;

/**
 * Created by Martinus on 7/8/2016.
 */
public interface LiveItem
{
	String getTitle();

	StringProperty titleProperty();

	String getTag();

	StringProperty tagProperty();

	String getContent();

	StringProperty contentProperty();

	void displayInProjector();

	void applyNewSetting();
}
