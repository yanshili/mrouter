package mrouter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;

import java.util.Set;

/**
 * 作者：mooney
 * 日期：2019/6/13
 * 描述：
 */

public class Router {

    public static RouterMeta build(String routerOrCanonicalName) {
        return new Router(routerOrCanonicalName, null, null).mRouterMeta;
    }

    public static RouterMeta build(String routerOrCanonicalName, RouterMeta.Serializer serializer) {
        return new Router(routerOrCanonicalName, serializer, null).mRouterMeta;
    }

    public static RouterMeta build(String routerOrCanonicalName, RouterMeta.Serializer serializer, INavigator navigator) {
        return new Router(routerOrCanonicalName, serializer, navigator).mRouterMeta;
    }

    public static void config(Context context, INavigator iNavigator, RouterMeta.Serializer serializer) {
        sNavigator = iNavigator;
        sSerializer = serializer;
        Mrouter.getInstance().init(context);
    }

    private Router(String routerOrCanonicalName, RouterMeta.Serializer serializer, INavigator navigator) {
        mSerializer = serializer;
        mNavigator = navigator;
        mRouterMeta = new RouterMeta(this);
        mRouterMeta.setRouterUri(routerOrCanonicalName);

        if (MrouterHelper.isValidURI(routerOrCanonicalName)) {
            Uri uri = Uri.parse(routerOrCanonicalName);
            Set<String> queryParameterNames = uri.getQueryParameterNames();
            if (queryParameterNames != null && queryParameterNames.size() > 0) {
                for (String key : queryParameterNames) {
                    mRouterMeta.withString(key, uri.getQueryParameter(key));
                }
            }
            mClass = Mrouter.getInstance().getFragmentClz(routerOrCanonicalName);
        } else {
            try {
                mClass = Class.forName(routerOrCanonicalName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public RouterMeta.Serializer getSerializer() {
        if (mSerializer != null) return mSerializer;
        return sSerializer;
    }

    public INavigator getNavigator() {
        if (mNavigator != null) return mNavigator;
        return sNavigator;
    }

    private static INavigator sNavigator;
    private static RouterMeta.Serializer sSerializer;
    private INavigator mNavigator;
    private RouterMeta.Serializer mSerializer;
    private Class mClass;
    private RouterMeta mRouterMeta;

    public Class start(Context context) {
        if (Activity.class.isAssignableFrom(mClass)) {
            getNavigator().startActivity(context, mClass, mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(mClass) || android.app.Fragment.class.isAssignableFrom(mClass)) {
            getNavigator().startFragment(context, mClass, mRouterMeta);
        }
        return mClass;
    }

    public Class startForResult(Fragment fragment) {
        if (Activity.class.isAssignableFrom(mClass)) {
            getNavigator().startActivityForResult(fragment, mClass, mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(mClass) || android.app.Fragment.class.isAssignableFrom(mClass)) {
            getNavigator().startFragmentForResult(fragment, mClass, mRouterMeta);
        }
        return mClass;
    }

    public Class startForResult(android.app.Fragment fragment) {
        if (Activity.class.isAssignableFrom(mClass)) {
            getNavigator().startActivityForResult(fragment, mClass, mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(mClass) || android.app.Fragment.class.isAssignableFrom(mClass)) {
            getNavigator().startFragmentForResult(fragment, mClass, mRouterMeta);
        }
        return mClass;
    }

    public Class startForResult(Activity activity) {
        if (Activity.class.isAssignableFrom(mClass)) {
            getNavigator().startActivityForResult(activity, mClass, mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(mClass) || android.app.Fragment.class.isAssignableFrom(mClass)) {
            getNavigator().startFragmentForResult(activity, mClass, mRouterMeta);
        }
        return mClass;
    }


}
