package top.potens.ptchat.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wenshao on 2018/8/27.
 * RecyclerView滚动类
 */

public class RecyclerViewScroll {
    private static final Logger logger = LoggerFactory.getLogger(RecyclerViewScroll.class);

    // 当前item的索引
    private int index = 0;
    // RecyclerView 对象
    private RecyclerView recyclerView;
    // linearLayoutManager对象
    private LinearLayoutManager linearLayoutManager;

    private boolean isMove = false;


    public RecyclerViewScroll(RecyclerView recyclerView, final LinearLayoutManager linearLayoutManager) {
        this.recyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isMove && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isMove = false;
                    int n = index - linearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < recyclerView.getChildCount()) {
                        int top = recyclerView.getChildAt(n).getTop();
                        logger.debug("onScrollStateChanged top=" + top);
                        recyclerView.smoothScrollBy(0, top);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isMove) {
                    isMove = false;
                    int n = index - linearLayoutManager.findFirstVisibleItemPosition();
                    if (0 <= n && n < recyclerView.getChildCount()) {
                        int top = recyclerView.getChildAt(n).getTop();
                        logger.debug("onScrolled top=" + top);
                        recyclerView.smoothScrollBy(0, top);
                    }
                }
            }

        });

    }

    public int getIndex() {
        return index;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    /**
     * 指定下标为n的item置顶
     *
     * @param n        下标
     * @param isSmooth 是否平滑移动  在键盘打开的情况下必须用smoothScrollToPosition模拟滚动 使用scrollToPosition无效
     */
    public void move(int n, boolean isSmooth) {
        if (n < 0) {
            return;
        }
        index = n;
        recyclerView.stopScroll();
        if (isSmooth) smoothMoveToPosition(n);
        else moveToPosition(n);
    }

    /**
     * 直接移动到底部
     */
    public void moveBottom(boolean isSmooth) {
        move(recyclerView.getAdapter().getItemCount() -1, isSmooth);
    }

    private void smoothMoveToPosition(int n) {
        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            recyclerView.smoothScrollToPosition(n);
        } else if (n <= lastItem) {
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.smoothScrollBy(0, top);
        } else {
            recyclerView.smoothScrollToPosition(n);
            isMove = true;
        }

    }

    private void moveToPosition(int n) {

        int firstItem = linearLayoutManager.findFirstVisibleItemPosition();
        int lastItem = linearLayoutManager.findLastVisibleItemPosition();
        if (n <= firstItem) {
            recyclerView.scrollToPosition(n);
        } else if (n <= lastItem) {
            int top = recyclerView.getChildAt(n - firstItem).getTop();
            recyclerView.scrollBy(0, top);
        } else {
            recyclerView.scrollToPosition(n);
            isMove = true;
        }
    }



}
