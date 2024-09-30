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

import java.util.ArrayList;
import java.util.List;

public class ExamScheduleFragment extends Fragment {
    private TextView examCountText;
    private RecyclerView examRecyclerView;
    private ExamAdapter examAdapter;
    private HorizontalScrollView semesterScrollView;
    private LinearLayout semesterButtonsLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_schedule, container, false);

        examCountText = view.findViewById(R.id.exam_count_text);
        examRecyclerView = view.findViewById(R.id.exam_recycler_view);
        semesterScrollView = view.findViewById(R.id.semester_scroll_view);
        semesterButtonsLayout = view.findViewById(R.id.semester_buttons_layout);

        initializeViews();
        fetchExamData();

        return view;
    }

    private void initializeViews() {
        examRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        examAdapter = new ExamAdapter(new ArrayList<>());
        examRecyclerView.setAdapter(examAdapter);

        String[] semesters = {"FALL2024", "SUMMER2024", "SPRING2024"};
        for (String semester : semesters) {
            Button button = new Button(getContext());
            button.setText(semester);
            button.setOnClickListener(v -> onSemesterButtonClick(semester));
            semesterButtonsLayout.addView(button);
        }
    }

    private void onSemesterButtonClick(String semester) {
        fetchExamData();
    }

    private void fetchExamData() {
        List<ExamItem> dummyData = getDummyExamData();
        examAdapter.updateData(dummyData);
        updateExamCount(dummyData.size());
    }

    private void updateExamCount(int count) {
        examCountText.setText("Number of exams: " + count);
    }

    private List<ExamItem> getDummyExamData() {
        List<ExamItem> dummyData = new ArrayList<>();
        dummyData.add(new ExamItem("13/10/2024", "MLN111", "222", "07:30", "09:00", "FE", "EOS (Multiple Choice)"));
        return dummyData;
    }

    private static class ExamItem {
        String date, subject, room, startTime, endTime, type, note;

        ExamItem(String date, String subject, String room, String startTime, String endTime, String type, String note) {
            this.date = date;
            this.subject = subject;
            this.room = room;
            this.startTime = startTime;
            this.endTime = endTime;
            this.type = type;
            this.note = note;
        }
    }

    private class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {
        private List<ExamItem> examItems;

        ExamAdapter(List<ExamItem> examItems) {
            this.examItems = examItems;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ExamItem item = examItems.get(position);
            holder.dateText.setText("Date: " + item.date);
            holder.subjectText.setText("Subject: " + item.subject);
            holder.roomText.setText("Room: " + item.room);
            holder.timeText.setText(item.startTime + " - " + item.endTime);
            holder.typeText.setText("Type: " + item.type);
            holder.noteText.setText("Note: " + item.note);
        }

        @Override
        public int getItemCount() {
            return examItems.size();
        }

        void updateData(List<ExamItem> newData) {
            examItems = newData;
            notifyDataSetChanged();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView dateText, subjectText, roomText, timeText, typeText, noteText;

            ViewHolder(View itemView) {
                super(itemView);
                dateText = itemView.findViewById(R.id.date_text);
                subjectText = itemView.findViewById(R.id.subject_text);
                roomText = itemView.findViewById(R.id.room_text);
                timeText = itemView.findViewById(R.id.time_text);
                typeText = itemView.findViewById(R.id.type_text);
                noteText = itemView.findViewById(R.id.note_text);
            }
        }
    }
}