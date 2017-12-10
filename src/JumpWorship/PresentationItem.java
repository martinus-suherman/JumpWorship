package JumpWorship;

import java.util.List;

/**
 * Created by Martinus on 7/19/2016.
 */
public interface PresentationItem
{
	ItemType getItemType();

	int getId();

	String getTitle();

	List<LiveItem> getLiveItems();

	void parse();
}
