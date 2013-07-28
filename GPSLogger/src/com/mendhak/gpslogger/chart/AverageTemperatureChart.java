/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mendhak.gpslogger.chart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.DisplayMetrics;
import android.view.View;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import org.achartengine.util.MathHelper;

/**
 * Average temperature demo chart.
 */
public class AverageTemperatureChart extends AbstractChart
{

    private static final long HOUR = 3600 * 1000;

    private static final long DAY = HOUR * 24;

    private static final int HOURS = 24;
    public static final int TEXT_SIZE_XHDPI = 32;
    public static final int TEXT_SIZE_HDPI = 24;
    public static final int TEXT_SIZE_MDPI = 24;
    public static final int TEXT_SIZE_LDPI = 18;

    /**
     * Returns the chart name.
     *
     * @return the chart name
     */
    public String getName() {
        return "Average temperature";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc() {
        return "The average temperature in 4 Greek islands (line chart)";
    }

    /**
     * Executes the chart demo.
     *
     * @param context the context
     * @return the built intent
     */
    public GraphicalView execute(Context context) {







        String[] titles = new String[] { "Inside" };
        long now = Math.round(new Date().getTime() / DAY) * DAY;
        List<Date[]> x = new ArrayList<Date[]>();
        for (int i = 0; i < titles.length; i++) {
//            Date[] dates = new Date[HOURS];
//            for (int j = 0; j < HOURS; j++) {
//                dates[j] = new Date(now - (HOURS - j) * HOUR);
//            }
            x.add(new Date[] {new Date(1375003091),new Date(1375004091),new Date(1375005091),new Date(1375006091),new Date(1375007091),
                    new Date(1375008091),new Date(1375009091),new Date(1375010091),new Date(1375011091),new Date(1375012091),
                    new Date(1375013091),new Date(1375014091),new Date(1375015091),new Date(1375016091),new Date(1375017091),
                    new Date(1375018091),new Date(1375019091),new Date(1375020091),new Date(1375021091),new Date(1375022091)});
        }
        List<double[]> values = new ArrayList<double[]>();

        values.add(new double[] { 21.2, 21.5, 21.7, 21.5, 21.4, 21.2, 21.5, 21.7, 21.5, 21.4, 21.2, 21.5, 21.7, 21.5, 21.4, 21.2, 21.5, 21.7, 21.5, 21.4 });
        //, 21.4, 21.3, 21.1, 20.6, 20.3, 20.2,
         //       19.9, 19.7, 19.6, 19.9, 20.3, 20.6, 20.9, 21.2, 21.6, 21.9, 22.1, 21.7, 21.5 });
//        values.add(new double[] { 1.9, 1.2, 0.9, 0.5, 0.1, -0.5, -0.6, MathHelper.NULL_VALUE,
//                MathHelper.NULL_VALUE, -1.8, -0.3, 1.4, 3.4, 4.9, 7.0, 6.4, 3.4, 2.0, 1.5, 0.9, -0.5,
//                MathHelper.NULL_VALUE, -1.9, -2.5, -4.3 });

        int[] colors = new int[] { Color.GREEN};
        PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++) {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        setChartSettings(renderer, "Altitude over time", null, "Meters",
                x.get(0)[0].getTime(), x.get(0)[4].getTime(), -5, 25, Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(10);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setShowLegend(false);
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setXLabelsAngle(125);
        renderer.setYLabelsAlign(Align.RIGHT);

        XYMultipleSeriesDataset dataset = buildDateDataset(titles, x, values);

//        double maxX = dataset.getSeries()[0].getMaxX();
//        double minX = maxX - 25; // deltaX is your required x-range
//        double maxY = dataset.getSeries()[0].getMinY();
//        double minY = dataset.getSeries()[0].getMaxY();
//
        renderer.setRange(new double[] { 1375018091, 1375032091, 10, 30 });



        switch (context.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_XHIGH:
                renderer.setMargins(new int[] { 40, 90, 25, 10 });
                renderer.setAxisTitleTextSize(TEXT_SIZE_XHDPI);
                renderer.setChartTitleTextSize(TEXT_SIZE_XHDPI);
                renderer.setLabelsTextSize(TEXT_SIZE_XHDPI);
                renderer.setLegendTextSize(TEXT_SIZE_XHDPI);
                break;
            case DisplayMetrics.DENSITY_HIGH:
                renderer.setMargins(new int[] { 30, 50, 20, 10 });
                renderer.setAxisTitleTextSize(TEXT_SIZE_HDPI);
                renderer.setChartTitleTextSize(TEXT_SIZE_HDPI);
                renderer.setLabelsTextSize(TEXT_SIZE_HDPI);
                renderer.setLegendTextSize(TEXT_SIZE_HDPI);
                break;
            default:
                renderer.setMargins(new int[] { 30, 50, 20, 10 });
                renderer.setAxisTitleTextSize(TEXT_SIZE_LDPI);
                renderer.setChartTitleTextSize(TEXT_SIZE_LDPI);
                renderer.setLabelsTextSize(TEXT_SIZE_LDPI);
                renderer.setLegendTextSize(TEXT_SIZE_LDPI);
                break;
        }

//        Intent intent = ChartFactory.getTimeChartIntent(context, buildDateDataset(titles, x, values),
//                renderer, "h:mm a");
//

        return ChartFactory.getTimeChartView(context, dataset, renderer, "h:mm a");
    }



}
