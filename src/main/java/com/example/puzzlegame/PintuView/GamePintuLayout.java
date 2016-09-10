package com.example.puzzlegame.PintuView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
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

    //交换item
    private void exchangerview() {
        mfirst.setColorFilter(null);
        String firsttag = (String) mfirst.getTag();
        String secondtag = (String) msecond.getTag();

        String[] firstparams = firsttag.split("_");
        String[] secondparams = secondtag.split("_");

        Bitmap firstbitmap = mitembitmaps.get(Integer.parseInt(firstparams[0])).getBitmap();

        Bitmap secondbitmap = mitembitmaps.get(Integer.parseInt(secondparams[0])).getBitmap();
        msecond.setImageBitmap(firstbitmap);
        mfirst.setImageBitmap(secondbitmap);

        mfirst.setTag(secondtag);
        msecond.setTag(firsttag);
        mfirst = msecond = null;

    }
}
