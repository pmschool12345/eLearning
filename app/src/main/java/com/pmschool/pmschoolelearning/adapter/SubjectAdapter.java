package com.pmschool.pmschoolelearning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.pmschool.pmschoolelearning.R;
import com.pmschool.pmschoolelearning.model.SubjectModel;
import com.pmschool.pmschoolelearning.model.TeacherModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    private ArrayList<SubjectModel> mTeacherList;

    public SubjectAdapter(ArrayList<SubjectModel> list) {
        mTeacherList = list;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView classText;
        public TextView sectionText;
        public TextView subjectText;
        public ImageButton deleteSubject;

        public MyViewHolder(View v) {
            super(v);
            classText = v.findViewById(R.id.rv_class_name);
            sectionText = v.findViewById(R.id.rv_section_name);
            subjectText = v.findViewById(R.id.rv_subject_name);
            deleteSubject = v.findViewById(R.id.btnDeleteSubject);
        }
    }

    @NonNull
    @Override
    public SubjectAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View customView = layoutInflater.inflate(R.layout.list_subject, parent, false);

        MyViewHolder vh = new MyViewHolder(customView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectAdapter.MyViewHolder holder, final int position) {

        SubjectModel model = mTeacherList.get(position);
        String t = "Section "+model.getSection();
        TextView className = holder.classText;
        className.setText(model.getClass_name());
        TextView sectionName = holder.sectionText;
        sectionName.setText(t);
        TextView subjectName = holder.subjectText;
        subjectName.setText(model.getSubject());

        holder.deleteSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTeacherList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mTeacherList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTeacherList.size();
    }
}
