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
        return build(routerOrCanonicalName, null, null, null);
    }

    public static RouterMeta build(String routerOrCanonicalName, RouterMeta.Serializer serializer) {
        return build(routerOrCanonicalName, serializer, null, null);
    }

    public static RouterMeta build(String routerOrCanonicalName, RouterMeta.Serializer serializer, INavigator navigator) {
        return build(routerOrCanonicalName, serializer, navigator, null);
    }

    public static RouterMeta build(String routerOrCanonicalName, RouterMeta.Serializer serializer, INavigator navigator, IWebChecker webChecker) {
        return new Router(routerOrCanonicalName, serializer, navigator, webChecker).mRouterMeta;
    }

    public static void init(Context context, INavigator iNavigator, RouterMeta.Serializer serializer) {
        init(context, iNavigator, serializer, new IWebChecker() {
            @Override
            public boolean isWebMode(RouterMeta meta) {
                String routerUri = meta == null ? null : meta.getRouterUri();
                return routerUri != null && (routerUri.startsWith("http://") || routerUri.startsWith("https://"));
            }
        });
    }

    public static void init(Context context, INavigator iNavigator, RouterMeta.Serializer serializer, IWebChecker webChecker) {
        sNavigator = iNavigator;
        sSerializer = serializer;
        sWebChecker = webChecker;
        Mrouter.getInstance().init(context);
    }

    private Router(String routerOrCanonicalName, RouterMeta.Serializer serializer, INavigator navigator, IWebChecker webChecker) {
        mSerializer = serializer;
        mNavigator = navigator;
        mWebChecker = webChecker;
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
            if (!isWeb()) {
                mClass = Mrouter.getInstance().get(routerOrCanonicalName);
            }
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

    public IWebChecker getWebChecker() {
        if (mWebChecker != null) return mWebChecker;
        return sWebChecker;
    }

    private static INavigator sNavigator;
    private static RouterMeta.Serializer sSerializer;
    private static IWebChecker sWebChecker;
    private INavigator mNavigator;
    private RouterMeta.Serializer mSerializer;
    private IWebChecker mWebChecker;
    private Class mClass;
    private RouterMeta mRouterMeta;

    private boolean isWeb() {
        return getWebChecker().isWebMode(mRouterMeta);
    }

    public Class start(Context context) {
        if (isWeb()) {
            getNavigator().startWeb(context, mRouterMeta);
        }
        if (mClass == null) {
            return null;
        }
        if (Activity.class.isAssignableFrom(mClass)) {
            getNavigator().startActivity(context, mClass, mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(mClass) || android.app.Fragment.class.isAssignableFrom(mClass)) {
            getNavigator().startFragment(context, mClass, mRouterMeta);
        }
        return mClass;
    }

    public Class startForResult(Fragment fragment) {
        if (isWeb()) {
            getNavigator().startWebForResult(fragment, mRouterMeta);
            return null;
        }
        if (mClass == null) {
            return null;
        }
        if (Activity.class.isAssignableFrom(mClass)) {
            getNavigator().startActivityForResult(fragment, mClass, mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(mClass) || android.app.Fragment.class.isAssignableFrom(mClass)) {
            getNavigator().startFragmentForResult(fragment, mClass, mRouterMeta);
        }
        return mClass;
    }

    public Class startForResult(android.app.Fragment fragment) {
        if (isWeb()) {
            getNavigator().startWebForResult(fragment, mRouterMeta);
            return null;
        }
        if (mClass == null) {
            return null;
        }
        if (Activity.class.isAssignableFrom(mClass)) {
            getNavigator().startActivityForResult(fragment, mClass, mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(mClass) || android.app.Fragment.class.isAssignableFrom(mClass)) {
            getNavigator().startFragmentForResult(fragment, mClass, mRouterMeta);
        }
        return mClass;
    }

    public Class startForResult(Activity activity) {
        if (isWeb()) {
            getNavigator().startWebForResult(activity, mRouterMeta);
            return null;
        }
        if (mClass == null) {
            return null;
        }
        if (Activity.class.isAssignableFrom(mClass)) {
            getNavigator().startActivityForResult(activity, mClass, mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(mClass) || android.app.Fragment.class.isAssignableFrom(mClass)) {
            getNavigator().startFragmentForResult(activity, mClass, mRouterMeta);
        }
        return mClass;
    }


    public interface IWebChecker {
        boolean isWebMode(RouterMeta meta);
    }
}
