package com.ojk.retrofitmvp.base;


/**
 * @author azheng
 * @date 2018/4/24.
 * GitHub：https://github.com/RookieExaminer
 * Email：wei.azheng@foxmail.com
 * Description：
 */
public class BaseTonggPresenter<V extends BaseTonggView> {
    protected V mView;


    /**
     * 绑定view，一般在初始化中调用该方法
     *
     * @param view view
     */
    public void attachView(V view) {
        this.mView = view;
    }

    /**
     * 解除绑定view，一般在onDestroy中调用
     */

    public void detachView() {
        this.mView = null;
    }

    /**
     * View是否绑定
     *
     * @return
     */
    public boolean isViewAttached() {
        if (mView!=null){
//            if (!ToolSharedPreferencesUtils.getboolean(HomeTonggApplication.getInstance().getApplicationContext(), "hasNet", false)){
//                new AlertView(UITonggUtils.getString(R.string.tip), UITonggUtils.getString(R.string.net_error),
//                        null, new String[]{UITonggUtils.getString(R.string.sure)}, null, HomeTonggApplication.getInstance().getApplicationContext(),
//                        AlertView.Style.Alert, null).show();
//                return false;
//            }else {
//                return true;
//            }
            return true;
        }else {
            return false;
        }
    }


}
