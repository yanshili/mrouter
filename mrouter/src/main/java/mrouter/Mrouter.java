package mrouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * 作者： mooney
 * 日期： 2018/1/31
 * 邮箱： shili_yan@sina.com
 * 描述： 废弃，使用{@link Router}
 */

@Deprecated
public class Mrouter {

    private static class Holder {
        private static Mrouter instance = new Mrouter();
    }

    private static final String TAG = "Mrouter";

    private Context mContext;


    public static Mrouter getInstance() {
        return Holder.instance;
    }

    private boolean inited = false;

    public boolean isInited(){
        return inited;
    }

    public void init(Context context) {
        mContext = context;
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
        RouterMeta routerMeta = Router.build(routerUri);
        if (dataIntent!=null){
            if (dataIntent.getFlags()==0){
                dataIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            routerMeta.withExtras(dataIntent.getExtras())
                    .withFlags(dataIntent.getFlags())
                    .withAction(dataIntent.getAction());
        }else {
            routerMeta.withFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (routerMeta.isValidPageClass()) {
            try {
                routerMeta.start(mContext);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
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

        RouterMeta routerMeta = Router.build(routerUri)
                .withRequestCode(requestCode);
        if (dataIntent!=null){
            routerMeta.withExtras(dataIntent.getExtras())
                    .withFlags(dataIntent.getFlags())
                    .withAction(dataIntent.getAction());
        }

        if (routerMeta.isValidPageClass()) {
            try {
                if (context == null) {
                    return false;
                } else if (context instanceof Activity) {
                    routerMeta.startForResult((Activity) context);
                } else if (context instanceof Fragment) {
                    routerMeta.startForResult((Fragment) context);
                } else if (context instanceof android.app.Fragment) {
                    routerMeta.startForResult((android.app.Fragment) context);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public Class get(String routerUri) {
        return Router.build(routerUri)
                .getTargetClass();
    }

}
