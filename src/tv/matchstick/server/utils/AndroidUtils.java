package tv.matchstick.server.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public final class AndroidUtils {

	public static Intent buildIntent(final Context context, String action,
			String data) {
		Intent intent = new Intent(action);
		intent.putExtra("what", data);
		intent.setPackage(context.getPackageName());
		return intent;
	}

}
