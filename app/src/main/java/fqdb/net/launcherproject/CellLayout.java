/*
 * Copyright 2013 Alexander Osmanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package fqdb.net.launcherproject;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * A layout manager that allows splitting parents area into evenly sized cells
 * grid. Each child can be positioned acress one or several cells.
 * 
 * @author aosmanov
 */
public class CellLayout extends ViewGroup {

    /**
     * Default size in dp that will be used for a cell in case no other clues
     * were given by parent.
     */
    private static final int DEFAULT_CELL_SIZE = 80;
    /**
     * Number of columns.
     */
    private int columns = 4;
	/**
	 * Number of rows.
	 */
	private int rows = 6;

    /**
     * An optional margin to be applied to each child.
     */
    private int spacing = 0;

    private float cellSize;

    public CellLayout(Context context, AttributeSet attrs, int defStyle) {
	super(context, attrs, defStyle);

	initAttrs(context, attrs);
    }

    public CellLayout(Context context, AttributeSet attrs) {
	super(context, attrs);

	initAttrs(context, attrs);
    }

    public CellLayout(Context context) {
	super(context);
    }

    public void initAttrs(Context context, AttributeSet attrs) {
	TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CellLayout, 0, 0);

	try {
	    columns = a.getInt(R.styleable.CellLayout_columns, 4);
	    spacing = a.getDimensionPixelSize(R.styleable.CellLayout_spacing, 0);
	} finally {
	    a.recycle();
	}
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	int heightMode = MeasureSpec.getMode(heightMeasureSpec);

	int width = 0;
	int height = 0;

	if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.EXACTLY) {
	    width = MeasureSpec.getSize(widthMeasureSpec);
	    cellSize = (float) (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / (float) columns;
	} else {
	    cellSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_CELL_SIZE, getResources()
		    .getDisplayMetrics());
	    width = (int) (columns * cellSize);
	}

	int childCount = getChildCount();
	View child;

	for (int i = 0; i < childCount; i++) {
	    child = getChildAt(i);

	    LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

	    int top = layoutParams.top;
	    int w = layoutParams.width;
	    int h = layoutParams.height;

	    int childWidthSpec = MeasureSpec.makeMeasureSpec((int) (w * cellSize) - spacing * 2,
		    MeasureSpec.EXACTLY);
	    int childHeightSpec = MeasureSpec.makeMeasureSpec((int) (h * cellSize) - spacing * 2,
		    MeasureSpec.EXACTLY);
	    child.measure(childWidthSpec, childHeightSpec);
	}

	int measuredHeight = Math.round(rows * cellSize) + getPaddingTop() + getPaddingBottom();
	if (heightMode == MeasureSpec.EXACTLY) {
	    height = MeasureSpec.getSize(heightMeasureSpec);
	} else if (heightMode == MeasureSpec.AT_MOST) {
	    int atMostHeight = MeasureSpec.getSize(heightMeasureSpec);
	    height = Math.min(atMostHeight, measuredHeight);
	} else {
	    height = measuredHeight;
	}

	setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
	int childCount = getChildCount();

	View child;
	for (int i = 0; i < childCount; i++) {
	    child = getChildAt(i);

	    LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();

	    int top = (int) (layoutParams.top * cellSize) + getPaddingTop() + spacing;
	    int left = (int) (layoutParams.left * cellSize) + getPaddingLeft() + spacing;
	    int right = (int) ((layoutParams.left + layoutParams.width) * cellSize) + getPaddingLeft() - spacing;
	    int bottom = (int) ((layoutParams.top + layoutParams.height) * cellSize) + getPaddingTop() - spacing;

	    child.layout(left, top, right, bottom);
	}
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
	return new CellLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
	return p instanceof CellLayout.LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
	return new CellLayout.LayoutParams(p);
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
	return new LayoutParams();
    }

    public static class LayoutParams extends ViewGroup.LayoutParams {

	/**
	 * An Y coordinate of the top most cell the view resides in.
	 */
	int top = 0;

	/**
	 * An X coordinate of the left most cell the view resides in.
	 */
	int left = 0;

	/**
	 * Number of cells occupied by the view horizontally.
	 */
	int width = 1;

	/**
	 * Number of cells occupied by the view vertically.
	 */
	int height = 1;

	public LayoutParams(Context context, AttributeSet attrs) {
	    super(context, attrs);
	    TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CellLayout);
	    left = a.getInt(R.styleable.CellLayout_layout_left, 0);
	    top = a.getInt(R.styleable.CellLayout_layout_top, 0);
	    height = a.getInt(R.styleable.CellLayout_layout_cellsHeight, -1);
	    width = a.getInt(R.styleable.CellLayout_layout_cellsWidth, -1);

	    a.recycle();
	}

	public LayoutParams(ViewGroup.LayoutParams params) {
	    super(params);

	    if (params instanceof LayoutParams) {
		LayoutParams cellLayoutParams = (LayoutParams) params;
		left = cellLayoutParams.left;
		top = cellLayoutParams.top;
		height = cellLayoutParams.height;
		width = cellLayoutParams.width;
	    }
	}

	public LayoutParams() {
	    this(MATCH_PARENT, MATCH_PARENT);
	}

	public LayoutParams(int width, int height) {
	    super(width, height);
	}

    }

}
