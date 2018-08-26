package net.runelite.client.util;

import com.thizzer.jtouchbar.common.Image;
import com.thizzer.jtouchbar.item.view.TouchBarButton;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Provides operations for the Touch Bar on macOS.
 */
public class TouchBarUtil
{
	static final String[] touchBarButtonNames =
			{
					"combat_options", "stats", "quests",
					"worn_equipment", "prayer", "spellbook", "clan_chat",
					"friends_list", "ignore_list", "options", "emotes", "music"
			};

	private static String controlStripPreferences;

	public static void hideControlStrip()
	{
		if (controlStripPreferences == null)
		{
			String[] recordControlStripScript = new String[]
					{ "/bin/sh", "runelite-client/src/main/scripts/recordControlStripPrefs.sh" };
			controlStripPreferences = ScriptRunner.shell(recordControlStripScript);
		}

		String[] hideControlStripScript = new String[]
				{ "/bin/sh", "runelite-client/src/main/scripts/hideControlStrip.sh" };
		ScriptRunner.shell(hideControlStripScript);
	}

	public static void restoreControlStrip()
	{
		String strippedPrefsString = controlStripPreferences.replaceAll("\n", "").replaceAll("\"", "");
		String finalPrefsString = "'" + strippedPrefsString + "'";

		String[] hideControlStripScript = new String[]
				{ "/usr/bin/python", "runelite-client/src/main/scripts/restoreControlStrip.py" , finalPrefsString};
		ScriptRunner.shell(hideControlStripScript);
	}

	public static Image getTouchBarImageResource(String imagePath) throws IOException {
		BufferedImage icon = ImageUtil.getResourceStreamFromClass(TouchBarUtil.class, imagePath);
		ByteArrayOutputStream imageOutputStream = new ByteArrayOutputStream();
		ImageIO.write(icon, "png", imageOutputStream);

		return new Image(imageOutputStream.toByteArray());
	}

	static TouchBarButton createTouchBarButton(String imageName, final int keyCode)
	{
		TouchBarButton button = new TouchBarButton();

		try
		{
			String imagePath = "/interface_icons/" + imageName + ".png";
			button.setImage(getTouchBarImageResource(imagePath));
		} catch (IOException e)
		{
		    // Can't set the touch bar button's image
			e.printStackTrace();
		}

		button.setAction(touchBarView ->
		{
			try
			{
				Robot robot = new Robot();
				robot.keyPress(keyCode);
			}
			catch (AWTException e)
			{
			    // Disregard keypress
				e.printStackTrace();
			}
		});

		return button;
	}
}
