package no.srib.app.client.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

public class DeviceUtil {
	public static DisplayProperties getDisplayProperties(Context context) {
		return new DisplayProperties(context);
	}

	public static class DisplayProperties {
		public final int width;
		public final int height;

		public DisplayProperties(Context context) {
			Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
//			Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
			Point size = new Point();

			if(Build.VERSION.SDK_INT < 13) {
				width = display.getWidth();
				height = display.getHeight();
			}
			else {
				display.getSize(size);
				width = size.x;
				height = size.y;
			}
		}
	}
}
