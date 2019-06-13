package mrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 作者： mooney
 * 日期： 2018/1/31
 * 邮箱： shili_yan@sina.com
 * 描述：
 */

public class Mrouter {

    private static class Holder {
        private static Mrouter instance = new Mrouter();
    }

    private static final String TAG = "Mrouter";

    private Context mContext;

    private Map<String, Class> routerCache = new HashMap<>();

    public static Mrouter getInstance() {
        return Holder.instance;
    }

    public void init(Context context) {
        mContext = context;
        routerCache = new HashMap<>();
    }

    /**
     * 打开指定路由的页面
     *
     * @param routerUri
     * @return
     */
    public boolean open(String routerUri) {

        return open(routerUri, null);
    }

    /**
     * 打开指定路由的页面
     *
     * @param routerUri
     * @param dataIntent
     * @return
     */
    public boolean open(String routerUri, Intent dataIntent) {

        if (!MrouterHelper.isValidURI(routerUri)) {
            Log.e(TAG, "router \"" + routerUri + "\" is invalid uri path!!!");
            return false;
        }

        Log.i(TAG, "before open the router \"" + routerUri + "\".");

        /**
         * 设置参数
         */
        if (dataIntent == null) {
            dataIntent = new Intent();
        }
        Uri uri = Uri.parse(routerUri);
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        if (queryParameterNames != null && queryParameterNames.size() > 0) {
            for (String key : queryParameterNames) {
                dataIntent.putExtra(key, uri.getQueryParameter(key));
            }
        }

        /**
         * 打开web网页
         */
        if (routerUri.startsWith("http://") || routerUri.startsWith("https://")) {

//            Uri uri = Uri.parse(routerUri); // url为你要链接的地址
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            if (dataIntent == null) {
//                dataIntent = new Intent();
//            }
            dataIntent.setAction(Intent.ACTION_VIEW);
            dataIntent.setData(uri);
            dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            return doOpen(null, routerUri, -1, dataIntent);
        }

        /**
         * 打开系统注册的路由activity
         */
        Class activityClazz = get(routerUri);
        if (activityClazz == null) {
            Log.e(TAG, "there is no activity of the router \"" + routerUri + "\"!!!");
            return false;
        }
        if (!Activity.class.isAssignableFrom(activityClazz)){
            Log.e(TAG, "the router \"" + routerUri + "\" is not an activity`s router!!!");
            return false;
        }

        dataIntent.setClass(mContext, activityClazz);
        PackageManager packageManager = mContext.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(dataIntent, 0);
        if (activities.size() > 0) {

            dataIntent.setData(uri);
            dataIntent.setAction(Intent.ACTION_VIEW);
            dataIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            return doOpen(null, routerUri, -1, dataIntent);
        }

        /**
         * 打开指定类名的activity
         */
        dataIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        dataIntent.setClass(mContext, activityClazz);

        return doOpen(null, routerUri, -1, dataIntent);
    }

    /**
     * 打开页面并监听回调
     *
     * @param context
     * @param routerUri
     * @param requestCode
     * @return
     */
    public boolean openForResult(Object context, String routerUri, int requestCode) {
        return openForResult(context, routerUri, requestCode, null);
    }

    /**
     * 打开页面并监听回调
     *
     * @param context
     * @param routerUri
     * @param requestCode
     * @param dataIntent
     * @return
     */
    public boolean openForResult(Object context, String routerUri, int requestCode, Intent dataIntent) {

        if (!MrouterHelper.isValidURI(routerUri)) {
            Log.e(TAG, "router \"" + routerUri + "\" is invalid uri path!!!");
            return false;
        }

        Log.i(TAG, "before open the router \"" + routerUri + "\".");

        /**
         * 设置参数
         */
        if (dataIntent == null) {
            dataIntent = new Intent();
        }
        Uri uri = Uri.parse(routerUri);
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        if (queryParameterNames != null && queryParameterNames.size() > 0) {
            for (String key : queryParameterNames) {
                dataIntent.putExtra(key, uri.getQueryParameter(key));
            }
        }

        /**
         * 打开web网页
         */
        if (routerUri.startsWith("http://") || routerUri.startsWith("https://")) {

//            Uri uri = Uri.parse(routerUri); // url为你要链接的地址
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            dataIntent.setAction(Intent.ACTION_VIEW);
            dataIntent.setData(uri);
            dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            return doOpen(context, routerUri, requestCode, dataIntent);
        }

        /**
         * 打开系统注册的路由activity
         */
        Class activityClazz = get(routerUri);
        if (activityClazz == null) {
            Log.e(TAG, "there is no activity of the router \"" + routerUri + "\"!!!");
            return false;
        }
        if (!Activity.class.isAssignableFrom(activityClazz)){
            Log.e(TAG, "the router \"" + routerUri + "\" is not an activity`s router!!!");
            return false;
        }
        if (context==null){
            dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dataIntent.setClass(mContext, activityClazz);
        } else if (context instanceof Activity){
            dataIntent.setClass((Activity)context, activityClazz);
        }else if (context instanceof Fragment){
            dataIntent.setClass(((Fragment)context).getContext(), activityClazz);
        }else if (context instanceof android.app.Fragment){
            dataIntent.setClass(((android.app.Fragment)context).getActivity(), activityClazz);
        }else {
            dataIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dataIntent.setClass(mContext, activityClazz);
        }

        return doOpen(context, routerUri, requestCode, dataIntent);
    }


    private boolean doOpen(Object context, String routerUri, int requestCode, Intent dataIntent) {
        if (context == null) {
            try {
                mContext.startActivity(dataIntent);
                return true;
            } catch (Exception e) {
                Log.e(TAG, "Errors of opening the router \"" + routerUri + "\" for result!!!\n" + e);
                return false;
            }
        } else if (context instanceof Activity) {
            try {
                if (requestCode == -1) {
                    ((Activity) context).startActivity(dataIntent);
                } else {
                    ((Activity) context).startActivityForResult(dataIntent, requestCode);
                }
                return true;
            } catch (Exception e) {
                if (requestCode == -1) {
                    Log.e(TAG, "Errors of opening the router \"" + routerUri + "\"!!!\n" + e);
                } else {
                    Log.e(TAG, "Errors of opening the router \"" + routerUri + "\" for result!!!\n" + e);
                }
                return false;
            }
        } else if (context instanceof Fragment) {
            try {
                if (requestCode == -1) {
                    ((Fragment) context).startActivity(dataIntent);
                } else {
                    ((Fragment) context).startActivityForResult(dataIntent, requestCode);
                }
                return true;
            } catch (Exception e) {
                if (requestCode == -1) {
                    Log.e(TAG, "Errors of opening the router \"" + routerUri + "\"!!!\n" + e);
                } else {
                    Log.e(TAG, "Errors of opening the router \"" + routerUri + "\" for result!!!\n" + e);
                }
                return false;
            }
        } else if (context instanceof android.app.Fragment) {
            try {
                if (requestCode == -1) {
                    ((android.app.Fragment) context).startActivity(dataIntent);
                } else {
                    ((android.app.Fragment) context).startActivityForResult(dataIntent, requestCode);
                }
                return true;
            } catch (Exception e) {
                if (requestCode == -1) {
                    Log.e(TAG, "Errors of opening the router \"" + routerUri + "\"!!!\n" + e);
                } else {
                    Log.e(TAG, "Errors of opening the router \"" + routerUri + "\" for result!!!\n" + e);
                }
                return false;
            }
        } else {
            Log.e(TAG, "Errors of opening the router \"" + routerUri + "\" for result!!!" +
                    "\nError:the context " + context.getClass().getCanonicalName() + " is not proper context!!!");
            return false;
        }
    }


    public Class get(String routerUri) {

        String router = routerUri;
        if (routerUri.contains("?")) {
            router = routerUri.substring(0, routerUri.indexOf("?"));
        }

        Class clz = routerCache.get(router);

        if (clz == null) {
            try {
                clz = MrouterHelper.getClz(router);
            } catch (Exception e) {
                Log.e(TAG, "Errors of reflecting the activity of the router \"" + router + "\"!!!\n" + e);
                return null;
            }

            if (clz != null) {
                routerCache.put(router, clz);
            }
        }

        return clz;
    }

    public Class getFragmentClz(String routerUri) {

        Class clz = get(routerUri);
        if (Fragment.class.isAssignableFrom(clz)|| android.app.Fragment.class.isAssignableFrom(clz)){
            return clz;
        }
        Log.e(TAG, "the router \"" + routerUri + "\" is not an fragment`s router!!!");
        return null;
    }


}
