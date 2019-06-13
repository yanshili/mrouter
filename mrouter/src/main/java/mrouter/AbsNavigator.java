package mrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * 作者：mooney
 * 日期：2019/6/13
 * 描述：
 */

public abstract class AbsNavigator implements INavigator {

    @Override
    public void startActivity(Context context, Class activityClz, RouterMeta routerMeta) {
        Intent intent = new Intent();
        intent.setClass(context, activityClz);
        if (routerMeta != null) {
            intent.putExtras(routerMeta.getExtras());
        }
        context.startActivity(intent);
    }

    @Override
    public void startActivityForResult(Activity activity, Class activityClz, RouterMeta routerMeta) {
        Intent intent = new Intent();
        intent.setClass(activity, activityClz);
        int requestCode = 0;
        if (routerMeta != null) {
            intent.putExtras(routerMeta.getExtras());
            requestCode = routerMeta.getRequestCode() != null ? routerMeta.getRequestCode() : 0;
        }
        activity.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Fragment fragment, Class activityClz, RouterMeta routerMeta) {
        Intent intent = new Intent();
        intent.setClass(fragment.getContext(), activityClz);
        int requestCode = 0;
        if (routerMeta != null) {
            intent.putExtras(routerMeta.getExtras());
            requestCode = routerMeta.getRequestCode() != null ? routerMeta.getRequestCode() : 0;
        }
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(android.app.Fragment fragment, Class activityClz, RouterMeta routerMeta) {
        Intent intent = new Intent();
        intent.setClass(fragment.getActivity(), activityClz);
        int requestCode = 0;
        if (routerMeta != null) {
            intent.putExtras(routerMeta.getExtras());
            requestCode = routerMeta.getRequestCode() != null ? routerMeta.getRequestCode() : 0;
        }
        fragment.startActivityForResult(intent, requestCode);
    }
}
