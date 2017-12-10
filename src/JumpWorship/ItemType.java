package JumpWorship;

/**
 * Created by Martinus on 7/8/2016.
 */
enum ItemType
{
	Song,
	VideoBackground,
	ImageBackground;

	private static final ItemType[]
		fldItemTypeValues = ItemType.values();

	public static ItemType fromInteger( int value )
		throws ArrayIndexOutOfBoundsException
	{
		return fldItemTypeValues[ value ];
	}
}