package com.android.customview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CalvingPeriodAnalysisActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle1;
    @BindView(R.id.tv_count_no)
    AppCompatTextView tvCountNo1;
    @BindView(R.id.iv_image)
    AppCompatImageView ivImage;
    //    @BindView(R.id.tv_title)
//    AppCompatTextView tvTitle2;
//    @BindView(R.id.tv_count_no)
//    AppCompatTextView tvCountNo2;
//    @BindView(R.id.tv_title)
//    AppCompatTextView tvTitle3;
//    @BindView(R.id.tv_count_no)
//    AppCompatTextView tvCountNo3;
//    @BindView(R.id.tv_title)
//    AppCompatTextView tvTitle4;
//    @BindView(R.id.tv_count_no)
//    AppCompatTextView tvCountNo4;
//    @BindView(R.id.tv_title)
//    AppCompatTextView tvTitle5;
//    @BindView(R.id.tv_count_no)
//    AppCompatTextView tvCountNo5;
//    @BindView(R.id.tv_title)
//    AppCompatTextView tvTitle6;
//    @BindView(R.id.tv_count_no)
//    AppCompatTextView tvCountNo6;
//    @BindView(R.id.tv_title)
//    AppCompatTextView tvTitle7;
//    @BindView(R.id.tv_count_no)
//    AppCompatTextView tvCountNo7;
//    @BindView(R.id.tv_title)
//    AppCompatTextView tvTitle8;
//    @BindView(R.id.tv_count_no)
//    AppCompatTextView tvCountNo8;
    private LineChart lineChart;
    private LineData lineData;
    private String[] images = new String[]{"https://homepages.cae.wisc.edu/~ece533/images/airplane.png"
            , "https://homepages.cae.wisc.edu/~ece533/images/arctichare.png"
            , "https://homepages.cae.wisc.edu/~ece533/images/baboon.png"
            , "https://homepages.cae.wisc.edu/~ece533/images/baboon.png"
            , "https://homepages.cae.wisc.edu/~ece533/images/baboon.png"
            , "https://homepages.cae.wisc.edu/~ece533/images/arctichare.png"
            , "https://homepages.cae.wisc.edu/~ece533/images/arctichare.png"
            , "https://homepages.cae.wisc.edu/~ece533/images/baboon.png"
            , "https://homepages.cae.wisc.edu/~ece533/images/baboon.png"
            , "https://homepages.cae.wisc.edu/~ece533/images/baboon.png"};
    private View[] bitmapList;
    private float[] dataX;
    private float[] dataY;

    public static Bitmap createBitmapFromView(View customMarkerView) {
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_calving_period_analysis);
        ButterKnife.bind(this);
        lineData = new LineData();
        setupGrid();
        setUpChart();
    }

    private void setUpChart() {
        lineChart = findViewById(R.id.lineCartView);

        //mFragmentHomeBinding.barChart.setMaxVisibleValueCount(6);
        float barSpace = 0f, groupSpace = 0.7f;

        lineChart.setPinchZoom(true);
        lineChart.setScaleEnabled(true);
        lineChart.animateXY(1000, 1000);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.getViewPortHandler().setMaximumScaleX(2f);
        lineChart.getViewPortHandler().setMaximumScaleY(2f);

        YAxis axisRight = lineChart.getAxisRight();
        axisRight.setEnabled(false);

        setUpBlueLine();
//        setUpYellowLine();
        // X axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setAxisLineColor(R.color.black);
        xAxis.setAxisLineWidth(3f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1); // only intervals of 1 day
        xAxis.setGranularityEnabled(true);

        // Y axis
        YAxis yAxis = lineChart.getAxisLeft();
        yAxis.setAxisLineColor(R.color.black);
        yAxis.setAxisLineWidth(3f);
        yAxis.setDrawGridLines(false);
        yAxis.setGranularity(0.1f); // only intervals of 1 day
        yAxis.setGranularityEnabled(true);

        xAxis.setAxisMinimum(0.5f);
        xAxis.setAxisMaximum(getMaxFloat(dataX));
        yAxis.setAxisMinimum(-0.7f);
        yAxis.setAxisMaximum(24f);
        lineChart.setDragXEnabled(true);
        lineChart.setVisibleXRangeMaximum(7f);
        lineChart.moveViewToX(0.5f);
    }

    public static float getMaxFloat(float[] data) {

        float[] copy = Arrays.copyOf(data, data.length);
        Arrays.sort(copy);
        return copy[data.length - 1];
    }

    public static float getMinFloat(float[] data) {

        float[] copy = Arrays.copyOf(data, data.length);
        Arrays.sort(copy);
        return copy[0];
    }

    private void setUpBlueLine() {
        dataX = new float[]{1f, 1f, 3f, 4f, 5f,6f,7f,8f,9f,10f};
        dataY = new float[]{1f, 3f, 3f, 6f, 2f,8f,5f,10f,2f,7f};
        bitmapList = new View[dataX.length];
        for (int i = 0; i < dataX.length; i++) {
            bitmapList[i] = LayoutInflater.from(this).inflate(R.layout.layout_custom_data_point, null);
            ((AppCompatTextView) bitmapList[i].findViewById(R.id.tv_custm_text)).setText("" + i);
        }
        for (int i = 0; i < dataX.length; i++) {
            loadImage(images[i], i);
        }
    }

    private void loadImage(String url, final int position) {
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.circleCropTransform())
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        ((ImageView) bitmapList[position].findViewById(R.id.iv_custom_image)).setImageDrawable(resource);
                        ArrayList<Entry> entries = new ArrayList<>();
                        for (int i = 0; i < dataX.length; i++) {
                            entries.add(i, new Entry(dataX[i], dataY[i], new BitmapDrawable(getResources(), createBitmapFromView(bitmapList[i]))));
                        }
                        LineDataSet dataSet = new LineDataSet(entries, " \n ");
                        lineData.addDataSet(dataSet);
                        lineData.setDrawValues(false);
                        lineChart.setData(lineData);
                        lineChart.invalidate();
                        lineChart.refreshDrawableState();
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        Log.e("a", "onLoadCleared: ");
                    }
                });
    }

    private void setupGrid() {
        View grid1 = findViewById(R.id.grid_1);
        View grid2 = findViewById(R.id.grid_2);
        View grid3 = findViewById(R.id.grid_3);
        View grid4 = findViewById(R.id.grid_4);
        View grid5 = findViewById(R.id.grid_5);
        View grid6 = findViewById(R.id.grid_6);
        View grid7 = findViewById(R.id.grid_7);
        View grid8 = findViewById(R.id.grid_8);

        AppCompatTextView tvTitle1 = grid1.findViewById(R.id.tv_title);
        AppCompatTextView tvTitle2 = grid2.findViewById(R.id.tv_title);
        AppCompatTextView tvTitle3 = grid3.findViewById(R.id.tv_title);
        AppCompatTextView tvTitle4 = grid4.findViewById(R.id.tv_title);
        AppCompatTextView tvTitle5 = grid5.findViewById(R.id.tv_title);
        AppCompatTextView tvTitle6 = grid6.findViewById(R.id.tv_title);
        AppCompatTextView tvTitle7 = grid7.findViewById(R.id.tv_title);
        AppCompatTextView tvTitle8 = grid8.findViewById(R.id.tv_title);

        AppCompatTextView tvNumber1 = grid1.findViewById(R.id.tv_count_no);
        AppCompatTextView tvNumber2 = grid2.findViewById(R.id.tv_count_no);
        AppCompatTextView tvNumber3 = grid3.findViewById(R.id.tv_count_no);
        AppCompatTextView tvNumber4 = grid4.findViewById(R.id.tv_count_no);
        AppCompatTextView tvNumber5 = grid5.findViewById(R.id.tv_count_no);
        AppCompatTextView tvNumber6 = grid6.findViewById(R.id.tv_count_no);
        AppCompatTextView tvNumber7 = grid7.findViewById(R.id.tv_count_no);
        AppCompatTextView tvNumber8 = grid8.findViewById(R.id.tv_count_no);

        tvTitle1.setText("Straws Used\n" + "Per heat cycle");
        tvTitle2.setText("Inter Calving\n" + "period (days)");
        tvTitle3.setText("Gestation\n" + "Period");
        tvTitle4.setText("Anoestrus\n" + "(days)");
        tvTitle5.setText("Location\n" + "length (days)");
        tvTitle6.setText("Repeater\n" + "(days)");
        tvTitle7.setText("Location\n" + "yield (liters)");
        tvTitle8.setText("Dry days");

        tvNumber1.setText("4");
        tvNumber2.setText("5");
        tvNumber3.setText("10");
        tvNumber4.setText("6");
        tvNumber5.setText("11");
        tvNumber6.setText("4");
        tvNumber7.setText("2");
        tvNumber8.setText("7");
    }

