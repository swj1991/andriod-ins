
package instapi.ui;

import android.support.v4.app.LoaderManager.LoaderCallbacks;

import instapi.AsyncResourceLoader.Result;


public interface Loadable<T> extends LoaderCallbacks<Result<T>> {

    boolean hasMore();

    boolean hasError();

    void init();

    void destroy();

    boolean isReadyToLoadMore();

    void loadMore();
}
