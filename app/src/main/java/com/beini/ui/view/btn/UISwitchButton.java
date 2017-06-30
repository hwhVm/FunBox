package com.beini.ui.view.btn;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.CheckBox;

import com.beini.R;
import com.beini.util.Px_DipUtils;


public class UISwitchButton extends CheckBox {
	private Paint mPaint;
	private RectF mSaveLayerRectF;
	private float mFirstDownY;
	private float mFirstDownX;
	private int mClickTimeout;
	private int mTouchSlop;
	private final int MAX_ALPHA = 255;
	private int mAlpha = MAX_ALPHA;
	private boolean mChecked = false; 
	private boolean mBroadcasting;
	private boolean mTurningOn;
	private PerformClick mPerformClick;
	private OnCheckedChangeListener mOnCheckedChangeListener;
	private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
	private boolean mAnimating;
	private final float VELOCITY = 350;
	private float mVelocity;
	private float mAnimationPosition;
	private float mAnimatedVelocity;
	private Bitmap bmBgGreen;
	private Bitmap bmBgWhite;
	private Bitmap bmBtnNormal;
	private Bitmap bmBtnPressed;
	private Bitmap bmCurBtnPic;
	private Bitmap bmCurBgPic;
	private float bgWidth;
	private float bgHeight;
	private float btnWidth;
	private float offBtnPos;
	private float onBtnPos;
	private float curBtnPos;
	private float startBtnPos;
	private  int COMMON_WIDTH_IN_PIXEL;
	private  int COMMON_HEIGHT_IN_PIXEL ;

