package com.zhejunzhu.ucviewpager.weight.recyclerview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhejunzhu.ucviewpager.R;
import com.zhejunzhu.ucviewpager.utils.AndroidUtils;
import com.zhejunzhu.ucviewpager.utils.PrivateAnimConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RefreshStateLayout {
    @BindView(R.id.refresh_icon)
    ImageView mRefreshIcon;

    @BindView(R.id.refresh_text)
    TextView mRefreshText;

    public static int sHeight;

    private Context mContext;

    private View mView;

    private ObjectAnimator mRefreshAnim;

    private String mStringDrag;

    private String mStringGo;

    private String mStringRefreshing;

    private String mStringRefreshCount;

    private String mStringNetError;

    private String mStringFault;

    private String mCurrentStr;

    private RefreshState mRefreshState = RefreshState.state_drag_refresh;

    enum RefreshState {
        state_drag_refresh, state_refreshing, state_complete
    }

    public RefreshStateLayout(Context context) {
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.layout_refresh_view, null);
        ButterKnife.bind(this, mView);
        mView.setAlpha(0);

        initStrings();
    }

    private void initStrings() {
        mStringDrag = mContext.getResources().getString(R.string.stream_refresh_drag);
        mStringGo = mContext.getResources().getString(R.string.stream_refresh_go);
        mStringRefreshing = mContext.getResources().getString(R.string.stream_refresh_ing);
        mStringRefreshCount = mContext.getResources().getString(R.string.stream_refresh_count);
        mStringNetError = mContext.getResources().getString(R.string.stream_refresh_error_net);
        mStringFault = mContext.getResources().getString(R.string.stream_refresh_error_fault);

        mCurrentStr = mStringDrag;
    }

    public View getView() {
        return mView;
    }

    public int setOverDrag(int overDrag) {
        if (overDrag < 0) {
            overDrag = 0;
        }
        if (sHeight <= 0) {
            sHeight = AndroidUtils.getViewMakeMeasureHeight(mView);
        }

        if (overDrag > sHeight) {
            overDrag = sHeight + (overDrag - sHeight) / 2;
        }

        float alpha = (float) overDrag / sHeight;
        mView.setAlpha(alpha * alpha);
        if (overDrag <= sHeight) {
            mView.setY(0);
        } else {
            mView.setY(overDrag - sHeight);
        }

        switch (mRefreshState) {
            case state_complete:
                break;
            case state_drag_refresh:
                if (overDrag <= sHeight) {
                    setText(mStringDrag);
                } else {
                    setText(mStringGo);
                }
                mRefreshIcon.setRotation(360 * overDrag / sHeight * 2);
                break;
            case state_refreshing:
                setText(mStringRefreshing);
                break;
        }
        return overDrag;
    }

    public void setText(String state) {
        if (!mCurrentStr.equals(state)) {
            mRefreshText.setText(state);
            mCurrentStr = state;
        }
    }

    public void setRefreshCountState(int count) {
        endRefreshAnim();
        mRefreshState = RefreshState.state_complete;
        String str = mContext.getResources().getString(R.string.stream_refresh_count);
        mRefreshIcon.setRotation(0);
        mRefreshText.setText(String.format(str, count));
        mRefreshIcon.setImageResource(R.drawable.refresh_complete);
    }

    public void setRefreshState() {
        setText(mStringDrag);
        mRefreshState = RefreshState.state_drag_refresh;
        mRefreshIcon.setImageResource(R.drawable.refresh_icon);
    }

    public void startRefreshAnim() {
        if (mRefreshAnim != null) {
            mRefreshAnim.end();
            mRefreshAnim = null;
        }
        mRefreshState = RefreshState.state_refreshing;
        mRefreshAnim = ObjectAnimator.ofFloat(mRefreshIcon, "Rotation", mRefreshIcon.getRotation(), mRefreshIcon.getRotation() + 360);
        mRefreshAnim.setRepeatCount(ValueAnimator.INFINITE);
        mRefreshAnim.setRepeatMode(ValueAnimator.INFINITE);
        mRefreshAnim.setInterpolator(new LinearInterpolator());
        mRefreshAnim.setDuration(PrivateAnimConstant.ANIM_DURA_XLONG);
        mRefreshAnim.start();
    }

    public void endRefreshAnim() {
        if (mRefreshAnim != null) {
            mRefreshAnim.end();
            mRefreshState = RefreshState.state_drag_refresh;
            mRefreshAnim = null;
        }
    }
}
