package com.sxu.basecomponent.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gyf.immersionbar.ImmersionBar;
import com.sxu.basecomponent.interfaces.IComponentProcessEx;
import com.sxu.basecomponent.interfaces.IContainerLayoutStyle;
import com.sxu.basecomponent.interfaces.IRequestProcessor;
import com.sxu.basecomponent.interfaces.OnShowContentLayoutListener;
import com.sxu.basecomponent.interfaces.impl.ContainerLayoutStyleImpl;
import com.sxu.basecomponent.interfaces.impl.LayoutManager;
import com.sxu.basecomponent.interfaces.impl.RequestProcessorImpl;
import com.sxu.basecomponent.uiwidget.ToolbarEx;

import java.util.List;

/*******************************************************************************
 * Description: 启动时不进行网络操作的Activity的基类
 *
 * Author: Freeman
 *
 * Date: 2018/7/13
 *
 * Copyright: all rights reserved by Freeman.
 *******************************************************************************/
public abstract class BaseActivity extends AppCompatActivity implements IComponentProcessEx, IContainerLayoutStyle, IRequestProcessor, View.OnClickListener {

    /**
     * 真实的内容布局
     */
    protected View realContentView;

    /**
     * 页面自带的Toolbar
     */
    protected ToolbarEx toolbar;

    protected FragmentManager fragmentManager;

    /**
     * 默认的页面请求处理器
     */
    protected RequestProcessorImpl defaultProcessor;

    /**
     * 默认的页面样式
     */
    private ContainerLayoutStyleImpl layoutStyle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getSupportFragmentManager();

        // 设置页面的样式，包括是否沉浸式、状态栏、NavigationBar
        int statusBarColor = getStatusBarColor();
        int navigationBarColor = getNavigationBarColor();
        // 如果状态栏背景色为透明，则需要根据Toolbar的背景色来设置状态栏文字颜色
        int realStatusBarColor = statusBarColor;
        if (realStatusBarColor == Color.TRANSPARENT) {
            realStatusBarColor = getToolbarColor();
        }
        ImmersionBar.with(this)
                // 是否启用沉浸式
                .barEnable(isImmersion())
                // 设置状态栏的背景色
                .statusBarColorInt(statusBarColor)
                // 状态栏的文字颜色是否为深色
                .statusBarDarkFont(isLightColor(realStatusBarColor))
                // 设置NavigationBar的背景色
                .navigationBarColorInt(navigationBarColor)
                // NavigationBar中的图标颜色是否为深色
                .navigationBarDarkIcon(isLightColor(navigationBarColor))
                .init();

        if (savedInstanceState != null) {
            restoreFragment(null);
            recreateActivity(savedInstanceState);
        }

        // 页面预处理，如设置Window样式、页面切换动画等
        pretreatment();

        // 根据页面样式创建页面容器
        layoutStyle = new ContainerLayoutStyleImpl(this);
        // 根据是否需要网络请求，填充布局内容
        realContentView = LayoutManager.getRealContentView(this, this);
        defaultProcessor = new RequestProcessorImpl(this);
        defaultProcessor.setContainerLayout(getContainerLayout(this),
                realContentView, needRequest());
        toolbar = layoutStyle.getToolbar();
        if (toolbar != null) {
            toolbar.setBackgroundColor(getToolbarColor());
        }
        // 加载页面布局
        setContentView(getContainerLayout(this));
        // 初始化View
        initViews();
        // 设置各种事件监听
        initListener();

        // 如果需要网络请求，则执行requestData进行数据请求, 否则为View绑定数据
        if (needRequest()) {
            defaultProcessor.setOnShowContentLayoutListener(new OnShowContentLayoutListener() {
                @Override
                public void onShowContentLayout() {
                    // 为View绑定请求的网络数据
                    bindDataForView();
                }
            });
            requestData();
        } else {
            bindDataForView();
        }
    }

    @Override
    public boolean isImmersion() {
        return true;
    }

    /**
     * 修改状态栏的背景色
     * @return
     */
    protected @ColorInt int getStatusBarColor() {
        return Color.TRANSPARENT;
    }

    /**
     * 修改Toolbar的背景色
     * @return
     */
    protected @ColorInt int getToolbarColor() {
        return Color.WHITE;
    }

    /**
     * 修改NavigationBar的背景色
     * @return
     */
    protected @ColorInt int getNavigationBarColor() {
        return Color.WHITE;
    }

    @Override
    public final ViewGroup getContainerLayout(Context context) {
        return layoutStyle.getContainerLayout(context);
    }

    @Override
    public void pretreatment() {

    }

    @Override
    public View getContentView() {
        return null;
    }

    @Override
    public int getToolbarStyle() {
        return TOOL_BAR_STYLE_NORMAL;
    }

    @Override
    public boolean needRequest() {
        return true;
    }

    @Override
    public View loadLoadingLayout() {
        return defaultProcessor.loadLoadingLayout();
    }

    @Override
    public View loadEmptyLayout() {
        return defaultProcessor.loadEmptyLayout();
    }

    @Override
    public View loadFailureLayout() {
        return defaultProcessor.loadFailureLayout();
    }

    @Override
    public View loadLoginLayout() {
        return defaultProcessor.loadLoginLayout();
    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 网络请求成功，通知页面更换布局
     */
    protected final void loadFinish() {
        defaultProcessor.notifyLoadFinish(IRequestProcessor.MSG_LOAD_FINISH);
    }

    /**
     * 是否是网络请求错误，用于区分网络出错和数据为空的场景
     * @param code 网络请求的返回码
     * @return
     */
    protected boolean isNetworkError(int code) {
        return false;
    }

    /**
     * 网络请求失败，通知页面更换布局
     * 由于请求失败的原因是多样的，而且与约定的网络数据协议有关，故需要在Common层进行实现
     * @param code
     */
    protected final void loadError(int code) {
        if (isNetworkError(code)) {
            defaultProcessor.notifyLoadFinish(IRequestProcessor.MSG_LOAD_FAILURE);
        } else {
            defaultProcessor.notifyLoadFinish(IRequestProcessor.MSG_LOAD_EMPTY);
        }
    }

    /**
     * 重建Activity时调用
     * @param savedInstanceState
     */
    protected void recreateActivity(Bundle savedInstanceState) {

    }

    /**
     * 当Activity重建时恢复Fragment
     */
    private void restoreFragment(Fragment currentFragment) {
        FragmentManager fm = currentFragment == null
                ? getSupportFragmentManager()
                : currentFragment.getChildFragmentManager();
        List<Fragment> fragmentList = fm.getFragments();
        if (fragmentList.isEmpty()) {
            return;
        }

        for (Fragment fragment : fragmentList) {
            fragment.onAttach((Context) this);
            // 恢复嵌套的Fragment
            restoreFragment(fragment);
        }
    }

    /**
     * 判断颜色值是否为暗色调
     * @param color
     * @return
     */
    private boolean isLightColor(int color) {
        // 颜色值的临界值
        double lightnessThreshold = 0.5;
        double lightness = (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return lightness > lightnessThreshold;
    }
}
