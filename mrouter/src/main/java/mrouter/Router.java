package mrouter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
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

    public static RouterMeta build(String routerOrCanonicalName, INavigator navigator) {
        return build(routerOrCanonicalName, null, navigator, null);
    }

    public static RouterMeta build(String routerOrCanonicalName, IWebChecker webChecker) {
        return build(routerOrCanonicalName, null, null, webChecker);
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

    private static Map<String, Class> routerCache = new HashMap<>();

    private static boolean inited = false;

    public static boolean isInited(){
        return inited;
    }

    public static void init(Context context, INavigator iNavigator, RouterMeta.Serializer serializer, IWebChecker webChecker) {
        sNavigator = iNavigator;
        sSerializer = serializer;
        sWebChecker = webChecker;
        routerCache = new HashMap<>();
        if (!Mrouter.getInstance().isInited()){
            Mrouter.getInstance().init(context);
        }
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
                mClass = get(routerOrCanonicalName);
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

    public boolean isWeb() {
        return mRouterMeta.isWeb();
    }

    public Class start(Context context) {
        if (isWeb()) {
            getNavigator().startWeb(context, mRouterMeta);
        }
        if (getTargetClass() == null) {
            return null;
        }
        if (Activity.class.isAssignableFrom(getTargetClass())) {
            getNavigator().startActivity(context, getTargetClass(), mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(getTargetClass()) || android.app.Fragment.class.isAssignableFrom(getTargetClass())) {
            getNavigator().startFragment(context, getTargetClass(), mRouterMeta);
        }
        return getTargetClass();
    }

    public Class startForResult(Fragment fragment) {
        if (isWeb()) {
            getNavigator().startWebForResult(fragment, mRouterMeta);
            return null;
        }
        if (getTargetClass() == null) {
            return null;
        }
        if (Activity.class.isAssignableFrom(getTargetClass())) {
            getNavigator().startActivityForResult(fragment, getTargetClass(), mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(getTargetClass()) || android.app.Fragment.class.isAssignableFrom(getTargetClass())) {
            getNavigator().startFragmentForResult(fragment, getTargetClass(), mRouterMeta);
        }
        return getTargetClass();
    }

    public Class startForResult(android.app.Fragment fragment) {
        if (isWeb()) {
            getNavigator().startWebForResult(fragment, mRouterMeta);
            return null;
        }
        if (getTargetClass() == null) {
            return null;
        }
        if (Activity.class.isAssignableFrom(getTargetClass())) {
            getNavigator().startActivityForResult(fragment, getTargetClass(), mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(getTargetClass()) || android.app.Fragment.class.isAssignableFrom(getTargetClass())) {
            getNavigator().startFragmentForResult(fragment, getTargetClass(), mRouterMeta);
        }
        return getTargetClass();
    }

    public Class startForResult(Activity activity) {
        if (isWeb()) {
            getNavigator().startWebForResult(activity, mRouterMeta);
            return null;
        }
        if (getTargetClass() == null) {
            return null;
        }
        if (Activity.class.isAssignableFrom(getTargetClass())) {
            getNavigator().startActivityForResult(activity, getTargetClass(), mRouterMeta);
        } else if (Fragment.class.isAssignableFrom(getTargetClass()) || android.app.Fragment.class.isAssignableFrom(getTargetClass())) {
            getNavigator().startFragmentForResult(activity, getTargetClass(), mRouterMeta);
        }
        return getTargetClass();
    }

    public boolean isValidPageClass() {
        if (isWeb()) {
            return true;
        }
        if (getTargetClass() == null) {
            return false;
        }
        if (Activity.class.isAssignableFrom(getTargetClass())
                || Fragment.class.isAssignableFrom(getTargetClass())
                || android.app.Fragment.class.isAssignableFrom(getTargetClass())) {
            return true;
        }
        return false;
    }

    public Class getTargetClass() {
        return mClass;
    }

    private Class get(String routerUri) {

        String router = routerUri;
        if (routerUri.contains("?")) {
            router = routerUri.substring(0, routerUri.indexOf("?"));
        }

        Class clz = routerCache.get(router);

        if (clz == null) {
            try {
                clz = MrouterHelper.getClz(router);
            } catch (Exception e) {
                Log.e("Router", "Errors of reflecting the activity of the router \"" + router + "\"!!!\n" + e);
                return null;
            }

            if (clz != null) {
                routerCache.put(router, clz);
            }
        }

        return clz;
    }

    public interface IWebChecker {
        boolean isWebMode(RouterMeta meta);
    }
}
