package xunhu.instagram;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hu on 5/10/2015.
 */
public class feedAdapter extends ArrayAdapter {
    private List list = new ArrayList();
    public feedAdapter(Context context, int resource) {
        super(context, resource);
    }

}
