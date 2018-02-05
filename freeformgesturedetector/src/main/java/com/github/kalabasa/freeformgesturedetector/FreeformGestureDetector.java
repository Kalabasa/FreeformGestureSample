package com.github.kalabasa.freeformgesturedetector;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

public class FreeformGestureDetector {

	public interface OnGestureListener {
		boolean onTransform(MotionEvent ev, Matrix transform);
	}

	// TODO forceRigid
	private final OnGestureListener listener;

	private float touchSlop;
	private boolean pastSlop;

	private SparseArray<PointF> pointerStartLocations = new SparseArray<>();
	private SparseArray<PointF> pointerLocations = new SparseArray<>();

	private float[] record = null;

	private int[] pointerIdMap = null;
	private Matrix tmpMatrix = new Matrix();

	private Matrix matrix = new Matrix();

	public FreeformGestureDetector(Context context, OnGestureListener listener) {
		this(ViewConfiguration.get(context).getScaledTouchSlop(), listener);
	}

	public FreeformGestureDetector(float touchSlop, OnGestureListener listener) {
		if (listener == null) throw new NullPointerException("listener must not be null.");
		this.listener = listener;
		this.touchSlop = touchSlop;
	}

	public void setTouchSlop(float touchSlop) {
		this.touchSlop = touchSlop;
	}

	public void onTouchEvent(MotionEvent ev) {
		int actionMasked = ev.getActionMasked();
		switch (actionMasked) {
			case MotionEvent.ACTION_DOWN: {
				pastSlop = false;
				startPointer(ev.getPointerId(0), ev.getX(), ev.getY());
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				recordUpdate();
				for (int i = 0; i < ev.getPointerCount(); i++) {
					movePointer(ev.getPointerId(i), ev.getX(i), ev.getY(i));
				}
				applyUpdate(ev);
				break;
			}
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL: {
				endPointer(ev.getPointerId(0), ev.getX(), ev.getY());
				break;
			}
			case MotionEvent.ACTION_POINTER_DOWN: {
				int index = ev.getActionIndex();
				startPointer(ev.getPointerId(index), ev.getX(index), ev.getY(index));
				break;
			}
			case MotionEvent.ACTION_POINTER_UP: {
				int index = ev.getActionIndex();
				endPointer(ev.getPointerId(index), ev.getX(index), ev.getY(index));
				break;
			}
		}
	}

	private void startPointer(int pointerId, float x, float y) {
		if (!pastSlop) pointerStartLocations.put(pointerId, new PointF(x, y));
		pointerLocations.put(pointerId, new PointF(x, y));
	}

	private void movePointer(int pointerId, float x, float y) {
		if (!pastSlop) {
			PointF startLoc = pointerStartLocations.get(pointerId);
			pastSlop |= PointF.length(x - startLoc.x, y - startLoc.y) >= touchSlop;
		}
		pointerLocations.get(pointerId).set(x, y);
	}

	private void endPointer(int pointerId, float x, float y) {
		pointerLocations.get(pointerId).set(x, y);
		pointerLocations.remove(pointerId);
		pointerStartLocations.remove(pointerId);
	}

	private void recordUpdate() {
		int n = pointerLocations.size();
		record = new float[n * 4];
		pointerIdMap = new int[n];
		for (int i = 0; i < n; i++) {
			PointF point = pointerLocations.valueAt(i);
			record[i * 2] = point.x;
			record[i * 2 + 1] = point.y;
			pointerIdMap[i] = pointerLocations.keyAt(i);
		}
	}

	private void applyUpdate(MotionEvent ev) {
		int n = pointerLocations.size();
		for (int i = 0; i < n; i++) {
			int pointerId = pointerIdMap[i];
			PointF point = pointerLocations.get(pointerId);
			record[n * 2 + i * 2] = point.x;
			record[n * 2 + i * 2 + 1] = point.y;
		}

		for (int i = Math.min(4, n); i > 0; i--) {
			if (tmpMatrix.setPolyToPoly(record, 0, record, n * 2, i)) {
				matrix.postConcat(tmpMatrix);
				if (pastSlop) sendTransform(ev);
				break;
			}
		}
	}

	private void sendTransform(MotionEvent ev) {
		if (listener.onTransform(ev, new Matrix(matrix))) {
			matrix.reset();
		}
	}
}