	public UISwitchButton(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.checkboxStyle);
	}

	public UISwitchButton(Context context) {
		this(context, null);
	}

	public UISwitchButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		COMMON_WIDTH_IN_PIXEL= Px_DipUtils.dip2px(context,43);
		COMMON_HEIGHT_IN_PIXEL=Px_DipUtils.dip2px(context,23);
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		Resources resources = context.getResources();

		// get attrConfiguration
		TypedArray array = context.obtainStyledAttributes(attrs,
				R.styleable.SwitchButton);
		int width = (int) array.getDimensionPixelSize(
				R.styleable.SwitchButton_bmWidth, 0);
		int height = (int) array.getDimensionPixelSize(
				R.styleable.SwitchButton_bmHeight, 0);
		array.recycle();


		// size width or height
		if (width <= 0 || height <= 0) {
			width = COMMON_WIDTH_IN_PIXEL;
			height = COMMON_HEIGHT_IN_PIXEL;
		} else {
			float scale = (float) COMMON_WIDTH_IN_PIXEL
					/ COMMON_HEIGHT_IN_PIXEL;
			if ((float) width / height > scale) {
				width = (int) (height * scale);
			} else if ((float) width / height < scale) {
				height = (int) (width / scale);
			}
		}
		// get viewConfiguration
		mClickTimeout = ViewConfiguration.getPressedStateDuration()
				+ ViewConfiguration.getTapTimeout();
		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		// get Bitmap
		bmBgGreen = BitmapFactory.decodeResource(resources,
				R.drawable.switch_btn_bg_green);
		bmBgWhite = BitmapFactory.decodeResource(resources,
				R.drawable.switch_btn_bg_white);
		bmBtnNormal = BitmapFactory.decodeResource(resources,
				R.drawable.switch_btn_normal);
		bmBtnPressed = BitmapFactory.decodeResource(resources,
				R.drawable.switch_btn_pressed);

		// size Bitmap
		bmBgGreen = Bitmap.createScaledBitmap(bmBgGreen, width, height, true);
		bmBgWhite = Bitmap.createScaledBitmap(bmBgWhite, width, height, true);
		bmBtnNormal = Bitmap.createScaledBitmap(bmBtnNormal, height, height,
				true);
		bmBtnPressed = Bitmap.createScaledBitmap(bmBtnPressed, height, height,
				true);

		bmCurBtnPic = bmBtnNormal;// ��ʼ��ťͼƬ
		bmCurBgPic = mChecked ? bmBgGreen : bmBgWhite;// ��ʼ����ͼƬ
		bgWidth = bmBgGreen.getWidth();// �������
		bgHeight = bmBgGreen.getHeight();// �����߶�
		btnWidth = bmBtnNormal.getWidth();// ��ť���
		offBtnPos = 0;// �ر�ʱ�������
		onBtnPos = bgWidth - btnWidth;// ��ʼʱ���ұ�
		curBtnPos = mChecked ? onBtnPos : offBtnPos;// ��ť��ǰΪ��ʼλ��

		// get density
		float density = resources.getDisplayMetrics().density;
		mVelocity = (int) (VELOCITY * density + 0.5f);// ��������
		mSaveLayerRectF = new RectF(0, 0, bgWidth, bgHeight);
	}

	@Override
	public void setEnabled(boolean enabled) {
		mAlpha = enabled ? MAX_ALPHA : MAX_ALPHA / 3;
		super.setEnabled(enabled);
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void toggle() {
		setChecked(!mChecked);
	}

	private void setCheckedDelayed(final boolean checked) {
		postDelayed(new Runnable() {
			@Override
			public void run() {
				setChecked(checked);
			}
		}, 10);
	}

	public void setChecked(boolean checked,String isNull) {
		if (mChecked != checked) {
			mChecked = checked;

			curBtnPos = checked ? onBtnPos : offBtnPos;

			bmCurBgPic = checked ? bmBgGreen : bmBgWhite;
			invalidate();


		}
	}
	public void setChecked(boolean checked) {
		if (mChecked != checked) {
			mChecked = checked;

			// ��ʼ����ťλ��
			curBtnPos = checked ? onBtnPos : offBtnPos;
			// �ı䱳��ͼƬ
			bmCurBgPic = checked ? bmBgGreen : bmBgWhite;
			invalidate();

			if (mBroadcasting) {
				// NO-OP
				return;
			}
			// ����ִ�м����¼�
			mBroadcasting = true;
			if (mOnCheckedChangeListener != null) {
				mOnCheckedChangeListener.onCheckedChanged(UISwitchButton.this,
						mChecked);
			}
			if (mOnCheckedChangeWidgetListener != null) {
				mOnCheckedChangeWidgetListener.onCheckedChanged(
						UISwitchButton.this, mChecked);
			}
			// �����¼�����
			mBroadcasting = false;

		}
	}
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeListener = listener;
	}

	void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
		mOnCheckedChangeWidgetListener = listener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		float x = event.getX();
		float y = event.getY();
		float deltaX = Math.abs(x - mFirstDownX);
		float deltaY = Math.abs(y - mFirstDownY);
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			ViewParent mParent = getParent();
			if (mParent != null) {
				// ֪ͨ���ؼ���Ҫ���ر�view�Ĵ����¼�
				mParent.requestDisallowInterceptTouchEvent(true);
			}
			mFirstDownX = x;
			mFirstDownY = y;
			bmCurBtnPic = bmBtnPressed;
			startBtnPos = mChecked ? onBtnPos : offBtnPos;
			break;
		case MotionEvent.ACTION_MOVE:
			float time = event.getEventTime() - event.getDownTime();
			curBtnPos = startBtnPos + event.getX() - mFirstDownX;
			if (curBtnPos >= onBtnPos) {
				curBtnPos = onBtnPos;
			}
			if (curBtnPos <= offBtnPos) {
				curBtnPos = offBtnPos;
			}
			mTurningOn = curBtnPos > bgWidth / 2 - btnWidth / 2;
			break;
		case MotionEvent.ACTION_UP:
			bmCurBtnPic = bmBtnNormal;
			time = event.getEventTime() - event.getDownTime();
			if (deltaY < mTouchSlop && deltaX < mTouchSlop
					&& time < mClickTimeout) {
				if (mPerformClick == null) {
					mPerformClick = new PerformClick();
				}
				if (!post(mPerformClick)) {
					performClick();
				}
			} else {
				startAnimation(mTurningOn);
			}
			break;
		}
		invalidate();
		return isEnabled();
	}

	private class PerformClick implements Runnable {
		public void run() {
			performClick();
		}
	}

	@Override
	public boolean performClick() {
		startAnimation(!mChecked);
		return true;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.saveLayerAlpha(mSaveLayerRectF, mAlpha, Canvas.MATRIX_SAVE_FLAG
				| Canvas.CLIP_SAVE_FLAG | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
				| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
				| Canvas.CLIP_TO_LAYER_SAVE_FLAG);

		// ���Ƶײ�ͼƬ
		canvas.drawBitmap(bmCurBgPic, 0, 0, mPaint);

		// ���ư�ť
		canvas.drawBitmap(bmCurBtnPic, curBtnPos, 0, mPaint);

		canvas.restore();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension((int) bgWidth, (int) bgHeight);
	}

	private void startAnimation(boolean turnOn) {
		mAnimating = true;
		mAnimatedVelocity = turnOn ? mVelocity : -mVelocity;
		mAnimationPosition = curBtnPos;
		new SwitchAnimation().run();
	}

	private void stopAnimation() {
		mAnimating = false;
	}

	private final class SwitchAnimation implements Runnable {
		@Override
		public void run() {
			if (!mAnimating) {
				return;
			}
			doAnimation();
			requestAnimationFrame(this);
		}
	}

	private void doAnimation() {
		mAnimationPosition += mAnimatedVelocity * ANIMATION_FRAME_DURATION
				/ 1000;
		if (mAnimationPosition <= offBtnPos) {
			stopAnimation();
			mAnimationPosition = offBtnPos;
			setCheckedDelayed(false);
		} else if (mAnimationPosition >= onBtnPos) {
			stopAnimation();
			mAnimationPosition = onBtnPos;
			setCheckedDelayed(true);
		}
		curBtnPos = mAnimationPosition;
		invalidate();
	}

	private static final int MSG_ANIMATE = 1000;
	public static final int ANIMATION_FRAME_DURATION = 1000 / 60;

	public void requestAnimationFrame(Runnable runnable) {
		Message message = new Message();
		message.what = MSG_ANIMATE;
		message.obj = runnable;
		mHandler.sendMessageDelayed(message, ANIMATION_FRAME_DURATION);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {
			switch (m.what) {
			case MSG_ANIMATE:
				if (m.obj != null) {
					((Runnable) m.obj).run();
				}
				break;
			}
		}
	};
}
