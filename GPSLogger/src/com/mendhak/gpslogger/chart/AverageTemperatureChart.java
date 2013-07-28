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
    public String getName()
    {
        return "Average temperature";
    }

    /**
     * Returns the chart description.
     *
     * @return the chart description
     */
    public String getDesc()
    {
        return "The average temperature in 4 Greek islands (line chart)";
    }

    /**
     * Executes the chart demo.
     *
     * @param context the context
     * @return the built intent
     */
    public GraphicalView execute(Context context)
    {


        String[] titles = new String[]{"Inside"};
        long now = Math.round(new Date().getTime() / DAY) * DAY;
        List<Date[]> x = new ArrayList<Date[]>();
        for (int i = 0; i < titles.length; i++)
        {

            x.add(new Date[]{
                    new Date(1373120442000L),
                    new Date(1373120442000L),
                    new Date(1373120502000L),
                    new Date(1373120562000L),
                    new Date(1373120602000L),
                    new Date(1373120622000L),
                    new Date(1373120641000L),
                    new Date(1373120647000L),
                    new Date(1373120652000L),
                    new Date(1373122018000L),
                    new Date(1373122021000L),
                    new Date(1373122024000L),
                    new Date(1373122028000L)
            });
        }
        List<double[]> values = new ArrayList<double[]>();

        values.add(new double[]{
                76.5999984741211,
                76.0999984741211,
                74.80000305175781,
                81.30000305175781,
                82.19999694824219,
                81.5,
                81.19999694824219,
                81.19999694824219,
                84.0,
                81.9000015258789,
                83.4000015258789,
                78.5999984741211,
                77.29183
        });


        int[] colors = new int[]{Color.GREEN};
        PointStyle[] styles = new PointStyle[]{PointStyle.CIRCLE};
        XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
        int length = renderer.getSeriesRendererCount();
        for (int i = 0; i < length; i++)
        {
            ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
        }
        setChartSettings(renderer, "Altitude over time", null, "Meters",
                x.get(0)[0].getTime(), x.get(0)[x.get(0).length-1].getTime(), 50, 100, Color.LTGRAY, Color.LTGRAY);
        renderer.setXLabels(10);
        renderer.setYLabels(10);
        renderer.setShowGrid(true);
        renderer.setShowLegend(false);
        renderer.setXLabelsAlign(Align.CENTER);
        renderer.setXLabelsAngle(77);
        renderer.setYLabelsAlign(Align.RIGHT);

        XYMultipleSeriesDataset dataset = buildDateDataset(titles, x, values);


        renderer.setRange(new double[]{x.get(0)[0].getTime(), x.get(0)[x.get(0).length-1].getTime(), 10, 180});


        switch (context.getResources().getDisplayMetrics().densityDpi)
        {
            case DisplayMetrics.DENSITY_XHIGH:
                renderer.setMargins(new int[]{40, 90, 25, 10});
                renderer.setAxisTitleTextSize(TEXT_SIZE_XHDPI);
                renderer.setChartTitleTextSize(TEXT_SIZE_XHDPI);
                renderer.setLabelsTextSize(TEXT_SIZE_XHDPI);
                renderer.setLegendTextSize(TEXT_SIZE_XHDPI);
                break;
            case DisplayMetrics.DENSITY_HIGH:
                renderer.setMargins(new int[]{30, 50, 20, 10});
                renderer.setAxisTitleTextSize(TEXT_SIZE_HDPI);
                renderer.setChartTitleTextSize(TEXT_SIZE_HDPI);
                renderer.setLabelsTextSize(TEXT_SIZE_HDPI);
                renderer.setLegendTextSize(TEXT_SIZE_HDPI);
                break;
            default:
                renderer.setMargins(new int[]{30, 50, 20, 10});
                renderer.setAxisTitleTextSize(TEXT_SIZE_LDPI);
                renderer.setChartTitleTextSize(TEXT_SIZE_LDPI);
                renderer.setLabelsTextSize(TEXT_SIZE_LDPI);
                renderer.setLegendTextSize(TEXT_SIZE_LDPI);
                break;
        }

//        Intent intent = ChartFactory.getTimeChartIntent(context, buildDateDataset(titles, x, values),
//                renderer, "h:mm a");
//

        return ChartFactory.getTimeChartView(context, dataset, renderer, "H:mm");
    }


}
