package com.example.puzzlegame.PintuView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.puzzlegame.R;
import com.example.puzzlegame.Util.ImagePiece;
import com.example.puzzlegame.Util.ImageSplitterUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by jerry on 16-9-10.
 */
public class GamePintuLayout extends RelativeLayout implements View.OnClickListener {

    private int mColumn = 3;
    //容器的内边距
    private int mPadding;
    //每张小图间的距离（横纵）dp
    private int mMagin = 3;

    //游戏面板的宽度
    private int mwidth;

    private ImageView[] mGamepintuItems;

    private int mItemWidth;
    //游戏的图片
    private Bitmap mBitmap;

    private List<ImagePiece> mitembitmaps;
    private boolean once;


    public GamePintuLayout(Context context) {
        this(context, null);

    }

    public GamePintuLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public GamePintuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mMagin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3,
                getResources().getDisplayMetrics());
        mPadding = min(getPaddingLeft(), getPaddingRight(), getPaddingTop(), getPaddingBottom());

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //取宽和高的最小值
        mwidth = Math.min(getMeasuredHeight(), getMeasuredWidth());
        if (!once) {
            //进行切图及排序
            initbitmap();
            //设置imageview（item）的宽高等属性
            inititem();
            once = true;
        }
        setMeasuredDimension(mwidth, mwidth);

    }

    //设置imageview（item）的宽高等属性
    private void inititem() {

        mItemWidth = (mwidth - mPadding * 2 - mMagin * (mColumn - 1)) / mColumn;
        mGamepintuItems = new ImageView[mColumn * mColumn];

        //生成item，设置rule
        for (int i = 0; i < mGamepintuItems.length; i++) {
            ImageView item = new ImageView(getContext());
            item.setOnClickListener(this);
            item.setImageBitmap(mitembitmaps.get(i).getBitmap());
            mGamepintuItems[i] = item;
            item.setId(i + 1);
            //在item的tag中存储了index
            item.setTag(i + "_" + mitembitmaps.get(i).getIndex());

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mItemWidth, mItemWidth);
            //设置item间横向间隙，不是最后一列，设置rightwidth
            if ((i + 1 % mColumn) != 0) {
                lp.rightMargin = mMagin;

            }
            //不是第一列
            if (i % mColumn != 0) {
                lp.addRule(RelativeLayout.RIGHT_OF, mGamepintuItems[i - 1].getId());
            }

            //如果不是第一行,谁知topmargin和rule
            if ((i + 1) > mColumn) {
                lp.topMargin = mMagin;
                lp.addRule(RelativeLayout.BELOW, mGamepintuItems[i - mColumn].getId());


            }
            addView(item, lp);


        }


    }

    //进行切图及排序
    private void initbitmap() {
        if (mBitmap == null) {

            mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test1);

        }
        mitembitmaps = ImageSplitterUtil.splitImage(mBitmap, mColumn);
        //使用sort完成乱序
        Collections.sort(mitembitmaps, new Comparator<ImagePiece>() {
            @Override
            public int compare(ImagePiece a, ImagePiece b) {
                return Math.random() > 0.5 ? 1 : -1;

            }
        });
    }

    //获取多个参数的最小值
    private int min(int... params) {
        int min = params[0];
        for (int param : params) {
            if (param < min) {
                min = param;

            }
        }
        return min;

    }

    private ImageView mfirst;
    private ImageView msecond;

    @Override
    public void onClick(View view) {

        if (isaniming) {
            return;
        }
        //两次点击同一个item，取消选择
        if (mfirst == view) {
            mfirst.setColorFilter(null);
            mfirst = null;
            return;
        }
        if (mfirst == null) {
            mfirst = (ImageView) view;
            mfirst.setColorFilter(Color.parseColor("#50FF0000"));
        } else {
            msecond = (ImageView) view;
            //交换item
            exchangerview();
        }


    }

    //动画层
    private RelativeLayout manimlayout;
    private boolean isaniming;


    //交换item
    private void exchangerview() {

        //取消高亮
        mfirst.setColorFilter(null);

        setupanimlayout();

        ImageView first = new ImageView(getContext());
        final Bitmap firstbitmap = mitembitmaps.get(getimageidbytag((String) mfirst.getTag())).getBitmap();

        first.setImageBitmap(firstbitmap);
        LayoutParams lp = new LayoutParams(mItemWidth, mItemWidth);
        lp.leftMargin = mfirst.getLeft() - mPadding;
        lp.topMargin = mfirst.getTop() - mPadding;
        first.setLayoutParams(lp);
        manimlayout.addView(first);


        ImageView second = new ImageView(getContext());
        final Bitmap secondbitmap = mitembitmaps.get(getimageidbytag((String) msecond.getTag())).getBitmap();

        second.setImageBitmap(secondbitmap);
        LayoutParams lp2 = new LayoutParams(mItemWidth, mItemWidth);
        lp2.leftMargin = msecond.getLeft() - mPadding;
        lp2.topMargin = msecond.getTop() - mPadding;
        second.setLayoutParams(lp2);
        manimlayout.addView(second);


        //设置动画
        TranslateAnimation anim = new TranslateAnimation(0, msecond.getLeft() - mfirst.getLeft(), 0,
                msecond.getTop() - mfirst.getTop());
        anim.setDuration(300);
        anim.setFillAfter(true);
        first.startAnimation(anim);

        TranslateAnimation animsecond = new TranslateAnimation(0, -msecond.getLeft() + mfirst.getLeft(), 0,
                -msecond.getTop() + mfirst.getTop());
        animsecond.setDuration(300);
        animsecond.setFillAfter(true);
        second.startAnimation(animsecond);

        //监听动画
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                //隐藏原来的图片
                mfirst.setVisibility(View.INVISIBLE);
                msecond.setVisibility(View.INVISIBLE);
                isaniming = true;

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                String firsttag = (String) mfirst.getTag();
                String secondtag = (String) msecond.getTag();


                msecond.setImageBitmap(firstbitmap);
                mfirst.setImageBitmap(secondbitmap);

                mfirst.setTag(secondtag);
                msecond.setTag(firsttag);
                //显示交换后的图片
                mfirst.setVisibility(View.VISIBLE);
                msecond.setVisibility(View.VISIBLE);

                mfirst = msecond = null;
                //移除动画层
                manimlayout.removeAllViews();
                isaniming = false;


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    //根据tag获取id
    public int getimageidbytag(String tag) {

        String[] split = tag.split("_");
        return Integer.parseInt(split[0]);


    }

    public int getimageindex(String tag) {
        String[] split = tag.split("_");
        return Integer.parseInt(split[1]);
    }

    //构造动画层
    private void setupanimlayout() {

        if (manimlayout == null) {
            manimlayout = new RelativeLayout(getContext());
            addView(manimlayout);

        }
    }
}