//    private void setUpYellowLine() {
//        ArrayList<Entry> entriesYellow = new ArrayList<>();
//        entriesYellow.add(new Entry(0f, 0f, R.drawable.ic_insert_emoticon));
//        entriesYellow.add(new Entry(1.5f, 1.5f, R.drawable.ic_insert_emoticon));
//        entriesYellow.add(new Entry(1.5f, 1f, R.drawable.ic_insert_emoticon));
//        entriesYellow.add(new Entry(2f, 2f, R.drawable.ic_insert_emoticon));
//        entriesYellow.add(new Entry(2.5f, 5f, R.drawable.ic_insert_emoticon));
//        LineDataSet dataSet2 = new LineDataSet(entriesYellow, "Yellow");
//        dataSet2.setColor(Color.BLACK);
//        lineData.addDataSet(dataSet2);
//        lineChart.setData(lineData);
//        lineChart.invalidate();
//    }


    private Bitmap GetBitmapClippedCircle(Bitmap bitmap) {

        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final Bitmap outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        final Path path = new Path();
        path.addCircle(
                (float) (width / 2)
                , (float) (height / 2)
                , (float) Math.min(width, (height / 2))
                , Path.Direction.CCW);

        final Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(bitmap, 0, 0, null);
        return outputBitmap;
    }


    class ImageLineChartRenderer extends LineChartRenderer {
        private final View[] imageList;
        private float[] mCirclesBuffer = new float[2];

        ImageLineChartRenderer(LineChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler, View[] imageList) {
            super(chart, animator, viewPortHandler);
            this.imageList = imageList;
        }

        @Override
        protected void drawCircles(Canvas c) {
            mRenderPaint.setStyle(Paint.Style.FILL);
            float phaseY = mAnimator.getPhaseY();
            mCirclesBuffer[0] = 0;
            mCirclesBuffer[1] = 0;
            List<ILineDataSet> dataSets = mChart.getLineData().getDataSets();
//            LayoutInflater li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View v = li.inflate(R.layout.activity_custom_background, null);
//            v.measure(View.MeasureSpec.getSize(v.getMeasuredWidth()), View.MeasureSpec.getSize(v.getMeasuredHeight()));
//            v.layout(400, 400, 400, 400);
//            v.draw(c);

            //Draw bitmap image for every dataX set with size as radius * 10, and store it in scaled bitmaps array
            Bitmap[] scaledBitmaps = new Bitmap[dataSets.size()];
            float[] scaledBitmapOffsets = new float[dataSets.size()];
            for (int i = 0; i < dataSets.size(); i++) {
                float imageSize = dataSets.get(i).getCircleRadius() * 15;
                scaledBitmapOffsets[i] = imageSize / 2;
                scaledBitmaps[i] = scaleImage((int) imageSize, i);
            }

            for (int i = 0; i < dataSets.size(); i++) {
                ILineDataSet dataSet = dataSets.get(i);

                if (!dataSet.isVisible() || !dataSet.isDrawCirclesEnabled() || dataSet.getEntryCount() == 0)
                    continue;

                mCirclePaintInner.setColor(dataSet.getCircleHoleColor());
                Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());
                mXBounds.set(mChart, dataSet);


                int boundsRangeCount = mXBounds.range + mXBounds.min;
                for (int j = mXBounds.min; j <= boundsRangeCount; j++) {
                    Entry e = dataSet.getEntryForIndex(j);
                    if (e == null) break;
                    mCirclesBuffer[0] = e.getX();
                    mCirclesBuffer[1] = e.getY() * phaseY;
                    trans.pointValuesToPixel(mCirclesBuffer);
                    if (!mViewPortHandler.isInBoundsRight(mCirclesBuffer[0]))
                        break;
                    if (!mViewPortHandler.isInBoundsLeft(mCirclesBuffer[0]) || !mViewPortHandler.isInBoundsY(mCirclesBuffer[1]))
                        continue;

                    if (scaledBitmaps[i] != null) {
                        c.drawBitmap(scaledBitmaps[i],
                                mCirclesBuffer[0] - scaledBitmapOffsets[i],
                                mCirclesBuffer[1] - scaledBitmapOffsets[i],
                                mRenderPaint);
                    }
                }
            }

        }


        private Bitmap scaleImage(int radius, int position) {
            return Bitmap.createScaledBitmap(createBitmapFromView(imageList[position]), radius, radius, false);
        }
    }
}
