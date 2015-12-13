package com.michaelvescovo.moviehotness.view_movies.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.michaelvescovo.moviehotness.R;
import com.michaelvescovo.moviehotness.util.BitmapHelper;
import com.michaelvescovo.moviehotness.view_movies.entity.MoviePreviewInterface;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Michael on 25/11/15.
 *
 */
public class PosterAdapter extends RecyclerView.Adapter implements Serializable {
    private static final String TAG = "PosterAdapter";
    public int mSortBy = -1;
    private ArrayList<MoviePreviewInterface> mDataset = new ArrayList<>();
    BitmapHelper mBitmapHelper = new BitmapHelper();

    public PosterAdapter(int sortBy) {
        mSortBy = sortBy;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_grid_card, parent, false);
        return new MovieViewHolder(v, mDataset);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ImageView imageView = (ImageView) ((MovieViewHolder) holder).getView().findViewById(R.id.poster_image);
        mBitmapHelper.loadBitmap(imageView, imageView.getContext().getFilesDir() + "/" + "preview_poster_" + mDataset.get(position).getId());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateDataset(MoviePreviewInterface movie) {
        mDataset.add(movie);
        notifyItemInserted(getItemCount() - 1);
    }

//    /*
//    * Helper methods to load posters
//    * Code taken and modified from http://developer.android.com/training/displaying-bitmaps/index.html
//    *
//    * */
//    public void loadBitmap(ImageView imageView, String path) {
//        Bitmap.Config config = Bitmap.Config.ALPHA_8;
//        Bitmap mPlaceHolderBitmap = Bitmap.createBitmap(350, 500, config);
//
//        if (cancelPotentialWork(imageView, path)) {
//            final BitmapWorkerTask task = new BitmapWorkerTask(imageView, path);
//            final AsyncDrawable asyncDrawable = new AsyncDrawable(imageView.getContext().getResources(), mPlaceHolderBitmap, task);
//            imageView.setImageDrawable(asyncDrawable);
//            task.execute(path);
//        }
//    }
//
//    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
//        private final WeakReference<ImageView> imageViewReference;
//        private String path = "";
//
//        public BitmapWorkerTask(ImageView imageView, String path) {
//            // Use a WeakReference to ensure the ImageView can be garbage collected
//            imageViewReference = new WeakReference<ImageView>(imageView);
//            this.path = path;
//        }
//
//        // Decode image in background.
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            path = params[0];
//            return decodeSampledBitmapFromFile(path, 350, 550);
//        }
//
//        // Once complete, see if ImageView is still around and set bitmap.
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            if (isCancelled()) {
//                bitmap = null;
//            }
//
//            if (imageViewReference != null && bitmap != null) {
//                final ImageView imageView = imageViewReference.get();
//                final BitmapWorkerTask bitmapWorkerTask =
//                        getBitmapWorkerTask(imageView);
//                if (this == bitmapWorkerTask && imageView != null) {
//                    imageView.setImageBitmap(bitmap);
//                }
//            }
//        }
//    }
//
//    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) {
//
//        // First decode with inJustDecodeBounds=true to check dimensions
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(path, options);
//
//        // Calculate inSampleSize
//        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeFile(path, options);
//    }
//
//    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
//        // Raw height and width of image
//        final int height = options.outHeight;
//        final int width = options.outWidth;
//        int inSampleSize = 1;
//
//        if (height > reqHeight || width > reqWidth) {
//
//            final int halfHeight = height / 2;
//            final int halfWidth = width / 2;
//
//            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
//            // height and width larger than the requested height and width.
//            while ((halfHeight / inSampleSize) > reqHeight
//                    && (halfWidth / inSampleSize) > reqWidth) {
//                inSampleSize *= 2;
//            }
//        }
//
//        return inSampleSize;
//    }
//
//    static class AsyncDrawable extends BitmapDrawable {
//        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;
//
//        public AsyncDrawable(Resources res, Bitmap bitmap, BitmapWorkerTask bitmapWorkerTask) {
//            super(res, bitmap);
//            bitmapWorkerTaskReference = new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
//        }
//
//        public BitmapWorkerTask getBitmapWorkerTask() {
//            return bitmapWorkerTaskReference.get();
//        }
//    }
//
//    public static boolean cancelPotentialWork(ImageView imageView, String path) {
//        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
//
//        if (bitmapWorkerTask != null) {
//            final String bitmapData = bitmapWorkerTask.path;
//            // If bitmapData is not yet set or it differs from the new data
//            if (bitmapData.contentEquals("") || !bitmapData.contentEquals(path)) {
//                // Cancel previous task
//                bitmapWorkerTask.cancel(true);
//            } else {
//                // The same work is already in progress
//                return false;
//            }
//        }
//        // No task associated with the ImageView, or an existing task was cancelled
//        return true;
//    }
//
//    private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
//        if (imageView != null) {
//            final Drawable drawable = imageView.getDrawable();
//            if (drawable instanceof AsyncDrawable) {
//                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
//                return asyncDrawable.getBitmapWorkerTask();
//            }
//        }
//        return null;
//    }
}