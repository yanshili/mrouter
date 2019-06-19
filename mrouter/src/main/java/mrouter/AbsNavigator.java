package mrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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

    @Override
    public void startWeb(Context context, RouterMeta routerMeta) {
        Intent intent = new Intent();
        intent.putExtras(routerMeta.getExtras());
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(routerMeta.getRouterUri()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            context.startActivity(intent, routerMeta.getOptions());
        } else {
            context.startActivity(intent);
        }
    }

    @Override
    public void startWebForResult(Activity activity, RouterMeta routerMeta) {
        if (routerMeta == null) return;

        Intent intent = new Intent();
        intent.putExtras(routerMeta.getExtras());
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(routerMeta.getRouterUri()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int requestCode = routerMeta.getRequestCode() != null ? routerMeta.getRequestCode() : 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            activity.startActivityForResult(intent, requestCode, routerMeta.getOptions());
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startWebForResult(Fragment fragment, RouterMeta routerMeta) {
        if (routerMeta == null) return;

        Intent intent = new Intent();
        intent.putExtras(routerMeta.getExtras());
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(routerMeta.getRouterUri()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int requestCode = routerMeta.getRequestCode() != null ? routerMeta.getRequestCode() : 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            fragment.startActivityForResult(intent, requestCode, routerMeta.getOptions());
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void startWebForResult(android.app.Fragment fragment, RouterMeta routerMeta) {
        if (routerMeta == null) return;

        Intent intent = new Intent();
        intent.putExtras(routerMeta.getExtras());
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(routerMeta.getRouterUri()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        int requestCode = routerMeta.getRequestCode() != null ? routerMeta.getRequestCode() : 0;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            fragment.startActivityForResult(intent, requestCode, routerMeta.getOptions());
        } else {
            fragment.startActivityForResult(intent, requestCode);
        }
    }
}
