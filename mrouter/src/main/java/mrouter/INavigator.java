package mrouter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * 作者：mooney
 * 日期：2019/6/13
 * 描述：
 */

public interface INavigator {

    void startFragment(Context context, Class fragmentClz, RouterMeta routerMeta);

    void startFragmentForResult(Activity activity, Class fragmentClz, RouterMeta routerMeta);

    void startFragmentForResult(Fragment fragment, Class fragmentClz, RouterMeta routerMeta);

    void startFragmentForResult(android.app.Fragment fragment, Class fragmentClz, RouterMeta routerMeta);


    void startActivity(Context context, Class activityClz, RouterMeta routerMeta);

    void startActivityForResult(Activity activity, Class activityClz, RouterMeta routerMeta);

    void startActivityForResult(Fragment fragment, Class activityClz, RouterMeta routerMeta);

    void startActivityForResult(android.app.Fragment fragment, Class activityClz, RouterMeta routerMeta);

    void startWeb(Context context, RouterMeta routerMeta);

    void startWebForResult(Activity activity, RouterMeta routerMeta);

    void startWebForResult(Fragment fragment, RouterMeta routerMeta);

    void startWebForResult(android.app.Fragment fragment, RouterMeta routerMeta);

}
