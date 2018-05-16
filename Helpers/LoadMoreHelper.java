package idv.haojun.helpers;

import java.util.ArrayList;
import java.util.List;

public class LoadMoreHelper {

    public interface LoadMoreListener {
        void onLoadMore();
    }

    // load more data
    private static final int REMAIN_TO_LOAD_MORE = 2;
    private static final int ONCE_LOAD_COUNT = 10;
    private boolean loading = false;
    private int index = -1;
    // listener
    private LoadMoreListener loadMoreListener;
    // data
    private List<Integer> ids;

    public LoadMoreHelper() {
        this.ids = new ArrayList<>();
    }

    public LoadMoreHelper(LoadMoreListener loadMoreListener) {
        this();
        this.loadMoreListener = loadMoreListener;
    }

    public void setIds(List<Integer> ids) {
        this.ids.clear();
        this.ids.addAll(ids);
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    public void clear() {
        this.index = -1;
        this.ids.clear();
    }

    public boolean isLoading() {
        return loading;
    }

    public void changeIndex() {
        index = index + ONCE_LOAD_COUNT > ids.size() - 1 ? ids.size() - 1 : index + ONCE_LOAD_COUNT;
    }

    /*  LoadMore 觸發條件
    *
    *   totalItemCount != 0 >> RecyclerView不是空的
    *   (lastVisibleItem + remainToLoadMore) >= totalItemCount >> 滑到剩下remainToLoadMore個時
    *   !loading >> 不是在Loading狀態
    *   ids.size() != totalItemCount >> 還沒滑到底時
    *   loadMoreListener != null >> 有註冊監聽
    */

    public void scroll(int totalItemCount, int lastVisibleItem) {
        if (totalItemCount != 0 && (lastVisibleItem + REMAIN_TO_LOAD_MORE) >= totalItemCount &&
                !loading && ids.size() != totalItemCount && loadMoreListener != null) {
            loadMoreListener.onLoadMore();
        }
    }

    public List<Integer> nextIds() {
        List<Integer> nextIds = new ArrayList<>();
        int start = index + 1;
        int end = index + ONCE_LOAD_COUNT > ids.size() - 1 ? ids.size() - 1 : index + ONCE_LOAD_COUNT;
        for (int i = start; i <= end; i++) {
            nextIds.add(ids.get(i));
        }
        return nextIds;
    }
}
