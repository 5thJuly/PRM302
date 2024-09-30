package com.example.fap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.fap.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WeeklyTimetableFragment extends Fragment {
    private TextView currentWeekText;
    private RecyclerView scheduleRecyclerView;
    private ScheduleAdapter scheduleAdapter;
    private HorizontalScrollView semesterScrollView;
    private LinearLayout semesterButtonsLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_timetable, container, false);

        currentWeekText = view.findViewById(R.id.current_week_text);
        scheduleRecyclerView = view.findViewById(R.id.schedule_recycler_view);
        semesterScrollView = view.findViewById(R.id.semester_scroll_view);
        semesterButtonsLayout = view.findViewById(R.id.semester_buttons_layout);

        initializeViews();
        fetchScheduleData();

        return view;
    }

    private void initializeViews() {
        updateCurrentWeekText();

        scheduleRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        scheduleAdapter = new ScheduleAdapter(new ArrayList<>());
        scheduleRecyclerView.setAdapter(scheduleAdapter);

        String[] semesters = {"FALL2024", "SUMMER2024", "SPRING2024"};
        for (String semester : semesters) {
            Button button = new Button(getContext());
            button.setText(semester);
            button.setOnClickListener(v -> onSemesterButtonClick(semester));
            semesterButtonsLayout.addView(button);
        }
    }

    private void updateCurrentWeekText() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        String startDate = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        String endDate = sdf.format(cal.getTime());
        currentWeekText.setText(String.format("Current week: %s - %s", startDate, endDate));
    }

    private void onSemesterButtonClick(String semester) {
        // TODO: Implement semester change logic
        fetchScheduleData(); // For now, just refresh the data
    }

    private void fetchScheduleData() {
        // TODO: Implement actual API call
        // For now, we'll use dummy data
        List<ScheduleItem> dummyData = getDummyScheduleData();
        scheduleAdapter.updateData(dummyData);
    }

    private List<ScheduleItem> getDummyScheduleData() {
        List<ScheduleItem> dummyData = new ArrayList<>();
        dummyData.add(new ScheduleItem("MLN111", "NVH 302", "7:00", "9:15", "MKT1707", "DuyNK32"));
        dummyData.add(new ScheduleItem("WDU203c", "106", "19:30", "21:00", "WDU203c_FA24_Csr01", "PhuongLHK"));
        return dummyData;
    }

    // ScheduleItem class
    private static class ScheduleItem {
        String subjectCode, room, startTime, endTime, groupClass, lecturer;

        ScheduleItem(String subjectCode, String room, String startTime, String endTime, String groupClass, String lecturer) {
            this.subjectCode = subjectCode;
            this.room = room;
            this.startTime = startTime;
            this.endTime = endTime;
            this.groupClass = groupClass;
            this.lecturer = lecturer;
        }
    }

    // ScheduleAdapter class
    private class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
        private List<ScheduleItem> scheduleItems;

        ScheduleAdapter(List<ScheduleItem> scheduleItems) {
            this.scheduleItems = scheduleItems;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ScheduleItem item = scheduleItems.get(position);
            holder.subjectCodeText.setText(item.subjectCode);
            holder.roomText.setText("Room: " + item.room);
            holder.timeText.setText(item.startTime + " - " + item.endTime);
            holder.groupClassText.setText("Group: " + item.groupClass);
            holder.lecturerText.setText("Lecturer: " + item.lecturer);
        }

        @Override
        public int getItemCount() {
            return scheduleItems.size();
        }

        void updateData(List<ScheduleItem> newData) {
            scheduleItems = newData;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView subjectCodeText, roomText, timeText, groupClassText, lecturerText;

            ViewHolder(View itemView) {
                super(itemView);
                subjectCodeText = itemView.findViewById(R.id.subject_code_text);
                roomText = itemView.findViewById(R.id.room_text);
                timeText = itemView.findViewById(R.id.time_text);
                groupClassText = itemView.findViewById(R.id.group_class_text);
                lecturerText = itemView.findViewById(R.id.lecturer_text);
            }
        }
    }
}