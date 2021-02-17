package com.example.BBATTLE;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;




public class BattleInfoFragment extends Fragment {

    String[] exercise = {"스쿼트", "줄넘기", "걷기"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_battle_fragment_info, container, false);

        LineChart lineChart = (LineChart)view.findViewById(R.id.chart);
        Spinner spinner = (Spinner)view.findViewById(R.id.spinner);

        //스피너 어댑터 장착
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.spiner_font,exercise);
        spinner.setAdapter(adapter);

        LineDataSet lineDataSet = new LineDataSet(dataValues(), "나");
        LineDataSet lineDataSetEnermy = new LineDataSet(dataValuesEnermy(), "적");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        dataSets.add(lineDataSetEnermy);

        // 나의 그래프 셋팅
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSet.setColor(Color.parseColor("#000000"));
        lineDataSet.setLineWidth(3);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setDrawValues(false);
        lineDataSet.setFillColor(Color.BLACK);
        lineDataSet.setValueTextColor(Color.RED);
        lineDataSet.setFillFormatter((dataSet, dataProvider) -> Color.RED);

        // 적의 그래프 셋팅
        lineDataSetEnermy.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        lineDataSetEnermy.setColor(Color.parseColor("#ff0000"));
        lineDataSetEnermy.setLineWidth(3);
        lineDataSetEnermy.setDrawCircles(false);
        lineDataSetEnermy.setDrawValues(false);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);

        //X축 설정
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setTypeface(Typeface.DEFAULT_BOLD);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        //Y축 왼쪽
        YAxis yLAxis = lineChart.getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setTypeface(Typeface.DEFAULT_BOLD);

        //Y축 오른쪽
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);

        // 왼쪽 하단 그래프 나타낼 목록
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.BLACK);
        legend.setTextSize(10);
        legend.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"dalmoori.ttf"));
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(20);
        legend.setXEntrySpace(20);

        int color[] = new int[] {Color.BLACK, Color.RED};
        String[] Name = {"나", "적"};

        // 배열을 통해 나와 적의 색을 바꿀 수 있다.
        LegendEntry[] legendEntry = new LegendEntry[2];
        for(int i=0; i<legendEntry.length; i++){
            LegendEntry entry = new LegendEntry();
            entry.formColor = color[i];
            entry.label = Name[i];
            legendEntry[i] = entry;
        }
        legend.setCustom(legendEntry);

        //오른쪽 하단 설명 글씨

        /*Description description = new Description();
        description.setText("스쿼트");
        description.setTextSize(20);
        description.setXOffset(20);
        description.setYOffset(20);
        description.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),"dalmoori.ttf"));*/

        //차트의 설정
        lineChart.setBackgroundColor(Color.parseColor("#ffffff"));
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setDrawGridBackground(true);
//        lineChart.setDescription(description);
        lineChart.setNoDataText("데이터가 없습니다.");
        lineChart.setDrawBorders(true);
        lineChart.setBorderColor(Color.BLACK);
        lineChart.setBorderWidth(5);
        lineChart.setNoDataTextColor(Color.parseColor("#000000"));
        lineChart.animateY(1000, Easing.EasingOption.EaseInCubic);
        lineChart.invalidate();

        return view;
    }

    //나의 그래프 데이터
    private ArrayList<Entry> dataValues() {
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(1, 100));
        entries.add(new Entry(2, 70));
        entries.add(new Entry(3, 100));
        entries.add(new Entry(4, 60));
        entries.add(new Entry(5, 70));
        entries.add(new Entry(6, 70));
        entries.add(new Entry(7, 70));
        return entries;
    }

    // 적의 그래프 데이터
   private ArrayList<Entry> dataValuesEnermy() {
        ArrayList<Entry> entriesEnermy = new ArrayList<>();
        entriesEnermy.add(new Entry(1, 50));
        entriesEnermy.add(new Entry(2, 100));
        entriesEnermy.add(new Entry(3, 70));
        entriesEnermy.add(new Entry(4, 60));
        entriesEnermy.add(new Entry(5, 80));
        entriesEnermy.add(new Entry(6, 100));
        entriesEnermy.add(new Entry(7, 20));
        return entriesEnermy;
    }
}