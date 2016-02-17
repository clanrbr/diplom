package imagemanipulation.path;

import android.content.Context;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import imagemanipulation.path.parser.IoUtil;
import imagemanipulation.path.parser.PathInfo;
import imagemanipulation.path.parser.SvgToPath;

public class SvgUtil {
    private static final Map<Integer, PathInfo> PATH_MAP = new ConcurrentHashMap<Integer, PathInfo>();

    public static final PathInfo readSvg(Context context, int resId) {
        PathInfo pathInfo = PATH_MAP.get(resId);
        if(pathInfo == null) {
            InputStream is = null;
            try {
                is = context.getResources().openRawResource(resId);
                pathInfo = SvgToPath.getSVGFromInputStream(is);
                PATH_MAP.put(resId, pathInfo);
            } finally {
                IoUtil.closeQuitely(is);
            }
        }

        return pathInfo;
    }
}
